import React from 'react';
import { ArrowLeft, FileText, ShieldCheck, Scale, Lock } from 'lucide-react';

interface TermsPrivacyModalProps {
  isOpen: boolean;
  onClose: () => void;
}

export const TermsPrivacyModal: React.FC<TermsPrivacyModalProps> = ({ isOpen, onClose }) => {
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
                <FileText className="w-4 h-4 text-[#7B5CFF]" /> Terms & Privacy
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Platform terms, 18+ policy & compliance</p>
            </div>
          </div>
          <span className="px-2 py-0.5 bg-[#7B5CFF]/20 text-[#7B5CFF] font-black text-[10px] rounded-full border border-[#7B5CFF]/30">
            COMPLIANT
          </span>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto space-y-3.5 flex-1 text-xs text-[#A7A7C0]">
          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <h4 className="font-bold text-white flex items-center gap-1.5">
              <Scale className="w-4 h-4 text-[#C8FF3D]" /> 1. Strictly Free-Entry Platform
            </h4>
            <p className="leading-relaxed text-[11px]">
              FF Arena does not accept cash deposits, wagers, entry stakes, or entry fees of any kind. All tournaments are free to join for registered participants.
            </p>
          </div>

          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <h4 className="font-bold text-white flex items-center gap-1.5">
              <ShieldCheck className="w-4 h-4 text-[#FFB800]" /> 2. 18+ Age Requirement
            </h4>
            <p className="leading-relaxed text-[11px]">
              To participate in competitive esports tournaments and win sponsor-funded rewards, players must be at least 18 years of age. By creating an account, you confirm you meet this legal age criteria.
            </p>
          </div>

          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <h4 className="font-bold text-white flex items-center gap-1.5">
              <Lock className="w-4 h-4 text-[#7B5CFF]" /> 3. Loyalty Coins Disclaimer
            </h4>
            <p className="leading-relaxed text-[11px]">
              Loyalty Coins are virtual points granted for gameplay activities (streaks, match participation, missions). Coins carry zero monetary value, cannot be redeemed for fiat currency, cannot be transferred to real money accounts, and cannot be bought. They are solely spendable on in-app cosmetic frames, badges, and extra free-entry tickets.
            </p>
          </div>

          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <h4 className="font-bold text-white flex items-center gap-1.5">
              <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" /> 4. Sponsor-Funded Prizes Fulfillment
            </h4>
            <p className="leading-relaxed text-[11px]">
              Tournament prize pools are contributed solely by commercial brand sponsors. Prize records are published by the tournament organizer following match verification. The platform serves as the referee and recording hub.
            </p>
          </div>

          <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1">
            <h4 className="font-bold text-white flex items-center gap-1.5">
              <Lock className="w-4 h-4 text-[#A7A7C0]" /> 5. Data & UID Privacy
            </h4>
            <p className="leading-relaxed text-[11px]">
              Your Free Fire UID and in-game name are stored solely for custom room invites, lobby verification, and match standings. We do not sell or monetize personal identity data.
            </p>
          </div>
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-[#1B1B2B] hover:bg-[#262640] text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
          >
            Close Terms & Privacy
          </button>
        </div>
      </div>
    </div>
  );
};
