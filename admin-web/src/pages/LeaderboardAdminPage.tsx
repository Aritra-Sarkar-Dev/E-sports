import React, { useState } from 'react';
import { Medal, Trophy, Star, RefreshCw, Flame, Award, ShieldCheck } from 'lucide-react';
import { Badge } from '../components/common/Badge';

interface LeaderboardPlayer {
  rank: number;
  ign: string;
  ffUid: string;
  score: number;
  kills: number;
  booyahs: number;
  prizeMoney: number;
  avatarSeed: string;
}

const WEEKLY_DATA: LeaderboardPlayer[] = [
  { rank: 1, ign: '⚡ Raistar_Op', ffUid: '984572910', score: 3840, kills: 74, booyahs: 9, prizeMoney: 2500, avatarSeed: 'Rai' },
  { rank: 2, ign: 'Total_Ajju99', ffUid: '512938471', score: 3410, kills: 68, booyahs: 7, prizeMoney: 1500, avatarSeed: 'Ajj' },
  { rank: 3, ign: 'Pahadi_Gaming', ffUid: '827103948', score: 3120, kills: 61, booyahs: 6, prizeMoney: 1000, avatarSeed: 'Pah' },
  { rank: 4, ign: 'VasiyoCR7', ffUid: '748291034', score: 2890, kills: 52, booyahs: 5, prizeMoney: 0, avatarSeed: 'Vas' },
  { rank: 5, ign: 'MafiaBala', ffUid: '629104829', score: 2640, kills: 48, booyahs: 4, prizeMoney: 0, avatarSeed: 'Maf' },
  { rank: 6, ign: 'KillerFF_Boss', ffUid: '483920194', score: 2310, kills: 41, booyahs: 3, prizeMoney: 0, avatarSeed: 'Kil' },
  { rank: 7, ign: 'BermudaGhost', ffUid: '392810482', score: 2150, kills: 38, booyahs: 3, prizeMoney: 0, avatarSeed: 'Ber' },
];

