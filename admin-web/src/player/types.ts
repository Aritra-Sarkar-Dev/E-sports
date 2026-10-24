export type GameMode = 'Solo' | 'Duo' | 'Squad';
export type MapType = 'Bermuda' | 'Purgatory' | 'Kalahari' | 'Alpine';
export type MatchStatus = 'Upcoming' | 'Live' | 'Completed';
export type JoinStatus = 'Registered' | 'RoomRevealed' | 'Live' | 'Completed';

export interface PlayerMatch {
  id: string;
  title: string;
  mode: GameMode;
  division: 'Rookie' | 'Mid' | 'Pro' | 'Open';
  map: MapType;
  mapImage: string;
  startTime: string; // ISO date string
  entryFee: number; // in INR (0 for free)
  prizePool: number;
  perKillPrize: number;
  maxSlots: number;
  joinedSlots: number;
  status: MatchStatus;
  sponsorName: string;
  prizeDistribution: {
    first: number;
    second: number;
    third: number;
    perKill: number;
  };
  rules: string[];
  roomId?: string;
  roomPassword?: string;
  isRoomRevealed?: boolean;
  userJoined?: boolean;
  myRank?: number;
  myKills?: number;
  myWinnings?: number;
}

export interface PlayerProfile {
  name: string;
  inGameName: string;
  ffUid: string;
  avatarUrl: string;
  avatarFrame: string;
  phone: string;
  email: string;
  level: number;
  xp: number;
  maxXp: number;
  rankTier: 'Bronze' | 'Silver' | 'Gold' | 'Platinum' | 'Diamond' | 'Heroic' | 'Grandmaster';
  stats: {
    matches: number;
    wins: number;
    kills: number;
    kdRatio: number;
    winRate: number; // percentage
    totalEarnings: number;
  };
  referralCode: string;
  isKycVerified: boolean;
  linkedUpiId: string;
}

export type TransactionType = 'deposit' | 'winning' | 'entry_fee' | 'withdrawal' | 'bonus';

export interface WalletTransaction {
  id: string;
  type: TransactionType;
  title: string;
  amount: number;
  date: string;
  status: 'Completed' | 'Pending' | 'Failed';
  refId: string;
}

export interface WalletBalance {
  deposit: number;
  winnings: number;
  bonus: number;
  total: number;
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
  earnings: number;
  wins: number;
  kills: number;
  badge?: string;
}

export interface PlayerNotification {
  id: string;
  title: string;
  message: string;
  time: string;
  type: 'room' | 'match' | 'wallet' | 'reward' | 'system';
  isRead: boolean;
  actionUrl?: string;
}
