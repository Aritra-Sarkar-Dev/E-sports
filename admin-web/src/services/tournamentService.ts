import { Tournament, Registration, RegistrationStatus, MatchResult, PrizePayout } from '../types';
import { INITIAL_TOURNAMENTS, INITIAL_REGISTRATIONS, INITIAL_RESULTS, INITIAL_PAYOUTS } from '../data/mockData';
import {
  PlayerMatch,
  GameMode,
  MapType,
  TournamentRegistration,
  MatchPlacementResult,
  PlayerNotification
} from '../player/types';
import {
  INITIAL_MATCHES,
  INITIAL_REGISTRATIONS as INITIAL_PLAYER_REGS,
  INITIAL_NOTIFICATIONS as INITIAL_PLAYER_NOTIFS
} from '../player/mockPlayerData';
import { CoinService } from './coinService';
import { db } from './firebase';
import { doc, setDoc, deleteDoc } from 'firebase/firestore';

const STORAGE_KEY_TOURNAMENTS = 'ff_arena_admin_tournaments';
const STORAGE_KEY_REGISTRATIONS = 'ff_arena_admin_registrations';
const STORAGE_KEY_RESULTS = 'ff_arena_admin_results';
const STORAGE_KEY_PAYOUTS = 'ff_arena_admin_payouts';
const STORAGE_KEY_PLAYER_MATCHES = 'ff_arena_player_matches';
const STORAGE_KEY_PLAYER_REGISTRATIONS = 'ff_arena_player_registrations';
const STORAGE_KEY_PLAYER_NOTIFICATIONS = 'ff_arena_player_notifications';

function getStored<T>(key: string, defaultValue: T): T {
  try {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : defaultValue;
  } catch {
    return defaultValue;
  }
}

function setStored<T>(key: string, value: T): void {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (e) {
    console.error('Storage error', e);
  }
}

export class TournamentService {
  static async getTournaments(): Promise<Tournament[]> {
    return getStored<Tournament[]>(STORAGE_KEY_TOURNAMENTS, INITIAL_TOURNAMENTS);
  }

  static async saveTournament(tournament: Tournament): Promise<Tournament> {
    const list = await this.getTournaments();
    const index = list.findIndex((t) => t.id === tournament.id);
    if (index >= 0) {
      list[index] = tournament;
    } else {
      list.unshift(tournament);
    }
    setStored(STORAGE_KEY_TOURNAMENTS, list);

    try {
      await setDoc(doc(db, 'tournaments', tournament.id), tournament, { merge: true });
    } catch (e) {
      console.warn('Firestore tournament sync skipped', e);
    }

    return tournament;
  }

  static async duplicateTournament(tournamentId: string): Promise<Tournament> {
    const list = await this.getTournaments();
    const original = list.find((t) => t.id === tournamentId);
    if (!original) throw new Error('Tournament not found');

    const copy: Tournament = {
      ...original,
      id: `tourn_${Date.now()}`,
      name: `${original.name} (Copy)`,
      status: 'Draft',
      joinedSlots: 0,
      roomId: '',
      roomPassword: '',
      isRoomRevealed: false,
      createdAt: new Date().toISOString(),
    };

    return this.saveTournament(copy);
  }

  static async cancelTournament(tournamentId: string, reason?: string): Promise<void> {
    const list = await this.getTournaments();
    const t = list.find((item) => item.id === tournamentId);
    if (t) {
      t.status = 'Cancelled';
      setStored(STORAGE_KEY_TOURNAMENTS, list);
    }
  }

  /**
   * Room Management
   */
  static async updateRoomCredentials(
    tournamentId: string,
    roomId: string,
    roomPassword: string,
    autoRevealMinutes: number
  ): Promise<Tournament> {
    const list = await this.getTournaments();
    const t = list.find((item) => item.id === tournamentId);
    if (!t) throw new Error('Tournament not found');

    t.roomId = roomId;
    t.roomPassword = roomPassword;
    t.autoRevealMinutes = autoRevealMinutes;
    setStored(STORAGE_KEY_TOURNAMENTS, list);

    try {
      await setDoc(doc(db, 'tournaments', tournamentId), { roomId, roomPassword, autoRevealMinutes }, { merge: true });
    } catch (e) {
      console.warn('Firestore room update skipped', e);
    }

    return t;
  }

