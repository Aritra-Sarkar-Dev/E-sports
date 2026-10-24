import React, { useState } from 'react';
import { PlayerMatch, PlayerProfile, WalletBalance } from '../types';
import { X, ShieldCheck, Wallet, Check, AlertCircle, Sparkles } from 'lucide-react';

interface JoinMatchModalProps {
  match: PlayerMatch | null;
  profile: PlayerProfile;
  wallet: WalletBalance;
  onClose: () => void;
  onSuccess: (match: PlayerMatch, teamMembers: string[]) => void;
  onOpenAddMoney: () => void;
}

export const JoinMatchModal: React.FC<JoinMatchModalProps> = ({
  match,
  profile,
  wallet,
  onClose,
  onSuccess,
  onOpenAddMoney,
}) => {
  const [ign, setIgn] = useState(profile.inGameName);
  const [ffUid, setFfUid] = useState(profile.ffUid);
  const [teamMembers, setTeamMembers] = useState<string[]>(['', '', '']);
  const [acceptedTerms, setAcceptedTerms] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showConfetti, setShowConfetti] = useState(false);

  if (!match) return null;

  const totalBalance = wallet.deposit + wallet.winnings + wallet.bonus;
  const hasEnoughBalance = match.entryFee === 0 || totalBalance >= match.entryFee;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!acceptedTerms || !hasEnoughBalance) return;

    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setShowConfetti(true);
      setTimeout(() => {
        onSuccess(match, teamMembers.filter(Boolean));
      }, 1600);
    }, 600);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl relative">
        {/* Confetti celebration overlay */}
        {showConfetti && (
          <div className="absolute inset-0 z-50 bg-[#0A0A0F]/95 flex flex-col items-center justify-center p-6 text-center animate-fade-in">
            <div className="w-20 h-20 rounded-full bg-[#C8FF3D]/20 border border-[#C8FF3D] flex items-center justify-center text-[#C8FF3D] mb-4 animate-bounce">
              <Sparkles className="w-10 h-10" />
            </div>
            <h3 className="text-2xl font-black text-white mb-1">REGISTRATION CONFIRMED!</h3>
            <p className="text-xs text-[#9498AE] max-w-xs">
              Slot reserved in <span className="text-[#C8FF3D] font-bold">{match.title}</span>. Room credentials will unlock 10 mins before start.
            </p>
          </div>
        )}

        {/* Modal Header */}
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between bg-[#0E0E17]">
          <div>
            <h3 className="text-base font-bold text-white">Confirm Tournament Entry</h3>
            <p className="text-xs text-[#9498AE]">{match.title} • {match.mode}</p>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="p-4 overflow-y-auto space-y-4 flex-1">
          {/* Fee & Balance Card */}
          <div className="bg-[#1C1C2E] border border-[#23233A] p-4 rounded-2xl space-y-2.5">
            <div className="flex justify-between items-center text-xs">
              <span className="text-[#9498AE]">Entry Fee:</span>
              <span className="font-extrabold text-base text-white">
                {match.entryFee === 0 ? <span className="text-[#C8FF3D]">FREE</span> : `₹${match.entryFee}`}
              </span>
            </div>

            <div className="flex justify-between items-center text-xs pt-1 border-t border-[#23233A]">
              <span className="text-[#9498AE] flex items-center gap-1.5">
                <Wallet className="w-3.5 h-3.5 text-[#C8FF3D]" /> Available Balance:
              </span>
              <span className="font-bold text-[#FFB300]">₹{totalBalance}</span>
            </div>

            {!hasEnoughBalance && (
              <div className="p-2.5 bg-red-500/10 border border-red-500/30 rounded-xl flex items-center justify-between text-xs text-red-400">
                <div className="flex items-center gap-1.5">
                  <AlertCircle className="w-4 h-4 shrink-0" />
                  <span>Insufficient balance (Need ₹{match.entryFee - totalBalance} more)</span>
                </div>
                <button
                  type="button"
                  onClick={onOpenAddMoney}
                  className="px-2.5 py-1 bg-[#C8FF3D] text-black font-bold text-[11px] rounded-lg"
                >
                  Add Cash
                </button>
              </div>
            )}
          </div>

          {/* Player In-Game Info */}
          <div className="space-y-3">
            <div>
              <label className="text-xs font-semibold text-[#9498AE] block mb-1">
                Your Free Fire In-Game Name (IGN)
              </label>
              <input
                type="text"
                required
                value={ign}
                onChange={(e) => setIgn(e.target.value)}
                className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#C8FF3D]"
                placeholder="e.g. NEON_SOUL"
              />
            </div>

            <div>
              <label className="text-xs font-semibold text-[#9498AE] block mb-1">
                Your Free Fire Numeric UID
              </label>
              <input
                type="text"
                required
                value={ffUid}
                onChange={(e) => setFfUid(e.target.value)}
                className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#C8FF3D]"
                placeholder="e.g. 994821034"
              />
            </div>

            {/* Teammates fields for Duo or Squad */}
            {match.mode === 'Duo' && (
              <div>
                <label className="text-xs font-semibold text-[#9498AE] block mb-1">
                  Teammate 2 (IGN / UID)
                </label>
                <input
                  type="text"
                  required
                  value={teamMembers[0]}
                  onChange={(e) => setTeamMembers([e.target.value, teamMembers[1], teamMembers[2]])}
                  className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#C8FF3D]"
                  placeholder="e.g. VIPER_STRIKE (889210)"
                />
              </div>
            )}

            {match.mode === 'Squad' && (
              <div className="space-y-2">
                <label className="text-xs font-semibold text-[#9498AE] block">
                  Squad Teammates (IGN / UID)
                </label>
                {[0, 1, 2].map((idx) => (
                  <input
                    key={idx}
                    type="text"
                    required
                    value={teamMembers[idx]}
                    onChange={(e) => {
                      const updated = [...teamMembers];
                      updated[idx] = e.target.value;
                      setTeamMembers(updated);
                    }}
                    className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                    placeholder={`Squad Member ${idx + 2} (IGN / UID)`}
                  />
                ))}
              </div>
            )}
          </div>

          {/* Terms and Fair Play */}
          <div className="pt-2">
            <label className="flex items-start gap-2.5 cursor-pointer">
              <input
                type="checkbox"
                checked={acceptedTerms}
                onChange={(e) => setAcceptedTerms(e.target.checked)}
                className="mt-0.5 rounded border-[#23233A] text-[#C8FF3D] focus:ring-0 bg-[#0E0E17]"
              />
              <span className="text-[11px] text-[#9498AE] leading-relaxed">
                I confirm that I am <strong className="text-white">18+ years of age</strong> and agree to FF Arena Fair Play Rules. Emulators, mod APKs, or teaming up will result in an immediate permanent ban.
              </span>
            </label>
          </div>

          {/* Actions */}
          <div className="pt-3">
            <button
              type="submit"
              disabled={!acceptedTerms || !hasEnoughBalance || isSubmitting}
              className={`w-full py-3.5 rounded-2xl font-extrabold text-sm flex items-center justify-center gap-2 transition-all ${
                !acceptedTerms || !hasEnoughBalance
                  ? 'bg-[#1C1C2E] text-slate-500 cursor-not-allowed border border-[#23233A]'
                  : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_20px_rgba(200,255,61,0.35)] active:scale-[0.98]'
              }`}
            >
              {isSubmitting ? (
                <div className="w-5 h-5 border-2 border-black border-t-transparent rounded-full animate-spin" />
              ) : (
                <>
                  <ShieldCheck className="w-4 h-4" />
                  <span>Confirm & Deduct {match.entryFee === 0 ? '₹0' : `₹${match.entryFee}`}</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
