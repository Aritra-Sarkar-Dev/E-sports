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
  const [selectedItemToRedeem, setSelectedItemToRedeem] = useState<RewardStoreItem | null>(null);

  const streakDays = [
    { day: 1, coins: 25, label: 'Day 1' },
    { day: 2, coins: 30, label: 'Day 2' },
    { day: 3, coins: 40, label: 'Day 3' },
    { day: 4, coins: 50, label: 'Day 4' },
    { day: 5, coins: 60, label: 'Day 5' },
    { day: 6, coins: 80, label: 'Day 6' },
    { day: 7, coins: 200, label: 'Day 7 (Crate)' },
  ];

  return (
    <div className="space-y-4 pb-12 animate-fade-in">
      {/* Header */}
      <div className="px-4 pt-3 pb-1">
        <h2 className="text-lg font-black text-white flex items-center gap-2">
          <Coins className="w-5 h-5 text-[#FFB300]" /> Loyalty Rewards
        </h2>
        <p className="text-xs text-[#9498AE]">Play tournaments, complete missions & redeem exclusive gear</p>
      </div>

      {/* Non-cash policy notice banner */}
      <div className="px-4">
        <div className="flex items-center gap-2 px-3 py-2 bg-[#1C1C2E]/60 border border-[#23233A] rounded-xl text-[10px] text-[#9498AE]">
          <Info className="w-3.5 h-3.5 text-[#FFB300] shrink-0" />
          <span>Loyalty coins are non-cash game credits earned by playing and cannot be purchased or converted to real money.</span>
        </div>
      </div>

      {/* Coin Balance Hero */}
      <div className="px-4">
        <div className="relative rounded-[24px] overflow-hidden bg-gradient-to-br from-[#1C1C2E] via-[#2A1D0B] to-[#14141F] border border-[#FFB300]/40 p-5 shadow-2xl flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-[10px] text-[#FFB300] font-bold uppercase tracking-wider block">
              Available Loyalty Coins
            </span>
            <div className="flex items-center gap-2">
              <span className="text-3xl font-black text-white">{coins}</span>
              <Coins className="w-6 h-6 text-[#FFB300] animate-bounce" />
            </div>
            <span className="text-[10px] text-[#9498AE] block">
              Redeem for match tickets, custom HUD themes & avatar frames
            </span>
          </div>

          <button
            onClick={onOpenSpinWheel}
            className="flex flex-col items-center justify-center p-3 bg-[#FFB300]/10 border border-[#FFB300]/40 rounded-2xl text-[#FFB300] hover:bg-[#FFB300]/20 transition-all active:scale-95 shrink-0"
          >
            <RotateCw className="w-6 h-6 mb-1 animate-spin-slow" />
            <span className="text-[10px] font-extrabold uppercase">Daily Spin</span>
          </button>
        </div>
      </div>

      {/* 7-Day Check-in Streak Calendar */}
      <div className="px-4 space-y-2">
        <div className="flex items-center justify-between">
          <span className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
            <CalendarCheck className="w-4 h-4 text-[#C8FF3D]" /> 7-Day Login Streak
          </span>
          <span className="text-[10px] text-[#C8FF3D] font-bold">
            {hasCheckedInToday ? 'Checked in today!' : 'Tap to claim today'}
          </span>
        </div>

        <div className="bg-[#14141F] border border-[#23233A] p-3.5 rounded-2xl">
          <div className="grid grid-cols-7 gap-1.5 text-center mb-3">
            {streakDays.map((s, idx) => {
              const isChecked = idx < 5; // e.g. day 1 to 5 checked
              const isToday = idx === 4;
              return (
                <div
                  key={s.day}
                  className={`p-1.5 rounded-xl border flex flex-col items-center justify-between min-h-[58px] transition-all ${
                    isChecked
                      ? 'bg-[#C8FF3D]/10 border-[#C8FF3D]/40 text-[#C8FF3D]'
                      : 'bg-[#0E0E17] border-[#23233A] text-slate-400'
                  }`}
                >
                  <span className="text-[9px] font-bold">{s.label.split(' ')[0]} {s.day}</span>
                  {isChecked ? (
                    <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
                  ) : (
                    <Coins className="w-4 h-4 text-[#FFB300]" />
                  )}
                  <span className="text-[9px] font-extrabold">+{s.coins}</span>
                </div>
              );
            })}
          </div>

          <button
            onClick={onDailyCheckIn}
            disabled={hasCheckedInToday}
            className={`w-full py-2.5 rounded-xl font-bold text-xs flex items-center justify-center gap-2 transition-all ${
              hasCheckedInToday
                ? 'bg-[#1C1C2E] text-slate-400 border border-[#23233A] cursor-default'
                : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_15px_rgba(200,255,61,0.25)] active:scale-95'
            }`}
          >
            <CalendarCheck className="w-4 h-4" />
            {hasCheckedInToday ? 'Streak Claimed (+60 Coins Today)' : 'Claim Day 5 Streak Bonus'}
          </button>
        </div>
      </div>

      {/* Switcher: Daily Missions vs Coin Store */}
      <div className="px-4">
        <div className="flex bg-[#14141F] p-1 rounded-2xl border border-[#23233A]">
          <button
            onClick={() => setActiveSubTab('missions')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'missions'
                ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                : 'text-[#9498AE] hover:text-white'
            }`}
          >
            Missions & Quests
          </button>
          <button
            onClick={() => setActiveSubTab('store')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'store'
                ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                : 'text-[#9498AE] hover:text-white'
            }`}
          >
            Coin Store
          </button>
        </div>
      </div>

      {/* Tab 1: Missions */}
      {activeSubTab === 'missions' && (
        <div className="px-4 space-y-2.5">
          {missions.map((m) => {
            const isCompleted = m.current >= m.target;
            const percent = Math.min(100, Math.round((m.current / m.target) * 100));

            return (
              <div
                key={m.id}
                className="p-3.5 bg-[#14141F] border border-[#23233A] rounded-2xl flex items-center justify-between gap-3 hover:border-slate-600 transition-all"
              >
                <div className="flex-1 space-y-1.5">
                  <div className="flex items-center gap-2">
                    <span className="text-[9px] px-2 py-0.5 bg-[#7B5CFF]/15 text-[#7B5CFF] rounded font-extrabold uppercase">
                      {m.type}
                    </span>
                    <h4 className="text-xs font-bold text-white line-clamp-1">{m.title}</h4>
                  </div>

                  {/* Progress bar */}
                  <div className="w-full bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D] rounded-full"
                      style={{ width: `${percent}%` }}
                    />
                  </div>

                  <div className="flex justify-between items-center text-[10px] text-[#9498AE]">
                    <span>Progress: {m.current} / {m.target}</span>
                    <span className="text-[#FFB300] font-bold flex items-center gap-1">
                      <Coins className="w-3 h-3" /> +{m.rewardCoins}
                    </span>
                  </div>
                </div>

                <div className="shrink-0">
                  {m.isClaimed ? (
                    <button
                      disabled
                      className="px-3 py-1.5 bg-[#1C1C2E] text-slate-500 rounded-xl text-[10px] font-bold border border-[#23233A]"
                    >
                      Claimed
                    </button>
                  ) : isCompleted ? (
                    <button
                      onClick={() => onClaimMission(m.id)}
                      className="px-3 py-1.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold rounded-xl text-xs shadow-[0_0_10px_rgba(200,255,61,0.25)] active:scale-95 animate-pulse"
                    >
                      Claim
                    </button>
                  ) : (
                    <button
                      disabled
                      className="px-3 py-1.5 bg-[#1C1C2E] text-[#9498AE] rounded-xl text-[10px] font-bold border border-[#23233A]"
                    >
                      In Progress
                    </button>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Tab 2: Store */}
      {activeSubTab === 'store' && (
        <div className="px-4 space-y-3">
          <div className="grid grid-cols-2 gap-3">
            {storeItems.map((item) => (
              <div
                key={item.id}
                className="bg-[#14141F] border border-[#23233A] rounded-2xl overflow-hidden p-3 flex flex-col justify-between hover:border-[#FFB300]/40 transition-all shadow-lg"
              >
                <div>
                  <div className="relative h-24 rounded-xl overflow-hidden mb-2 bg-[#0A0A0F]">
                    <img
                      src={item.imageUrl}
                      alt={item.title}
                      className="w-full h-full object-cover"
                    />
                    <span className="absolute top-1.5 right-1.5 px-2 py-0.5 bg-black/70 backdrop-blur-md rounded-md text-[9px] font-extrabold uppercase text-[#C8FF3D]">
                      {item.category}
                    </span>
                  </div>

                  <h4 className="text-xs font-bold text-white line-clamp-1">{item.title}</h4>
                  <p className="text-[10px] text-[#9498AE] line-clamp-2 mt-0.5">{item.description}</p>
                </div>

                <div className="pt-3 border-t border-[#23233A] mt-2 flex items-center justify-between">
                  <div className="flex items-center gap-1 text-xs font-black text-[#FFB300]">
                    <Coins className="w-3.5 h-3.5" />
                    <span>{item.coins}</span>
                  </div>

                  <button
                    onClick={() => onRedeemStoreItem(item)}
                    disabled={coins < item.coins}
                    className={`px-3 py-1 text-[11px] font-extrabold rounded-xl transition-all ${
                      coins < item.coins
                        ? 'bg-[#1C1C2E] text-slate-500 border border-[#23233A] cursor-not-allowed'
                        : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_10px_rgba(200,255,61,0.2)] active:scale-95'
                    }`}
                  >
                    Redeem
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
