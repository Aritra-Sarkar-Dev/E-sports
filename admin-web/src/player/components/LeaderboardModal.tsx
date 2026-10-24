import React, { useState } from 'react';
import { LeaderboardPlayer } from '../types';
import { X, Trophy, Flame, Crown, Medal } from 'lucide-react';

interface LeaderboardModalProps {
  players: LeaderboardPlayer[];
  onClose: () => void;
}

export const LeaderboardModal: React.FC<LeaderboardModalProps> = ({ players, onClose }) => {
  const [filter, setFilter] = useState<'daily' | 'weekly' | 'alltime'>('weekly');

  const top3 = players.slice(0, 3);
  const remaining = players.slice(3);

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header */}
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between bg-[#0E0E17]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#FFB300]/20 flex items-center justify-center text-[#FFB300]">
              <Trophy className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-base font-bold text-white">Esports Leaderboard</h3>
              <p className="text-[11px] text-[#9498AE]">Top earners & champion gladiators</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Filter pills */}
        <div className="flex p-3 gap-2 bg-[#0E0E17]/60 border-b border-[#23233A]">
          {(['daily', 'weekly', 'alltime'] as const).map((period) => (
            <button
              key={period}
              onClick={() => setFilter(period)}
              className={`flex-1 py-1.5 text-xs font-bold rounded-xl capitalize transition-all ${
                filter === period
                  ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                  : 'bg-[#1C1C2E] text-[#9498AE] hover:text-white'
              }`}
            >
              {period === 'alltime' ? 'All-Time' : period}
            </button>
          ))}
        </div>

        {/* Scrollable body */}
        <div className="p-4 overflow-y-auto space-y-4 flex-1">
          {/* Top 3 Podium */}
          {top3.length >= 3 && (
            <div className="grid grid-cols-3 gap-2 pt-4 pb-2 items-end text-center">
              {/* 2nd Place */}
              <div className="flex flex-col items-center bg-[#1C1C2E]/80 border border-slate-400/30 rounded-2xl p-2.5 relative">
                <div className="absolute -top-3 w-6 h-6 rounded-full bg-slate-300 text-black font-extrabold text-xs flex items-center justify-center shadow">
                  2
                </div>
                <img
                  src={top3[1].avatar}
                  alt={top3[1].inGameName}
                  className="w-12 h-12 rounded-full border-2 border-slate-300 object-cover mt-2 mb-1"
                />
                <span className="text-xs font-bold text-white truncate max-w-[90px] block">
                  {top3[1].inGameName}
                </span>
                <span className="text-[10px] text-slate-300 font-extrabold mt-0.5">
                  ₹{top3[1].earnings.toLocaleString()}
                </span>
              </div>

              {/* 1st Place (Center taller) */}
              <div className="flex flex-col items-center bg-gradient-to-b from-[#FFB300]/20 to-[#1C1C2E] border-2 border-[#FFB300] rounded-2xl p-3 relative shadow-[0_0_20px_rgba(255,179,0,0.2)]">
                <div className="absolute -top-4 w-7 h-7 rounded-full bg-[#FFB300] text-black font-black text-sm flex items-center justify-center shadow">
                  <Crown className="w-4 h-4" />
                </div>
                <img
                  src={top3[0].avatar}
                  alt={top3[0].inGameName}
                  className="w-14 h-14 rounded-full border-2 border-[#FFB300] object-cover mt-2 mb-1"
                />
                <span className="text-xs font-black text-white truncate max-w-[95px] block">
                  {top3[0].inGameName}
                </span>
                <span className="text-xs text-[#FFB300] font-black mt-0.5">
                  ₹{top3[0].earnings.toLocaleString()}
                </span>
                <span className="text-[9px] text-[#C8FF3D] font-bold">1st Booyah King</span>
              </div>

              {/* 3rd Place */}
              <div className="flex flex-col items-center bg-[#1C1C2E]/80 border border-[#CD7F32]/40 rounded-2xl p-2.5 relative">
                <div className="absolute -top-3 w-6 h-6 rounded-full bg-[#CD7F32] text-white font-extrabold text-xs flex items-center justify-center shadow">
                  3
                </div>
                <img
                  src={top3[2].avatar}
                  alt={top3[2].inGameName}
                  className="w-12 h-12 rounded-full border-2 border-[#CD7F32] object-cover mt-2 mb-1"
                />
                <span className="text-xs font-bold text-white truncate max-w-[90px] block">
                  {top3[2].inGameName}
                </span>
                <span className="text-[10px] text-[#CD7F32] font-extrabold mt-0.5">
                  ₹{top3[2].earnings.toLocaleString()}
                </span>
              </div>
            </div>
          )}

          {/* Remaining ranks */}
          <div className="space-y-2 pt-2">
            {remaining.map((p) => {
              const isMe = p.name.includes('(You)');
              return (
                <div
                  key={p.rank}
                  className={`flex items-center justify-between p-3 rounded-2xl border transition-all ${
                    isMe
                      ? 'bg-[#1C1C2E] border-[#C8FF3D] shadow-[0_0_15px_rgba(200,255,61,0.15)]'
                      : 'bg-[#14141F] border-[#23233A]'
                  }`}
                >
                  <div className="flex items-center gap-3">
                    <span className="w-6 text-center font-bold text-xs text-[#9498AE]">
                      #{p.rank}
                    </span>
                    <img
                      src={p.avatar}
                      alt={p.inGameName}
                      className="w-9 h-9 rounded-full object-cover border border-[#23233A]"
                    />
                    <div>
                      <span className={`text-xs font-bold block ${isMe ? 'text-[#C8FF3D]' : 'text-white'}`}>
                        {p.inGameName}
                      </span>
                      <span className="text-[10px] text-[#9498AE] flex items-center gap-2">
                        <span>Wins: {p.wins}</span>
                        <span>•</span>
                        <span>Kills: {p.kills}</span>
                      </span>
                    </div>
                  </div>

                  <div className="text-right">
                    <span className="text-xs font-extrabold text-[#FFB300] block">
                      ₹{p.earnings.toLocaleString()}
                    </span>
                    <span className="text-[10px] text-[#9498AE]">Earnings</span>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};
