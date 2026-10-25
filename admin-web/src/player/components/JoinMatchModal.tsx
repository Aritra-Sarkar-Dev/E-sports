import React, { useState } from 'react';
import { PlayerMatch, PlayerProfile } from '../types';
import { X, ShieldCheck, Sparkles, Check, Users, Trophy, AlertCircle } from 'lucide-react';

interface JoinMatchModalProps {
  match: PlayerMatch | null;
  profile: PlayerProfile;
  onClose: () => void;
  onSuccess: (match: PlayerMatch, teamMembers: string[]) => void;
}

export const JoinMatchModal: React.FC<JoinMatchModalProps> = ({
  match,
  profile,
  onClose,
  onSuccess,
}) => {
  const [ign, setIgn] = useState(profile.inGameName);
  const [ffUid, setFfUid] = useState(profile.ffUid);
  const [teamMembers, setTeamMembers] = useState<string[]>(['', '', '']);
  const [acceptedTerms, setAcceptedTerms] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showConfetti, setShowConfetti] = useState(false);

  if (!match) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!acceptedTerms) return;

    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      setShowConfetti(true);
      setTimeout(() => {
        onSuccess(match, teamMembers.filter(Boolean));
      }, 1500);
    }, 600);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl relative">
        {/* Confetti celebration overlay */}
        {showConfetti && (
          <div className="absolute inset-0 z-50 bg-[#0A0A0F]/95 flex flex-col items-center justify-center p-6 text-center animate-fade-in">
            <div className="w-20 h-20 rounded-full bg-[#C8FF3D]/20 border border-[#C8FF3D] flex items-center justify-center text-[#C8FF3D] mb-4 animate-bounce">
              <Sparkles className="w-10 h-10" />
            </div>
            <h3 className="text-xl font-black text-white mb-1">FREE ENTRY CONFIRMED!</h3>
            <p className="text-xs text-[#A7A7C0] max-w-xs leading-relaxed">
              Slot reserved in <span className="text-[#C8FF3D] font-bold">{match.title}</span>. Room ID & password will unlock 15 minutes before kick-off.
            </p>
          </div>
        )}

        {/* Modal Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div>
            <div className="flex items-center gap-2">
              <h3 className="text-sm font-black text-white">Join Tournament</h3>
              <span className="px-2 py-0.5 bg-[#C8FF3D] text-black font-extrabold text-[9px] rounded-full uppercase tracking-wider">
                FREE ENTRY
              </span>
            </div>
            <p className="text-[11px] text-[#A7A7C0] mt-0.5">{match.title} • {match.mode} • {match.map}</p>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="p-4 overflow-y-auto space-y-4 flex-1">
          {/* Sponsor & Free Entry Card */}
          <div className="bg-[#1B1B2B] border border-[#262640] p-3.5 rounded-2xl space-y-2">
            <div className="flex justify-between items-center text-xs">
              <span className="text-[#A7A7C0]">Entry Type:</span>
              <span className="font-black text-xs text-[#C8FF3D] bg-[#C8FF3D]/10 px-2 py-0.5 rounded-full border border-[#C8FF3D]/30">
                100% FREE ENTRY
              </span>
            </div>

            <div className="flex justify-between items-center text-xs pt-1 border-t border-[#262640]">
              <span className="text-[#A7A7C0] flex items-center gap-1.5">
                <Trophy className="w-3.5 h-3.5 text-[#7B5CFF]" /> Sponsor Prize Pool:
              </span>
              <span className="font-black text-white drop-shadow-[0_0_8px_rgba(200,255,61,0.3)]">
                ₹{match.prizePool.toLocaleString()}
              </span>
            </div>

            <div className="text-[10px] text-[#7B5CFF] font-semibold pt-0.5">
              Sponsored by {match.sponsorName} • Free entry. Prizes are sponsor-funded.
            </div>
          </div>

          {/* Player In-Game Info */}
          <div className="space-y-3">
            <div>
              <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                Your Free Fire In-Game Name (IGN)
              </label>
              <input
                type="text"
                required
                value={ign}
                onChange={(e) => setIgn(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                placeholder="e.g. ⚡NEON_SOUL⚡"
              />
            </div>

            <div>
              <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                Your Free Fire Numeric UID
              </label>
              <input
                type="text"
                required
                value={ffUid}
                onChange={(e) => setFfUid(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white font-mono focus:outline-none focus:border-[#C8FF3D]"
                placeholder="e.g. 994821034"
              />
            </div>

            {/* Squad / Duo teammates */}
            {match.mode !== 'Solo' && (
              <div className="space-y-2 pt-1 border-t border-[#262640]">
                <label className="text-[10px] font-bold text-white uppercase tracking-wider flex items-center gap-1.5">
                  <Users className="w-3.5 h-3.5 text-[#C8FF3D]" /> Teammates Free Fire IGNs (Optional)
                </label>
                {(match.mode === 'Duo' ? [0] : [0, 1, 2]).map((idx) => (
                  <input
                    key={idx}
                    type="text"
                    value={teamMembers[idx] || ''}
                    onChange={(e) => {
                      const updated = [...teamMembers];
                      updated[idx] = e.target.value;
                      setTeamMembers(updated);
                    }}
                    placeholder={`Teammate #${idx + 1} IGN`}
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                  />
                ))}
              </div>
            )}
          </div>

          {/* Rules confirmation */}
          <div className="bg-[#0A0A0F] p-3 rounded-xl border border-[#262640] space-y-1.5">
            <span className="text-[10px] font-bold text-white uppercase tracking-wider block">
              Match Rules & Verification
            </span>
            <ul className="text-[10px] text-[#A7A7C0] space-y-1">
              <li>• Mobile phones only (Android/iOS). Emulators strictly prohibited.</li>
              <li>• Anti-cheat telemetry is active on all official custom rooms.</li>
              <li>• Room ID and Password will unlock 15 minutes before start.</li>
            </ul>
          </div>

          <label className="flex items-start gap-2 pt-1 text-[11px] text-[#A7A7C0] cursor-pointer">
            <input
              type="checkbox"
              required
              checked={acceptedTerms}
              onChange={(e) => setAcceptedTerms(e.target.checked)}
              className="mt-0.5 rounded border-[#262640] text-[#C8FF3D] focus:ring-0 bg-[#0A0A0F]"
            />
            <span>I agree to the Fair Play Anti-Cheat rules and confirm I am playing on mobile.</span>
          </label>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={!acceptedTerms || isSubmitting}
            className="w-full py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] disabled:opacity-50 text-black font-extrabold text-xs rounded-xl shadow-[0_0_20px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 active:scale-95 transition-all"
          >
            {isSubmitting ? (
              <span>Reserving Slot...</span>
            ) : (
              <>
                <Check className="w-4 h-4" />
                <span>Confirm Free Slot Reservation</span>
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
};
