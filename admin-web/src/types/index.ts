export type AdminRole = 'SuperAdmin' | 'Moderator' | 'Support';

export interface AdminUser {
  uid: string;
  email: string;
  displayName: string;
  role: AdminRole;
  avatarUrl?: string;
  lastLogin?: string;
}

export type TournamentMode = 'Solo' | 'Duo' | 'Squad';
export type TournamentDivision = 'Rookie' | 'Mid' | 'Pro' | 'Open';
export type TournamentStatus = 'Draft' | 'Open' | 'Full' | 'Live' | 'Completed' | 'Cancelled';

export interface Tournament {
  id: string;
  name: string;
  mode: TournamentMode;
  division: TournamentDivision;
  startDateTime: string; // ISO string
  maxSlots: number;
  joinedSlots: number;
  sponsorName: string;
  sponsorLogo?: string;
  prizeAmount: number;
  prizeDescription: string;
  rules: string;
  status: TournamentStatus;
  roomId?: string;
  roomPassword?: string;
  autoRevealMinutes: number; // default 10
  isRoomRevealed?: boolean;
  createdAt: string;
}

export type RegistrationStatus = 'Pending' | 'Approved' | 'Rejected' | 'Confirmed' | 'CheckedIn' | 'Disqualified';

export interface Registration {
  id: string;
  tournamentId: string;
  tournamentName?: string;
  userId: string;
  ffUid: string;
  inGameName: string;
  status: RegistrationStatus;
  registeredAt: string;
  adminNote?: string;
}

export interface MatchResult {
  id: string;
  tournamentId: string;
  userId: string;
  playerName: string;
  ffUid: string;
  rank: number;
  kills: number;
  points: number;
  proofUrl: string;
  isVerified: boolean;
  isPublished: boolean;
  publishedAt?: string;
}

export type PayoutStatus = 'Pending' | 'Paid' | 'Failed';

export interface PrizePayout {
  id: string;
  tournamentId: string;
  tournamentName: string;
  winnerName: string;
  ffUid: string;
  rank: number;
  amount: number;
  status: PayoutStatus;
  note: string;
  updatedAt: string;
}

export type UserStatus = 'Active' | 'Warned' | 'Suspended' | 'Banned';

export interface UserAccount {
  uid: string;
  name: string;
  ffUid: string;
  inGameName: string;
  phone: string;
  email: string;
  coinBalance: number;
  status: UserStatus;
  isVerified: boolean;
  matchesPlayed: number;
  wins: number;
  kills: number;
  winRate: number;
  createdAt: string;
  banReason?: string;
}

export type CoinTransactionType = 'earn' | 'spend' | 'admin_adjust';

export interface CoinTransaction {
  id: string;
  uid: string;
  playerName?: string;
  type: CoinTransactionType;
  amount: number; // positive for earn / credit, negative for spend / debit
  reason: string;
  refId?: string;
  createdAt: string;
  createdBy: string;
}

export interface CoinRule {
  id: string;
  actionKey: string;
  name: string;
  coins: number;
  description: string;
  isEnabled: boolean;
}

export interface StoreItem {
  id: string;
  name: string;
  category: 'badge' | 'frame' | 'theme' | 'ticket';
  costCoins: number;
  previewUrl: string;
  isAvailable: boolean;
}

export type ReportCategory = 'cheating' | 'wrong_result' | 'room_issue' | 'harassment';
export type ReportStatus = 'Open' | 'In review' | 'Resolved';

export interface Report {
  id: string;
  reporterUid: string;
  reporterName: string;
  reportedUid?: string;
  reportedName?: string;
  tournamentId?: string;
  tournamentName?: string;
  category: ReportCategory;
  status: ReportStatus;
  details: string;
  adminNotes?: string;
  createdAt: string;
}

export interface AdminNotification {
  id: string;
  title: string;
  message: string;
  target: 'all' | 'tournament' | 'division';
  targetValue?: string;
  sentAt: string;
  sentBy: string;
  recipientCount: number;
}

export interface AuditLog {
  id: string;
  adminEmail: string;
  adminRole: AdminRole;
  action: string;
  targetEntity: string;
  details: string;
  timestamp: string;
}
