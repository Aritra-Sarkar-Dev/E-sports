import { Tournament, Registration, RegistrationStatus, MatchResult, PrizePayout } from '../types';
import { INITIAL_TOURNAMENTS, INITIAL_REGISTRATIONS, INITIAL_RESULTS, INITIAL_PAYOUTS } from '../data/mockData';
import { CoinService } from './coinService';
import { db } from './firebase';
import { doc, setDoc, deleteDoc } from 'firebase/firestore';

const STORAGE_KEY_TOURNAMENTS = 'ff_arena_admin_tournaments';
const STORAGE_KEY_REGISTRATIONS = 'ff_arena_admin_registrations';
const STORAGE_KEY_RESULTS = 'ff_arena_admin_results';
const STORAGE_KEY_PAYOUTS = 'ff_arena_admin_payouts';

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
}
