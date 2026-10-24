import React, { useState, useEffect } from 'react';
import { PlayerMatch } from '../types';
import {
  X,
  Trophy,
  Users,
  Clock,
  ShieldCheck,
  Copy,
  Check,
  Share2,
  Lock,
  Key,
  Flame,
  Award
} from 'lucide-react';

interface MatchDetailModalProps {
  match: PlayerMatch | null;
  onClose: () => void;
  onJoin: (match: PlayerMatch) => void;
  onCopyRoomCreds: (text: string) => void;
}

export const MatchDetailModal: React.FC<MatchDetailModalProps> = ({
  match,
  onClose,
  onJoin,
  onCopyRoomCreds,
}) => {
  const [activeTab, setActiveTab] = useState<'overview' | 'prizes' | 'rules' | 'slots'>('overview');
  const [copiedField, setCopiedField] = useState<string | null>(null);
  const [timeLeft, setTimeLeft] = useState<string>('');

  useEffect(() => {
    if (!match) return;
    const updateCountdown = () => {
      const diff = new Date(match.startTime).getTime() - Date.now();
      if (diff <= 0) {
        setTimeLeft('LIVE NOW');
      } else {
        const m = Math.floor((diff / (1000 * 60)) % 60);
        const h = Math.floor(diff / (1000 * 60 * 60));
        setTimeLeft(`${h > 0 ? `${h}h ` : ''}${m}m left`);
      }
    };
    updateCountdown();
    const interval = setInterval(updateCountdown, 30000);
    return () => clearInterval(interval);
  }, [match]);

  if (!match) return null;

  const handleCopy = (val: string, field: string) => {
    onCopyRoomCreds(val);
    setCopiedField(field);
    setTimeout(() => setCopiedField(null), 2000);
  };

  const isFull = match.joinedSlots >= match.maxSlots;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/80 backdrop-blur-sm p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header Hero Banner */}
        <div className="relative h-44 shrink-0 overflow-hidden">
          <img
            src={match.mapImage}
            alt={match.map}
            className="w-full h-full object-cover brightness-60"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-[#14141F] via-[#14141F]/40 to-transparent" />

          {/* Close button */}
          <button
            onClick={onClose}
            className="absolute top-4 right-4 p-2 bg-black/60 hover:bg-black/80 rounded-full text-white/80 hover:text-white transition-all backdrop-blur-md"
          >
            <X className="w-5 h-5" />
          </button>

          {/* Top badges */}
          <div className="absolute top-4 left-4 flex gap-2">
            <span className="px-2.5 py-1 bg-[#C8FF3D] text-black text-xs font-bold rounded-full uppercase tracking-wider">
              {match.mode}
            </span>
            <span className="px-2.5 py-1 bg-black/60 border border-white/10 text-white text-xs font-medium rounded-full backdrop-blur-md">
              {match.map}
            </span>
            {match.status === 'Live' && (
              <span className="px-2.5 py-1 bg-red-500/80 text-white text-xs font-bold rounded-full flex items-center gap-1 animate-pulse">
                <span className="w-2 h-2 rounded-full bg-white"></span> LIVE
              </span>
            )}
          </div>

          {/* Title on banner */}
          <div className="absolute bottom-3 left-4 right-4">
            <h3 className="text-lg font-bold text-white leading-snug line-clamp-1">
              {match.title}
            </h3>
            <p className="text-xs text-[#9498AE] flex items-center gap-1.5 mt-0.5">
              <Clock className="w-3.5 h-3.5 text-[#C8FF3D]" />
              <span>Starts: {new Date(match.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} ({timeLeft})</span>
            </p>
          </div>
        </div>

        {/* Tab switchers */}
        <div className="flex border-b border-[#23233A] bg-[#0E0E17] px-3 shrink-0">
          {(['overview', 'prizes', 'rules', 'slots'] as const).map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`flex-1 py-3 text-xs font-semibold capitalize border-b-2 transition-all ${
                activeTab === tab
                  ? 'border-[#C8FF3D] text-[#C8FF3D]'
                  : 'border-transparent text-[#9498AE] hover:text-white'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Content body */}
        <div className="p-4 overflow-y-auto space-y-4 flex-1">
          {activeTab === 'overview' && (
            <div className="space-y-4">
              {/* Prize & Entry stats row */}
              <div className="grid grid-cols-3 gap-2.5">
                <div className="bg-[#1C1C2E] border border-[#23233A] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#9498AE] uppercase tracking-wider block">Prize Pool</span>
                  <span className="text-base font-extrabold text-[#FFB300]">₹{match.prizePool}</span>
                </div>
                <div className="bg-[#1C1C2E] border border-[#23233A] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#9498AE] uppercase tracking-wider block">Entry Fee</span>
                  <span className={`text-base font-extrabold ${match.entryFee === 0 ? 'text-[#C8FF3D]' : 'text-white'}`}>
                    {match.entryFee === 0 ? 'FREE' : `₹${match.entryFee}`}
                  </span>
                </div>
                <div className="bg-[#1C1C2E] border border-[#23233A] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#9498AE] uppercase tracking-wider block">Per Kill</span>
                  <span className="text-base font-extrabold text-[#FF6A00]">₹{match.perKillPrize}</span>
                </div>
              </div>

              {/* Slot Progress */}
              <div className="bg-[#1C1C2E] border border-[#23233A] p-3.5 rounded-2xl">
                <div className="flex justify-between items-center text-xs mb-2">
                  <span className="text-[#9498AE] flex items-center gap-1.5">
                    <Users className="w-3.5 h-3.5 text-[#C8FF3D]" /> Registered Players
                  </span>
                  <span className="font-bold text-white">{match.joinedSlots} / {match.maxSlots}</span>
                </div>
                <div className="w-full bg-[#0A0A0F] h-2.5 rounded-full overflow-hidden">
                  <div
                    className={`h-full rounded-full transition-all duration-500 ${
                      match.joinedSlots / match.maxSlots > 0.85
                        ? 'bg-gradient-to-r from-[#FF6A00] to-red-500'
                        : 'bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D]'
                    }`}
                    style={{ width: `${Math.min(100, (match.joinedSlots / match.maxSlots) * 100)}%` }}
                  />
                </div>
              </div>

              {/* Room ID & Pass Container */}
              <div className="bg-gradient-to-br from-[#1C1C2E] to-[#17113D] border border-[#7B5CFF]/30 p-4 rounded-2xl relative overflow-hidden">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-xs font-bold text-white flex items-center gap-1.5">
                    <Key className="w-4 h-4 text-[#FFB300]" /> Room Credentials
                  </span>
                  {match.userJoined ? (
                    <span className="text-[10px] px-2 py-0.5 bg-[#C8FF3D]/20 text-[#C8FF3D] rounded-full font-bold">
                      Registered Player
                    </span>
                  ) : (
                    <span className="text-[10px] px-2 py-0.5 bg-white/10 text-[#9498AE] rounded-full">
                      Join to Unlock
                    </span>
                  )}
                </div>

                {match.userJoined ? (
                  match.isRoomRevealed && match.roomId ? (
                    <div className="grid grid-cols-2 gap-3 mt-3">
                      <div className="bg-[#0A0A0F]/80 border border-[#23233A] p-2.5 rounded-xl">
                        <span className="text-[10px] text-[#9498AE] block">Room ID</span>
                        <div className="flex items-center justify-between mt-0.5">
                          <span className="font-mono text-base font-extrabold text-[#C8FF3D]">{match.roomId}</span>
                          <button
                            onClick={() => handleCopy(match.roomId!, 'room')}
                            className="p-1 hover:bg-white/10 rounded-md text-slate-300"
                          >
                            {copiedField === 'room' ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
                          </button>
                        </div>
                      </div>

                      <div className="bg-[#0A0A0F]/80 border border-[#23233A] p-2.5 rounded-xl">
                        <span className="text-[10px] text-[#9498AE] block">Password</span>
                        <div className="flex items-center justify-between mt-0.5">
                          <span className="font-mono text-base font-extrabold text-[#FFB300]">{match.roomPassword}</span>
                          <button
                            onClick={() => handleCopy(match.roomPassword!, 'pass')}
                            className="p-1 hover:bg-white/10 rounded-md text-slate-300"
                          >
                            {copiedField === 'pass' ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
                          </button>
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="bg-[#0A0A0F]/60 p-3 rounded-xl flex items-center gap-2.5 text-xs text-[#9498AE] mt-2">
                      <Lock className="w-4 h-4 text-[#FFB300] shrink-0" />
                      <span>Room ID & Pass will be revealed 10 minutes prior to match launch.</span>
                    </div>
                  )
                ) : (
                  <div className="bg-[#0A0A0F]/60 p-3 rounded-xl flex items-center gap-2.5 text-xs text-[#9498AE] mt-2">
                    <Lock className="w-4 h-4 text-[#9498AE] shrink-0" />
                    <span>Join this tournament to gain access to authorized Room ID and Password.</span>
                  </div>
                )}
              </div>

              {/* Sponsor Tag */}
              <div className="flex items-center justify-between px-3 py-2 bg-[#1C1C2E]/60 rounded-xl text-xs text-[#9498AE]">
                <span>Sponsored by</span>
                <span className="font-bold text-white flex items-center gap-1">
                  <Flame className="w-3.5 h-3.5 text-[#FF6A00]" /> {match.sponsorName}
                </span>
              </div>
            </div>
          )}

          {activeTab === 'prizes' && (
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 bg-[#1C1C2E] rounded-xl border border-[#23233A]">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-[#FFB300]/20 flex items-center justify-center text-[#FFB300] font-bold">
                    🥇
                  </div>
                  <div>
                    <span className="text-xs text-white font-bold block">1st Place (Booyah!)</span>
                    <span className="text-[10px] text-[#9498AE]">Champion Reward</span>
                  </div>
                </div>
                <span className="text-sm font-extrabold text-[#FFB300]">₹{match.prizeDistribution.first}</span>
              </div>

              <div className="flex items-center justify-between p-3 bg-[#1C1C2E] rounded-xl border border-[#23233A]">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-slate-300/20 flex items-center justify-center text-slate-300 font-bold">
                    🥈
                  </div>
                  <div>
                    <span className="text-xs text-white font-bold block">2nd Place (Runner-up)</span>
                    <span className="text-[10px] text-[#9498AE]">Silver Finish</span>
                  </div>
                </div>
                <span className="text-sm font-extrabold text-slate-200">₹{match.prizeDistribution.second}</span>
              </div>

              <div className="flex items-center justify-between p-3 bg-[#1C1C2E] rounded-xl border border-[#23233A]">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-[#CD7F32]/20 flex items-center justify-center text-[#CD7F32] font-bold">
                    🥉
                  </div>
                  <div>
                    <span className="text-xs text-white font-bold block">3rd Place</span>
                    <span className="text-[10px] text-[#9498AE]">Bronze Podium</span>
                  </div>
                </div>
                <span className="text-sm font-extrabold text-[#CD7F32]">₹{match.prizeDistribution.third}</span>
              </div>

              <div className="flex items-center justify-between p-3 bg-[#1C1C2E] rounded-xl border border-[#FF6A00]/20">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-[#FF6A00]/20 flex items-center justify-center text-[#FF6A00] font-bold">
                    🎯
                  </div>
                  <div>
                    <span className="text-xs text-white font-bold block">Per Elimination Bounty</span>
                    <span className="text-[10px] text-[#9498AE]">Verified Scorecard</span>
                  </div>
                </div>
                <span className="text-sm font-extrabold text-[#FF6A00]">₹{match.perKillPrize} / kill</span>
              </div>
            </div>
          )}

          {activeTab === 'rules' && (
            <div className="space-y-3">
              <div className="bg-[#1C1C2E] p-3.5 rounded-2xl border border-[#23233A]">
                <h4 className="text-xs font-bold text-white mb-2 flex items-center gap-1.5">
                  <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" /> Tournament Regulations
                </h4>
                <ul className="space-y-2 text-xs text-[#9498AE]">
                  {match.rules.map((rule, idx) => (
                    <li key={idx} className="flex items-start gap-2">
                      <span className="text-[#C8FF3D] font-bold">•</span>
                      <span>{rule}</span>
                    </li>
                  ))}
                  <li className="flex items-start gap-2">
                    <span className="text-[#C8FF3D] font-bold">•</span>
                    <span>Age 18+ verification mandatory for all prize withdrawals.</span>
                  </li>
                  <li className="flex items-start gap-2">
                    <span className="text-[#C8FF3D] font-bold">•</span>
                    <span>Matches will commence promptly at the scheduled kick-off time.</span>
                  </li>
                </ul>
              </div>
            </div>
          )}

          {activeTab === 'slots' && (
            <div className="space-y-2">
              <p className="text-xs text-[#9498AE] mb-1">
                Joined Warriors ({match.joinedSlots}/{match.maxSlots})
              </p>
              <div className="grid grid-cols-2 gap-2 text-xs">
                {Array.from({ length: Math.min(match.joinedSlots, 16) }).map((_, i) => (
                  <div
                    key={i}
                    className="flex items-center gap-2 p-2 bg-[#1C1C2E] border border-[#23233A] rounded-xl"
                  >
                    <span className="w-5 h-5 rounded-full bg-[#0A0A0F] text-[#C8FF3D] text-[10px] font-bold flex items-center justify-center">
                      {i + 1}
                    </span>
                    <span className="font-mono text-white truncate text-[11px]">
                      {i === 0 ? '⚡NEON_SOUL⚡ (You)' : `Player_${i + 104}`}
                    </span>
                  </div>
                ))}
              </div>
              {match.joinedSlots > 16 && (
                <p className="text-center text-[11px] text-[#9498AE] pt-2">
                  + {match.joinedSlots - 16} more registered participants
                </p>
              )}
            </div>
          )}
        </div>

        {/* Footer actions */}
        <div className="p-4 bg-[#0E0E17] border-t border-[#23233A] flex items-center gap-3 shrink-0">
          <button
            onClick={() => handleCopy(window.location.href, 'share')}
            className="p-3 bg-[#1C1C2E] hover:bg-[#23233A] text-white rounded-2xl border border-[#23233A] transition-all flex items-center justify-center shrink-0"
            title="Share match"
          >
            <Share2 className="w-5 h-5" />
          </button>

          {match.userJoined ? (
            <button
              disabled
              className="flex-1 py-3.5 bg-[#1C1C2E] text-[#C8FF3D] border border-[#C8FF3D]/30 font-bold rounded-2xl flex items-center justify-center gap-2 cursor-default"
            >
              <Check className="w-5 h-5" /> Already Registered
            </button>
          ) : isFull ? (
            <button
              disabled
              className="flex-1 py-3.5 bg-[#1C1C2E] text-slate-500 border border-[#23233A] font-bold rounded-2xl cursor-not-allowed"
            >
              Tournament Full (0 Slots Left)
            </button>
          ) : (
            <button
              onClick={() => onJoin(match)}
              className="flex-1 py-3.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold rounded-2xl shadow-[0_0_20px_rgba(200,255,61,0.35)] transition-all flex items-center justify-center gap-2 active:scale-[0.98]"
            >
              <Award className="w-5 h-5" />
              <span>Join Tournament ({match.entryFee === 0 ? 'FREE' : `₹${match.entryFee}`})</span>
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
