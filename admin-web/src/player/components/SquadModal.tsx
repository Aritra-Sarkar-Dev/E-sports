import React, { useState } from 'react';
import { Squad, SquadMember } from '../types';
import { INITIAL_SQUAD } from '../mockPlayerData';
import { Users, UserPlus, Shield, Trophy, Target, Copy, Check, X, Crown } from 'lucide-react';

interface SquadModalProps {
  isOpen: boolean;
  onClose: () => void;
  onShowToast: (type: 'success' | 'error' | 'info', message: string) => void;
}

export const SquadModal: React.FC<SquadModalProps> = ({ isOpen, onClose, onShowToast }) => {
  const [squad, setSquad] = useState<Squad>(INITIAL_SQUAD);
  const [inviteUid, setInviteUid] = useState('');
  const [copiedTag, setCopiedTag] = useState(false);

  if (!isOpen) return null;

  const handleCopyTag = () => {
    navigator.clipboard?.writeText(squad.tag);
    setCopiedTag(true);
    onShowToast('info', `Copied squad tag [${squad.tag}]`);
    setTimeout(() => setCopiedTag(false), 2000);
  };

  const handleInvite = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inviteUid.trim()) return;
    if (squad.members.length >= 4) {
      onShowToast('error', 'Squad roster is full (max 4 players).');
      return;
    }
    const newMember: SquadMember = {
      uid: `usr_${Date.now()}`,
      name: `Player_${inviteUid.slice(-4)}`,
      inGameName: `SQUAD_${inviteUid.slice(-4)}`,
      ffUid: inviteUid.trim(),
      role: 'Member',
    };
    setSquad((prev) => ({
      ...prev,
      members: [...prev.members, newMember],
    }));
    setInviteUid('');
    onShowToast('success', `Teammate UID ${inviteUid} added to squad!`);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4 animate-fade-in">
      <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[28px] overflow-hidden shadow-2xl flex flex-col relative max-h-[90vh]">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D]">
              <Users className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-black text-white">Squad & Teammates</h3>
              <span className="text-[10px] text-[#A7A7C0]">Squad Tournaments Ready</span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Squad Info Card */}
        <div className="p-4 space-y-4 overflow-y-auto flex-1">
          <div className="bg-[#1B1B2B] border border-[#262640] rounded-2xl p-4 flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-2xl bg-[#0A0A0F] border border-[#C8FF3D]/40 flex items-center justify-center text-xl shadow-[0_0_15px_rgba(200,255,61,0.2)]">
                {squad.logo}
              </div>
              <div>
                <h4 className="text-base font-black text-white">{squad.name}</h4>
                <div className="flex items-center gap-1.5 mt-0.5">
                  <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] font-mono font-bold text-[10px] rounded-md">
                    [{squad.tag}]
                  </span>
                  <button
                    onClick={handleCopyTag}
                    className="text-[#A7A7C0] hover:text-white text-xs"
                    title="Copy squad tag"
                  >
                    {copiedTag ? <Check className="w-3 h-3 text-[#C8FF3D]" /> : <Copy className="w-3 h-3" />}
                  </button>
                </div>
              </div>
            </div>

            <div className="text-right">
              <span className="text-[10px] text-[#A7A7C0] block">Victories</span>
              <span className="text-base font-black text-[#C8FF3D]">{squad.stats.wins} Booyah</span>
            </div>
          </div>

          {/* Roster list */}
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-[10px] font-extrabold text-[#A7A7C0] uppercase tracking-wider">
                Active Roster ({squad.members.length}/4)
              </span>
              <span className="text-[10px] text-[#C8FF3D] font-bold">Free Fire Verified</span>
            </div>

            <div className="space-y-1.5">
              {squad.members.map((m, idx) => (
                <div
                  key={m.uid}
                  className="bg-[#0A0A0F] border border-[#262640] rounded-xl p-2.5 flex items-center justify-between"
                >
                  <div className="flex items-center gap-2.5">
                    <div className="w-6 h-6 rounded-lg bg-[#1B1B2B] flex items-center justify-center text-[10px] font-black text-[#A7A7C0]">
                      {idx + 1}
                    </div>
                    <div>
                      <div className="flex items-center gap-1.5">
                        <span className="text-xs font-black text-white">{m.inGameName}</span>
                        {m.role === 'Leader' && (
                          <Crown className="w-3 h-3 text-[#FFB800]" />
                        )}
                      </div>
                      <span className="text-[10px] font-mono text-[#A7A7C0]">UID: {m.ffUid}</span>
                    </div>
                  </div>
                  <span className="text-[10px] font-extrabold text-[#7B5CFF] uppercase">
                    {m.role}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Add member form */}
          {squad.members.length < 4 && (
            <form onSubmit={handleInvite} className="bg-[#1B1B2B] border border-[#262640] rounded-2xl p-3 space-y-2">
              <span className="text-[10px] font-bold text-white uppercase tracking-wider block">
                Add Squad Member by UID
              </span>
              <div className="flex gap-2">
                <input
                  type="text"
                  value={inviteUid}
                  onChange={(e) => setInviteUid(e.target.value)}
                  placeholder="Enter Free Fire UID (e.g. 884910231)"
                  className="flex-1 bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white font-mono placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
                />
                <button
                  type="submit"
                  className="px-3 py-2 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow flex items-center gap-1 active:scale-95"
                >
                  <UserPlus className="w-3.5 h-3.5" />
                  <span>Add</span>
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};
