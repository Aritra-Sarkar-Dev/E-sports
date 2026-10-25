import React, { useState } from 'react';
import { PlayerProfile } from '../types';
import { Trophy, Share2, Copy, Check, X, Sparkles, Award } from 'lucide-react';

interface ShareCardModalProps {
  isOpen: boolean;
  profile: PlayerProfile;
  onClose: () => void;
  onShowToast: (type: 'success' | 'error' | 'info', message: string) => void;
}

export const ShareCardModal: React.FC<ShareCardModalProps> = ({
  isOpen,
  profile,
  onClose,
  onShowToast,
}) => {
  const [copied, setCopied] = useState(false);

  if (!isOpen) return null;

  const handleShare = () => {
    const text = `🔥 Join me on FF Arena! Use my referral code ${profile.referralCode} - both of us get 100 free bonus coins! Free entry Free Fire tournaments with sponsor-funded prizes!`;
    navigator.clipboard?.writeText(text);
    setCopied(true);
    onShowToast('success', 'Brag card and invite copied to clipboard!');
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4 animate-fade-in">
      <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[28px] overflow-hidden shadow-2xl flex flex-col relative">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <h3 className="text-sm font-black text-white flex items-center gap-1.5">
            <Sparkles className="w-4 h-4 text-[#C8FF3D]" /> Player Career Card
          </h3>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Card Canvas Preview */}
        <div className="p-5 space-y-4">
          <div className="relative rounded-2xl overflow-hidden bg-gradient-to-br from-[#1B1B2B] via-[#0A0A0F] to-[#14141F] border-2 border-[#C8FF3D] p-5 shadow-[0_0_25px_rgba(200,255,61,0.2)]">
            {/* Top brand tag */}
            <div className="flex items-center justify-between mb-4">
              <span className="text-[10px] font-black uppercase tracking-wider px-2 py-0.5 bg-[#C8FF3D] text-black rounded-full">
                FF ARENA ESPORTS
              </span>
              <span className="text-[10px] text-[#A7A7C0] font-mono">
                UID: {profile.ffUid}
              </span>
            </div>

            {/* Avatar & In-Game Name */}
            <div className="flex items-center gap-3 mb-4">
              <img
                src={profile.avatarUrl}
                alt={profile.inGameName}
                className="w-14 h-14 rounded-full object-cover border-2 border-[#C8FF3D] shadow-[0_0_15px_rgba(200,255,61,0.3)]"
              />
              <div>
                <h4 className="text-base font-black text-white font-mono">{profile.inGameName}</h4>
                <div className="flex items-center gap-1.5 mt-0.5">
                  <span className="px-2 py-0.5 bg-[#7B5CFF]/20 text-[#7B5CFF] font-extrabold text-[10px] rounded-md">
                    {profile.rankTier}
                  </span>
                  <span className="text-[10px] text-[#C8FF3D] font-bold">
                    LVL {profile.level}
                  </span>
                </div>
              </div>
            </div>

            {/* 3 Highlights */}
            <div className="grid grid-cols-3 gap-2 text-center bg-[#0A0A0F]/80 p-2.5 rounded-xl border border-[#262640] mb-4">
              <div>
                <span className="text-[9px] text-[#A7A7C0] block">Victories</span>
                <span className="text-sm font-black text-[#C8FF3D]">{profile.stats.wins} Booyah</span>
              </div>
              <div>
                <span className="text-[9px] text-[#A7A7C0] block">Kills</span>
                <span className="text-sm font-black text-white">{profile.stats.kills}</span>
              </div>
              <div>
                <span className="text-[9px] text-[#A7A7C0] block">Prizes Won</span>
                <span className="text-sm font-black text-[#FFB800]">{profile.stats.prizesWon}</span>
              </div>
            </div>

            {/* Invite Footer */}
            <div className="border-t border-[#262640] pt-2.5 flex items-center justify-between text-[10px]">
              <span className="text-[#A7A7C0]">Invite code: <strong className="text-[#FFB800] font-mono">{profile.referralCode}</strong></span>
              <span className="text-[#C8FF3D] font-extrabold">+100 Coins</span>
            </div>
          </div>

          {/* Share Button */}
          <button
            onClick={handleShare}
            className="w-full py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-2 active:scale-95 transition-all"
          >
            {copied ? <Check className="w-4 h-4" /> : <Share2 className="w-4 h-4" />}
            <span>{copied ? 'Copied to Clipboard!' : 'Share Career Card'}</span>
          </button>
        </div>
      </div>
    </div>
  );
};
