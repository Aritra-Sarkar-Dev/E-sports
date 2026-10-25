import React, { useState } from 'react';
import { ArrowLeft, User, Hash, Gamepad2, Check, Sparkles, AlertCircle } from 'lucide-react';
import { PlayerProfile } from '../types';

const PRO_AVATARS = [
  'https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=160&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=160&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=160&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1580489944761-15a19d654956?w=160&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=160&auto=format&fit=crop&q=80',
  'https://images.unsplash.com/photo-1628157582853-a796fa650a6a?w=160&auto=format&fit=crop&q=80',
];

interface EditProfileModalProps {
  isOpen: boolean;
  onClose: () => void;
  profile: PlayerProfile;
  onSave: (updated: Partial<PlayerProfile>) => void;
  onShowToast: (type: 'success' | 'error' | 'info', message: string) => void;
}

export const EditProfileModal: React.FC<EditProfileModalProps> = ({
  isOpen,
  onClose,
  profile,
  onSave,
  onShowToast,
}) => {
  const [ign, setIgn] = useState(profile.inGameName);
  const [ffUid, setFfUid] = useState(profile.ffUid);
  const [name, setName] = useState(profile.name);
  const [avatarUrl, setAvatarUrl] = useState(profile.avatarUrl);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    // Validate 8-12 digits Free Fire UID
    const uidRegex = /^\d{8,12}$/;
    if (!uidRegex.test(ffUid.trim())) {
      setError('Free Fire Numeric UID must be between 8 and 12 digits (numbers only).');
      onShowToast('error', 'Free Fire UID must be 8-12 digits.');
      return;
    }

    if (!ign.trim()) {
      setError('In-game name cannot be empty.');
      return;
    }

    onSave({
      inGameName: ign.trim(),
      ffUid: ffUid.trim(),
      name: name.trim() || profile.name,
      avatarUrl,
    });

    onShowToast('success', 'Profile updated successfully!');
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl">
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
              <h3 className="text-sm font-black text-white">Edit Profile Identity</h3>
              <p className="text-[10px] text-[#A7A7C0]">Update your Free Fire IGN and Numeric UID</p>
            </div>
          </div>
        </div>

        {/* Form Body */}
        <form onSubmit={handleSubmit} className="p-4 overflow-y-auto space-y-4 flex-1">
          {error && (
            <div className="p-3 bg-red-500/10 border border-red-500/30 rounded-xl text-xs text-red-400 flex items-start gap-2">
              <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
              <span>{error}</span>
            </div>
          )}

          {/* Avatar Picker */}
          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-2">
              Select Avatar
            </label>
            <div className="grid grid-cols-6 gap-2">
              {PRO_AVATARS.map((url, idx) => {
                const isSelected = avatarUrl === url;
                return (
                  <button
                    key={idx}
                    type="button"
                    onClick={() => setAvatarUrl(url)}
                    className={`relative rounded-full aspect-square overflow-hidden border-2 transition-all ${
                      isSelected
                        ? 'border-[#C8FF3D] scale-105 shadow-[0_0_10px_rgba(200,255,61,0.5)]'
                        : 'border-[#262640] opacity-60 hover:opacity-100'
                    }`}
                  >
                    <img src={url} alt={`Avatar ${idx}`} className="w-full h-full object-cover" />
                    {isSelected && (
                      <span className="absolute inset-0 bg-[#C8FF3D]/20 flex items-center justify-center">
                        <Check className="w-3.5 h-3.5 text-[#C8FF3D]" />
                      </span>
                    )}
                  </button>
                );
              })}
            </div>
          </div>

          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
              Full Name / Display Name
            </label>
            <div className="relative">
              <User className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#A7A7C0]" />
              <input
                type="text"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
              />
            </div>
          </div>

          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
              Free Fire In-Game Name (IGN)
            </label>
            <div className="relative">
              <Gamepad2 className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#C8FF3D]" />
              <input
                type="text"
                required
                value={ign}
                onChange={(e) => setIgn(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
              />
            </div>
          </div>

          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
              Free Fire Numeric UID (8 to 12 digits)
            </label>
            <div className="relative">
              <Hash className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#7B5CFF]" />
              <input
                type="text"
                required
                value={ffUid}
                onChange={(e) => setFfUid(e.target.value)}
                placeholder="e.g. 994821034"
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white font-mono focus:outline-none focus:border-[#C8FF3D]"
              />
            </div>
            <span className="text-[10px] text-[#A7A7C0] block mt-1">
              Used for automated lobby verification and custom room invites.
            </span>
          </div>

          <div className="pt-2 flex gap-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2.5 bg-[#1B1B2B] hover:bg-[#262640] text-[#A7A7C0] hover:text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] transition-all active:scale-95"
            >
              Save Profile
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
