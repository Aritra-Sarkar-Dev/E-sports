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
  LogOut,
  ChevronRight,
  Shield,
  Edit3,
  Sliders,
  CheckCircle2,
  Sparkles,
  Users
} from 'lucide-react';

interface ProfileTabProps {
  profile: PlayerProfile;
  recentMatches: PlayerMatch[];
  onUpdateProfile: (updated: Partial<PlayerProfile>) => void;
  onCopyText: (text: string) => void;
  onSwitchToAdmin: () => void;
  onOpenOrganizerConsole: () => void;
  onOpenSquad: () => void;
  onOpenShareCard: () => void;
  onLogout: () => void;
  onOpenSupport: () => void;
}

export const ProfileTab: React.FC<ProfileTabProps> = ({
  profile,
  recentMatches,
  onUpdateProfile,
  onCopyText,
  onSwitchToAdmin,
  onOpenOrganizerConsole,
  onOpenSquad,
  onOpenShareCard,
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
  const longPressTimerRef = React.useRef<any>(null);

  const handleAvatarPressStart = () => {
    longPressTimerRef.current = setTimeout(() => {
      onOpenOrganizerConsole();
    }, 600);
  };

  const handleAvatarPressEnd = () => {
    if (longPressTimerRef.current) {
      clearTimeout(longPressTimerRef.current);
    }
  };

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
        <div className="h-28 bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#0A0A0F] overflow-hidden border-b border-[#262640]">
          <div className="w-full h-full opacity-30 bg-[radial-gradient(#C8FF3D_1px,transparent_1px)] [background-size:16px_16px]" />
        </div>

        {/* Profile Card Overlay */}
        <div className="px-4 -mt-12 relative z-10">
          <div className="bg-[#14141F] border border-[#262640] rounded-[24px] p-4 shadow-2xl flex flex-col items-center text-center">
            {/* Avatar with glowing frame + long-press shortcut to Organizer Console */}
            <div
              className="relative -mt-10 mb-2 cursor-pointer select-none active:scale-95 transition-transform"
              title="Long-press to open Organizer Console"
              onMouseDown={handleAvatarPressStart}
              onMouseUp={handleAvatarPressEnd}
              onMouseLeave={handleAvatarPressEnd}
              onTouchStart={handleAvatarPressStart}
              onTouchEnd={handleAvatarPressEnd}
            >
              <div className="w-20 h-20 rounded-full p-1 bg-gradient-to-tr from-[#C8FF3D] via-[#7B5CFF] to-[#C8FF3D] shadow-[0_0_20px_rgba(200,255,61,0.3)]">
                <img
                  src={profile.avatarUrl}
                  alt={profile.name}
                  className="w-full h-full object-cover rounded-full border-2 border-[#14141F]"
                />
              </div>
              <span className="absolute bottom-0 right-0 px-2 py-0.5 bg-[#7B5CFF] text-white text-[9px] font-black rounded-full shadow border border-[#14141F]">
                LVL {profile.level}
              </span>
            </div>

            {/* Name & In-Game Tag */}
            <div className="flex items-center gap-1.5 justify-center">
              <h2 className="text-base font-black text-white">{profile.name}</h2>
              <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] font-extrabold text-[10px] rounded-full border border-[#C8FF3D]/30">
                {profile.rankTier}
              </span>
            </div>

            {/* Free Fire IGN & UID */}
            <div className="flex items-center gap-2 mt-1 text-xs">
              <span className="font-mono text-[#C8FF3D] font-bold">{profile.inGameName}</span>
              <span className="text-[#A7A7C0]">•</span>
              <button
                onClick={handleCopyUid}
                className="flex items-center gap-1 text-[#A7A7C0] hover:text-white font-mono"
              >
                <span>UID: {profile.ffUid}</span>
                {copiedUid ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
              </button>
            </div>

            {/* XP Level Bar */}
            <div className="w-full mt-3 bg-[#0A0A0F] p-2.5 rounded-xl border border-[#262640] space-y-1">
              <div className="flex justify-between text-[10px] text-[#A7A7C0]">
                <span>XP Progress</span>
                <span className="font-bold text-white">{profile.xp} / {profile.maxXp} XP</span>
              </div>
              <div className="w-full bg-[#14141F] h-1.5 rounded-full overflow-hidden">
                <div
                  className="h-full bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D] rounded-full"
                  style={{ width: `${(profile.xp / profile.maxXp) * 100}%` }}
                />
              </div>
            </div>

            {/* Action Buttons: Edit & Share */}
            <div className="flex items-center gap-4 mt-3">
              <button
                onClick={() => setIsEditing(true)}
                className="text-xs font-bold text-[#C8FF3D] hover:underline flex items-center gap-1"
              >
                <Edit3 className="w-3.5 h-3.5" /> Edit Profile
              </button>
              <span className="text-[#262640]">•</span>
              <button
                onClick={onOpenShareCard}
                className="text-xs font-bold text-[#7B5CFF] hover:underline flex items-center gap-1"
              >
                <Share2 className="w-3.5 h-3.5" /> Career Card
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* 6-Metric Stats Grid (NO real-money earnings) */}
      <div className="px-4 space-y-2">
        <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
          <Trophy className="w-4 h-4 text-[#C8FF3D]" /> Career Statistics
        </h3>

        <div className="grid grid-cols-3 gap-2 text-center">
          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">Matches</span>
            <span className="text-base font-black text-white">{profile.stats.matches}</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">Victories (Booyah)</span>
            <span className="text-base font-black text-[#C8FF3D]">{profile.stats.wins}</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">Eliminations</span>
            <span className="text-base font-black text-white">{profile.stats.kills}</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">K/D Ratio</span>
            <span className="text-base font-black text-white">{profile.stats.kdRatio}</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">Win Rate</span>
            <span className="text-base font-black text-[#7B5CFF]">{profile.stats.winRate}%</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-3 rounded-2xl">
            <span className="text-[10px] text-[#A7A7C0] block">PRIZES WON</span>
            <span className="text-base font-black text-[#FFB800]">{profile.stats.prizesWon}</span>
          </div>
        </div>
      </div>

      {/* Badges Showcase: "Top Player" (renamed from Top Earner) */}
      <div className="px-4 space-y-2">
        <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
          <Award className="w-4 h-4 text-[#7B5CFF]" /> Earned Badges
        </h3>

        <div className="grid grid-cols-3 gap-2">
          <div className="bg-[#14141F] border border-[#C8FF3D]/40 p-2.5 rounded-2xl text-center space-y-1">
            <span className="text-xl block">👑</span>
            <span className="text-[10px] font-black text-[#C8FF3D] block">Top Player</span>
            <span className="text-[8px] text-[#A7A7C0] block">Tier 1 Elite</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-2.5 rounded-2xl text-center space-y-1">
            <span className="text-xl block">⚡</span>
            <span className="text-[10px] font-bold text-white block">Booyah Master</span>
            <span className="text-[8px] text-[#A7A7C0] block">10+ Victories</span>
          </div>

          <div className="bg-[#14141F] border border-[#262640] p-2.5 rounded-2xl text-center space-y-1">
            <span className="text-xl block">🎯</span>
            <span className="text-[10px] font-bold text-white block">Sharpshooter</span>
            <span className="text-[8px] text-[#A7A7C0] block">150+ Eliminations</span>
          </div>
        </div>
      </div>

      {/* Squad Card */}
      <div className="px-4">
        <button
          onClick={onOpenSquad}
          className="w-full bg-[#14141F] border border-[#262640] hover:border-[#7B5CFF] rounded-2xl p-3.5 flex items-center justify-between transition-all group"
        >
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-[#7B5CFF]/15 border border-[#7B5CFF]/30 flex items-center justify-center text-lg">
              ⚡
            </div>
            <div className="text-left">
              <span className="text-xs font-black text-white group-hover:text-[#C8FF3D] transition-colors block">
                CYBER TITANS [CYBR]
              </span>
              <span className="text-[10px] text-[#A7A7C0]">
                4 Roster Members • Squad Tournament Ready
              </span>
            </div>
          </div>
          <ChevronRight className="w-4 h-4 text-[#A7A7C0] group-hover:text-white" />
        </button>
      </div>

      {/* Referral Code Card (Coins only, no cash) */}
      <div className="px-4">
        <div className="bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border border-[#7B5CFF]/40 rounded-2xl p-4 flex items-center justify-between shadow-lg">
          <div>
            <span className="text-[10px] text-[#C8FF3D] font-extrabold uppercase tracking-wider block">
              Invite Friends
            </span>
            <div className="text-xs font-black text-white mt-0.5">
              Code: <span className="font-mono text-[#FFB800]">{profile.referralCode}</span>
            </div>
            <span className="text-[10px] text-[#A7A7C0]">Share your code and both of you get 100 bonus coins</span>
          </div>

          <button
            onClick={handleCopyReferral}
            className="px-3.5 py-2 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow flex items-center gap-1.5 active:scale-95 transition-all"
          >
            {copiedReferral ? <Check className="w-3.5 h-3.5" /> : <Share2 className="w-3.5 h-3.5" />}
            <span>{copiedReferral ? 'Copied' : 'Share'}</span>
          </button>
        </div>
      </div>

      {/* Organizer Console Quick Access (Shown ONLY when user.role is 'admin' or 'organizer') */}
      {(profile.role === 'admin' || profile.role === 'organizer') && (
        <div className="px-4">
          <button
            onClick={onOpenOrganizerConsole}
            className="w-full p-4 bg-gradient-to-r from-[#17113D] via-[#1B1B2B] to-[#14141F] border-2 border-[#C8FF3D] rounded-2xl flex items-center justify-between shadow-[0_0_25px_rgba(200,255,61,0.25)] hover:border-[#b5f02c] transition-all group active:scale-[0.99]"
          >
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D] group-hover:scale-110 transition-transform">
                <ShieldCheck className="w-5 h-5" />
              </div>
              <div className="text-left">
                <div className="flex items-center gap-2">
                  <span className="font-black text-white text-sm">Organizer Console</span>
                  <span className="px-2 py-0.5 bg-[#C8FF3D] text-black text-[9px] font-black rounded-full uppercase tracking-wider">
                    {profile.role}
                  </span>
                </div>
                <span className="text-[10px] text-[#A7A7C0] block mt-0.5">
                  Create tournaments, manage rooms & publish results
                </span>
              </div>
            </div>
            <ChevronRight className="w-5 h-5 text-[#C8FF3D] group-hover:translate-x-1 transition-transform" />
          </button>
        </div>
      )}

      {/* Settings & Policies Menu (Zero UPI/KYC for payouts) */}
      <div className="px-4 space-y-2">
        <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
          <Settings className="w-4 h-4 text-[#A7A7C0]" /> Account & Policies
        </h3>

        <div className="bg-[#14141F] border border-[#262640] rounded-2xl overflow-hidden divide-y divide-[#262640] text-xs">
          {/* Demo: Switch to organizer role toggle */}
          <div className="w-full p-3.5 flex items-center justify-between bg-[#14141F] hover:bg-[#1B1B2B] transition-colors">
            <div className="flex items-center gap-2.5">
              <div className="w-7 h-7 rounded-xl bg-[#C8FF3D]/20 flex items-center justify-center text-[#C8FF3D]">
                <Sliders className="w-4 h-4" />
              </div>
              <div className="text-left">
                <span className="font-bold text-white block">Demo: switch to organizer role</span>
                <span className="text-[10px] text-[#A7A7C0]">
                  Current role:{' '}
                  <strong className={profile.role === 'organizer' || profile.role === 'admin' ? 'text-[#C8FF3D]' : 'text-slate-400'}>
                    {profile.role.toUpperCase()}
                  </strong>
                </span>
              </div>
            </div>
            <button
              type="button"
              role="switch"
              aria-checked={profile.role === 'organizer' || profile.role === 'admin'}
              onClick={() => {
                const nextRole = profile.role === 'organizer' ? 'player' : 'organizer';
                onUpdateProfile({ role: nextRole });
              }}
              className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors focus:outline-none ${
                profile.role === 'organizer' || profile.role === 'admin' ? 'bg-[#C8FF3D]' : 'bg-[#262640]'
              }`}
            >
              <span
                className={`inline-block h-4 w-4 transform rounded-full bg-black transition-transform ${
                  profile.role === 'organizer' || profile.role === 'admin' ? 'translate-x-6' : 'translate-x-1'
                }`}
              />
            </button>
          </div>

          {/* Organizer Console row (Always visible for demo) */}
          <button
            onClick={onOpenOrganizerConsole}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1B1B2B] text-left transition-colors group"
          >
            <div className="flex items-center gap-2.5">
              <div className="w-7 h-7 rounded-xl bg-[#C8FF3D]/20 flex items-center justify-center text-[#C8FF3D] group-hover:scale-110 transition-transform">
                <ShieldCheck className="w-4 h-4" />
              </div>
              <div>
                <div className="flex items-center gap-1.5">
                  <span className="font-bold text-white block">Organizer Console</span>
                  <span className="px-1.5 py-0.2 bg-[#C8FF3D] text-black text-[9px] font-black rounded-full uppercase tracking-wider">
                    DEMO
                  </span>
                </div>
                <span className="text-[10px] text-[#A7A7C0]">
                  Create tournaments, set room ID/password & publish results
                </span>
              </div>
            </div>
            <ChevronRight className="w-4 h-4 text-[#C8FF3D]" />
          </button>

          {/* Switch to Admin Console */}
          <button
            onClick={onSwitchToAdmin}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1B1B2B] text-left transition-colors group"
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
            <ChevronRight className="w-4 h-4 text-[#A7A7C0]" />
          </button>

          {/* Fair play policy */}
          <button
            onClick={() => setShowFairPlay(true)}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1B1B2B] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <Shield className="w-4 h-4 text-[#C8FF3D]" />
              <div>
                <span className="font-bold text-white block">Fair Play & Anti-Cheat Protocol</span>
                <span className="text-[10px] text-[#A7A7C0]">Free entry. Prizes are sponsor-funded.</span>
              </div>
            </div>
            <ChevronRight className="w-4 h-4 text-[#A7A7C0]" />
          </button>

          {/* Responsible gaming */}
          <button
            onClick={() => setShowResponsibleGaming(true)}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1B1B2B] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <ShieldCheck className="w-4 h-4 text-[#FFB800]" />
              <span className="font-bold text-white">Responsible Gaming & 18+ Rules</span>
            </div>
            <ChevronRight className="w-4 h-4 text-[#A7A7C0]" />
          </button>

          {/* 24/7 Support */}
          <button
            onClick={onOpenSupport}
            className="w-full p-3.5 flex items-center justify-between hover:bg-[#1B1B2B] text-left transition-colors"
          >
            <div className="flex items-center gap-2.5">
              <HelpCircle className="w-4 h-4 text-[#00E5FF]" />
              <span className="font-bold text-white">Help & Dispute Support</span>
            </div>
            <ChevronRight className="w-4 h-4 text-[#A7A7C0]" />
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
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[24px] p-5 shadow-2xl">
            <h3 className="text-base font-bold text-white mb-3">Edit Player Identity</h3>
            <form onSubmit={handleSaveProfile} className="space-y-3">
              <div>
                <label className="text-xs font-semibold text-[#A7A7C0] block mb-1">
                  In-Game Name (IGN)
                </label>
                <input
                  type="text"
                  required
                  value={editIgn}
                  onChange={(e) => setEditIgn(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3.5 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div>
                <label className="text-xs font-semibold text-[#A7A7C0] block mb-1">
                  Free Fire Numeric UID
                </label>
                <input
                  type="text"
                  required
                  value={editUid}
                  onChange={(e) => setEditUid(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3.5 py-2 text-xs text-white font-mono focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setIsEditing(false)}
                  className="flex-1 py-2.5 bg-[#1B1B2B] border border-[#262640] rounded-xl text-xs font-bold text-[#A7A7C0]"
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
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[24px] p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Shield className="w-4 h-4 text-[#C8FF3D]" /> Fair Play Commitment
              </h3>
              <button onClick={() => setShowFairPlay(false)} className="text-[#A7A7C0] hover:text-white">
                ✕
              </button>
            </div>
            <p className="text-xs text-[#A7A7C0] leading-relaxed">
              FF Arena operates an automated telemetry anti-cheat radar. Third-party APKs, recoil scripts, emulators, iPad screens, or collusion will lead to instant account ban and forfeiture of sponsor tournament prizes. Free entry. Prizes are sponsor-funded.
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
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[24px] p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <ShieldCheck className="w-4 h-4 text-[#FFB800]" /> Responsible Gaming
              </h3>
              <button onClick={() => setShowResponsibleGaming(false)} className="text-[#A7A7C0] hover:text-white">
                ✕
              </button>
            </div>
            <p className="text-xs text-[#A7A7C0] leading-relaxed">
              FF Arena tournaments are strictly free-entry with sponsor-funded rewards. Open to players <strong className="text-white">18 years or older</strong>. Maintain healthy gaming habits and follow community fair-play guidelines.
            </p>
            <button
              onClick={() => setShowResponsibleGaming(false)}
              className="w-full py-2.5 bg-[#FFB800] text-black font-extrabold text-xs rounded-xl"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
