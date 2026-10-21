import React, { useState, useEffect } from 'react';
import {
  Plus,
  Search,
  Filter,
  Copy,
  Edit2,
  Trash2,
  Calendar,
  Users,
  Trophy,
  CheckCircle2,
} from 'lucide-react';
import { Tournament, TournamentMode, TournamentStatus } from '../types';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';
import { TournamentEditorModal } from './TournamentEditorModal';

export const TournamentsPage: React.FC = () => {
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [modeFilter, setModeFilter] = useState<string>('all');
  const [editingTournament, setEditingTournament] = useState<Tournament | null>(null);
  const [isEditorOpen, setIsEditorOpen] = useState(false);
  const [actionSuccess, setActionSuccess] = useState<string | null>(null);

  const loadTournaments = async () => {
    const list = await TournamentService.getTournaments();
    setTournaments(list);
  };

  useEffect(() => {
    loadTournaments();
  }, []);

  const handleDuplicate = async (id: string) => {
    await TournamentService.duplicateTournament(id);
    await loadTournaments();
    showNotice('Tournament duplicated successfully as draft.');
  };

  const handleCancel = async (id: string) => {
    if (confirm('Are you sure you want to cancel this tournament?')) {
      await TournamentService.cancelTournament(id);
      await loadTournaments();
      showNotice('Tournament marked as Cancelled.');
    }
  };

  const handleSave = async (saved: Tournament) => {
    await TournamentService.saveTournament(saved);
    await loadTournaments();
    showNotice(editingTournament ? 'Tournament updated.' : 'New tournament published.');
  };

  const showNotice = (msg: string) => {
    setActionSuccess(msg);
    setTimeout(() => setActionSuccess(null), 3500);
  };

  const filtered = tournaments.filter((t) => {
    const matchesSearch =
      t.name.toLowerCase().includes(search.toLowerCase()) ||
      t.sponsorName.toLowerCase().includes(search.toLowerCase());
    const matchesStatus = statusFilter === 'all' || t.status === statusFilter;
    const matchesMode = modeFilter === 'all' || t.mode === modeFilter;
    return matchesSearch && matchesStatus && matchesMode;
  });

  const getStatusBadgeVariant = (status: TournamentStatus) => {
    switch (status) {
      case 'Live': return 'red';
      case 'Open': return 'lime';
      case 'Full': return 'amber';
      case 'Completed': return 'violet';
      case 'Cancelled': return 'gray';
      default: return 'blue';
    }
  };

  return (
    <div className="space-y-6">
      {actionSuccess && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-xs font-bold text-[#C8FF3D] animate-fade-in">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{actionSuccess}</span>
        </div>
      )}

      {/* Top Action Bar */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4">
        {/* Search & Filters */}
        <div className="flex flex-1 flex-wrap items-center gap-3">
          <div className="relative flex-1 min-w-[200px]">
            <Search className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search tournaments or sponsors..."
              className="w-full bg-[#14141F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2 text-xs focus:outline-none"
            />
          </div>

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-xs focus:outline-none"
          >
            <option value="all">All Statuses</option>
            <option value="Open">Open</option>
            <option value="Live">Live</option>
            <option value="Full">Full</option>
            <option value="Draft">Draft</option>
            <option value="Completed">Completed</option>
            <option value="Cancelled">Cancelled</option>
          </select>

          <select
            value={modeFilter}
            onChange={(e) => setModeFilter(e.target.value)}
            className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-xs focus:outline-none"
          >
            <option value="all">All Modes</option>
            <option value="Solo">Solo</option>
            <option value="Duo">Duo</option>
            <option value="Squad">Squad</option>
          </select>
        </div>

        {/* Create Button */}
        <button
          onClick={() => {
            setEditingTournament(null);
            setIsEditorOpen(true);
          }}
          className="inline-flex items-center justify-center gap-2 bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold px-5 py-2.5 rounded-[18px] text-xs transition-all shadow-md shadow-[#C8FF3D]/10"
        >
          <Plus className="w-4 h-4 stroke-2" />
          <span>CREATE TOURNAMENT</span>
        </button>
      </div>

      {/* Tournaments Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F] text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
              <tr>
                <th className="px-6 py-4">Tournament</th>
                <th className="px-4 py-4">Mode / Tier</th>
                <th className="px-4 py-4">Schedule</th>
                <th className="px-4 py-4">Slots</th>
                <th className="px-4 py-4">Prize Pool</th>
                <th className="px-4 py-4">Status</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {filtered.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-12 text-center text-[#9498AE]">
                    No tournaments match the selected filters.
                  </td>
                </tr>
              ) : (
                filtered.map((t) => (
                  <tr key={t.id} className="hover:bg-[#1A1A28]/50 transition-colors">
                    <td className="px-6 py-4">
                      <div>
                        <span className="font-extrabold text-sm text-[#F5F5FA] block">{t.name}</span>
                        <span className="text-[11px] text-[#9498AE] flex items-center gap-1 mt-0.5">
                          <Trophy className="w-3 h-3 text-[#7B5CFF]" /> {t.sponsorName}
                        </span>
                      </div>
                    </td>
                    <td className="px-4 py-4">
                      <div className="flex flex-col gap-1 items-start">
                        <span className="font-bold text-[#F5F5FA]">{t.mode}</span>
                        <Badge variant="blue">{t.division}</Badge>
                      </div>
                    </td>
                    <td className="px-4 py-4 text-[#F5F5FA]">
                      <div className="flex items-center gap-1.5 font-medium">
                        <Calendar className="w-3.5 h-3.5 text-[#9498AE]" />
                        <span>{new Date(t.startDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', month: 'short', day: 'numeric' })}</span>
                      </div>
                    </td>
                    <td className="px-4 py-4">
                      <div className="flex items-center gap-2">
                        <Users className="w-3.5 h-3.5 text-[#9498AE]" />
                        <span className="font-bold text-[#F5F5FA]">
                          {t.joinedSlots} / {t.maxSlots}
                        </span>
                      </div>
                      <div className="w-20 h-1.5 bg-[#0A0A0F] rounded-full overflow-hidden mt-1 border border-[#23233A]">
                        <div
                          className="h-full bg-[#C8FF3D] rounded-full"
                          style={{ width: `${Math.min(100, (t.joinedSlots / t.maxSlots) * 100)}%` }}
                        />
                      </div>
                    </td>
                    <td className="px-4 py-4 font-extrabold text-[#C8FF3D] text-sm">
                      ₹{t.prizeAmount.toLocaleString()}
                    </td>
                    <td className="px-4 py-4">
                      <Badge variant={getStatusBadgeVariant(t.status)}>{t.status}</Badge>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="inline-flex items-center space-x-1">
                        <button
                          onClick={() => {
                            setEditingTournament(t);
                            setIsEditorOpen(true);
                          }}
                          title="Edit Tournament"
                          className="p-1.5 text-[#9498AE] hover:text-[#C8FF3D] hover:bg-[#23233A] rounded-lg transition-colors"
                        >
                          <Edit2 className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => handleDuplicate(t.id)}
                          title="Duplicate Tournament"
                          className="p-1.5 text-[#9498AE] hover:text-[#7B5CFF] hover:bg-[#23233A] rounded-lg transition-colors"
                        >
                          <Copy className="w-4 h-4" />
                        </button>
                        {t.status !== 'Cancelled' && (
                          <button
                            onClick={() => handleCancel(t.id)}
                            title="Cancel Tournament"
                            className="p-1.5 text-[#9498AE] hover:text-[#FF7A8A] hover:bg-[#23233A] rounded-lg transition-colors"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Editor Modal */}
      <TournamentEditorModal
        isOpen={isEditorOpen}
        onClose={() => setIsEditorOpen(false)}
        tournament={editingTournament}
        onSave={handleSave}
      />
    </div>
  );
};
