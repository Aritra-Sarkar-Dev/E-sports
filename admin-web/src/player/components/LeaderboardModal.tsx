import React, { useState } from 'react';
import { LeaderboardPlayer } from '../types';
import { X, Trophy, Crown, Flame, Award } from 'lucide-react';

interface LeaderboardModalProps {
  players: LeaderboardPlayer[];
  onClose: () => void;
}

export const LeaderboardModal: React.FC<LeaderboardModalProps> = ({ players, onClose }) => {
  const [filter, setFilter] = useState<'weekly' | 'alltime'>('weekly');

  const top3 = players.slice(0, 3);
  const remaining = players.slice(3);

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D]">
              <Trophy className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-black text-white">Esports Leaderboard</h3>
              <p className="text-[10px] text-[#A7A7C0]">Top players & champion squads</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Filter pills */}
        <div className="flex p-3 gap-2 bg-[#0A0A0F] border-b border-[#262640]">
          {(['weekly', 'alltime'] as const).map((period) => (
            <button
              key={period}
              onClick={() => setFilter(period)}
              className={`flex-1 py-1.5 text-xs font-bold rounded-xl capitalize transition-all ${
                filter === period
                  ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                  : 'bg-[#1B1B2B] text-[#A7A7C0] hover:text-white'
              }`}
            >
              {period === 'alltime' ? 'All-Time' : 'Weekly Cup'}
            </button>
          ))}
        </div>

        {/* Scrollable body */}
        <div className="p-4 overflow-y-auto space-y-4 flex-1">
          {/* Top 3 Podium */}
          {top3.length >= 3 && (
            <div className="grid grid-cols-3 gap-2 pt-4 pb-2 items-end text-center">
              {/* 2nd Place */}
              <div className="flex flex-col items-center bg-[#1B1B2B] border border-slate-400/30 rounded-2xl p-2.5 relative">
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
                <span className="text-[10px] text-[#C8FF3D] font-extrabold mt-0.5">
                  {top3[1].points} PTS
                </span>
                <span className="text-[9px] text-[#A7A7C0]">
                  {top3[1].sponsorPrizesWon} Prizes Won
                </span>
              </div>

              {/* 1st Place (Center taller) */}
              <div className="flex flex-col items-center bg-gradient-to-b from-[#C8FF3D]/20 to-[#1B1B2B] border-2 border-[#C8FF3D] rounded-2xl p-3 relative shadow-[0_0_20px_rgba(200,255,61,0.2)]">
                <div className="absolute -top-4 w-7 h-7 rounded-full bg-[#C8FF3D] text-black font-black text-sm flex items-center justify-center shadow">
                  <Crown className="w-4 h-4" />
                </div>
                <img
                  src={top3[0].avatar}
                  alt={top3[0].inGameName}
                  className="w-14 h-14 rounded-full border-2 border-[#C8FF3D] object-cover mt-2 mb-1"
                />
                <span className="text-xs font-black text-white truncate max-w-[95px] block">
                  {top3[0].inGameName}
                </span>
                <span className="text-xs text-[#C8FF3D] font-black mt-0.5">
                  {top3[0].points} PTS
                </span>
                <span className="text-[9px] text-[#FFB800] font-bold">
                  {top3[0].sponsorPrizesWon} Sponsor Trophies
                </span>
              </div>

              {/* 3rd Place */}
              <div className="flex flex-col items-center bg-[#1B1B2B] border border-[#CD7F32]/40 rounded-2xl p-2.5 relative">
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
                <span className="text-[10px] text-[#C8FF3D] font-extrabold mt-0.5">
                  {top3[2].points} PTS
                </span>
                <span className="text-[9px] text-[#A7A7C0]">
                  {top3[2].sponsorPrizesWon} Prizes Won
                </span>
              </div>
            </div>
          )}

          {/* Remaining Rankings List */}
          <div className="space-y-1.5">
            {remaining.map((player) => (
              <div
                key={player.rank}
                className="bg-[#1B1B2B] border border-[#262640] rounded-xl p-2.5 flex items-center justify-between"
              >
                <div className="flex items-center gap-2.5">
                  <span className="w-6 text-center text-xs font-black text-[#A7A7C0]">
                    #{player.rank}
                  </span>
                  <img
                    src={player.avatar}
                    alt={player.inGameName}
                    className="w-8 h-8 rounded-full object-cover border border-[#262640]"
                  />
                  <div>
                    <span className="text-xs font-bold text-white block">
                      {player.inGameName}
                    </span>
                    <span className="text-[10px] text-[#A7A7C0]">
                      {player.wins} Booyah • {player.kills} Kills
                    </span>
                  </div>
                </div>

                <div className="text-right">
                  <span className="text-xs font-black text-[#C8FF3D] block">
                    {player.points} PTS
                  </span>
                  <span className="text-[9px] text-[#A7A7C0]">
                    {player.sponsorPrizesWon} Prizes Won
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
