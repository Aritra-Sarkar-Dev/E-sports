import { PlayerNotification } from '../player/types';
import { INITIAL_NOTIFICATIONS } from '../player/mockPlayerData';

const STORAGE_KEY_NOTIFS = 'ff_arena_player_notifications';

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

export class NotificationService {
  static getNotifications(): PlayerNotification[] {
    return getStored<PlayerNotification[]>(STORAGE_KEY_NOTIFS, INITIAL_NOTIFICATIONS);
  }

  static addNotification(
    title: string,
    message: string,
    type: 'room' | 'match' | 'reward' | 'system' = 'system',
    actionUrl?: string
  ): PlayerNotification {
    const list = this.getNotifications();
    const newNotif: PlayerNotification = {
      id: `notif_${Date.now()}`,
      title,
      message,
      time: 'Just now',
      type,
      isRead: false,
      actionUrl,
    };
    list.unshift(newNotif);
    setStored(STORAGE_KEY_NOTIFS, list);
    return newNotif;
  }

  static markAsRead(id: string): PlayerNotification[] {
    const list = this.getNotifications().map((n) =>
      n.id === id ? { ...n, isRead: true } : n
    );
    setStored(STORAGE_KEY_NOTIFS, list);
    return list;
  }

  static markAllAsRead(): PlayerNotification[] {
    const list = this.getNotifications().map((n) => ({ ...n, isRead: true }));
    setStored(STORAGE_KEY_NOTIFS, list);
    return list;
  }

  static clearAll(): void {
    setStored(STORAGE_KEY_NOTIFS, []);
  }
}