export const LeaderboardAdminPage: React.FC = () => {
  const [tab, setTab] = useState<'Weekly' | 'Season' | 'All-time'>('Weekly');
  const [isRecalculating, setIsRecalculating] = useState(false);
  const [recalcSuccess, setRecalcSuccess] = useState(false);

  const handleRecalculate = () => {
    setIsRecalculating(true);
    setTimeout(() => {
      setIsRecalculating(false);
      setRecalcSuccess(true);
      setTimeout(() => setRecalcSuccess(false), 3000);
    }, 1200);
  };

  return (
    <div className="space-y-6">
      {/* Header with Controls */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 shadow-xl">
        <div className="flex items-center space-x-3">
          <div className="p-3 bg-[#7B5CFF]/10 text-[#7B5CFF] rounded-[16px]">
            <Trophy className="w-6 h-6" />
          </div>
          <div>
            <h2 className="font-extrabold text-[#F5F5FA] text-base">Esports Hall of Fame</h2>
            <p className="text-xs text-[#9498AE]">Tournament ELO ratings and verified competitive scoreboards</p>
          </div>
        </div>

        <div className="flex items-center space-x-3">
          <div className="flex bg-[#0A0A0F] border border-[#23233A] p-1 rounded-[16px]">
            {(['Weekly', 'Season', 'All-time'] as const).map((t) => (
              <button
                key={t}
                onClick={() => setTab(t)}
                className={`px-3.5 py-1.5 rounded-[12px] text-xs font-bold transition-all ${
                  tab === t
                    ? 'bg-[#C8FF3D] text-black shadow-sm font-extrabold'
                    : 'text-[#9498AE] hover:text-[#F5F5FA]'
                }`}
              >
                {t}
              </button>
            ))}
          </div>

          <button
            onClick={handleRecalculate}
            disabled={isRecalculating}
            className="flex items-center space-x-2 bg-[#23233A] hover:bg-[#2E2E4A] text-[#F5F5FA] px-4 py-2 rounded-[16px] text-xs font-bold transition-all"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${isRecalculating ? 'animate-spin text-[#C8FF3D]' : ''}`} />
            <span>{isRecalculating ? 'Syncing...' : 'Recalculate Points'}</span>
          </button>
        </div>
      </div>

      {recalcSuccess && (
        <div className="p-3.5 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[16px] text-xs font-bold text-[#C8FF3D] flex items-center gap-2">
          <ShieldCheck className="w-4 h-4" />
          <span>Leaderboard ELO ratings and match points synchronized across all mobile clients!</span>
        </div>
      )}

      {/* Top 3 Podium Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {/* Rank 2 */}
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 text-center flex flex-col items-center justify-between relative order-2 md:order-1">
          <div className="w-10 h-10 rounded-full bg-slate-300 text-black font-extrabold text-sm flex items-center justify-center mb-2 shadow">
            #2
          </div>
          <div className="w-14 h-14 rounded-full bg-gradient-to-tr from-[#7B5CFF] to-[#3A24A8] flex items-center justify-center font-bold text-white text-lg mb-2">
            2
          </div>
          <p className="font-extrabold text-sm text-[#F5F5FA]">{WEEKLY_DATA[1].ign}</p>
          <p className="text-[11px] text-[#9498AE]">UID: {WEEKLY_DATA[1].ffUid}</p>
          <div className="mt-3 py-2 px-4 bg-[#0A0A0F] rounded-xl w-full flex justify-around text-xs">
            <div>
              <span className="text-[#9498AE] block text-[10px]">Points</span>
              <span className="font-extrabold text-[#C8FF3D]">{WEEKLY_DATA[1].score}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Kills</span>
              <span className="font-extrabold text-white">{WEEKLY_DATA[1].kills}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Booyah</span>
              <span className="font-extrabold text-white">{WEEKLY_DATA[1].booyahs}</span>
            </div>
          </div>
        </div>

        {/* Rank 1 (Champion) */}
        <div className="bg-gradient-to-b from-[#23233A] to-[#14141F] border-2 border-[#C8FF3D]/50 rounded-[24px] p-6 text-center flex flex-col items-center justify-between relative order-1 md:order-2 shadow-2xl shadow-[#C8FF3D]/10">
          <div className="absolute -top-3.5 bg-[#C8FF3D] text-black font-extrabold text-xs px-3 py-0.5 rounded-full uppercase tracking-wider flex items-center gap-1 shadow">
            <Flame className="w-3.5 h-3.5 fill-black" /> Champion #1
          </div>
          <div className="w-16 h-16 rounded-full bg-gradient-to-tr from-[#C8FF3D] to-[#7B5CFF] flex items-center justify-center font-black text-black text-2xl mb-2 mt-2 shadow-lg">
            👑
          </div>
          <p className="font-black text-base text-[#F5F5FA]">{WEEKLY_DATA[0].ign}</p>
          <p className="text-xs text-[#C8FF3D]">UID: {WEEKLY_DATA[0].ffUid}</p>
          <div className="mt-4 py-2.5 px-4 bg-[#0A0A0F]/90 rounded-2xl w-full flex justify-around text-xs border border-[#C8FF3D]/20">
            <div>
              <span className="text-[#9498AE] block text-[10px]">Points</span>
              <span className="font-black text-[#C8FF3D] text-sm">{WEEKLY_DATA[0].score}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Kills</span>
              <span className="font-bold text-white text-sm">{WEEKLY_DATA[0].kills}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Booyah</span>
              <span className="font-bold text-white text-sm">{WEEKLY_DATA[0].booyahs}</span>
            </div>
          </div>
        </div>

        {/* Rank 3 */}
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 text-center flex flex-col items-center justify-between relative order-3">
          <div className="w-10 h-10 rounded-full bg-amber-700 text-white font-extrabold text-sm flex items-center justify-center mb-2 shadow">
            #3
          </div>
          <div className="w-14 h-14 rounded-full bg-gradient-to-tr from-[#3A24A8] to-[#14141F] flex items-center justify-center font-bold text-white text-lg mb-2">
            3
          </div>
          <p className="font-extrabold text-sm text-[#F5F5FA]">{WEEKLY_DATA[2].ign}</p>
          <p className="text-[11px] text-[#9498AE]">UID: {WEEKLY_DATA[2].ffUid}</p>
          <div className="mt-3 py-2 px-4 bg-[#0A0A0F] rounded-xl w-full flex justify-around text-xs">
            <div>
              <span className="text-[#9498AE] block text-[10px]">Points</span>
              <span className="font-extrabold text-[#C8FF3D]">{WEEKLY_DATA[2].score}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Kills</span>
              <span className="font-extrabold text-white">{WEEKLY_DATA[2].kills}</span>
            </div>
            <div>
              <span className="text-[#9498AE] block text-[10px]">Booyah</span>
              <span className="font-extrabold text-white">{WEEKLY_DATA[2].booyahs}</span>
            </div>
          </div>
        </div>
      </div>

      {/* Ranked Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden shadow-xl">
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between">
          <h3 className="font-bold text-sm text-[#F5F5FA]">Complete Ranked Standings ({tab})</h3>
          <span className="text-xs text-[#9498AE]">Top 50 Displayed</span>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-[#0A0A0F]/80 text-[#9498AE] uppercase text-[10px] tracking-wider border-b border-[#23233A]">
                <th className="py-3 px-4 font-bold">Rank</th>
                <th className="py-3 px-4 font-bold">Player & IGN</th>
                <th className="py-3 px-4 font-bold">Free Fire UID</th>
                <th className="py-3 px-4 font-bold text-right">Match Points</th>
                <th className="py-3 px-4 font-bold text-right">Total Kills</th>
                <th className="py-3 px-4 font-bold text-right">Booyahs</th>
                <th className="py-3 px-4 font-bold text-right">Sponsor Bounty (₹)</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {WEEKLY_DATA.map((player) => (
                <tr key={player.ffUid} className="hover:bg-[#1A1A2A] transition-colors">
                  <td className="py-3 px-4 font-black text-sm">
                    {player.rank === 1 && <span className="text-[#C8FF3D]">🥇 #1</span>}
                    {player.rank === 2 && <span className="text-slate-300">🥈 #2</span>}
                    {player.rank === 3 && <span className="text-amber-500">🥉 #3</span>}
                    {player.rank > 3 && <span className="text-[#9498AE]">#{player.rank}</span>}
                  </td>
                  <td className="py-3 px-4 font-bold text-[#F5F5FA] flex items-center gap-2">
                    <div className="w-7 h-7 rounded-full bg-[#23233A] flex items-center justify-center text-[10px] font-bold text-[#C8FF3D]">
                      {player.avatarSeed.slice(0, 2)}
                    </div>
                    <span>{player.ign}</span>
                  </td>
                  <td className="py-3 px-4 font-mono text-[#9498AE]">{player.ffUid}</td>
                  <td className="py-3 px-4 font-extrabold text-[#C8FF3D] text-right text-sm">
                    {player.score.toLocaleString()}
                  </td>
                  <td className="py-3 px-4 font-semibold text-white text-right">{player.kills}</td>
                  <td className="py-3 px-4 font-semibold text-white text-right">{player.booyahs}</td>
                  <td className="py-3 px-4 font-bold text-right text-[#00E5FF]">
                    {player.prizeMoney > 0 ? `₹${player.prizeMoney}` : '—'}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
