import React, { useState, useEffect } from 'react';
import { Bell, Send, CheckCircle2, Shield, Users, Trophy } from 'lucide-react';
import { AdminNotification, Tournament } from '../types';
import { AdminService } from '../services/adminService';
import { TournamentService } from '../services/tournamentService';
import { useAuth } from '../context/AuthContext';
import { Badge } from '../components/common/Badge';

export const AnnouncementsPage: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [notifications, setNotifications] = useState<AdminNotification[]>([]);
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState('');
  const [target, setTarget] = useState<'all' | 'tournament' | 'division'>('all');
  const [targetValue, setTargetValue] = useState('');
  const [isSending, setIsSending] = useState(false);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    const list = await AdminService.getNotifications();
    setNotifications(list);
    const tourns = await TournamentService.getTournaments();
    setTournaments(tourns);
    if (tourns.length > 0) {
      setTargetValue(tourns[0].name);
    }
  };

  const handleSend = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !message.trim()) return;

    setIsSending(true);
    try {
      const newNotif = await AdminService.sendNotification(
        title,
        message,
        target,
        target === 'all' ? 'All Active FF Arena Fighters' : targetValue,
        currentAdmin?.email || 'admin@ffarena.in'
      );
      setNotifications((prev) => [newNotif, ...prev]);
      setTitle('');
      setMessage('');
      setSuccessMsg(`Broadcast successfully queued to ${newNotif.recipientCount} players.`);
      setTimeout(() => setSuccessMsg(null), 4000);
    } finally {
      setIsSending(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header alert */}
      {successMsg && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-sm text-[#C8FF3D]">
          <CheckCircle2 className="w-5 h-5 shrink-0" />
          <span>{successMsg}</span>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Broadcast Form */}
        <div className="lg:col-span-1 bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 shadow-xl space-y-4">
          <div className="flex items-center space-x-3 pb-3 border-b border-[#23233A]">
            <div className="p-2.5 bg-[#C8FF3D]/10 rounded-[14px] text-[#C8FF3D]">
              <Bell className="w-5 h-5" />
            </div>
            <div>
              <h2 className="font-extrabold text-[#F5F5FA] text-base">New Announcement</h2>
              <p className="text-xs text-[#9498AE]">Send push notices to mobile app players</p>
            </div>
          </div>

          <form onSubmit={handleSend} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
                Announcement Headline
              </label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="e.g., Bermuda Blitz Room Revealed!"
                required
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-xs focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
                Target Audience
              </label>
              <div className="grid grid-cols-3 gap-2">
                {(['all', 'tournament', 'division'] as const).map((t) => (
                  <button
                    key={t}
                    type="button"
                    onClick={() => setTarget(t)}
                    className={`py-2 text-[11px] font-bold rounded-[14px] capitalize transition-all ${
                      target === t
                        ? 'bg-[#C8FF3D] text-black font-extrabold shadow-sm'
                        : 'bg-[#0A0A0F] border border-[#2E2E4A] text-[#9498AE] hover:text-[#F5F5FA]'
                    }`}
                  >
                    {t}
                  </button>
                ))}
              </div>
            </div>

            {target === 'tournament' && (
              <div>
                <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
                  Select Tournament
                </label>
                <select
                  value={targetValue}
                  onChange={(e) => setTargetValue(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-xs focus:outline-none"
                >
                  {tournaments.map((tourn) => (
                    <option key={tourn.id} value={tourn.name}>
                      {tourn.name}
                    </option>
                  ))}
                </select>
              </div>
            )}

            {target === 'division' && (
              <div>
                <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
                  Select Division Tier
                </label>
                <select
                  value={targetValue}
                  onChange={(e) => setTargetValue(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-xs focus:outline-none"
                >
                  <option value="ROOKIE">Rookie Tier</option>
                  <option value="MID">Mid Tier</option>
                  <option value="PRO">Pro Tier</option>
                  <option value="OPEN">Open Tier</option>
                </select>
              </div>
            )}

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
                Message Body
              </label>
              <textarea
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder="Write your push notification message..."
                rows={4}
                required
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] p-3 text-xs focus:outline-none"
              />
            </div>

            <button
              type="submit"
              disabled={isSending}
              className="w-full flex items-center justify-center space-x-2 bg-[#C8FF3D] hover:bg-[#b8f02d] text-[#0A0A0F] font-extrabold py-3 rounded-[16px] transition-all shadow-md shadow-[#C8FF3D]/10"
            >
              <Send className="w-4 h-4" />
              <span>{isSending ? 'Sending...' : 'Broadcast to App'}</span>
            </button>
          </form>
        </div>

        {/* History Feed */}
        <div className="lg:col-span-2 bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 shadow-xl space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-[#23233A]">
            <div>
              <h2 className="font-extrabold text-[#F5F5FA] text-base">Broadcast History</h2>
              <p className="text-xs text-[#9498AE]">Recently dispatched push alerts and logs</p>
            </div>
            <span className="text-xs font-semibold text-[#9498AE]">
              {notifications.length} notifications sent
            </span>
          </div>

          <div className="space-y-3">
            {notifications.map((n) => (
              <div
                key={n.id}
                className="p-4 bg-[#0A0A0F] border border-[#23233A] hover:border-[#2E2E4A] rounded-[18px] transition-all space-y-2"
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-2">
                    <span className="text-xs font-extrabold text-[#F5F5FA]">{n.title}</span>
                    <Badge variant={n.target === 'all' ? 'lime' : 'violet'}>
                      {n.target.toUpperCase()}
                    </Badge>
                  </div>
                  <span className="text-[11px] text-[#9498AE]">{n.sentAt}</span>
                </div>

                <p className="text-xs text-[#9498AE] leading-relaxed">{n.message}</p>

                <div className="pt-2 border-t border-[#23233A] flex items-center justify-between text-[11px] text-[#9498AE]">
                  <span className="flex items-center gap-1">
                    <Users className="w-3.5 h-3.5 text-[#C8FF3D]" />
                    <span>Delivered to {n.recipientCount} fighters</span>
                  </span>
                  <span>By: {n.sentBy}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
