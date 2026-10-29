import React, { useState, useEffect } from 'react';
import { PlayerProfile, PlayerMatch, LeaderboardPlayer } from '../types';
import {
  Bell,
  Coins,
  CalendarCheck,
  RotateCw,
  Users,
  Trophy,
  Flame,
  Radio,
  Clock,
  ChevronRight,
  ShieldCheck,
  Sparkles,
  Zap,
  Search
} from 'lucide-react';

interface HomeTabProps {
  profile: PlayerProfile;
  coins: number;
  matches: PlayerMatch[];
  leaderboard: LeaderboardPlayer[];
  unreadNotifsCount: number;
  onOpenNotifications: () => void;
  onOpenRewards: () => void;
  onOpenSpinWheel: () => void;
  onOpenLeaderboard: () => void;
  onOpenSquad: () => void;
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
    sponsor: 'ASUS ROG Sponsored',
    prizeLabel: '₹50,000 SPONSOR POOL',
    tag: 'MEGA SPONSOR CUP',
    image: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80',
  },
  {
    id: 2,
    title: 'SUNDAY ROOKIE SHOWDOWN',
    sponsor: 'Red Bull Esports',
    prizeLabel: '₹5,000 SPONSOR POOL',
    tag: '100% FREE ENTRY',
    image: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&auto=format&fit=crop&q=80',
  },
  {
    id: 3,
    title: 'CLASH SQUAD 4v4 INVITATIONAL',
    sponsor: 'Monster Energy',
    prizeLabel: '₹12,000 SPONSOR POOL',
    tag: 'SPONSOR TROPHY',
    image: 'https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=800&auto=format&fit=crop&q=80',
  },
];

