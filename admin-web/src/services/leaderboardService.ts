import { LeaderboardPlayer } from '../player/types';
import { INITIAL_LEADERBOARD } from '../player/mockPlayerData';

const STORAGE_KEY_LEADERBOARD_WEEKLY = 'ff_arena_leaderboard_weekly';
const STORAGE_KEY_LEADERBOARD_ALLTIME = 'ff_arena_leaderboard_alltime';

function getStored<T>(key: string, fallback: T): T {
  try {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : fallback;
  } catch {
    return fallback;
  }
}

function setStored<T>(key: string, value: T): void {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (e) {
    console.error('Storage error', e);
  }
}

export class LeaderboardService {
  static getWeekly(): LeaderboardPlayer[] {
    return getStored<LeaderboardPlayer[]>(STORAGE_KEY_LEADERBOARD_WEEKLY, INITIAL_LEADERBOARD);
  }

  static getAllTime(): LeaderboardPlayer[] {
    const defaultAllTime: LeaderboardPlayer[] = [
      {
        rank: 1,
        name: 'Aman Sharma',
        inGameName: '👑SOUL_MORTAL👑',
        avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=120&auto=format&fit=crop&q=80',
        points: 12450,
        wins: 114,
        kills: 1290,
        sponsorPrizesWon: 34,
        badge: 'Hall of Fame',
      },
      {
        rank: 2,
        name: 'Rohan Deshmukh',
        inGameName: '⚡VIPER_GOD⚡',
        avatar: 'https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=120&auto=format&fit=crop&q=80',
        points: 10890,
        wins: 89,
        kills: 1040,
        sponsorPrizesWon: 27,
        badge: 'Grandmaster',
      },
      {
        rank: 3,
        name: 'Devansh Roy',
        inGameName: '🔥PHOENIX_FF🔥',
        avatar: 'https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=120&auto=format&fit=crop&q=80',
        points: 9410,
        wins: 76,
        kills: 885,
        sponsorPrizesWon: 22,
        badge: 'Pro Tier',
      },
      {
        rank: 4,
        name: 'Aritra Sarkar (You)',
        inGameName: '⚡NEON_SOUL⚡',
        avatar: 'https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=120&auto=format&fit=crop&q=80',
        points: 8200,
        wins: 58,
        kills: 742,
        sponsorPrizesWon: 18,
        badge: 'Top Player',
      },
      {
        rank: 5,
        name: 'Sameer Sheikh',
        inGameName: '🎯HEADSHOT_KING',
        avatar: 'https://images.unsplash.com/photo-1580489944761-15a19d654956?w=120&auto=format&fit=crop&q=80',
        points: 7640,
        wins: 49,
        kills: 680,
        sponsorPrizesWon: 14,
        badge: 'Marksman',
      },
    ];
    return getStored<LeaderboardPlayer[]>(STORAGE_KEY_LEADERBOARD_ALLTIME, defaultAllTime);
  }

  static getLeaderboard(period: 'weekly' | 'alltime'): LeaderboardPlayer[] {
    return period === 'weekly' ? this.getWeekly() : this.getAllTime();
  }
}
