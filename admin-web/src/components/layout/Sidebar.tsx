import React from 'react';
import {
  LayoutDashboard,
  Trophy,
  KeyRound,
  Users,
  Award,
  CircleDollarSign,
  Coins,
  AlertTriangle,
  Bell,
  Medal,
  Sliders,
  FileText,
  LogOut,
  ShieldAlert,
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { AdminRole } from '../../types';

interface SidebarProps {
  currentTab: string;
  onSelectTab: (tab: string) => void;
  isOpen: boolean;
  onClose: () => void;
}

interface NavItem {
  id: string;
  label: string;
  icon: React.ComponentType<{ className?: string }>;
  roles: AdminRole[];
}

const NAV_ITEMS: NavItem[] = [
  { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard, roles: ['SuperAdmin', 'Moderator', 'Support'] },
  { id: 'tournaments', label: 'Tournaments', icon: Trophy, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'rooms', label: 'Room Management', icon: KeyRound, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'registrations', label: 'Registrations', icon: Users, roles: ['SuperAdmin', 'Moderator', 'Support'] },
  { id: 'results', label: 'Results & Proofs', icon: Award, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'payouts', label: 'Prize Payouts (Log)', icon: CircleDollarSign, roles: ['SuperAdmin'] },
  { id: 'coins', label: 'Coin System & Store', icon: Coins, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'users', label: 'Player Management', icon: Users, roles: ['SuperAdmin', 'Moderator', 'Support'] },
  { id: 'reports', label: 'Disputes & Cheats', icon: AlertTriangle, roles: ['SuperAdmin', 'Moderator', 'Support'] },
  { id: 'notifications', label: 'Announcements', icon: Bell, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'leaderboard', label: 'Leaderboards', icon: Medal, roles: ['SuperAdmin', 'Moderator'] },
  { id: 'settings', label: 'System Settings', icon: Sliders, roles: ['SuperAdmin'] },
  { id: 'audit', label: 'Audit Logs', icon: FileText, roles: ['SuperAdmin'] },
];

export const Sidebar: React.FC<SidebarProps> = ({ currentTab, onSelectTab, isOpen, onClose }) => {
  const { currentAdmin, logout, hasRole } = useAuth();

  const allowedItems = NAV_ITEMS.filter((item) => hasRole(item.roles));

  return (
    <>
      {/* Mobile Backdrop */}
      {isOpen && (
        <div className="fixed inset-0 z-40 bg-black/70 lg:hidden" onClick={onClose} />
      )}

      {/* Sidebar Container */}
      <aside
        className={`fixed top-0 bottom-0 left-0 z-50 w-64 bg-[#14141F] border-r border-[#23233A] flex flex-col transition-transform duration-300 ease-in-out lg:translate-x-0 ${
          isOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Logo Header */}
        <div className="h-20 flex items-center px-6 border-b border-[#23233A]">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 rounded-[14px] bg-gradient-to-br from-[#C8FF3D] to-[#7B5CFF] flex items-center justify-center font-extrabold text-black text-xl shadow-lg shadow-[#C8FF3D]/10">
              FF
            </div>
            <div>
              <div className="font-extrabold text-[#F5F5FA] text-base tracking-tight flex items-center gap-1.5">
                FF Arena <span className="text-[#C8FF3D] text-xs font-mono uppercase bg-[#C8FF3D]/10 px-1.5 py-0.5 rounded-md">Admin</span>
              </div>
              <div className="text-[10px] text-[#9498AE] tracking-wide uppercase font-semibold">
                Esports Operations
              </div>
            </div>
          </div>
        </div>

        {/* Role Pill Banner */}
        <div className="px-5 py-3 bg-[#0A0A0F]/80 border-b border-[#23233A] flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <ShieldAlert className="w-3.5 h-3.5 text-[#C8FF3D]" />
            <span className="text-xs font-bold text-[#F5F5FA]">{currentAdmin?.role || 'Guest'}</span>
          </div>
          <span className="text-[10px] font-bold uppercase tracking-wider text-[#C8FF3D] bg-[#C8FF3D]/10 px-2 py-0.5 rounded-full">
            Role Enforced
          </span>
        </div>

        {/* Navigation Links */}
        <div className="flex-1 overflow-y-auto px-3 py-4 space-y-1">
          {allowedItems.map((item) => {
            const Icon = item.icon;
            const isActive = currentTab === item.id;
            return (
              <button
                key={item.id}
                onClick={() => {
                  onSelectTab(item.id);
                  onClose();
                }}
                className={`w-full flex items-center space-x-3 px-3.5 py-2.5 rounded-[16px] text-xs font-semibold tracking-wide transition-all ${
                  isActive
                    ? 'bg-[#C8FF3D] text-[#0A0A0F] font-extrabold shadow-md shadow-[#C8FF3D]/20'
                    : 'text-[#9498AE] hover:text-[#F5F5FA] hover:bg-[#23233A]/60'
                }`}
              >
                <Icon className={`w-4 h-4 stroke-2 ${isActive ? 'text-[#0A0A0F]' : 'text-[#9498AE]'}`} />
                <span>{item.label}</span>
              </button>
            );
          })}
        </div>

        {/* User Info & Logout Footer */}
        <div className="p-4 border-t border-[#23233A] bg-[#0A0A0F]/50">
          <div className="flex items-center justify-between">
            <div className="truncate mr-2">
              <p className="text-xs font-bold text-[#F5F5FA] truncate">{currentAdmin?.displayName}</p>
              <p className="text-[10px] text-[#9498AE] truncate">{currentAdmin?.email}</p>
            </div>
            <button
              onClick={logout}
              title="Sign Out"
              className="p-2 text-[#9498AE] hover:text-[#FF7A8A] hover:bg-[#23233A] rounded-[12px] transition-colors"
            >
              <LogOut className="w-4 h-4" />
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};
