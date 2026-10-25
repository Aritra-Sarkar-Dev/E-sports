import React from 'react';
import { ArrowLeft, Shield, CheckCircle2, AlertOctagon, Smartphone, Eye, Ban } from 'lucide-react';

interface FairPlayModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const FairPlayModal: React.FC<FairPlayModalProps> = ({ isOpen, onClose }) => {
  if (!isOpen) return null;

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
                <Shield className="w-4 h-4 text-[#C8FF3D]" /> Fair Play & Anti-Cheat
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Rules and competitive integrity standards</p>
            </div>
          </div>
          <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] font-extrabold text-[9px] rounded-full border border-[#C8FF3D]/30">
            ZERO TOLERANCE
          </span>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto space-y-3.5 flex-1 text-xs">
          {/* Highlight Card */}
          <div className="p-3.5 bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border border-[#C8FF3D]/30 rounded-2xl">
            <span className="text-xs font-black text-[#C8FF3D] block mb-1">
              100% Free Entry • Sponsor-Funded Platform
            </span>
            <p className="text-[11px] text-[#A7A7C0] leading-relaxed">
              FF Arena tournaments are strictly free-entry esports events with zero entry fees, deposits, or cash wagers. Because prizes are sponsor-funded, competitive integrity is strictly enforced.
            </p>
          </div>

          {/* Rule Item 1 */}
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <div className="flex items-center gap-2 text-white font-bold">
              <Smartphone className="w-4 h-4 text-[#C8FF3D]" />
              <span>1. Mobile Devices Only (No Emulators)</span>
            </div>
            <p className="text-[11px] text-[#A7A7C0] pl-6 leading-relaxed">
              Only genuine handheld Android and iOS smartphones are allowed. Bluestacks, Gameloop, LDPlayer, tablet aspect ratio spoofers, or keyboard/mouse bypass tools result in permanent device and UID bans.
            </p>
          </div>

          {/* Rule Item 2 */}
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <div className="flex items-center gap-2 text-white font-bold">
              <Ban className="w-4 h-4 text-[#FF4D6D]" />
              <span>2. Zero Scripts, Hacks, or Modified APKs</span>
            </div>
            <p className="text-[11px] text-[#A7A7C0] pl-6 leading-relaxed">
              Auto-aim, headshot config files, wall penetration, antenna hacks, speed hacks, or packet manipulation triggers instant disqualify and public ledger ban.
            </p>
          </div>

          {/* Rule Item 3 */}
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <div className="flex items-center gap-2 text-white font-bold">
              <AlertOctagon className="w-4 h-4 text-[#FFB800]" />
              <span>3. Anti-Teaming & Fair Combat</span>
            </div>
            <p className="text-[11px] text-[#A7A7C0] pl-6 leading-relaxed">
              Intentional pacifism, sharing healing items with non-team opponents, or match-fixing in Solo or Squad lobbies will lead to instant disqualification of both parties.
            </p>
          </div>

          {/* Rule Item 4 */}
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <div className="flex items-center gap-2 text-white font-bold">
              <Eye className="w-4 h-4 text-[#7B5CFF]" />
              <span>4. Mandatory Screen Recording on Demand</span>
            </div>
            <p className="text-[11px] text-[#A7A7C0] pl-6 leading-relaxed">
              Finalists in tournaments with prize pools above ₹10,000 may be requested by official referees to submit full match POV screen recordings before results are finalized.
            </p>
          </div>
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl transition-all shadow"
          >
            I Agree to Fair Play Terms
          </button>
        </div>
      </div>
    </div>
  );
};
