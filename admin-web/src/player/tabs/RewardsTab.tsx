import React, { useState } from 'react';
import { Mission, RewardStoreItem } from '../types';
import {
  Coins,
  CalendarCheck,
  RotateCw,
  Gift,
  ShoppingBag,
  Check,
  CheckCircle2,
  Sparkles,
  Award,
  ChevronRight,
  Info
} from 'lucide-react';

interface RewardsTabProps {
  coins: number;
  missions: Mission[];
  storeItems: RewardStoreItem[];
  hasCheckedInToday: boolean;
  onDailyCheckIn: () => void;
  onOpenSpinWheel: () => void;
  onClaimMission: (missionId: string) => void;
  onRedeemStoreItem: (item: RewardStoreItem) => void;
}

export const RewardsTab: React.FC<RewardsTabProps> = ({
  coins,
  missions,
  storeItems,
  hasCheckedInToday,
  onDailyCheckIn,
  onOpenSpinWheel,
  onClaimMission,
  onRedeemStoreItem,
}) => {
  const [activeSubTab, setActiveSubTab] = useState<'missions' | 'store'>('missions');

  const streakDays = [
    { day: 1, coins: 25 },
    { day: 2, coins: 30 },
    { day: 3, coins: 40 },
    { day: 4, coins: 50 },
    { day: 5, coins: 60 },
    { day: 6, coins: 80 },
    { day: 7, coins: 150 },
  ];

  return (
    <div className="space-y-4 pb-12 animate-fade-in">
      {/* Header */}
      <div className="px-4 pt-3 pb-1">
        <h2 className="text-lg font-black text-white flex items-center gap-2">
          <Coins className="w-5 h-5 text-[#FFB800]" /> Loyalty Rewards
        </h2>
        <p className="text-xs text-[#A7A7C0]">Earn coins through matches, streaks & missions. Zero deposits.</p>
      </div>

      {/* Non-cash policy notice banner */}
      <div className="px-4">
        <div className="flex items-center gap-2 px-3 py-2 bg-[#1B1B2B] border border-[#262640] rounded-xl text-[10px] text-[#A7A7C0]">
          <Info className="w-3.5 h-3.5 text-[#FFB800] shrink-0" />
          <span>Loyalty coins are non-cash game credits earned by playing and cannot be purchased or converted to real money.</span>
        </div>
      </div>

      {/* Coin Balance Hero */}
      <div className="px-4">
        <div className="relative rounded-[24px] overflow-hidden bg-[#14141F] border border-[#FFB800]/40 p-5 shadow-2xl flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-[10px] text-[#FFB800] font-bold uppercase tracking-wider block">
              Available Loyalty Coins
            </span>
            <div className="flex items-center gap-2">
              <span className="text-3xl font-black text-white">{coins}</span>
              <Coins className="w-6 h-6 text-[#FFB800] animate-bounce" />
            </div>
            <span className="text-[10px] text-[#A7A7C0] block">
              Redeem for match tickets, custom HUD themes & avatar frames
            </span>
          </div>

          <button
            onClick={onOpenSpinWheel}
            className="flex flex-col items-center justify-center p-3 bg-[#FFB800]/10 border border-[#FFB800]/40 rounded-2xl text-[#FFB800] hover:bg-[#FFB800]/20 transition-all active:scale-95 shrink-0"
          >
            <RotateCw className="w-6 h-6 mb-1 animate-spin-slow" />
            <span className="text-[10px] font-black uppercase">Daily Spin</span>
          </button>
        </div>
      </div>

      {/* 7-Day Check-in Streak Calendar (Fixed, zero stray gap) */}
      <div className="px-4 space-y-2">
        <div className="flex items-center justify-between">
          <span className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
            <CalendarCheck className="w-4 h-4 text-[#C8FF3D]" /> 7-Day Login Streak
          </span>
          <span className="text-[10px] text-[#C8FF3D] font-bold">
            {hasCheckedInToday ? 'Checked in today!' : 'Tap to claim today'}
          </span>
        </div>

        <div className="bg-[#14141F] border border-[#262640] p-3.5 rounded-2xl">
          <div className="grid grid-cols-7 gap-1 text-center mb-3">
            {streakDays.map((s, idx) => {
              const isChecked = idx < 4 || (idx === 4 && hasCheckedInToday);
              const isCurrentDay = idx === 4;

              return (
                <div
                  key={s.day}
                  className={`p-1.5 rounded-xl border flex flex-col items-center justify-between min-h-[58px] transition-all ${
                    isChecked
                      ? 'bg-[#C8FF3D]/10 border-[#C8FF3D]/40 text-[#C8FF3D]'
                      : isCurrentDay
                      ? 'bg-[#1B1B2B] border-[#FFB800] text-white shadow-[0_0_8px_rgba(255,184,0,0.2)]'
                      : 'bg-[#0A0A0F] border-[#262640] text-[#A7A7C0]'
                  }`}
                >
                  <span className="text-[9px] font-bold">D{s.day}</span>
                  {isChecked ? (
                    <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
                  ) : (
                    <Coins className="w-3.5 h-3.5 text-[#FFB800]" />
                  )}
                  <span className="text-[9px] font-extrabold">+{s.coins}</span>
                </div>
              );
            })}
          </div>

          <button
            onClick={onDailyCheckIn}
            disabled={hasCheckedInToday}
            className={`w-full py-2.5 rounded-xl font-black text-xs flex items-center justify-center gap-2 transition-all ${
              hasCheckedInToday
                ? 'bg-[#1B1B2B] text-[#A7A7C0] border border-[#262640] cursor-default'
                : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_15px_rgba(200,255,61,0.25)] active:scale-95'
            }`}
          >
            {hasCheckedInToday ? (
              <>
                <Check className="w-4 h-4 text-[#C8FF3D]" />
                <span>Streak Claimed Today (+50 Coins)</span>
              </>
            ) : (
              <>
                <Sparkles className="w-4 h-4" />
                <span>Claim Day 5 Streak (+50 Coins)</span>
              </>
            )}
          </button>
        </div>
      </div>

      {/* Sub Tabs: Missions vs Store */}
      <div className="px-4">
        <div className="flex bg-[#14141F] p-1 rounded-2xl border border-[#262640]">
          <button
            onClick={() => setActiveSubTab('missions')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'missions'
                ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Quests & Missions
          </button>
          <button
            onClick={() => setActiveSubTab('store')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'store'
                ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Cosmetic Store
          </button>
        </div>
      </div>

      {/* Sub Tab: Missions */}
      {activeSubTab === 'missions' && (
        <div className="px-4 space-y-2">
          {missions.map((m) => {
            const isCompleted = m.current >= m.target;
            const progress = Math.min(100, (m.current / m.target) * 100);

            return (
              <div
                key={m.id}
                className="bg-[#14141F] border border-[#262640] rounded-2xl p-3.5 space-y-2"
              >
                <div className="flex items-start justify-between">
                  <div className="space-y-0.5">
                    <span className="text-[9px] font-black uppercase tracking-wider text-[#7B5CFF]">
                      {m.type === 'daily' ? 'Daily Mission' : 'Weekly Quest'}
                    </span>
                    <h4 className="text-xs font-black text-white">{m.title}</h4>
                  </div>

                  <div className="flex items-center gap-1 text-xs font-black text-[#FFB800] bg-[#FFB800]/10 px-2 py-0.5 rounded-full border border-[#FFB800]/30 shrink-0">
                    <Coins className="w-3.5 h-3.5" />
                    <span>+{m.rewardCoins}</span>
                  </div>
                </div>

                <div className="flex items-center justify-between text-xs pt-1">
                  <div className="flex items-center gap-2 flex-1 mr-3">
                    <div className="flex-1 max-w-[120px] bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                      <div
                        className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D]"
                        style={{ width: `${progress}%` }}
                      />
                    </div>
                    <span className="text-[10px] text-[#A7A7C0] font-mono">
                      {m.current}/{m.target}
                    </span>
                  </div>

                  {m.isClaimed ? (
                    <span className="text-[11px] font-bold text-[#A7A7C0] flex items-center gap-1">
                      <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> Claimed
                    </span>
                  ) : isCompleted ? (
                    <button
                      onClick={() => onClaimMission(m.id)}
                      className="px-3 py-1 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-[11px] rounded-xl shadow active:scale-95"
                    >
                      Claim Reward
                    </button>
                  ) : (
                    <span className="text-[11px] font-bold text-[#A7A7C0]">In Progress</span>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Sub Tab: Cosmetic Store */}
      {activeSubTab === 'store' && (
        <div className="px-4 grid grid-cols-2 gap-2.5">
          {storeItems.map((item) => (
            <div
              key={item.id}
              className="bg-[#14141F] border border-[#262640] rounded-2xl p-3 flex flex-col justify-between space-y-2 hover:border-[#C8FF3D]/50 transition-all"
            >
              <div className="space-y-2">
                <div className="h-24 rounded-xl overflow-hidden bg-[#0A0A0F] border border-[#262640] relative">
                  <img
                    src={item.imageUrl}
                    alt={item.title}
                    className="w-full h-full object-cover"
                  />
                  <span className="absolute top-1.5 left-1.5 px-2 py-0.5 bg-black/70 backdrop-blur-md text-[9px] font-black text-[#C8FF3D] rounded-full uppercase">
                    {item.category}
                  </span>
                </div>

                <div>
                  <h4 className="text-xs font-black text-white line-clamp-1">{item.title}</h4>
                  <p className="text-[10px] text-[#A7A7C0] line-clamp-2 mt-0.5">{item.description}</p>
                </div>
              </div>

              <div className="pt-2 border-t border-[#262640] flex items-center justify-between">
                <div className="flex items-center gap-1 text-xs font-black text-[#FFB800]">
                  <Coins className="w-3.5 h-3.5" />
                  <span>{item.coins}</span>
                </div>

                {item.isPurchased ? (
                  <span className="text-[10px] font-black text-[#C8FF3D] bg-[#C8FF3D]/10 px-2 py-1 rounded-lg">
                    Equipped
                  </span>
                ) : (
                  <button
                    onClick={() => onRedeemStoreItem(item)}
                    disabled={coins < item.coins}
                    className="px-2.5 py-1 bg-[#C8FF3D] hover:bg-[#b5f02c] disabled:opacity-40 text-black font-extrabold text-[10px] rounded-lg shadow active:scale-95"
                  >
                    Redeem
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
