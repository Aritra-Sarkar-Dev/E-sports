import React from 'react';
import { Menu, Shield, RefreshCw } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { AdminRole } from '../../types';

interface TopHeaderProps {
  currentTab: string;
  onToggleSidebar: () => void;
}

const TAB_TITLES: Record<string, { title: string; subtitle: string }> = {
  dashboard: { title: 'Operational Dashboard', subtitle: 'Live overview of tournaments, registrations, and loyalty economy' },
  tournaments: { title: 'Tournament Management', subtitle: 'Create, schedule, edit, and supervise Free Fire MAX arenas' },
  rooms: { title: 'Custom Room Key Management', subtitle: 'Set Room ID, password, and 10-minute auto-reveal lock for players' },
  registrations: { title: 'Player Registrations', subtitle: 'Audit participant rosters, prevent duplicates, and export CSV' },
  results: { title: 'Match Results & Proof Verification', subtitle: 'Publish certified scores, verify video proofs, and trigger coin bonuses' },
  payouts: { title: 'Prize Payout Records', subtitle: 'Offline sponsor prize disbursements & UTR tracking (India Law Compliant)' },
  coins: { title: 'Loyalty Coin Economy & Store', subtitle: 'Manage non-cash earn rules, store assets, and manual adjustments' },
  users: { title: 'Fighter Accounts & Moderation', subtitle: 'Player profiles, 18+ verification, warning and ban logs' },
  reports: { title: 'Disputes & Anti-Cheat Inquiries', subtitle: 'Player reports, cheating tickets, and referee dispute resolution' },
  notifications: { title: 'Broadcast Announcements', subtitle: 'Send push alerts to all players, specific tournaments, or divisions' },
  leaderboard: { title: 'Esports Hall of Fame Rankings', subtitle: 'Weekly, seasonal, and all-time tournament standing calculations' },
  settings: { title: 'Admin & System Settings', subtitle: 'Manage administrator accounts, maintenance mode, and app alerts' },
  audit: { title: 'System Security Audit Trail', subtitle: 'Immutable administrative action logs for complete transparency' },
};

export const TopHeader: React.FC<TopHeaderProps> = ({ currentTab, onToggleSidebar }) => {
  const { currentAdmin, switchRole } = useAuth();
  const info = TAB_TITLES[currentTab] || { title: 'Admin Console', subtitle: 'FF Arena Operations' };

  return (
    <header className="h-20 bg-[#14141F]/80 backdrop-blur-md border-b border-[#23233A] px-6 flex items-center justify-between sticky top-0 z-30">
      <div className="flex items-center space-x-4">
        <button
          onClick={onToggleSidebar}
          className="p-2 text-[#9498AE] hover:text-[#F5F5FA] hover:bg-[#23233A] rounded-[14px] lg:hidden"
        >
          <Menu className="w-6 h-6" />
        </button>

        <div>
          <h1 className="text-xl font-extrabold text-[#F5F5FA] tracking-tight flex items-center gap-2">
            {info.title}
          </h1>
          <p className="text-xs text-[#9498AE] hidden sm:block">{info.subtitle}</p>
        </div>
      </div>

      {/* Role Switcher for Demo / Testing */}
      <div className="flex items-center space-x-3">
        <div className="hidden md:flex items-center space-x-1.5 bg-[#0A0A0F] border border-[#23233A] p-1 rounded-[16px]">
          <span className="text-[10px] font-bold text-[#9498AE] uppercase px-2 flex items-center gap-1">
            <Shield className="w-3 h-3 text-[#C8FF3D]" /> Test Role:
          </span>
          {(['SuperAdmin', 'Moderator', 'Support'] as AdminRole[]).map((r) => (
            <button
              key={r}
              onClick={() => switchRole(r)}
              className={`text-xs px-2.5 py-1 rounded-[12px] font-bold transition-all ${
                currentAdmin?.role === r
                  ? 'bg-[#7B5CFF] text-white shadow'
                  : 'text-[#9498AE] hover:text-[#F5F5FA] hover:bg-[#23233A]'
              }`}
            >
              {r}
            </button>
          ))}
        </div>

        {/* India Law Compliance Chip */}
        <div className="hidden lg:flex items-center space-x-1.5 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 px-3 py-1.5 rounded-[16px]">
          <span className="w-2 h-2 rounded-full bg-[#C8FF3D] animate-pulse" />
          <span className="text-xs font-bold text-[#C8FF3D]">India Compliant (₹0 Free Entry)</span>
        </div>
      </div>
    </header>
  );
};
