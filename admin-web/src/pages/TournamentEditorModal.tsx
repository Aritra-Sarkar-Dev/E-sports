import React, { useState, useEffect } from 'react';
import { Modal } from '../components/common/Modal';
import { Tournament, TournamentMode, TournamentDivision, TournamentStatus } from '../types';

interface TournamentEditorModalProps {
  isOpen: boolean;
  onClose: () => void;
  tournament: Tournament | null;
  onSave: (tournament: Tournament) => void;
}

export const TournamentEditorModal: React.FC<TournamentEditorModalProps> = ({
  isOpen,
  onClose,
  tournament,
  onSave,
}) => {
  const [formData, setFormData] = useState<Partial<Tournament>>({
    name: '',
    mode: 'Squad',
    division: 'Open',
    startDateTime: new Date(Date.now() + 2 * 3600 * 1000).toISOString().slice(0, 16),
    maxSlots: 48,
    joinedSlots: 0,
    sponsorName: 'Asus ROG & Red Bull',
    sponsorLogo: '',
    prizeAmount: 25000,
    prizeDescription: '1st: ₹12,500 | 2nd: ₹7,500 | 3rd: ₹5,000 (Sponsor Funded)',
    rules: 'Mobile phones only. Emulators, triggers and iPads strictly banned. Level 30+ Free Fire UID required.',
    status: 'Open',
    roomId: '',
    roomPassword: '',
    autoRevealMinutes: 10,
  });

  useEffect(() => {
    if (tournament) {
      setFormData({
        ...tournament,
        startDateTime: tournament.startDateTime ? new Date(tournament.startDateTime).toISOString().slice(0, 16) : '',
      });
    } else {
      setFormData({
        name: '',
        mode: 'Squad',
        division: 'Open',
        startDateTime: new Date(Date.now() + 2 * 3600 * 1000).toISOString().slice(0, 16),
        maxSlots: 48,
        joinedSlots: 0,
        sponsorName: 'Asus ROG & Red Bull',
        sponsorLogo: '',
        prizeAmount: 25000,
        prizeDescription: '1st: ₹12,500 | 2nd: ₹7,500 | 3rd: ₹5,000 (Sponsor Funded)',
        rules: 'Mobile phones only. Emulators, triggers and iPads strictly banned. Level 30+ Free Fire UID required.',
        status: 'Open',
        roomId: '',
        roomPassword: '',
        autoRevealMinutes: 10,
      });
    }
  }, [tournament, isOpen]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!formData.name) return;

    const saved: Tournament = {
      id: tournament?.id || `tourn_${Date.now()}`,
      name: formData.name || 'Untitled Tournament',
      mode: (formData.mode as TournamentMode) || 'Squad',
      division: (formData.division as TournamentDivision) || 'Open',
      startDateTime: new Date(formData.startDateTime || Date.now()).toISOString(),
      maxSlots: Number(formData.maxSlots) || 48,
      joinedSlots: tournament?.joinedSlots || 0,
      sponsorName: formData.sponsorName || 'Official Sponsor',
      sponsorLogo: formData.sponsorLogo,
      prizeAmount: Number(formData.prizeAmount) || 0,
      prizeDescription: formData.prizeDescription || 'Sponsor prize breakdown',
      rules: formData.rules || 'Standard Free Fire MAX competitive rules apply.',
      status: (formData.status as TournamentStatus) || 'Draft',
      roomId: formData.roomId || '',
      roomPassword: formData.roomPassword || '',
      autoRevealMinutes: Number(formData.autoRevealMinutes) || 10,
      isRoomRevealed: tournament?.isRoomRevealed || false,
      createdAt: tournament?.createdAt || new Date().toISOString(),
    };

    onSave(saved);
    onClose();
  };

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={tournament ? 'Edit Tournament' : 'Create New Tournament'}
      subtitle="Free Fire MAX esports tournament configuration"
      maxWidth="max-w-2xl"
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Name */}
        <div>
          <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
            Tournament Title *
          </label>
          <input
            type="text"
            required
            value={formData.name || ''}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="e.g. Bermuda Blitz Solo Showdown #12"
            className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
          />
        </div>

        {/* Mode & Division & Status */}
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Battle Mode
            </label>
            <select
              value={formData.mode}
              onChange={(e) => setFormData({ ...formData, mode: e.target.value as TournamentMode })}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3 py-2.5 text-sm focus:outline-none"
            >
              <option value="Solo">Solo</option>
              <option value="Duo">Duo</option>
              <option value="Squad">Squad</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Skill Division
            </label>
            <select
              value={formData.division}
              onChange={(e) => setFormData({ ...formData, division: e.target.value as TournamentDivision })}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3 py-2.5 text-sm focus:outline-none"
            >
              <option value="Rookie">Rookie</option>
              <option value="Mid">Mid</option>
              <option value="Pro">Pro</option>
              <option value="Open">Open</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Status
            </label>
            <select
              value={formData.status}
              onChange={(e) => setFormData({ ...formData, status: e.target.value as TournamentStatus })}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3 py-2.5 text-sm focus:outline-none"
            >
              <option value="Draft">Draft</option>
              <option value="Open">Open</option>
              <option value="Full">Full</option>
              <option value="Live">Live</option>
              <option value="Completed">Completed</option>
              <option value="Cancelled">Cancelled</option>
            </select>
          </div>
        </div>

        {/* Schedule & Max Slots */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Start Date & Time (IST)
            </label>
            <input
              type="datetime-local"
              value={formData.startDateTime || ''}
              onChange={(e) => setFormData({ ...formData, startDateTime: e.target.value })}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-sm focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Max Participant Slots
            </label>
            <input
              type="number"
              min="2"
              max="100"
              value={formData.maxSlots || 48}
              onChange={(e) => setFormData({ ...formData, maxSlots: Number(e.target.value) })}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
            />
          </div>
        </div>

        {/* Sponsor & Prize (INR) */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Sponsor Name
            </label>
            <input
              type="text"
              value={formData.sponsorName || ''}
              onChange={(e) => setFormData({ ...formData, sponsorName: e.target.value })}
              placeholder="e.g. Asus ROG & Poco"
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Total Sponsor Prize Pool (₹ INR)
            </label>
            <input
              type="number"
              min="0"
              value={formData.prizeAmount || 0}
              onChange={(e) => setFormData({ ...formData, prizeAmount: Number(e.target.value) })}
              placeholder="e.g. 50000"
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#C8FF3D] font-extrabold rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
            />
          </div>
        </div>

        {/* Prize Breakdown Description */}
        <div>
          <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
            Prize Distribution Text
          </label>
          <input
            type="text"
            value={formData.prizeDescription || ''}
            onChange={(e) => setFormData({ ...formData, prizeDescription: e.target.value })}
            placeholder="1st: ₹25,000 | 2nd: ₹15,000 | 3rd: ₹10,000"
            className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
          />
        </div>

        {/* Rules */}
        <div>
          <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
            Tournament Rules & Fair Play Restrictions
          </label>
          <textarea
            rows={3}
            value={formData.rules || ''}
            onChange={(e) => setFormData({ ...formData, rules: e.target.value })}
            className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] p-3 text-sm focus:outline-none"
          />
        </div>

        <div className="flex items-center justify-end space-x-3 pt-3 border-t border-[#23233A]">
          <button
            type="button"
            onClick={onClose}
            className="px-5 py-2.5 rounded-[18px] text-xs font-bold text-[#9498AE] hover:text-[#F5F5FA] hover:bg-[#23233A] transition-colors"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="px-6 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] hover:bg-[#b8f02d] text-black transition-all shadow-md shadow-[#C8FF3D]/10"
          >
            {tournament ? 'SAVE CHANGES' : 'CREATE TOURNAMENT'}
          </button>
        </div>
      </form>
    </Modal>
  );
};
