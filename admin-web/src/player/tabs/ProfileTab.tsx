import React, { useState } from 'react';
import { PlayerProfile, PlayerMatch } from '../types';
import {
  Copy,
  Check,
  ShieldCheck,
  Trophy,
  Award,
  Flame,
  Share2,
  Settings,
  HelpCircle,
  FileText,
  LogOut,
  ExternalLink,
  ChevronRight,
  Shield,
  Edit3,
  Sliders,
  CheckCircle2,
  Lock
} from 'lucide-react';

interface ProfileTabProps {
  profile: PlayerProfile;
  recentMatches: PlayerMatch[];
  onUpdateProfile: (updated: Partial<PlayerProfile>) => void;
  onCopyText: (text: string) => void;
  onSwitchToAdmin: () => void;
  onLogout: () => void;
  onOpenSupport: () => void;
}

export const ProfileTab: React.FC<ProfileTabProps> = ({
  profile,
  recentMatches,
  onUpdateProfile,
  onCopyText,
  onSwitchToAdmin,
  onLogout,
  onOpenSupport,
}) => {
  const [copiedUid, setCopiedUid] = useState(false);
  const [copiedReferral, setCopiedReferral] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editIgn, setEditIgn] = useState(profile.inGameName);
  const [editUid, setEditUid] = useState(profile.ffUid);
  const [showFairPlay, setShowFairPlay] = useState(false);
  const [showResponsibleGaming, setShowResponsibleGaming] = useState(false);

  const handleCopyUid = () => {
    onCopyText(profile.ffUid);
    setCopiedUid(true);
    setTimeout(() => setCopiedUid(false), 2000);
  };

  const handleCopyReferral = () => {
    onCopyText(profile.referralCode);
    setCopiedReferral(true);
    setTimeout(() => setCopiedReferral(false), 2000);
  };

  const handleSaveProfile = (e: React.FormEvent) => {
    e.preventDefault();
    onUpdateProfile({ inGameName: editIgn, ffUid: editUid });
    setIsEditing(false);
  };

  return (
    <div className="space-y-4 pb-14 animate-fade-in">
      {/* Cover Banner & Avatar Header */}
      <div className="relative">
        <div className="h-32 bg-gradient-to-r from-[#FF6A00] via-[#7B5CFF] to-[#3A24A8] overflow-hidden">
          <div className="w-full h-full opacity-30 bg-[radial-gradient(#fff_1px,transparent_1px)] [background-size:16px_16px]" />
        </div>

        {/* Profile Card Overlay */}
        <div className="px-4 -mt-14 relative z-10">
          <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 shadow-2xl flex flex-col items-center text-center">
            {/* Avatar with glowing frame */}
            <div className="relative -mt-12 mb-2">
              <div className="w-20 h-20 rounded-full p-1 bg-gradient-to-tr from-[#C8FF3D] via-[#FF6A00] to-[#7B5CFF] shadow-[0_0_20px_rgba(200,255,61,0.4)]">
                <img
                  src={profile.avatarUrl}
                  alt={profile.name}
                  className="w-full h-full object-cover rounded-full border-2 border-[#14141F]"
                />
              </div>
              <span className="absolute bottom-0 right-0 px-2 py-0.5 bg-[#FF6A00] text-white text-[9px] font-black rounded-full shadow border border-[#14141F]">
                LVL {profile.level}
              </span>
            </div>

            {/* Name & In-Game Tag */}
            <div className="flex items-center gap-1.5 justify-center">
              <h2 className="text-base font-black text-white">{profile.name}</h2>
              <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] font-extrabold text-[10px] rounded-full">
                {profile.rankTier}
              </span>
            </div>

            {/* Free Fire IGN & UID */}
            <div className="flex items-center gap-2 mt-1 text-xs">
              <span className="font-mono text-[#C8FF3D] font-bold">{profile.inGameName}</span>
              <span className="text-[#9498AE]">•</span>
              <button
                onClick={handleCopyUid}
                className="flex items-center gap-1 text-[#9498AE] hover:text-white font-mono"
              >
                <span>UID: {profile.ffUid}</span>
                {copiedUid ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
              </button>
            </div>

            {/* XP Level Bar */}
            <div className="w-full mt-3 bg-[#0E0E17] p-2.5 rounded-xl border border-[#23233A] space-y-1">
              <div className="flex justify-between text-[10px] text-[#9498AE]">
                <span>XP Progress</span>
                <span className="font-bold text-white">{profile.xp} / {profile.maxXp} XP</span>
              </div>
              <div className="w-full bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                <div
                  className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D] rounded-full"
                  style={{ width: `${(profile.xp / profile.maxXp) * 100}%` }}
                />
              </div>
            </div>

            {/* Edit Button */}
            <button
              onClick={() => setIsEditing(true)}
              className="mt-3 text-xs font-bold text-[#C8FF3D] hover:underline flex items-center gap-1"
            >
              <Edit3 className="w-3.5 h-3.5" /> Edit Player Profile
            </button>
          </div>
        </div>
      </div>

      {/* 6-Metric Stats Grid */}
      <div className="px-4 space-y-2">
        <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
          <Trophy className="w-4 h-4 text-[#FFB300]" /> Career Statistics
        </h3>

        <div className="grid grid-cols-3 gap-2 text-center">
          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">Matches</span>
            <span className="text-base font-black text-white">{profile.stats.matches}</span>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">Victories (Booyah)</span>
            <span className="text-base font-black text-[#C8FF3D]">{profile.stats.wins}</span>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">Eliminations</span>
            <span className="text-base font-black text-[#FF6A00]">{profile.stats.kills}</span>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">K/D Ratio</span>
            <span className="text-base font-black text-white">{profile.stats.kdRatio}</span>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">Win Rate</span>
            <span className="text-base font-black text-[#7B5CFF]">{profile.stats.winRate}%</span>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl">
            <span className="text-[10px] text-[#9498AE] block">Total Won</span>
            <span className="text-base font-black text-[#FFB300]">₹{profile.stats.totalEarnings}</span>
          </div>
        </div>
      </div>

      {/* Referral Code Card */}
      <div className="px-4">
        <div className="bg-gradient-to-r from-[#17113D] via-[#1C1C2E] to-[#14141F] border border-[#7B5CFF]/40 rounded-2xl p-4 flex items-center justify-between shadow-lg">
          <div>
            <span className="text-[10px] text-[#C8FF3D] font-extrabold uppercase tracking-wider block">
              Refer a Warrior
            </span>
            <div className="text-sm font-black text-white mt-0.5">
              Code: <span className="font-mono text-[#FFB300]">{profile.referralCode}</span>
            </div>
            <span className="text-[10px] text-[#9498AE]">Get 150 Coins + ₹25 Bonus per friend</span>
          </div>

          <button
            onClick={handleCopyReferral}
            className="px-3.5 py-2 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow flex items-center gap-1.5 active:scale-95"
          >
            {copiedReferral ? <Check className="w-3.5 h-3.5" /> : <Share2 className="w-3.5 h-3.5" />}
            <span>{copiedReferral ? 'Copied' : 'Share'}</span>
          </button>
        </div>
      </div>

      {/* Settings & Policies Menu */}
      <div className="px-4 space-y-2">
        <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
          <Settings className="w-4 h-4 text-slate-400" /> Account & Policies
        </h3>

        <div className="bg-[#14141F] border border-[#23233A] rounded-2xl overflow-hidden divide-y divide-[#23233A] text-xs">
          {/* Switch to Admin Console */}
          <button
            onClick={onSwitchToAdmin}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1C1C2E] text-left transition-colors group"
          >
            <div className="flex items-center gap-2.5">
              <div className="w-7 h-7 rounded-xl bg-[#7B5CFF]/20 flex items-center justify-center text-[#7B5CFF] group-hover:scale-110 transition-transform">
                <Sliders className="w-4 h-4" />
              </div>
              <div>
                <span className="font-bold text-white block">Tournament Organizer Portal</span>
                <span className="text-[10px] text-[#C8FF3D]">Switch to Admin Console Dashboard</span>
              </div>
            </div>
            <ChevronRight className="w-4 h-4 text-[#9498AE]" />
          </button>

          {/* Fair play policy */}
          <button
            onClick={() => setShowFairPlay(true)}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1C1C2E] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <Shield className="w-4 h-4 text-[#C8FF3D]" />
              <span className="font-bold text-white">Fair Play & Anti-Cheat Protocol</span>
            </div>
            <ChevronRight className="w-4 h-4 text-[#9498AE]" />
          </button>

          {/* Responsible gaming */}
          <button
            onClick={() => setShowResponsibleGaming(true)}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1C1C2E] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <ShieldCheck className="w-4 h-4 text-[#FFB300]" />
              <span className="font-bold text-white">Responsible Gaming & 18+ Rules</span>
            </div>
            <ChevronRight className="w-4 h-4 text-[#9498AE]" />
          </button>

          {/* 24/7 Help */}
          <button
            onClick={onOpenSupport}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1C1C2E] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <HelpCircle className="w-4 h-4 text-[#00E5FF]" />
              <span className="font-bold text-white">Help & Dispute Support</span>
            </div>
            <ChevronRight className="w-4 h-4 text-[#9498AE]" />
          </button>

          {/* Logout */}
          <button
            onClick={onLogout}
            className="w-full p-3.5 flex items-center justify-between hover:bg-red-500/10 text-left transition-colors"
          >
            <div className="flex items-center gap-2.5 text-red-400">
              <LogOut className="w-4 h-4" />
              <span className="font-bold">Log Out</span>
            </div>
            <ChevronRight className="w-4 h-4 text-red-400" />
          </button>
        </div>
      </div>

      {/* Edit Profile Modal */}
      {isEditing && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#23233A] rounded-[24px] p-5 shadow-2xl">
            <h3 className="text-base font-bold text-white mb-3">Edit Player Identity</h3>
            <form onSubmit={handleSaveProfile} className="space-y-3">
              <div>
                <label className="text-xs font-semibold text-[#9498AE] block mb-1">
                  In-Game Name (IGN)
                </label>
                <input
                  type="text"
                  required
                  value={editIgn}
                  onChange={(e) => setEditIgn(e.target.value)}
                  className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#9498AE] block mb-1">
                  Free Fire Numeric UID
                </label>
                <input
                  type="text"
                  required
                  value={editUid}
                  onChange={(e) => setEditUid(e.target.value)}
                  className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setIsEditing(false)}
                  className="flex-1 py-2.5 bg-[#1C1C2E] border border-[#23233A] rounded-xl text-xs font-bold text-slate-300"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="flex-1 py-2.5 bg-[#C8FF3D] text-black rounded-xl text-xs font-black shadow"
                >
                  Save Changes
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Fair Play Modal */}
      {showFairPlay && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#23233A] rounded-[24px] p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Shield className="w-4 h-4 text-[#C8FF3D]" /> Fair Play Commitment
              </h3>
              <button onClick={() => setShowFairPlay(false)} className="text-[#9498AE] hover:text-white">
                ✕
              </button>
            </div>
            <p className="text-xs text-[#9498AE] leading-relaxed">
              FF Arena operates an automated telemetry anti-cheat radar. Third-party APKs, recoil scripts, emulators, iPad screens, or collusion will lead to instant account ban and forfeiture of tournament pools.
            </p>
            <button
              onClick={() => setShowFairPlay(false)}
              className="w-full py-2.5 bg-[#C8FF3D] text-black font-extrabold text-xs rounded-xl"
            >
              I Understand
            </button>
          </div>
        </div>
      )}

      {/* Responsible Gaming Modal */}
      {showResponsibleGaming && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#23233A] rounded-[24px] p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <ShieldCheck className="w-4 h-4 text-[#FFB300]" /> Responsible Gaming
              </h3>
              <button onClick={() => setShowResponsibleGaming(false)} className="text-[#9498AE] hover:text-white">
                ✕
              </button>
            </div>
            <p className="text-xs text-[#9498AE] leading-relaxed">
              Paid tournaments are restricted to users <strong className="text-white">18 years or older</strong> residing in Indian states where skill gaming is permitted. Please play responsibly and maintain healthy gaming limits.
            </p>
            <button
              onClick={() => setShowResponsibleGaming(false)}
              className="w-full py-2.5 bg-[#FFB300] text-black font-extrabold text-xs rounded-xl"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
