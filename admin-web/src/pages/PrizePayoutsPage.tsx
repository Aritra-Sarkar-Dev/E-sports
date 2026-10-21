import React, { useState, useEffect } from 'react';
import {
  CircleDollarSign,
  Search,
  Filter,
  CheckCircle2,
  Clock,
  AlertCircle,
  FileText,
  ShieldCheck,
  Edit2,
} from 'lucide-react';
import { PrizePayout, PayoutStatus } from '../types';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';

export const PrizePayoutsPage: React.FC = () => {
  const [payouts, setPayouts] = useState<PrizePayout[]>([]);
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [search, setSearch] = useState('');
  const [editingPayout, setEditingPayout] = useState<PrizePayout | null>(null);
  const [editStatus, setEditStatus] = useState<PayoutStatus>('Pending');
  const [editNote, setEditNote] = useState('');
  const [actionNotice, setActionNotice] = useState<string | null>(null);

  useEffect(() => {
    loadPayouts();
  }, []);

  const loadPayouts = async () => {
    const list = await TournamentService.getPayouts();
    setPayouts(list);
  };

  const handleOpenEdit = (p: PrizePayout) => {
    setEditingPayout(p);
    setEditStatus(p.status);
    setEditNote(p.note);
  };

  const handleSaveEdit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingPayout) return;

    await TournamentService.updatePayoutStatus(editingPayout.id, editStatus, editNote);
    await loadPayouts();
    setEditingPayout(null);
    setActionNotice(`Payout record for ${editingPayout.winnerName} updated to ${editStatus}.`);
    setTimeout(() => setActionNotice(null), 3000);
  };

  const filtered = payouts.filter((p) => {
    const matchesSearch =
      p.winnerName.toLowerCase().includes(search.toLowerCase()) ||
      p.tournamentName.toLowerCase().includes(search.toLowerCase()) ||
      p.ffUid.includes(search);
    const matchesStatus = statusFilter === 'all' || p.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  const totalPrizeTracked = payouts.reduce((sum, p) => sum + p.amount, 0);
  const totalPaid = payouts.filter((p) => p.status === 'Paid').reduce((sum, p) => sum + p.amount, 0);
  const totalPending = payouts.filter((p) => p.status === 'Pending').reduce((sum, p) => sum + p.amount, 0);

  return (
    <div className="space-y-6">
      {/* India Compliance Banner */}
      <div className="p-4 bg-[#7B5CFF]/10 border border-[#7B5CFF]/30 rounded-[20px] flex items-start gap-3">
        <ShieldCheck className="w-5 h-5 text-[#7B5CFF] shrink-0 mt-0.5" />
        <div className="text-xs">
          <span className="font-bold text-[#F5F5FA] block mb-0.5">
            India Esports Regulatory Compliance Notice (Record-Keeping Only)
          </span>
          <p className="text-[#9498AE]">
            All prize pools are 100% funded by brand sponsors. In compliance with Indian laws, this panel
            strictly acts as an administrative log for offline bank/UPI disbursements. No deposits or cash wallets exist in-app.
          </p>
        </div>
      </div>

      {actionNotice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-xs font-bold text-[#C8FF3D] animate-fade-in">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{actionNotice}</span>
        </div>
      )}

      {/* Summary KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5">
          <span className="text-xs font-semibold text-[#9498AE] uppercase">Total Tracked Prizes</span>
          <p className="text-2xl font-extrabold text-[#F5F5FA] mt-2">₹{totalPrizeTracked.toLocaleString()}</p>
          <span className="text-[11px] text-[#9498AE]">All official tournaments</span>
        </div>
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5">
          <span className="text-xs font-semibold text-[#9498AE] uppercase">Disbursed (Paid)</span>
          <p className="text-2xl font-extrabold text-[#C8FF3D] mt-2">₹{totalPaid.toLocaleString()}</p>
          <span className="text-[11px] text-[#C8FF3D]">Verified with UTR reference</span>
        </div>
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5">
          <span className="text-xs font-semibold text-[#9498AE] uppercase">Pending Verification</span>
          <p className="text-2xl font-extrabold text-[#FFB300] mt-2">₹{totalPending.toLocaleString()}</p>
          <span className="text-[11px] text-[#FFB300]">Awaiting captain bank details</span>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4">
        <div className="flex flex-1 flex-wrap items-center gap-3">
          <div className="relative flex-1 min-w-[220px]">
            <Search className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search winner name, FF UID, or tournament..."
              className="w-full bg-[#14141F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2 text-xs focus:outline-none"
            />
          </div>

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3.5 py-2 text-xs focus:outline-none"
          >
            <option value="all">All Payout Statuses</option>
            <option value="Pending">Pending Only</option>
            <option value="Paid">Paid (Completed)</option>
            <option value="Failed">Failed / Dispute</option>
          </select>
        </div>
      </div>

      {/* Payouts Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F] text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
              <tr>
                <th className="px-6 py-4">Winner & IGN</th>
                <th className="px-4 py-4">Tournament</th>
                <th className="px-4 py-4">Placement</th>
                <th className="px-4 py-4">Sponsor Prize</th>
                <th className="px-4 py-4">Payout Status</th>
                <th className="px-6 py-4">Disbursement Note / UTR</th>
                <th className="px-6 py-4 text-right">Update</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {filtered.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-12 text-center text-[#9498AE]">
                    No payout records found.
                  </td>
                </tr>
              ) : (
                filtered.map((p) => (
                  <tr key={p.id} className="hover:bg-[#1A1A28]/50 transition-colors">
                    <td className="px-6 py-4">
                      <div>
                        <span className="font-extrabold text-sm text-[#F5F5FA] block">{p.winnerName}</span>
                        <span className="text-[11px] font-mono text-[#C8FF3D]">UID: {p.ffUid}</span>
                      </div>
                    </td>
                    <td className="px-4 py-4 font-medium text-[#F5F5FA]">{p.tournamentName}</td>
                    <td className="px-4 py-4 font-bold text-[#F5F5FA]">
                      {p.rank === 1 ? '🥇 Rank #1 (Booyah)' : p.rank === 2 ? '🥈 Rank #2' : `🥉 Rank #${p.rank}`}
                    </td>
                    <td className="px-4 py-4 font-extrabold text-sm text-[#C8FF3D]">
                      ₹{p.amount.toLocaleString()}
                    </td>
                    <td className="px-4 py-4">
                      <Badge variant={p.status === 'Paid' ? 'lime' : p.status === 'Pending' ? 'amber' : 'red'}>
                        {p.status}
                      </Badge>
                    </td>
                    <td className="px-6 py-4 text-[11px] text-[#9498AE] max-w-xs truncate">
                      {p.note || 'None recorded'}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => handleOpenEdit(p)}
                        className="inline-flex items-center gap-1 px-3 py-1 bg-[#23233A] hover:bg-[#2E2E4A] text-[#F5F5FA] rounded-xl text-xs font-bold transition-colors"
                      >
                        <Edit2 className="w-3.5 h-3.5 text-[#C8FF3D]" />
                        <span>Edit Log</span>
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Edit Payout Modal */}
      {editingPayout && (
        <Modal
          isOpen={true}
          onClose={() => setEditingPayout(null)}
          title="Update Prize Payout Record"
          subtitle={`Disbursement record for ${editingPayout.winnerName} (₹${editingPayout.amount.toLocaleString()})`}
          maxWidth="max-w-md"
        >
          <form onSubmit={handleSaveEdit} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                Payout Status
              </label>
              <select
                value={editStatus}
                onChange={(e) => setEditStatus(e.target.value as PayoutStatus)}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none"
              >
                <option value="Pending">Pending (Under Verification)</option>
                <option value="Paid">Paid (Transfer Completed by Sponsor)</option>
                <option value="Failed">Failed (Invalid Bank / Refused)</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                Disbursement Note / Bank UTR Number *
              </label>
              <textarea
                rows={3}
                required
                value={editNote}
                onChange={(e) => setEditNote(e.target.value)}
                placeholder="e.g. Transferred via Axis Bank sponsor desk. UTR #ROG-IN-9812401824"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] p-3 text-xs focus:outline-none"
              />
            </div>

            <div className="flex items-center justify-end space-x-3 pt-3 border-t border-[#23233A]">
              <button
                type="button"
                onClick={() => setEditingPayout(null)}
                className="px-4 py-2 text-xs font-bold text-[#9498AE]"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black hover:bg-[#b8f02d]"
              >
                SAVE LOG RECORD
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
};
