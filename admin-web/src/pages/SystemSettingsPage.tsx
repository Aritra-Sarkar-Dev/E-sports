import React, { useState } from 'react';
import { Sliders, Shield, AlertTriangle, CheckCircle2, Lock, Save, RefreshCw } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { INITIAL_ADMINS } from '../data/mockData';

export const SystemSettingsPage: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [maintenanceMode, setMaintenanceMode] = useState(false);
  const [ageVerificationStrict, setAgeVerificationStrict] = useState(true);
  const [allowGuestSpectator, setAllowGuestSpectator] = useState(true);
  const [minRoomRevealMinutes, setMinRoomRevealMinutes] = useState(10);
  const [saveSuccess, setSaveSuccess] = useState(false);

  const handleSave = () => {
    setSaveSuccess(true);
    setTimeout(() => setSaveSuccess(false), 3000);
  };

  return (
    <div className="space-y-6 max-w-4xl">
      {saveSuccess && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] text-xs font-bold text-[#C8FF3D] flex items-center gap-2">
          <CheckCircle2 className="w-5 h-5" />
          <span>Platform configuration updated and broadcast to all connected services.</span>
        </div>
      )}

      {/* Compliance Box */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 shadow-xl space-y-4">
        <div className="flex items-center space-x-3 pb-3 border-b border-[#23233A]">
          <div className="p-2.5 bg-[#C8FF3D]/10 text-[#C8FF3D] rounded-[14px]">
            <Shield className="w-5 h-5" />
          </div>
          <div>
            <h2 className="font-extrabold text-[#F5F5FA] text-base">Jurisdiction & Legal Policy</h2>
            <p className="text-xs text-[#9498AE]">India Skill-Based Gaming & Free-Entry Mandates</p>
          </div>
        </div>

        <div className="p-4 bg-[#0A0A0F] border border-[#2E2E4A] rounded-[18px] space-y-2 text-xs">
          <div className="flex items-center justify-between">
            <span className="font-bold text-[#F5F5FA]">Paid Entry / Cash Wagering System</span>
            <span className="px-2.5 py-1 rounded-full bg-[#FF7A8A]/10 text-[#FF7A8A] font-extrabold uppercase text-[10px]">
              Strictly Disabled (enablePaidEntry = false)
            </span>
          </div>
          <p className="text-[#9498AE] leading-relaxed">
            In accordance with Indian esports guidelines, tournaments are strictly free to enter. Prize pools are fully funded by verified sponsors. Real-money deposit wallets and UPI payment gateways are locked out in code.
          </p>
        </div>

        <div className="flex items-center justify-between pt-2">
          <div>
            <p className="text-xs font-bold text-[#F5F5FA]">Mandatory 18+ Player Verification</p>
            <p className="text-[11px] text-[#9498AE]">Block sign-ups unless players accept the 18+ Fair Play pledge.</p>
          </div>
          <button
            onClick={() => setAgeVerificationStrict(!ageVerificationStrict)}
            className={`w-12 h-6 rounded-full transition-colors relative p-0.5 ${
              ageVerificationStrict ? 'bg-[#C8FF3D]' : 'bg-[#2E2E4A]'
            }`}
          >
            <span
              className={`block w-5 h-5 rounded-full bg-black transition-transform ${
                ageVerificationStrict ? 'translate-x-6' : 'translate-x-0'
              }`}
            />
          </button>
        </div>
      </div>

      {/* Room Security Settings */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 shadow-xl space-y-4">
        <div className="flex items-center space-x-3 pb-3 border-b border-[#23233A]">
          <div className="p-2.5 bg-[#7B5CFF]/10 text-[#7B5CFF] rounded-[14px]">
            <Lock className="w-5 h-5" />
          </div>
          <div>
            <h2 className="font-extrabold text-[#F5F5FA] text-base">Match Room Reveal Security</h2>
            <p className="text-xs text-[#9498AE]">Automatic timing thresholds for credentials delivery</p>
          </div>
        </div>

        <div className="space-y-4 text-xs">
          <div className="flex items-center justify-between">
            <div>
              <p className="font-bold text-[#F5F5FA]">Default Auto-Reveal Lead Time</p>
              <p className="text-[#9498AE]">Number of minutes before match start when Room ID & Password unlock.</p>
            </div>
            <div className="flex items-center space-x-2">
              <input
                type="number"
                min={3}
                max={30}
                value={minRoomRevealMinutes}
                onChange={(e) => setMinRoomRevealMinutes(Number(e.target.value))}
                className="w-16 bg-[#0A0A0F] border border-[#2E2E4A] rounded-xl px-2 py-1.5 text-center font-bold text-[#C8FF3D]"
              />
              <span className="text-[#9498AE]">minutes</span>
            </div>
          </div>

          <div className="flex items-center justify-between pt-2 border-t border-[#23233A]">
            <div>
              <p className="font-bold text-[#F5F5FA]">Allow Guest Spectators</p>
              <p className="text-[#9498AE]">Allow non-registered players to view public leaderboards and match streams.</p>
            </div>
            <button
              onClick={() => setAllowGuestSpectator(!allowGuestSpectator)}
              className={`w-12 h-6 rounded-full transition-colors relative p-0.5 ${
                allowGuestSpectator ? 'bg-[#C8FF3D]' : 'bg-[#2E2E4A]'
              }`}
            >
              <span
                className={`block w-5 h-5 rounded-full bg-black transition-transform ${
                  allowGuestSpectator ? 'translate-x-6' : 'translate-x-0'
                }`}
              />
            </button>
          </div>
        </div>
      </div>

      {/* Admin Team Roster */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 shadow-xl space-y-4">
        <div className="flex items-center space-x-3 pb-3 border-b border-[#23233A]">
          <div className="p-2.5 bg-[#FF385C]/10 text-[#FF385C] rounded-[14px]">
            <Shield className="w-5 h-5" />
          </div>
          <div>
            <h2 className="font-extrabold text-[#F5F5FA] text-base">Authorized Administrators</h2>
            <p className="text-xs text-[#9498AE]">Staff accounts with role permissions</p>
          </div>
        </div>

        <div className="space-y-2">
          {INITIAL_ADMINS.map((admin) => (
            <div
              key={admin.uid}
              className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] flex items-center justify-between text-xs"
            >
              <div>
                <p className="font-bold text-[#F5F5FA]">{admin.displayName}</p>
                <p className="text-[11px] text-[#9498AE]">{admin.email}</p>
              </div>
              <span
                className={`px-2.5 py-1 rounded-full text-[10px] font-extrabold ${
                  admin.role === 'SuperAdmin'
                    ? 'bg-[#C8FF3D]/10 text-[#C8FF3D]'
                    : admin.role === 'Moderator'
                    ? 'bg-[#7B5CFF]/10 text-[#7B5CFF]'
                    : 'bg-blue-500/10 text-blue-400'
                }`}
              >
                {admin.role}
              </span>
            </div>
          ))}
        </div>
      </div>

      <div className="flex justify-end">
        <button
          onClick={handleSave}
          className="flex items-center space-x-2 bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold px-6 py-3 rounded-[18px] transition-all shadow-lg shadow-[#C8FF3D]/15"
        >
          <Save className="w-4 h-4" />
          <span>Save Changes</span>
        </button>
      </div>
    </div>
  );
};
