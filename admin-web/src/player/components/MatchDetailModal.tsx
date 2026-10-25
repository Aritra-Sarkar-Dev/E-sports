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
  Lock,
  Key,
  Flame,
  Award,
  Zap,
  CheckCircle2,
  ExternalLink,
  Gamepad2,
  ArrowRight
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
  const [activeTab, setActiveTab] = useState<'overview' | 'prizes' | 'scoring' | 'rules'>('overview');
  const [copiedField, setCopiedField] = useState<string | null>(null);
  const [timeLeft, setTimeLeft] = useState<string>('');
  const [isWithin15Mins, setIsWithin15Mins] = useState<boolean>(false);

  useEffect(() => {
    if (!match) return;
    const updateCountdown = () => {
      const diff = new Date(match.startTime).getTime() - Date.now();
      if (diff <= 0) {
        setTimeLeft('LIVE NOW');
        setIsWithin15Mins(true);
      } else {
        const m = Math.floor((diff / (1000 * 60)) % 60);
        const h = Math.floor(diff / (1000 * 60 * 60));
        setTimeLeft(`${h > 0 ? `${h}h ` : ''}${m}m left`);
        setIsWithin15Mins(diff <= 15 * 60 * 1000);
      }
    };
    updateCountdown();
    const interval = setInterval(updateCountdown, 15000);
    return () => clearInterval(interval);
  }, [match]);

  if (!match) return null;

  const handleCopy = (val: string, field: string) => {
    onCopyRoomCreds(val);
    setCopiedField(field);
    setTimeout(() => setCopiedField(null), 2000);
  };

  const handleLaunchFreeFire = () => {
    const credText = `Room ID: ${match.roomId || ''} | Pass: ${match.roomPassword || ''}`;
    onCopyRoomCreds(credText);
    try {
      window.location.href = 'freefiremax://';
      setTimeout(() => {
        // Fallback: try standard package or notify user
      }, 1000);
    } catch (e) {
      // Ignored in browser
    }
  };

  const isFull = match.joinedSlots >= match.maxSlots;
  const isRoomUnlocked = (isWithin15Mins || match.isRoomRevealed) && !!match.roomId;

  const SCORING_POINTS = [
    { rank: '1st (Booyah)', pts: '12 pts' },
    { rank: '2nd Place', pts: '9 pts' },
    { rank: '3rd Place', pts: '8 pts' },
    { rank: '4th Place', pts: '7 pts' },
    { rank: '5th Place', pts: '6 pts' },
    { rank: '6th Place', pts: '5 pts' },
    { rank: '7th Place', pts: '4 pts' },
    { rank: '8th Place', pts: '3 pts' },
    { rank: '9th Place', pts: '2 pts' },
    { rank: '10th Place', pts: '1 pt' },
    { rank: '11th - 12th', pts: '0 pts' },
    { rank: 'Each Elimination (Kill)', pts: '+1 pt' },
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl">
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
            <span className="px-2.5 py-1 bg-[#C8FF3D] text-black text-[11px] font-black rounded-full uppercase tracking-wider">
              {match.mode}
            </span>
            <span className="px-2.5 py-1 bg-black/60 border border-white/10 text-white text-[11px] font-bold rounded-full backdrop-blur-md">
              {match.map}
            </span>
            {match.status === 'Live' ? (
              <span className="px-2.5 py-1 bg-[#FF4D6D] text-white text-[11px] font-black rounded-full flex items-center gap-1 animate-pulse">
                <span className="w-2 h-2 rounded-full bg-white"></span> LIVE
              </span>
            ) : (
              <span className="px-2.5 py-1 bg-[#7B5CFF]/30 border border-[#7B5CFF]/60 text-white text-[11px] font-bold rounded-full">
                {match.sponsorName}
              </span>
            )}
          </div>

          {/* Title on banner */}
          <div className="absolute bottom-3 left-4 right-4">
            <h3 className="text-base font-black text-white leading-snug line-clamp-1">
              {match.title}
            </h3>
            <p className="text-xs text-[#A7A7C0] flex items-center gap-1.5 mt-0.5">
              <Clock className="w-3.5 h-3.5 text-[#C8FF3D]" />
              <span>Starts: {new Date(match.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} ({timeLeft})</span>
            </p>
          </div>
        </div>

        {/* Tab switchers */}
        <div className="flex border-b border-[#262640] bg-[#0A0A0F] px-2 shrink-0">
          {(['overview', 'prizes', 'scoring', 'rules'] as const).map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`flex-1 py-2.5 text-xs font-bold capitalize border-b-2 transition-all ${
                activeTab === tab
                  ? 'border-[#C8FF3D] text-[#C8FF3D]'
                  : 'border-transparent text-[#A7A7C0] hover:text-white'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Content body */}
        <div className="p-4 overflow-y-auto space-y-4 flex-1 text-xs">
          {activeTab === 'overview' && (
            <div className="space-y-4">
              {/* Prize & Entry stats row */}
              <div className="grid grid-cols-3 gap-2">
                <div className="bg-[#1B1B2B] border border-[#262640] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#A7A7C0] uppercase tracking-wider block">Sponsor Pool</span>
                  <span className="text-base font-black text-white drop-shadow-[0_0_8px_rgba(200,255,61,0.3)]">
                    ₹{match.prizePool.toLocaleString()}
                  </span>
                  <span className="text-[9px] text-[#7B5CFF] font-bold block mt-0.5">Sponsor-funded</span>
                </div>
                <div className="bg-[#1B1B2B] border border-[#262640] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#A7A7C0] uppercase tracking-wider block">Entry</span>
                  <span className="text-base font-black text-[#C8FF3D]">
                    FREE
                  </span>
                  <span className="text-[9px] text-[#C8FF3D] font-bold block mt-0.5">Zero Fees</span>
                </div>
                <div className="bg-[#1B1B2B] border border-[#262640] p-3 rounded-2xl text-center">
                  <span className="text-[10px] text-[#A7A7C0] uppercase tracking-wider block">Elimination</span>
                  <span className="text-base font-black text-[#7B5CFF]">+10 XP</span>
                  <span className="text-[9px] text-[#A7A7C0] font-bold block mt-0.5">per kill</span>
                </div>
              </div>

              {/* Slots Bar */}
              <div className="bg-[#1B1B2B] border border-[#262640] p-3.5 rounded-2xl space-y-1.5">
                <div className="flex justify-between items-center text-xs">
                  <span className="text-[#A7A7C0] flex items-center gap-1.5">
                    <Users className="w-3.5 h-3.5 text-[#C8FF3D]" /> Slots Filled:
                  </span>
                  <span className="font-bold text-white">
                    {match.joinedSlots} / {match.maxSlots} Players
                  </span>
                </div>
                <div className="w-full bg-[#0A0A0F] h-2 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D] rounded-full transition-all duration-500"
                    style={{ width: `${Math.min(100, (match.joinedSlots / match.maxSlots) * 100)}%` }}
                  />
                </div>
              </div>

              {/* Room ID and Password Box */}
              {match.userJoined ? (
                <div className="bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border-2 border-[#C8FF3D] p-4 rounded-2xl space-y-3 shadow-[0_0_20px_rgba(200,255,61,0.15)]">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-black text-white flex items-center gap-1.5">
                      <Key className="w-4 h-4 text-[#C8FF3D]" /> Custom Room Credentials
                    </span>
                    <span className="text-[10px] font-extrabold px-2 py-0.5 bg-[#C8FF3D] text-black rounded-full">
                      {isRoomUnlocked ? 'REVEALED' : 'LOCKED'}
                    </span>
                  </div>

                  {isRoomUnlocked ? (
                    <div className="space-y-2.5">
                      <div className="grid grid-cols-2 gap-2 text-xs">
                        <div className="bg-[#0A0A0F] p-2.5 rounded-xl border border-[#262640] flex items-center justify-between">
                          <div>
                            <span className="text-[9px] text-[#A7A7C0] block">Room ID</span>
                            <span className="font-mono font-bold text-white text-sm">{match.roomId}</span>
                          </div>
                          <button
                            onClick={() => handleCopy(match.roomId || '', 'room')}
                            className="p-1.5 hover:bg-[#1B1B2B] rounded-lg text-[#A7A7C0] hover:text-white"
                          >
                            {copiedField === 'room' ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
                          </button>
                        </div>

                        <div className="bg-[#0A0A0F] p-2.5 rounded-xl border border-[#262640] flex items-center justify-between">
                          <div>
                            <span className="text-[9px] text-[#A7A7C0] block">Password</span>
                            <span className="font-mono font-bold text-[#C8FF3D] text-sm">{match.roomPassword}</span>
                          </div>
                          <button
                            onClick={() => handleCopy(match.roomPassword || '', 'pass')}
                            className="p-1.5 hover:bg-[#1B1B2B] rounded-lg text-[#A7A7C0] hover:text-white"
                          >
                            {copiedField === 'pass' ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
                          </button>
                        </div>
                      </div>

                      {/* Launch Free Fire deep link button */}
                      <button
                        onClick={handleLaunchFreeFire}
                        className="w-full py-2.5 bg-[#7B5CFF] hover:bg-[#6849e6] text-white font-extrabold text-xs rounded-xl flex items-center justify-center gap-2 shadow-[0_0_15px_rgba(123,92,255,0.3)] transition-all active:scale-95"
                      >
                        <Gamepad2 className="w-4 h-4" />
                        <span>Copy & Launch Free Fire MAX</span>
                        <ExternalLink className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  ) : (
                    <div className="bg-[#0A0A0F] p-3 rounded-xl border border-[#262640] flex items-center gap-2.5 text-xs text-[#A7A7C0]">
                      <Lock className="w-4 h-4 text-[#7B5CFF] shrink-0" />
                      <span>Room ID and password automatically unlock 15 minutes before kickoff.</span>
                    </div>
                  )}
                </div>
              ) : (
                <div className="bg-[#1B1B2B] border border-[#262640] p-3 rounded-xl text-xs text-[#A7A7C0] flex items-center gap-2">
                  <ShieldCheck className="w-4 h-4 text-[#C8FF3D] shrink-0" />
                  <span>Reserve a slot to receive the custom room ID and password before start.</span>
                </div>
              )}
            </div>
          )}

          {activeTab === 'prizes' && (
            <div className="space-y-3">
              <div className="bg-[#1B1B2B] border border-[#262640] p-4 rounded-2xl space-y-3">
                <span className="text-xs font-black text-white flex items-center gap-1.5 uppercase tracking-wider">
                  <Trophy className="w-4 h-4 text-[#C8FF3D]" /> Sponsor Prize Distribution
                </span>

                <div className="space-y-2">
                  <div className="flex items-center justify-between p-2.5 bg-[#0A0A0F] rounded-xl border border-[#FFB800]/40">
                    <span className="text-xs font-bold text-white flex items-center gap-2">
                      🥇 1st Place (Booyah)
                    </span>
                    <span className="font-black text-white text-sm drop-shadow-[0_0_8px_rgba(200,255,61,0.3)]">
                      ₹{match.prizeDistribution.first.toLocaleString()}
                    </span>
                  </div>

                  <div className="flex items-center justify-between p-2.5 bg-[#0A0A0F] rounded-xl border border-[#262640]">
                    <span className="text-xs font-bold text-white flex items-center gap-2">
                      🥈 2nd Place
                    </span>
                    <span className="font-black text-white text-sm">
                      ₹{match.prizeDistribution.second.toLocaleString()}
                    </span>
                  </div>

                  <div className="flex items-center justify-between p-2.5 bg-[#0A0A0F] rounded-xl border border-[#262640]">
                    <span className="text-xs font-bold text-white flex items-center gap-2">
                      🥉 3rd Place
                    </span>
                    <span className="font-black text-white text-sm">
                      ₹{match.prizeDistribution.third.toLocaleString()}
                    </span>
                  </div>

                  <div className="flex items-center justify-between p-2.5 bg-[#0A0A0F] rounded-xl border border-[#7B5CFF]/40">
                    <span className="text-xs font-bold text-white flex items-center gap-2">
                      🎯 Elimination Bounty
                    </span>
                    <span className="font-black text-[#7B5CFF] text-xs">
                      +10 XP per kill
                    </span>
                  </div>
                </div>

                <p className="text-[10px] text-[#A7A7C0] italic">
                  * Prizes are funded entirely by {match.sponsorName}. Free entry.
                </p>
              </div>
            </div>
          )}

          {activeTab === 'scoring' && (
            <div className="space-y-3">
              <div className="bg-[#1B1B2B] border border-[#262640] p-4 rounded-2xl space-y-2.5">
                <span className="text-xs font-black text-white flex items-center gap-1.5 uppercase tracking-wider">
                  <Award className="w-4 h-4 text-[#C8FF3D]" /> Official Free Fire Point System
                </span>
                <p className="text-[11px] text-[#A7A7C0]">
                  Standard competitive scoring matrix based on final lobby placement and kills:
                </p>

                <div className="grid grid-cols-2 gap-2 pt-1">
                  {SCORING_POINTS.map((item, idx) => (
                    <div
                      key={idx}
                      className="p-2 bg-[#0A0A0F] border border-[#262640] rounded-xl flex items-center justify-between"
                    >
                      <span className="text-[10px] text-[#A7A7C0]">{item.rank}</span>
                      <span className="text-xs font-black text-white">{item.pts}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {activeTab === 'rules' && (
            <div className="space-y-3">
              <div className="bg-[#1B1B2B] border border-[#262640] p-4 rounded-2xl space-y-2.5">
                <span className="text-xs font-black text-white flex items-center gap-1.5 uppercase tracking-wider">
                  <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" /> Tournament Regulations
                </span>
                <ul className="text-xs text-[#A7A7C0] space-y-2">
                  {match.rules.map((rule, idx) => (
                    <li key={idx} className="flex items-start gap-2">
                      <span className="text-[#C8FF3D] font-bold">•</span>
                      <span>{rule}</span>
                    </li>
                  ))}
                  <li className="flex items-start gap-2">
                    <span className="text-[#C8FF3D] font-bold">•</span>
                    <span>Free entry. Prizes are sponsor-funded.</span>
                  </li>
                </ul>
              </div>
            </div>
          )}
        </div>

        {/* Footer Actions */}
        <div className="p-4 border-t border-[#262640] bg-[#1B1B2B] shrink-0">
          {match.userJoined ? (
            <div className="w-full py-3 bg-[#C8FF3D]/15 border border-[#C8FF3D]/40 text-[#C8FF3D] font-black text-xs rounded-xl flex items-center justify-center gap-2">
              <CheckCircle2 className="w-4 h-4" />
              <span>You Are Registered in This Match</span>
            </div>
          ) : isFull ? (
            <div className="w-full py-3 bg-[#0A0A0F] border border-[#262640] text-[#A7A7C0] font-bold text-xs rounded-xl text-center">
              All Slots Filled (Waitlist Only)
            </div>
          ) : (
            <button
              onClick={() => onJoin(match)}
              className="w-full py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_20px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 active:scale-95 transition-all"
            >
              <span>Join Tournament (Free Entry)</span>
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
