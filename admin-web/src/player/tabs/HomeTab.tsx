import React, { useState, useEffect } from 'react';
import {
  PlayerProfile,
  WalletBalance,
  PlayerMatch,
  LeaderboardPlayer
} from '../types';
import {
  Bell,
  Coins,
  Wallet,
  CalendarCheck,
  RotateCw,
  UserPlus,
  Headphones,
  Flame,
  Radio,
  Clock,
  ChevronRight,
  Trophy,
  Crown
} from 'lucide-react';

interface HomeTabProps {
  profile: PlayerProfile;
  wallet: WalletBalance;
  coins: number;
  matches: PlayerMatch[];
  leaderboard: LeaderboardPlayer[];
  unreadNotifsCount: number;
  onOpenNotifications: () => void;
  onOpenWallet: () => void;
  onOpenSpinWheel: () => void;
  onOpenLeaderboard: () => void;
  onOpenMatchDetail: (match: PlayerMatch) => void;
  onSelectCategory: (cat: string) => void;
  onDailyCheckIn: () => void;
  hasCheckedInToday: boolean;
  onShareReferral: () => void;
  onOpenSupport: () => void;
}

const CAROUSEL_BANNERS = [
  {
    id: 1,
    title: 'CYBERFRAY 2077 GRAND FINALS',
    subtitle: '₹50,000 Sponsor Pool • ASUS ROG Sponsored',
    tag: 'MEGA TOURNAMENT',
    bg: 'from-[#FF6A00] via-[#E040FB] to-[#3A24A8]',
    image: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80',
  },
  {
    id: 2,
    title: 'FREE ENTRY SUNDAY ROOKIE CUP',
    subtitle: 'Zero Entry Fee • Win ₹1,500 Cash & Level-Up Coins',
    tag: '100% FREE ENTRY',
    bg: 'from-[#00E5FF] via-[#7B5CFF] to-[#14141F]',
    image: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&auto=format&fit=crop&q=80',
  },
  {
    id: 3,
    title: 'BOOYAH WARRIORS VIP PASS',
    subtitle: 'Unlock Exclusive Neon Frames & 2x Elimination Rewards',
    tag: 'SEASON 14 LIVE',
    bg: 'from-[#C8FF3D] via-[#00E676] to-[#0A0A0F]',
    image: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80',
  },
];