export const HomeTab: React.FC<HomeTabProps> = ({
  profile,
  coins,
  matches,
  leaderboard,
  unreadNotifsCount,
  onOpenNotifications,
  onOpenRewards,
  onOpenSpinWheel,
  onOpenLeaderboard,
  onOpenSquad,
  onOpenMatchDetail,
  onSelectCategory,
  onDailyCheckIn,
  hasCheckedInToday,
  onShareReferral,
  onOpenSupport,
}) => {
  const [activeBanner, setActiveBanner] = useState(0);
  const [selectedGameMode, setSelectedGameMode] = useState<string>('All');
  const [searchQuery, setSearchQuery] = useState('');
  const [roomCountdown, setRoomCountdown] = useState('12:30');

  // Auto-sliding banner
  useEffect(() => {
    const timer = setInterval(() => {
      setActiveBanner((prev) => (prev + 1) % CAROUSEL_BANNERS.length);
    }, 4500);
    return () => clearInterval(timer);
  }, []);

  // Room countdown timer ticker (12:30 -> 00:00)
  useEffect(() => {
    const timer = setInterval(() => {
      setRoomCountdown((prev) => {
        const [m, s] = prev.split(':').map(Number);
        if (m === 0 && s === 0) return '00:00';
        const total = m * 60 + s - 1;
        const nm = Math.floor(total / 60);
        const ns = total % 60;
        return `${String(nm).padStart(2, '0')}:${String(ns).padStart(2, '0')}`;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, []);

  const liveMatches = matches.filter((m) => m.status === 'Live');
  const upcomingMatches = matches.filter((m) => m.status === 'Upcoming');
  const myJoinedMatch = matches.find((m) => m.userJoined && m.status !== 'Completed');
  const beginnerMatches = matches.filter((m) => m.division === 'Rookie');

  const gameModes = [
    { id: 'All', label: 'All Modes' },
    { id: 'Squad', label: 'Squad (4v4)' },
    { id: 'Solo', label: 'Solo Cup' },
    { id: 'Duo', label: 'Duo' },
    { id: 'Sponsor Cups', label: 'Sponsor Cups' },
  ];

  return (
    <div className="space-y-4 pb-8 animate-fade-in">
      {/* Sticky Top Header */}
      <div className="sticky top-0 z-30 bg-[#0A0A0F]/90 backdrop-blur-md px-4 py-3 border-b border-[#262640] flex items-center justify-between">
        <div className="flex items-center gap-2.5">
          <div className="relative">
            <img
              src={profile.avatarUrl}
              alt={profile.inGameName}
              className="w-10 h-10 rounded-full object-cover border-2 border-[#C8FF3D] shadow-[0_0_10px_rgba(200,255,61,0.3)]"
            />
            <span className="absolute -bottom-1 -right-1 px-1 py-0.2 bg-[#7B5CFF] text-white text-xs font-black rounded-full border border-[#0A0A0F]">
              42
            </span>
          </div>
          <div>
            <span className="text-xs text-[#A7A7C0] uppercase tracking-wider block">
              Welcome back
            </span>
            <h2 className="text-sm font-black text-white flex items-center gap-1">
              Hey, {profile.name.split(' ')[0]}
              <span className="text-xs">⚡</span>
            </h2>
          </div>
        </div>

        {/* Chips row: Coins ONLY + Bell (NO wallet chip) */}
        <div className="flex items-center gap-2">
          {/* Coins chip */}
          <button
            onClick={onOpenRewards}
            className="flex items-center gap-1.5 px-3 py-1.5 bg-[#1B1B2B] border border-[#FFB800]/40 rounded-full text-xs font-black text-[#FFB800] hover:border-[#FFB800] transition-all shadow-[0_0_10px_rgba(255,184,0,0.15)] active:scale-95"
            title="Earned Loyalty Coins"
          >
            <Coins className="w-3.5 h-3.5 text-[#FFB800]" />
            <span>{coins}</span>
          </button>

          {/* Notification bell */}
          <button
            onClick={onOpenNotifications}
            className="relative p-2 bg-[#1B1B2B] border border-[#262640] rounded-full text-[#A7A7C0] hover:text-white hover:border-[#7B5CFF] transition-all"
            title="Notifications"
          >
            <Bell className="w-4 h-4" />
            {unreadNotifsCount > 0 && (
              <span className="absolute -top-1 -right-1 w-4 h-4 bg-[#FF4D6D] text-white text-xs font-black rounded-full flex items-center justify-center animate-pulse">
                {unreadNotifsCount}
              </span>
            )}
          </button>
        </div>
      </div>

      <div className="px-4 space-y-4">
        {/* Banner Carousel with Join Free button & Sponsor Label */}
        <div className="relative rounded-[24px] overflow-hidden border border-[#262640] shadow-2xl h-48">
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
              <div className="absolute inset-0 bg-gradient-to-t from-[#0A0A0F] via-[#0A0A0F]/70 to-transparent p-4 flex flex-col justify-end">
                <div className="flex items-center gap-2 mb-1">
                  <span className="px-2 py-0.5 bg-[#C8FF3D] text-black font-black text-xs rounded-full uppercase tracking-wider shadow">
                    {banner.tag}
                  </span>
                  <span className="text-xs text-[#A7A7C0] font-semibold">
                    {banner.sponsor}
                  </span>
                </div>
                <h3 className="text-base font-black text-white leading-tight mb-1">
                  {banner.title}
                </h3>
                <div className="flex items-center justify-between mt-1">
                  <div>
                    <span className="text-xs font-black text-white drop-shadow-[0_0_8px_rgba(200,255,61,0.4)] block">
                      {banner.prizeLabel}
                    </span>
                    <span className="text-xs text-[#7B5CFF] font-bold">
                      • Sponsor-funded prize
                    </span>
                  </div>

                  <button
                    onClick={() => onSelectCategory('Upcoming')}
                    className="px-3.5 py-1.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-black text-xs rounded-xl shadow-[0_0_12px_rgba(200,255,61,0.35)] active:scale-95 transition-all flex items-center gap-1 z-30"
                  >
                    <span>Join Free</span>
                    <ChevronRight className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>
            </div>
          ))}

          {/* Dots navigation */}
          <div className="absolute top-3 right-4 z-20 flex gap-1">
            {CAROUSEL_BANNERS.map((_, i) => (
              <div
                key={i}
                className={`h-1.5 rounded-full transition-all ${
                  activeBanner === i ? 'w-5 bg-[#C8FF3D]' : 'w-1.5 bg-white/30'
                }`}
              />
            ))}
          </div>
        </div>

        {/* Quick Action Buttons (4 grid items) */}
        <div className="grid grid-cols-4 gap-2">
          {/* Daily Streak */}
          <button
            onClick={onDailyCheckIn}
            className={`p-2.5 rounded-2xl border flex flex-col items-center text-center transition-all active:scale-95 ${
              hasCheckedInToday
                ? 'bg-[#14141F] border-[#262640] opacity-80'
                : 'bg-[#1B1B2B] border-[#C8FF3D]/40 shadow-[0_0_12px_rgba(200,255,61,0.15)]'
            }`}
          >
            <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/15 flex items-center justify-center text-[#C8FF3D] mb-1">
              <CalendarCheck className="w-4 h-4" />
            </div>
            <span className="text-xs font-bold text-white block">Streak</span>
            <span className="text-xs text-[#C8FF3D] font-extrabold">{hasCheckedInToday ? 'Claimed' : '+50 Coins'}</span>
          </button>

          {/* Daily Spin */}
          <button
            onClick={onOpenSpinWheel}
            className="p-2.5 rounded-2xl bg-[#1B1B2B] border border-[#262640] hover:border-[#FFB800]/50 flex flex-col items-center text-center transition-all active:scale-95"
          >
            <div className="w-8 h-8 rounded-xl bg-[#FFB800]/15 flex items-center justify-center text-[#FFB800] mb-1">
              <RotateCw className="w-4 h-4" />
            </div>
            <span className="text-xs font-bold text-white block">Daily Spin</span>
            <span className="text-xs text-[#FFB800] font-extrabold">bonus coins</span>
          </button>

          {/* Squad */}
          <button
            onClick={onOpenSquad}
            className="p-2.5 rounded-2xl bg-[#1B1B2B] border border-[#262640] hover:border-[#7B5CFF]/50 flex flex-col items-center text-center transition-all active:scale-95"
          >
            <div className="w-8 h-8 rounded-xl bg-[#7B5CFF]/15 flex items-center justify-center text-[#7B5CFF] mb-1">
              <Users className="w-4 h-4" />
            </div>
            <span className="text-xs font-bold text-white block">My Squad</span>
            <span className="text-xs text-[#7B5CFF] font-extrabold">4v4 Ready</span>
          </button>

          {/* Leaderboard */}
          <button
            onClick={onOpenLeaderboard}
            className="p-2.5 rounded-2xl bg-[#1B1B2B] border border-[#262640] hover:border-[#C8FF3D]/50 flex flex-col items-center text-center transition-all active:scale-95"
          >
            <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/15 flex items-center justify-center text-[#C8FF3D] mb-1">
              <Trophy className="w-4 h-4" />
            </div>
            <span className="text-xs font-bold text-white block">Ranks</span>
            <span className="text-xs text-[#C8FF3D] font-extrabold">Top Players</span>
          </button>
        </div>

        {/* Pinned "Next Match / My Match" Countdown Card */}
        {myJoinedMatch && (
          <div
            onClick={() => onOpenMatchDetail(myJoinedMatch)}
            className="bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border-2 border-[#C8FF3D] rounded-2xl p-3.5 shadow-[0_0_20px_rgba(200,255,61,0.2)] cursor-pointer hover:border-[#b5f02c] transition-all group"
          >
            <div className="flex items-center justify-between mb-2">
              <div className="flex items-center gap-1.5">
                <span className="w-2 h-2 rounded-full bg-[#C8FF3D] animate-ping" />
                <span className="text-xs font-black uppercase tracking-wider text-[#C8FF3D]">
                  NEXT MATCH KICK-OFF
                </span>
              </div>
              <div className="flex items-center gap-1 text-xs bg-[#0A0A0F] border border-[#262640] px-2.5 py-0.5 rounded-full font-mono text-[#C8FF3D]">
                <Clock className="w-3 h-3 text-[#C8FF3D]" />
                <span>Room reveals in {roomCountdown}</span>
              </div>
            </div>

            <div className="flex items-center justify-between">
              <div>
                <h4 className="text-sm font-black text-white group-hover:text-[#C8FF3D] transition-colors">
                  {myJoinedMatch.title}
                </h4>
                <div className="flex items-center gap-2 mt-0.5 text-xs text-[#A7A7C0]">
                  <span>{myJoinedMatch.mode}</span>
                  <span>•</span>
                  <span>{myJoinedMatch.map}</span>
                  <span>•</span>
                  <span className="text-white font-bold">
                    ₹{myJoinedMatch.prizePool.toLocaleString()} Pool
                  </span>
                  <span>•</span>
                  <span className="text-[#7B5CFF] font-bold">Sponsor-funded</span>
                </div>
              </div>

              <div className="flex items-center gap-1.5 shrink-0 whitespace-nowrap ml-3">
                <span className="text-xs font-black text-[#C8FF3D] bg-[#C8FF3D]/15 px-3.5 py-2 rounded-xl border border-[#C8FF3D]/40 shadow whitespace-nowrap inline-flex items-center gap-1">
                  View Room
                  <ChevronRight className="w-3.5 h-3.5 text-[#C8FF3D] group-hover:translate-x-0.5 transition-transform" />
                </span>
              </div>
            </div>
          </div>
        )}

        {/* Game Mode Filters (With fade edge and pr-8 so last chip never clips) */}
        <div className="relative">
          <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-1 pr-8">
            {gameModes.map((mode) => (
              <button
                key={mode.id}
                onClick={() => {
                  setSelectedGameMode(mode.id);
                  onSelectCategory(mode.id);
                }}
                className={`px-3 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition-all border shrink-0 ${
                  selectedGameMode === mode.id
                    ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                    : 'bg-[#14141F] text-[#A7A7C0] border-[#262640] hover:text-white'
                }`}
              >
                {mode.label}
              </button>
            ))}
          </div>
          <div className="pointer-events-none absolute right-0 top-0 bottom-0 w-8 bg-gradient-to-l from-[#0A0A0F] to-transparent" />
        </div>

        {/* Section 1: LIVE NOW (Urgent/Live) */}
        {liveMatches.length > 0 && (
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
                <span className="w-2 h-2 rounded-full bg-[#FF4D6D] animate-ping" />
                <span className="text-[#FF4D6D]">LIVE NOW</span>
              </span>
              <button
                onClick={() => onSelectCategory('Live')}
                className="text-xs text-[#A7A7C0] hover:text-[#C8FF3D] font-bold flex items-center"
              >
                View all <ChevronRight className="w-3 h-3" />
              </button>
            </div>

            <div className="space-y-2">
              {liveMatches.map((m) => (
                <div
                  key={m.id}
                  onClick={() => onOpenMatchDetail(m)}
                  className="bg-[#14141F] border border-[#262640] border-l-4 border-l-[#FF4D6D] rounded-2xl p-3 flex items-center justify-between hover:border-[#FF4D6D]/60 transition-all cursor-pointer group"
                >
                  <div className="flex items-center gap-3">
                    <img
                      src={m.mapImage}
                      alt={m.map}
                      className="w-12 h-12 rounded-xl object-cover"
                    />
                    <div>
                      <div className="flex items-center gap-1.5">
                        <span className="px-1.5 py-0.2 bg-[#FF4D6D]/20 text-[#FF4D6D] text-xs font-black rounded-full uppercase">
                          LIVE
                        </span>
                        <h4 className="text-xs font-black text-white group-hover:text-[#C8FF3D] transition-colors truncate max-w-[170px]">
                          {m.title}
                        </h4>
                      </div>
                      <span className="text-xs text-[#A7A7C0] block mt-0.5">
                        {m.mode} • {m.map} • {m.sponsorName}
                      </span>
                    </div>
                  </div>

                  <div className="text-right">
                    <span className="text-xs font-black text-white block">
                      ₹{m.prizePool.toLocaleString()}
                    </span>
                    <span className="text-xs text-[#C8FF3D] font-bold">
                      FREE ENTRY
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Section 2: STARTING SOON */}
        <div className="space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
              <Zap className="w-3.5 h-3.5 text-[#C8FF3D]" /> Starting Soon
            </span>
            <button
              onClick={() => onSelectCategory('Upcoming')}
              className="text-xs text-[#A7A7C0] hover:text-[#C8FF3D] font-bold flex items-center"
            >
              See all <ChevronRight className="w-3 h-3" />
            </button>
          </div>

          <div className="space-y-2">
            {upcomingMatches.slice(0, 3).map((m) => {
              const remainingSlots = m.maxSlots - m.joinedSlots;
              const isUnderTenPercent = (remainingSlots / m.maxSlots) <= 0.10;
              const isNearStart = m.status === 'Live' || (m.schedule && m.schedule.toLowerCase().includes('live')) || (m.schedule && m.schedule.toLowerCase().includes('min') && !m.schedule.toLowerCase().includes('tomorrow'));
              const isUrgent = isUnderTenPercent || isNearStart;
              const slotsPercent = Math.min(100, (m.joinedSlots / m.maxSlots) * 100);
              return (
                <div
                  key={m.id}
                  onClick={() => onOpenMatchDetail(m)}
                  className="bg-[#14141F] border border-[#262640] border-l-4 border-l-[#C8FF3D] rounded-2xl p-3.5 space-y-2.5 hover:border-[#C8FF3D]/50 transition-all cursor-pointer group"
                >
                  <div className="flex items-start justify-between">
                    <div>
                      <div className="flex items-center gap-1.5 mb-1">
                        <span className="px-2 py-0.5 bg-[#C8FF3D] text-black font-black text-xs rounded-full uppercase tracking-wider">
                          FREE ENTRY
                        </span>
                        <span className="text-xs text-[#7B5CFF] font-bold">
                          {m.sponsorName}
                        </span>
                      </div>
                      <h4 className="text-xs font-black text-white group-hover:text-[#C8FF3D] transition-colors leading-snug">
                        {m.title}
                      </h4>
                    </div>

                    <div className="text-right shrink-0">
                      <span className="text-sm font-black text-white drop-shadow-[0_0_8px_rgba(200,255,61,0.3)] block">
                        ₹{m.prizePool.toLocaleString()}
                      </span>
                      <span className="text-xs text-[#A7A7C0]">
                        Sponsor Pool
                      </span>
                    </div>
                  </div>

                  {/* Meter & Join Row */}
                  <div className="flex items-center justify-between text-xs pt-1 border-t border-[#262640]">
                    <div className="flex items-center gap-2">
                      <div className="w-20 bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                        <div
                          className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D]"
                          style={{ width: `${slotsPercent}%` }}
                        />
                      </div>
                      <span className="text-xs text-[#A7A7C0]">
                        {m.joinedSlots}/{m.maxSlots} Slots
                      </span>
                    </div>

                    <div className="flex items-center gap-1 text-xs font-black text-[#C8FF3D]">
                      <span>{m.userJoined ? 'Registered' : 'Join Free'}</span>
                      <ChevronRight className="w-3.5 h-3.5" />
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Section 3: BEGINNER FRIENDLY (Rookie Division) */}
        {beginnerMatches.length > 0 && (
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
                <ShieldCheck className="w-3.5 h-3.5 text-[#7B5CFF]" /> Beginner Friendly
              </span>
              <span className="text-xs text-[#7B5CFF] font-bold">All Ranks Welcome</span>
            </div>

            <div className="space-y-2">
              {beginnerMatches.slice(0, 2).map((m) => (
                <div
                  key={m.id}
                  onClick={() => onOpenMatchDetail(m)}
                  className="bg-[#14141F] border border-[#262640] border-l-4 border-l-[#7B5CFF] rounded-2xl p-3 flex items-center justify-between hover:border-[#7B5CFF]/60 transition-all cursor-pointer group"
                >
                  <div className="flex items-center gap-3">
                    <img
                      src={m.mapImage}
                      alt={m.map}
                      className="w-11 h-11 rounded-xl object-cover"
                    />
                    <div>
                      <h4 className="text-xs font-black text-white group-hover:text-[#C8FF3D] transition-colors truncate max-w-[170px]">
                        {m.title}
                      </h4>
                      <span className="text-xs text-[#A7A7C0] block mt-0.5">
                        {m.mode} • {m.map} • +10 XP per kill
                      </span>
                    </div>
                  </div>

                  <div className="text-right">
                    <span className="text-xs font-black text-white block">
                      ₹{m.prizePool.toLocaleString()}
                    </span>
                    <span className="text-xs text-[#C8FF3D] font-extrabold uppercase">
                      FREE ENTRY
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Referral Card (Coins only, no cash bonus) */}
        <div className="bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border border-[#7B5CFF]/40 rounded-2xl p-4 flex items-center justify-between shadow-lg">
          <div>
            <span className="text-xs text-[#C8FF3D] font-extrabold uppercase tracking-wider block">
              Referral Rewards
            </span>
            <div className="text-xs font-black text-white mt-0.5">
              Invite Friends: both of you get 100 bonus coins
            </div>
            <span className="text-xs text-[#A7A7C0] block mt-0.5">
              Code: <strong className="font-mono text-[#FFB800]">{profile.referralCode}</strong>
            </span>
          </div>

          <button
            onClick={onShareReferral}
            className="px-3 py-2 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow active:scale-95 transition-all shrink-0 ml-2"
          >
            Copy Code
          </button>
        </div>

        {/* Fair Play & Trust Subtitle */}
        <div className="p-3 bg-[#0A0A0F] border border-[#262640] rounded-2xl text-center space-y-1">
          <div className="flex items-center justify-center gap-1.5 text-xs font-bold text-white">
            <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" />
            <span>Fair Play Guarantee</span>
          </div>
          <p className="text-xs text-[#A7A7C0]">
            Free entry. Prizes are sponsor-funded. Anti-cheat automated telemetry active on all custom rooms.
          </p>
        </div>
      </div>
    </div>
  );
};
