export const enablePaidEntry = false;

export type GameMode = 'Solo' | 'Duo' | 'Squad';
export type MapType = 'Bermuda' | 'Purgatory' | 'Kalahari' | 'Alpine';
export type MatchStatus = 'Upcoming' | 'Live' | 'Completed';
export type UserRole = 'player' | 'organizer' | 'admin';

export interface PlayerProfile {
  uid: string;
  name: string;
  inGameName: string;
  ffUid: string;
  avatarUrl: string;
  avatarFrame: string;
  phone?: string;
  email: string;
  level: number;
  xp: number;
  maxXp: number;
  rankTier: 'Bronze' | 'Silver' | 'Gold' | 'Platinum' | 'Diamond' | 'Heroic' | 'Grandmaster';
  role: UserRole;
  stats: {
    matches: number;
    wins: number;
    kills: number;
    kdRatio: number;
    winRate: number; // percentage
    totalCoins: number;
    prizesWon: number; // count of sponsor prizes won
    totalEarnings?: number; // kept for backwards compat (always coins/prize count)
  };
  referralCode: string;
  squadId?: string;
  isKycVerified?: boolean;
  linkedUpiId?: string;
}

export interface PlayerMatch {
  id: string;
  title: string;
  mode: GameMode;
  division: 'Rookie' | 'Mid' | 'Pro' | 'Open';
  map: MapType;
  mapImage: string;
  startTime: string; // ISO date string
  entryFee: 0; // Strictly 0 (100% Free Entry)
  prizePool: number; // Sponsor prize pool in INR
  perKillPrize: number; // XP bounty per kill (e.g. +10 XP)
  maxSlots: number;
  joinedSlots: number;
  status: MatchStatus;
  sponsorName: string;
  sponsorTag: string; // "Sponsor-funded prize"
  prizeDistribution: {
    first: number;
    second: number;
    third: number;
    perKillXp: number;
  };
  rules: string[];
  roomId?: string;
  roomPassword?: string;
  isRoomRevealed?: boolean;
  userJoined?: boolean;
  myRank?: number;
  myKills?: number;
  mySponsorPrize?: string;
  myWinnings?: number;
}

export type CoinLedgerType =
  | 'daily_checkin'
  | 'spin_wheel'
  | 'mission'
  | 'tournament_join'
  | 'match_participation'
  | 'referral'
  | 'cosmetic_purchase'
  | 'welcome_bonus';

export interface CoinLedgerEntry {
  id: string;
  userId: string;
  amount: number; // positive for earn, negative for spend
  type: CoinLedgerType;
  reason: string;
  date: string;
}

export interface Mission {
  id: string;
  title: string;
  rewardCoins: number;
  current: number;
  target: number;
  isClaimed: boolean;
  type: 'daily' | 'weekly';
}

export interface RewardStoreItem {
  id: string;
  title: string;
  category: 'frame' | 'badge' | 'theme' | 'ticket';
  coins: number;
  imageUrl: string;
  description: string;
  isPurchased?: boolean;
}

export interface LeaderboardPlayer {
  rank: number;
  name: string;
  inGameName: string;
  avatar: string;
  points: number;
  wins: number;
  kills: number;
  sponsorPrizesWon: number;
  badge?: string;
  earnings?: number;
}

export interface PlayerNotification {
  id: string;
  userId?: string;
  title: string;
  message: string;
  time: string;
  type: 'room' | 'match' | 'reward' | 'system' | 'wallet';
  isRead: boolean;
  actionUrl?: string;
}

export interface TournamentRegistration {
  id: string;
  tournamentId: string;
  tournamentTitle: string;
  userId: string;
  inGameName: string;
  ffUid: string;
  teamMembers: string[];
  registeredAt: string;
  status: 'Confirmed' | 'CheckedIn' | 'Played';
}

export interface MatchPlacementResult {
  rank: number;
  inGameName: string;
  ffUid: string;
  kills: number;
  sponsorPrizeRecord: string;
}

export interface SquadMember {
  uid: string;
  name: string;
  inGameName: string;
  ffUid: string;
  role: 'Leader' | 'Member';
}

export interface Squad {
  id: string;
  name: string;
  tag: string;
  logo: string;
  leaderUid: string;
  members: SquadMember[];
  stats: {
    matches: number;
    wins: number;
    totalKills: number;
  };
}

// Deprecated wallet balance type for type-safety compatibility during cleanup
export interface WalletBalance {
  deposit: number;
  winnings: number;
  bonus: number;
  total: number;
}

export interface WalletTransaction {
  id: string;
  type: string;
  title: string;
  amount: number;
  date: string;
  status: string;
  refId?: string;
}
