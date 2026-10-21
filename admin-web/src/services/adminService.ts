import { UserAccount, Report, AdminNotification, AuditLog, AdminRole } from '../types';
import { INITIAL_USERS, INITIAL_REPORTS, INITIAL_NOTIFICATIONS, INITIAL_AUDIT_LOGS } from '../data/mockData';

const STORAGE_KEY_USERS = 'ff_arena_admin_users';
const STORAGE_KEY_REPORTS = 'ff_arena_admin_reports';
const STORAGE_KEY_NOTIFS = 'ff_arena_admin_notifs';
const STORAGE_KEY_AUDIT = 'ff_arena_admin_audit_logs';

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

export class AdminService {
  /**
   * Users Management
   */
  static async getUsers(): Promise<UserAccount[]> {
    return getStored<UserAccount[]>(STORAGE_KEY_USERS, INITIAL_USERS);
  }

  static async updateUserStatus(
    uid: string,
    status: 'Active' | 'Warned' | 'Suspended' | 'Banned',
    reason?: string
  ): Promise<UserAccount> {
    const users = await this.getUsers();
    const user = users.find((u) => u.uid === uid);
    if (!user) throw new Error('User not found');

    user.status = status;
    if (reason) user.banReason = reason;
    setStored(STORAGE_KEY_USERS, users);
    return user;
  }

  static async toggleUserVerification(uid: string): Promise<UserAccount> {
    const users = await this.getUsers();
    const user = users.find((u) => u.uid === uid);
    if (!user) throw new Error('User not found');

    user.isVerified = !user.isVerified;
    setStored(STORAGE_KEY_USERS, users);
    return user;
  }

  /**
   * Reports and Disputes
   */
  static async getReports(): Promise<Report[]> {
    return getStored<Report[]>(STORAGE_KEY_REPORTS, INITIAL_REPORTS);
  }

  static async updateReport(
    reportId: string,
    status: 'Open' | 'In review' | 'Resolved',
    adminNotes?: string
  ): Promise<Report> {
    const reports = await this.getReports();
    const report = reports.find((r) => r.id === reportId);
    if (!report) throw new Error('Report not found');

    report.status = status;
    if (adminNotes !== undefined) report.adminNotes = adminNotes;
    setStored(STORAGE_KEY_REPORTS, reports);
    return report;
  }

  /**
   * Notifications
   */
  static async getNotifications(): Promise<AdminNotification[]> {
    return getStored<AdminNotification[]>(STORAGE_KEY_NOTIFS, INITIAL_NOTIFICATIONS);
  }

  static async sendNotification(
    title: string,
    message: string,
    target: 'all' | 'tournament' | 'division',
    targetValue: string,
    adminEmail: string
  ): Promise<AdminNotification> {
    const list = await this.getNotifications();
    const newNotif: AdminNotification = {
      id: `notif_${Date.now()}`,
      title,
      message,
      target,
      targetValue,
      sentAt: 'Just now',
      sentBy: adminEmail,
      recipientCount: target === 'all' ? 1420 : 48,
    };
    list.unshift(newNotif);
    setStored(STORAGE_KEY_NOTIFS, list);
    return newNotif;
  }

  /**
   * Audit Logs
   */
  static async getAuditLogs(): Promise<AuditLog[]> {
    return getStored<AuditLog[]>(STORAGE_KEY_AUDIT, INITIAL_AUDIT_LOGS);
  }

  static async logAction(
    adminEmail: string,
    adminRole: AdminRole,
    action: string,
    targetEntity: string,
    details: string
  ): Promise<void> {
    const logs = await this.getAuditLogs();
    const newLog: AuditLog = {
      id: `aud_${Date.now()}`,
      adminEmail,
      adminRole,
      action,
      targetEntity,
      details,
      timestamp: 'Just now',
    };
    logs.unshift(newLog);
    setStored(STORAGE_KEY_AUDIT, logs);
  }
}