  /**
   * Registrations Management
   */
  static async getRegistrations(tournamentId?: string): Promise<Registration[]> {
    let all = getStored<Registration[]>(STORAGE_KEY_REGISTRATIONS, INITIAL_REGISTRATIONS);
    // If stored registrations lack the new statuses (Pending, Approved, Rejected), sync with INITIAL_REGISTRATIONS
    if (!all || all.length <= 5 || !all.some((r) => r.status === 'Pending')) {
      all = INITIAL_REGISTRATIONS;
      setStored(STORAGE_KEY_REGISTRATIONS, all);
    }
    if (tournamentId) {
      return all.filter((r) => r.tournamentId === tournamentId);
    }
    return all;
  }

  static async updateRegistrationStatus(
    regId: string,
    status: RegistrationStatus,
    adminNote?: string
  ): Promise<Registration | null> {
    const list = await this.getRegistrations();
    const target = list.find((r) => r.id === regId);
    if (!target) return null;
    target.status = status;
    if (adminNote !== undefined) {
      target.adminNote = adminNote;
    }
    setStored(STORAGE_KEY_REGISTRATIONS, list);
    return target;
  }

  static async removeRegistration(regId: string): Promise<void> {
    const list = await this.getRegistrations();
    const updated = list.filter((r) => r.id !== regId);
    setStored(STORAGE_KEY_REGISTRATIONS, updated);
  }

  /**
   * Results Management & Atomic Publishing
   */
  static async getResults(tournamentId?: string): Promise<MatchResult[]> {
    const all = getStored<MatchResult[]>(STORAGE_KEY_RESULTS, INITIAL_RESULTS);
    if (tournamentId) {
      return all.filter((r) => r.tournamentId === tournamentId);
    }
    return all;
  }

  static async saveResult(result: MatchResult): Promise<MatchResult> {
    const list = await this.getResults();
    const index = list.findIndex((r) => r.id === result.id);
    if (index >= 0) {
      list[index] = result;
    } else {
      list.unshift(result);
    }
    setStored(STORAGE_KEY_RESULTS, list);
    return result;
  }

  /**
   * Publish results:
   * 1. Marks results as published
   * 2. Sets tournament status to Completed
   * 3. Automatically distributes loyalty coins to participants according to coin rules!
   */
  static async publishTournamentResults(tournamentId: string, adminEmail: string): Promise<void> {
    const tournaments = await this.getTournaments();
    const t = tournaments.find((item) => item.id === tournamentId);
    if (t) {
      t.status = 'Completed';
      setStored(STORAGE_KEY_TOURNAMENTS, tournaments);
    }

    const results = await this.getResults(tournamentId);
    results.forEach((r) => {
      r.isPublished = true;
      r.publishedAt = new Date().toISOString();
    });
    setStored(STORAGE_KEY_RESULTS, await this.getResults());

    // Award loyalty coins based on tournament results
    const rules = await CoinService.getCoinRules();
    const booyahRule = rules.find((r) => r.actionKey === 'booyah_win' && r.isEnabled);
    const killRule = rules.find((r) => r.actionKey === 'per_kill' && r.isEnabled);
    const finishRule = rules.find((r) => r.actionKey === 'finish_match' && r.isEnabled);

    for (const r of results) {
      let coinsEarned = 0;
      if (finishRule) coinsEarned += finishRule.coins;
      if (r.rank === 1 && booyahRule) coinsEarned += booyahRule.coins;
      if (killRule && r.kills > 0) coinsEarned += killRule.coins * r.kills;

      if (coinsEarned > 0) {
        try {
          await CoinService.adminAdjustCoins(
            r.userId,
            coinsEarned,
            `Match Scorecard: Rank #${r.rank}, ${r.kills} Kills (${t?.name || 'Tournament'})`,
            adminEmail
          );
        } catch (e) {
          console.warn('Coin credit note', e);
        }
      }
    }
  }

  /**
   * Prize Payouts (Record keeping only - No in-app financial transactions)
   */
  static async getPayouts(): Promise<PrizePayout[]> {
    return getStored<PrizePayout[]>(STORAGE_KEY_PAYOUTS, INITIAL_PAYOUTS);
  }