export const HomeTab: React.FC<HomeTabProps> = ({
  profile,
  wallet,
  coins,
  matches,
  leaderboard,
  unreadNotifsCount,
  onOpenNotifications,
  onOpenWallet,
  onOpenSpinWheel,
  onOpenLeaderboard,
  onOpenMatchDetail,
  onSelectCategory,
  onDailyCheckIn,
  hasCheckedInToday,
  onShareReferral,
  onOpenSupport,
}) => {
  const [activeBanner, setActiveBanner] = useState(0);
  const [selectedGameMode, setSelectedGameMode] = useState<string>('All');

  // Auto-sliding banner
  useEffect(() => {
    const timer = setInterval(() => {
      setActiveBanner((prev) => (prev + 1) % CAROUSEL_BANNERS.length);
    }, 4500);
    return () => clearInterval(timer);
  }, []);

  const liveMatches = matches.filter((m) => m.status === 'Live');
  const startingSoon = matches.filter((m) => m.status === 'Upcoming').slice(0, 4);

  const gameModes = [
    { id: 'All', label: 'All Modes' },
    { id: 'Battle Royale', label: 'Battle Royale' },
    { id: 'Clash Squad', label: 'Clash Squad' },
    { id: 'Lone Wolf', label: 'Lone Wolf (1v1)' },
    { id: 'Free Entry', label: 'Free Entry' },
    { id: 'Pro Cash', label: 'Pro Cash' },
  ];

  return (
    <div className="space-y-5 pb-8 animate-fade-in">
      {/* Sticky Top Header */}
      <div className="sticky top-0 z-30 bg-[#0A0A0F]/90 backdrop-blur-md px-4 py-3 border-b border-[#23233A]/60 flex items-center justify-between">
        <div className="flex items-center gap-2.5">
          <div className="relative">
            <img
              src={profile.avatarUrl}
              alt={profile.inGameName}
              className="w-10 h-10 rounded-full object-cover border-2 border-[#C8FF3D] shadow-[0_0_10px_rgba(200,255,61,0.3)]"
            />
            <span className="absolute -bottom-1 -right-1 w-4 h-4 bg-[#FF6A00] text-white text-[9px] font-black rounded-full flex items-center justify-center border border-[#0A0A0F]">
              42
            </span>
          </div>
          <div>
            <span className="text-[10px] text-[#9498AE] uppercase tracking-wider block">
              Welcome back
            </span>
            <h2 className="text-sm font-extrabold text-white flex items-center gap-1">
              Hey, {profile.name.split(' ')[0]}
              <span className="text-xs">⚡</span>
            </h2>
          </div>
        </div>

        {/* Chips row: Coins, Wallet, Bell */}
        <div className="flex items-center gap-2">
          {/* Coins chip */}
          <button
            onClick={() => {}}
            className="flex items-center gap-1.5 px-2.5 py-1.5 bg-[#1C1C2E] border border-[#FFB300]/30 rounded-full text-xs font-bold text-[#FFB300] hover:border-[#FFB300] transition-all"
          >
            <Coins className="w-3.5 h-3.5 text-[#FFB300]" />
            <span>{coins}</span>
          </button>

          {/* Wallet chip */}
          <button
            onClick={onOpenWallet}
            className="flex items-center gap-1.5 px-2.5 py-1.5 bg-[#1C1C2E] border border-[#C8FF3D]/30 rounded-full text-xs font-bold text-[#C8FF3D] hover:border-[#C8FF3D] transition-all shadow-[0_0_8px_rgba(200,255,61,0.15)]"
          >
            <Wallet className="w-3.5 h-3.5 text-[#C8FF3D]" />
            <span>₹{wallet.total}</span>
          </button>

          {/* Notification bell */}
          <button
            onClick={onOpenNotifications}
            className="relative p-2 bg-[#1C1C2E] border border-[#23233A] rounded-full text-slate-300 hover:text-white hover:border-[#7B5CFF] transition-all"
          >
            <Bell className="w-4 h-4" />
            {unreadNotifsCount > 0 && (
              <span className="absolute -top-1 -right-1 w-4 h-4 bg-[#FF6A00] text-white text-[9px] font-bold rounded-full flex items-center justify-center animate-pulse">
                {unreadNotifsCount}
              </span>
            )}
          </button>
        </div>
      </div>

      <div className="px-4 space-y-5">
        {/* Banner Carousel */}
        <div className="relative rounded-[22px] overflow-hidden border border-[#23233A] shadow-2xl h-44">
          {CAROUSEL_BANNERS.map((banner, idx) => (
            <div
              key={banner.id}
              className={`absolute inset-0 transition-opacity duration-700 ${
                activeBanner === idx ? 'opacity-100 z-10' : 'opacity-0 z-0 pointer-events-none'
              }`}
            >
              <img
                src={banner.image}
                alt={banner.title}
                className="w-full h-full object-cover brightness-50"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-[#0A0A0F] via-[#0A0A0F]/50 to-transparent p-4 flex flex-col justify-end">
                <span className="self-start px-2 py-0.5 bg-[#C8FF3D] text-black font-extrabold text-[9px] rounded-full uppercase tracking-wider mb-1.5 shadow">
                  {banner.tag}
                </span>
                <h3 className="text-base font-black text-white leading-tight mb-1">
                  {banner.title}
                </h3>
                <p className="text-xs text-[#C8FF3D] font-medium line-clamp-1">
                  {banner.subtitle}
                </p>
              </div>
            </div>
          ))}

          {/* Dots Indicator */}
          <div className="absolute bottom-2.5 right-3.5 z-20 flex gap-1.5">
            {CAROUSEL_BANNERS.map((_, idx) => (
              <button
                key={idx}
                onClick={() => setActiveBanner(idx)}
                className={`h-1.5 rounded-full transition-all ${
                  activeBanner === idx ? 'w-5 bg-[#C8FF3D]' : 'w-1.5 bg-white/40'
                }`}
              />
            ))}
          </div>
        </div>

        {/* Quick Action Row */}
        <div className="grid grid-cols-4 gap-2">
          {/* Daily check-in */}
          <button
            onClick={onDailyCheckIn}
            className="flex flex-col items-center justify-center p-2.5 bg-[#14141F] border border-[#23233A] rounded-2xl hover:border-[#FFB300]/50 transition-all active:scale-95 group"
          >
            <div className={`w-10 h-10 rounded-2xl flex items-center justify-center mb-1.5 transition-all ${
              hasCheckedInToday
                ? 'bg-[#C8FF3D]/15 text-[#C8FF3D]'
                : 'bg-[#FFB300]/15 text-[#FFB300] group-hover:scale-105'
            }`}>
              <CalendarCheck className="w-5 h-5" />
            </div>
            <span className="text-[11px] font-bold text-white text-center leading-tight">
              {hasCheckedInToday ? 'Streak (5)' : 'Check-in'}
            </span>
          </button>

          {/* Spin Wheel */}
          <button
            onClick={onOpenSpinWheel}
            className="flex flex-col items-center justify-center p-2.5 bg-[#14141F] border border-[#23233A] rounded-2xl hover:border-[#7B5CFF]/50 transition-all active:scale-95 group"
          >
            <div className="w-10 h-10 rounded-2xl bg-[#7B5CFF]/15 text-[#7B5CFF] flex items-center justify-center mb-1.5 group-hover:rotate-45 transition-all">
              <RotateCw className="w-5 h-5" />
            </div>
            <span className="text-[11px] font-bold text-white text-center leading-tight">
              Spin Wheel
            </span>
          </button>

          {/* Invite & Earn */}
          <button
            onClick={onShareReferral}
            className="flex flex-col items-center justify-center p-2.5 bg-[#14141F] border border-[#23233A] rounded-2xl hover:border-[#FF6A00]/50 transition-all active:scale-95 group"
          >
            <div className="w-10 h-10 rounded-2xl bg-[#FF6A00]/15 text-[#FF6A00] flex items-center justify-center mb-1.5 group-hover:scale-105 transition-all">
              <UserPlus className="w-5 h-5" />
            </div>
            <span className="text-[11px] font-bold text-white text-center leading-tight">
              Invite & Earn
            </span>
          </button>

          {/* Support */}
          <button
            onClick={onOpenSupport}
            className="flex flex-col items-center justify-center p-2.5 bg-[#14141F] border border-[#23233A] rounded-2xl hover:border-[#00E5FF]/50 transition-all active:scale-95 group"
          >
            <div className="w-10 h-10 rounded-2xl bg-[#00E5FF]/15 text-[#00E5FF] flex items-center justify-center mb-1.5 group-hover:scale-105 transition-all">
              <Headphones className="w-5 h-5" />
            </div>
            <span className="text-[11px] font-bold text-white text-center leading-tight">
              24/7 Help
            </span>
          </button>
        </div>

        {/* Game Mode Horizontal Chips */}
        <div>
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
              <Flame className="w-4 h-4 text-[#FF6A00]" /> Tournament Arenas
            </span>
          </div>
          <div className="flex gap-2 overflow-x-auto no-scrollbar pb-1">
            {gameModes.map((mode) => (
              <button
                key={mode.id}
                onClick={() => {
                  setSelectedGameMode(mode.id);
                  onSelectCategory(mode.id);
                }}
                className={`px-3 py-1.5 text-xs font-bold rounded-xl whitespace-nowrap transition-all border ${
                  selectedGameMode === mode.id
                    ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                    : 'bg-[#14141F] text-[#9498AE] border-[#23233A] hover:text-white'
                }`}
              >
                {mode.label}
              </button>
            ))}
          </div>
        </div>

        {/* Live Now Section */}
        {liveMatches.length > 0 && (
          <div className="space-y-2.5">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span className="flex h-2.5 w-2.5 relative">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500"></span>
                </span>
                <span className="text-xs font-black text-white uppercase tracking-wider">
                  Live Battle Arena
                </span>
              </div>
              <span className="text-[11px] text-[#9498AE] flex items-center gap-1">
                <Radio className="w-3.5 h-3.5 text-red-500" /> 1.4k Spectators
              </span>
            </div>

            {liveMatches.map((m) => (
              <div
                key={m.id}
                onClick={() => onOpenMatchDetail(m)}
                className="relative rounded-2xl overflow-hidden border border-red-500/40 bg-[#14141F] p-4 cursor-pointer hover:border-red-500 transition-all shadow-[0_0_20px_rgba(239,68,68,0.15)] group"
              >
                <div className="flex items-start justify-between mb-2">
                  <div>
                    <span className="text-[10px] px-2 py-0.5 bg-red-500 text-white font-extrabold rounded-full uppercase tracking-wider inline-block mb-1">
                      LIVE MATCH
                    </span>
                    <h4 className="text-sm font-bold text-white group-hover:text-[#C8FF3D] transition-colors">
                      {m.title}
                    </h4>
                    <p className="text-[11px] text-[#9498AE] mt-0.5">
                      {m.mode} • Map: {m.map} • Prize: <strong className="text-[#FFB300]">₹{m.prizePool}</strong>
                    </p>
                  </div>

                  <button className="px-3 py-1.5 bg-[#FF6A00] text-white text-xs font-extrabold rounded-xl shadow-[0_0_12px_rgba(255,106,0,0.4)] flex items-center gap-1">
                    Watch
                  </button>
                </div>

                {/* Progress bar */}
                <div className="w-full bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden mt-3">
                  <div className="h-full bg-gradient-to-r from-[#FF6A00] to-red-500 w-[95%]" />
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Starting Soon Section */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
              <Clock className="w-4 h-4 text-[#C8FF3D]" /> Starting Soon
            </span>
            <button
              onClick={() => onSelectCategory('All')}
              className="text-[11px] font-bold text-[#C8FF3D] flex items-center gap-0.5 hover:underline"
            >
              See All <ChevronRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="grid grid-cols-1 gap-3">
            {startingSoon.map((m) => (
              <div
                key={m.id}
                onClick={() => onOpenMatchDetail(m)}
                className="bg-[#14141F] border border-[#23233A] rounded-[20px] p-3.5 flex flex-col gap-2.5 cursor-pointer hover:border-[#C8FF3D]/50 transition-all active:scale-[0.99] group shadow-lg"
              >
                <div className="flex items-start justify-between gap-2">
                  <div className="flex-1">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] text-[10px] font-extrabold rounded-md uppercase">
                        {m.mode}
                      </span>
                      <span className="text-[10px] text-[#9498AE]">
                        Map: <strong className="text-white">{m.map}</strong>
                      </span>
                    </div>
                    <h4 className="text-xs font-bold text-white group-hover:text-[#C8FF3D] transition-colors line-clamp-1">
                      {m.title}
                    </h4>
                  </div>

                  <div className="text-right shrink-0">
                    <span className="text-[10px] text-[#9498AE] block">Prize Pool</span>
                    <span className="text-xs font-extrabold text-[#FFB300]">₹{m.prizePool}</span>
                  </div>
                </div>

                {/* Bottom stats row */}
                <div className="flex items-center justify-between pt-2 border-t border-[#23233A]/60 text-xs">
                  <div className="flex items-center gap-3 text-[11px] text-[#9498AE]">
                    <span>
                      Entry: <strong className={m.entryFee === 0 ? 'text-[#C8FF3D]' : 'text-white'}>
                        {m.entryFee === 0 ? 'FREE' : `₹${m.entryFee}`}
                      </strong>
                    </span>
                    <span>•</span>
                    <span>Kill: <strong className="text-[#FF6A00]">₹{m.perKillPrize}</strong></span>
                  </div>

                  <div className="flex items-center gap-2">
                    <span className="text-[10px] text-[#9498AE]">
                      {m.joinedSlots}/{m.maxSlots} Slots
                    </span>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onOpenMatchDetail(m);
                      }}
                      className="px-3 py-1 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black text-[11px] font-black rounded-lg shadow-[0_0_10px_rgba(200,255,61,0.2)]"
                    >
                      {m.userJoined ? 'View Room' : 'Join'}
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Mini Leaderboard Strip */}
        <div className="bg-gradient-to-r from-[#17113D] via-[#1C1C2E] to-[#14141F] border border-[#7B5CFF]/40 rounded-[22px] p-4 shadow-xl">
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-full bg-[#FFB300]/20 flex items-center justify-center text-[#FFB300]">
                <Crown className="w-4 h-4" />
              </div>
              <span className="text-xs font-extrabold text-white">Top Champions This Week</span>
            </div>
            <button
              onClick={onOpenLeaderboard}
              className="text-[11px] font-bold text-[#C8FF3D] flex items-center gap-0.5 hover:underline"
            >
              Full Board <ChevronRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="flex items-center justify-between gap-2 overflow-x-auto no-scrollbar py-1">
            {leaderboard.slice(0, 3).map((player, idx) => (
              <div
                key={player.rank}
                className="flex items-center gap-2 p-2 bg-[#0A0A0F]/60 rounded-xl border border-white/5 flex-1 min-w-[105px]"
              >
                <div className="relative shrink-0">
                  <img
                    src={player.avatar}
                    alt={player.inGameName}
                    className="w-8 h-8 rounded-full object-cover border border-[#FFB300]"
                  />
                  <span className={`absolute -top-1 -left-1 w-3.5 h-3.5 rounded-full text-[9px] font-black flex items-center justify-center ${
                    idx === 0 ? 'bg-[#FFB300] text-black' : idx === 1 ? 'bg-slate-300 text-black' : 'bg-[#CD7F32] text-white'
                  }`}>
                    {idx + 1}
                  </span>
                </div>
                <div className="min-w-0">
                  <span className="text-[10px] font-bold text-white truncate block">
                    {player.inGameName}
                  </span>
                  <span className="text-[10px] font-extrabold text-[#FFB300] block">
                    ₹{player.earnings}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
