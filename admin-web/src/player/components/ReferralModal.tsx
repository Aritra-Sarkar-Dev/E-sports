import React, { useState } from 'react';
import { ArrowLeft, Share2, Copy, Check, Users, Gift, Coins, Sparkles } from 'lucide-react';
import { PlayerProfile } from '../types';

interface ReferralModalProps {
  isOpen: boolean;
  onClose: () => void;
  profile: PlayerProfile;
  onCopyText: (text: string) => void;
}

export const ReferralModal: React.FC<ReferralModalProps> = ({
  isOpen,
  onClose,
  profile,
  onCopyText,
}) => {
  const [copied, setCopied] = useState(false);

  if (!isOpen) return null;

  const handleCopy = () => {
    onCopyText(profile.referralCode);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const handleShare = () => {
    const text = `Join me on FF Arena! Use my referral code ${profile.referralCode} to get 100 free bonus loyalty coins. 100% Free Entry esports!`;
    if (navigator.share) {
      navigator.share({
        title: 'FF Arena Free-Entry Esports',
        text,
        url: window.location.href,
      }).catch(() => {
        handleCopy();
      });
    } else {
      handleCopy();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2.5">
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white transition-colors"
              title="Back"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
            <div>
              <h3 className="text-sm font-black text-white flex items-center gap-1.5">
                <Gift className="w-4 h-4 text-[#FFB800]" /> Invite Friends
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Earn loyalty coins together</p>
            </div>
          </div>
          <span className="px-2 py-0.5 bg-[#FFB800]/15 text-[#FFB800] font-black text-[10px] rounded-full border border-[#FFB800]/30">
            +100 COINS
          </span>
        </div>

        {/* Content */}
        <div className="p-5 overflow-y-auto space-y-4 flex-1 text-center">
          {/* Hero Illustration Box */}
          <div className="w-20 h-20 rounded-3xl bg-gradient-to-tr from-[#7B5CFF]/20 via-[#FFB800]/20 to-[#C8FF3D]/20 border border-[#FFB800]/40 mx-auto flex items-center justify-center text-[#FFB800] shadow-[0_0_25px_rgba(255,184,0,0.2)]">
            <Coins className="w-10 h-10 animate-bounce" />
          </div>

          <div>
            <h3 className="text-base font-black text-white">Both of you get 100 bonus coins</h3>
            <p className="text-xs text-[#A7A7C0] max-w-xs mx-auto mt-1 leading-relaxed">
              When a squad mate signs up using your unique referral code, both accounts immediately receive 100 Loyalty Coins.
            </p>
          </div>

          {/* Referral Code Box */}
          <div className="p-4 bg-[#0A0A0F] border border-[#262640] rounded-2xl space-y-2">
            <span className="text-[10px] text-[#A7A7C0] uppercase tracking-wider font-bold block">
              Your Referral Code
            </span>
            <div className="flex items-center justify-center gap-2">
              <span className="text-2xl font-black font-mono tracking-widest text-[#FFB800]">
                {profile.referralCode}
              </span>
              <button
                onClick={handleCopy}
                className="p-2 hover:bg-[#1B1B2B] rounded-xl text-[#A7A7C0] hover:text-white transition-all active:scale-95"
                title="Copy Code"
              >
                {copied ? <Check className="w-5 h-5 text-[#C8FF3D]" /> : <Copy className="w-5 h-5" />}
              </button>
            </div>
          </div>

          {/* Action buttons */}
          <div className="flex gap-2">
            <button
              onClick={handleCopy}
              className="flex-1 py-3 bg-[#1B1B2B] hover:bg-[#262640] text-white font-bold text-xs rounded-xl border border-[#262640] flex items-center justify-center gap-2 transition-all active:scale-95"
            >
              {copied ? <Check className="w-4 h-4 text-[#C8FF3D]" /> : <Copy className="w-4 h-4" />}
              <span>{copied ? 'Code Copied' : 'Copy Code'}</span>
            </button>

            <button
              onClick={handleShare}
              className="flex-1 py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-2 transition-all active:scale-95"
            >
              <Share2 className="w-4 h-4" />
              <span>Share Link</span>
            </button>
          </div>

          {/* Policy disclaimer */}
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-xl text-[10px] text-[#A7A7C0] leading-relaxed">
            * Loyalty coins carry zero cash value, cannot be converted to fiat currency, and are only spendable on in-app cosmetic frames, badges, and extra free-entry tickets.
          </div>
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2 bg-[#1B1B2B] text-[#A7A7C0] hover:text-white font-bold text-xs rounded-xl transition-colors"
          >
            Back to Profile
          </button>
        </div>
      </div>
    </div>
  );
};