  static async updatePayoutStatus(
    payoutId: string,
    status: 'Pending' | 'Paid' | 'Failed',
    note: string
  ): Promise<PrizePayout> {
    const list = await this.getPayouts();
    const p = list.find((item) => item.id === payoutId);
    if (!p) throw new Error('Payout record not found');

    p.status = status;
    p.note = note;
    p.updatedAt = new Date().toLocaleDateString([], { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    setStored(STORAGE_KEY_PAYOUTS, list);
    return p;
  }

  /**
   * Organizer Console & Player Match Management
   */
  static getPlayerMatches(): PlayerMatch[] {
    return getStored<PlayerMatch[]>(STORAGE_KEY_PLAYER_MATCHES, INITIAL_MATCHES);
  }

  static savePlayerMatches(matches: PlayerMatch[]): void {
    setStored(STORAGE_KEY_PLAYER_MATCHES, matches);
  }

  static createPlayerTournament(data: {
    title: string;
    sponsorName: string;
    mode: GameMode;
    map: MapType;
    startTime: string;
    prizePool: number;
    maxSlots: number;
  }): PlayerMatch {
    const matches = this.getPlayerMatches();
    const newId = `m_${Date.now()}`;
    const mapImages: Record<MapType, string> = {
      Bermuda: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80',
      Purgatory: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&auto=format&fit=crop&q=80',
      Kalahari: 'https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=800&auto=format&fit=crop&q=80',
      Alpine: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80',
    };

    const firstPrize = Math.round(data.prizePool * 0.5);
    const secondPrize = Math.round(data.prizePool * 0.3);
    const thirdPrize = Math.round(data.prizePool * 0.2);

    const newMatch: PlayerMatch = {
      id: newId,
      title: data.title,
      mode: data.mode,
      division: 'Open',
      map: data.map,
      mapImage: mapImages[data.map] || mapImages.Bermuda,
      startTime: data.startTime || new Date(Date.now() + 2 * 3600 * 1000).toISOString(),
      entryFee: 0,
      prizePool: data.prizePool,
      perKillPrize: 10,
      maxSlots: data.maxSlots || 48,
      joinedSlots: 0,
      status: 'Upcoming',
      sponsorName: data.sponsorName || 'Official Sponsor',
      sponsorTag: 'Sponsor-funded prize pool',
      prizeDistribution: {
        first: firstPrize,
        second: secondPrize,
        third: thirdPrize,
        perKillXp: 10,
      },
      rules: [
        'Mobile phones only (Android/iOS). Emulators strictly prohibited.',
        'Anti-cheat telemetry active. Violators will be disqualified.',
        'Room ID & Password unlocks before kick-off.',
        'Free entry. Prizes are sponsor-funded.'
      ],
      roomId: '',
      roomPassword: '',
      isRoomRevealed: false,
      userJoined: false,
    };

    matches.unshift(newMatch);
    this.savePlayerMatches(matches);

    // Also sync to admin tournaments list
    const adminTourns = getStored<Tournament[]>(STORAGE_KEY_TOURNAMENTS, INITIAL_TOURNAMENTS);
    const adminCopy: Tournament = {
      id: newId,
      name: data.title,
      mode: data.mode,
      division: 'Open',
      startDateTime: newMatch.startTime,
      maxSlots: newMatch.maxSlots,
      joinedSlots: 0,
      sponsorName: newMatch.sponsorName,
      prizeAmount: data.prizePool,
      prizeDescription: `1st: ₹${firstPrize}, 2nd: ₹${secondPrize}, 3rd: ₹${thirdPrize}`,
      rules: newMatch.rules.join('\n'),
      status: 'Open',
      autoRevealMinutes: 15,
      createdAt: new Date().toISOString(),
    };
    adminTourns.unshift(adminCopy);
    setStored(STORAGE_KEY_TOURNAMENTS, adminTourns);

    return newMatch;
  }

  static setMatchRoomCredentials(
    matchId: string,
    roomId: string,
    roomPassword: string,
    autoRevealMinutes: number = 15,
    revealNow: boolean = false
  ): PlayerMatch | null {
    const matches = this.getPlayerMatches();
    const match = matches.find((m) => m.id === matchId);
    if (!match) return null;

    match.roomId = roomId;
    match.roomPassword = roomPassword;
    if (revealNow) {
      match.isRoomRevealed = true;
    }
    this.savePlayerMatches(matches);

    // Also sync admin tournament credentials
    this.updateRoomCredentials(matchId, roomId, roomPassword, autoRevealMinutes).catch(() => {});

    // Broadcast room notification
    this.sendAnnouncement(
      `Room Credentials Updated: ${match.title}`,
      `Room ID: ${roomId} | Password: ${roomPassword}. Join 5 minutes before kickoff!`,
      'room'
    );

    return match;
  }

  static getPlayerRegistrations(tournamentId?: string): TournamentRegistration[] {
    const all = getStored<TournamentRegistration[]>(STORAGE_KEY_PLAYER_REGISTRATIONS, INITIAL_PLAYER_REGS);
    if (tournamentId) {
      return all.filter((r) => r.tournamentId === tournamentId);
    }
    return all;
  }

  static addPlayerRegistration(reg: TournamentRegistration): void {
    const list = this.getPlayerRegistrations();
    list.unshift(reg);
    setStored(STORAGE_KEY_PLAYER_REGISTRATIONS, list);

    // Increment joinedSlots on match
    const matches = this.getPlayerMatches();
    const m = matches.find((item) => item.id === reg.tournamentId);
    if (m && m.joinedSlots < m.maxSlots) {
      m.joinedSlots += 1;
      this.savePlayerMatches(matches);
    }
  }

  static publishMatchResults(
    matchId: string,
    placements: MatchPlacementResult[]
  ): PlayerMatch | null {
    const matches = this.getPlayerMatches();
    const match = matches.find((m) => m.id === matchId);
    if (!match) return null;

    match.status = 'Completed';

    // Check if current user or top players match
    const myPlacement = placements.find((p) => p.inGameName.toLowerCase().includes('neon') || p.ffUid === '994821034');
    if (myPlacement) {
      match.myRank = myPlacement.rank;
      match.myKills = myPlacement.kills;
      match.mySponsorPrize = myPlacement.sponsorPrizeRecord;
    } else if (placements.length > 0) {
      match.myRank = placements[0].rank;
      match.myKills = placements[0].kills;
      match.mySponsorPrize = placements[0].sponsorPrizeRecord;
    }

    this.savePlayerMatches(matches);

    // Store in admin match results
    const results = getStored<MatchResult[]>(STORAGE_KEY_RESULTS, INITIAL_RESULTS);
    placements.forEach((p, idx) => {
      results.unshift({
        id: `res_${matchId}_${idx}`,
        tournamentId: matchId,
        userId: `usr_${p.ffUid}`,
        playerName: p.inGameName,
        ffUid: p.ffUid,
        rank: p.rank,
        kills: p.kills,
        points: (10 - p.rank) * 2 + p.kills * 2,
        proofUrl: '',
        isVerified: true,
        isPublished: true,
        publishedAt: new Date().toISOString(),
      });
    });
    setStored(STORAGE_KEY_RESULTS, results);

    // Send notification
    const winner = placements[0];
    this.sendAnnouncement(
      `Official Results: ${match.title}`,
      `Booyah won by ${winner ? winner.inGameName : 'Champion'} with ${winner ? winner.kills : 0} kills! Prize: ${winner ? winner.sponsorPrizeRecord : 'Sponsor Trophy'}.`,
      'match'
    );

    return match;
  }

  static getPlayerNotifications(): PlayerNotification[] {
    return getStored<PlayerNotification[]>(STORAGE_KEY_PLAYER_NOTIFICATIONS, INITIAL_PLAYER_NOTIFS);
  }

  static sendAnnouncement(
    title: string,
    message: string,
    type: 'room' | 'match' | 'reward' | 'system' = 'system'
  ): PlayerNotification {
    const list = this.getPlayerNotifications();
    const newNotif: PlayerNotification = {
      id: `notif_${Date.now()}`,
      title,
      message,
      time: 'Just now',
      type,
      isRead: false,
    };
    list.unshift(newNotif);
    setStored(STORAGE_KEY_PLAYER_NOTIFICATIONS, list);
    return newNotif;
  }
}
