import React from 'react';
import { ArrowLeft, ShieldCheck, CheckCircle2, UserCheck, Smartphone, AlertTriangle } from 'lucide-react';
import { PlayerProfile } from '../types';

interface VerifyAccountModalProps {
  isOpen: boolean;
  onClose: () => void;
  profile: PlayerProfile;
}

export const VerifyAccountModal: React.FC<VerifyAccountModalProps> = ({
  isOpen,
  onClose,
  profile,
}) => {
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
                <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" /> Account Verification
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Free-Entry Tournament Eligibility Status</p>
            </div>
          </div>
          <span className="px-2.5 py-1 bg-[#C8FF3D]/15 text-[#C8FF3D] font-black text-[10px] rounded-full border border-[#C8FF3D]/30 flex items-center gap-1">
            <CheckCircle2 className="w-3 h-3" /> VERIFIED
          </span>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto space-y-4 flex-1">
          {/* Status summary banner */}
          <div className="p-4 rounded-2xl bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border border-[#C8FF3D]/40 space-y-2">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D]">
                <ShieldCheck className="w-5 h-5" />
              </div>
              <div>
                <h4 className="text-xs font-black text-white">Full Tournament Clearance</h4>
                <p className="text-[10px] text-[#A7A7C0]">
                  Your account is eligible to enter all sponsor cups and custom rooms.
                </p>
              </div>
            </div>
          </div>

          {/* 3 Step Verification Checklist */}
          <div className="space-y-2.5">
            <h4 className="text-xs font-black text-white uppercase tracking-wider">
              Verification Checklist
            </h4>

            {/* Step 1: Free Fire Numeric UID */}
            <div className="p-3.5 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-[#0A0A0F] border border-[#262640] flex items-center justify-center text-[#C8FF3D]">
                  <Smartphone className="w-4 h-4" />
                </div>
                <div>
                  <span className="text-xs font-bold text-white block">1. Free Fire UID Linked</span>
                  <span className="text-[10px] font-mono text-[#C8FF3D]">
                    UID: {profile.ffUid} ({profile.inGameName})
                  </span>
                </div>
              </div>
              <span className="flex items-center gap-1 px-2.5 py-1 bg-[#C8FF3D]/10 text-[#C8FF3D] font-extrabold text-[10px] rounded-full border border-[#C8FF3D]/20">
                <CheckCircle2 className="w-3.5 h-3.5" /> Linked
              </span>
            </div>

            {/* Step 2: 18+ Age Requirement */}
            <div className="p-3.5 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-[#0A0A0F] border border-[#262640] flex items-center justify-center text-[#7B5CFF]">
                  <UserCheck className="w-4 h-4" />
                </div>
                <div>
                  <span className="text-xs font-bold text-white block">2. 18+ Age Declaration</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    Confirmed during registration • Adult competitive player
                  </span>
                </div>
              </div>
              <span className="flex items-center gap-1 px-2.5 py-1 bg-[#C8FF3D]/10 text-[#C8FF3D] font-extrabold text-[10px] rounded-full border border-[#C8FF3D]/20">
                <CheckCircle2 className="w-3.5 h-3.5" /> Confirmed
              </span>
            </div>

            {/* Step 3: Level 30+ Anti-Smurf Check */}
            <div className="p-3.5 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-[#0A0A0F] border border-[#262640] flex items-center justify-center text-[#FFB800]">
                  <ShieldCheck className="w-4 h-4" />
                </div>
                <div>
                  <span className="text-xs font-bold text-white block">3. Level 30+ Anti-Smurf Check</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    Current Level: <strong className="text-white">Level {profile.level}</strong> (Minimum required: 30)
                  </span>
                </div>
              </div>
              <span className="flex items-center gap-1 px-2.5 py-1 bg-[#C8FF3D]/10 text-[#C8FF3D] font-extrabold text-[10px] rounded-full border border-[#C8FF3D]/20">
                <CheckCircle2 className="w-3.5 h-3.5" /> Passed
              </span>
            </div>
          </div>

          {/* Anti-cheat disclaimer */}
          <div className="p-3 bg-[#0A0A0F] border border-[#262640] rounded-2xl text-[11px] text-[#A7A7C0] space-y-1.5">
            <div className="flex items-center gap-1.5 text-white font-bold">
              <AlertTriangle className="w-3.5 h-3.5 text-[#FFB800]" />
              <span>Anti-Cheat Automated Radar Active</span>
            </div>
            <p className="leading-relaxed text-[10px]">
              Every match is monitored for emulators, config scripts, and teaming. Any violation will instantly revoke tournament clearance and cancel sponsor prize eligibility.
            </p>
          </div>
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl transition-all shadow-[0_0_15px_rgba(200,255,61,0.2)]"
          >
            Done
          </button>
        </div>
      </div>
    </div>
  );
};
