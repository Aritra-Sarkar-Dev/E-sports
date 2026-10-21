import React, { useState, useEffect } from 'react';
import {
  Users,
  Search,
  CheckCircle,
  AlertTriangle,
  Ban,
  Shield,
  Coins,
  Trophy,
  History,
  Phone,
  Mail,
  UserCheck,
} from 'lucide-react';
import { UserAccount, CoinTransaction, Registration, UserStatus } from '../types';
import { AdminService } from '../services/adminService';
import { CoinService } from '../services/coinService';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';

export const UsersPage: React.FC = () => {
  const [users, setUsers] = useState<UserAccount[]>([]);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [selectedUser, setSelectedUser] = useState<UserAccount | null>(null);
  const [userTx, setUserTx] = useState<CoinTransaction[]>([]);
  const [userRegs, setUserRegs] = useState<Registration[]>([]);
  const [activeUserTab, setActiveUserTab] = useState<'profile' | 'ledger' | 'tournaments'>('profile');

  // Moderation state
  const [moderationAction, setModerationAction] = useState<UserStatus | null>(null);
  const [moderationReason, setModerationReason] = useState('');
  const [notice, setNotice] = useState<string | null>(null);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const list = await AdminService.getUsers();
    setUsers(list);
  };

  const handleSelectUser = async (user: UserAccount) => {
    setSelectedUser(user);
    setActiveUserTab('profile');
    const tx = await CoinService.getUserTransactions(user.uid);
    setUserTx(tx);
    const regs = (await TournamentService.getRegistrations()).filter((r) => r.userId === user.uid || r.ffUid === user.ffUid);
    setUserRegs(regs);
  };

  const handleApplyModeration = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedUser || !moderationAction) return;
    if (moderationAction !== 'Active' && (!moderationReason || moderationReason.trim().length < 5)) {
      alert('A detailed reason (at least 5 characters) is required for audit compliance.');
      return;
    }

    const updated = await AdminService.updateUserStatus(selectedUser.uid, moderationAction, moderationReason);
    setSelectedUser(updated);
    setModerationAction(null);
    setModerationReason('');
    await loadUsers();
    showNotice(`User status updated to ${moderationAction}.`);
  };

  const handleToggleVerification = async () => {
    if (!selectedUser) return;
    const updated = await AdminService.toggleUserVerification(selectedUser.uid);
    setSelectedUser(updated);
    await loadUsers();
    showNotice(`Verification status toggled for ${selectedUser.inGameName}.`);
  };

  const showNotice = (msg: string) => {
    setNotice(msg);
    setTimeout(() => setNotice(null), 3000);
  };

  const filtered = users.filter((u) => {
    const matchesSearch =
      u.name.toLowerCase().includes(search.toLowerCase()) ||
      u.inGameName.toLowerCase().includes(search.toLowerCase()) ||
      u.ffUid.includes(search) ||
      u.phone.includes(search);
    const matchesStatus = statusFilter === 'all' || u.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6">
      {notice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] text-xs font-bold text-[#C8FF3D] animate-fade-in">
          {notice}
        </div>
      )}

      {/* Filter and Search Bar */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4">
        <div className="flex flex-1 flex-wrap items-center gap-3">
          <div className="relative flex-1 min-w-[260px]">
            <Search className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search by player name, IGN, Free Fire UID, or phone..."
              className="w-full bg-[#14141F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2 text-xs focus:outline-none"
            />
          </div>

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3.5 py-2 text-xs focus:outline-none"
          >
            <option value="all">All Account Statuses</option>
            <option value="Active">Active Players</option>
            <option value="Warned">Warned</option>
            <option value="Suspended">Suspended</option>
            <option value="Banned">Banned</option>
          </select>
        </div>
      </div>

      {/* Users Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F] text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
              <tr>
                <th className="px-6 py-4">Player & IGN</th>
                <th className="px-4 py-4">Free Fire UID</th>
                <th className="px-4 py-4">Phone / Contact</th>
                <th className="px-4 py-4">Coin Balance</th>
                <th className="px-4 py-4">Performance</th>
                <th className="px-4 py-4">Status</th>
                <th className="px-6 py-4 text-right">Details</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {filtered.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-12 text-center text-[#9498AE]">
                    No user accounts match the search criteria.
                  </td>
                </tr>
              ) : (
                filtered.map((u) => (
                  <tr key={u.uid} className="hover:bg-[#1A1A28]/50 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 rounded-full bg-[#7B5CFF]/20 flex items-center justify-center font-bold text-[#7B5CFF]">
                          {u.inGameName.slice(0, 2).replace(/[^A-Za-z]/g, '') || 'FF'}
                        </div>
                        <div>
                          <div className="flex items-center gap-1.5">
                            <span className="font-extrabold text-sm text-[#F5F5FA]">{u.inGameName}</span>
                            {u.isVerified && <CheckCircle className="w-3.5 h-3.5 text-[#C8FF3D]" />}
                          </div>
                          <span className="text-[11px] text-[#9498AE]">{u.name}</span>
                        </div>
                      </div>
                    </td>
                    <td className="px-4 py-4 font-mono font-bold text-[#C8FF3D]">{u.ffUid}</td>
                    <td className="px-4 py-4 font-mono">{u.phone}</td>
                    <td className="px-4 py-4 font-extrabold text-[#FFB300] text-sm">
                      🪙 {u.coinBalance.toLocaleString()}
                    </td>
                    <td className="px-4 py-4">
                      <span className="font-bold text-[#F5F5FA]">{u.wins}W / {u.matchesPlayed}M</span>
                      <span className="text-[11px] text-[#9498AE] block">({u.winRate}% win rate)</span>
                    </td>
                    <td className="px-4 py-4">
                      <Badge
                        variant={
                          u.status === 'Active'
                            ? 'lime'
                            : u.status === 'Warned'
                            ? 'amber'
                            : 'red'
                        }
                      >
                        {u.status}
                      </Badge>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => handleSelectUser(u)}
                        className="px-3 py-1 bg-[#23233A] hover:bg-[#2E2E4A] text-[#F5F5FA] rounded-xl text-xs font-bold transition-colors"
                      >
                        Inspect
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Inspect User Modal with Profile, Coin Ledger & Tournaments */}
      {selectedUser && (
        <Modal
          isOpen={true}
          onClose={() => setSelectedUser(null)}
          title={`Player: ${selectedUser.inGameName} (${selectedUser.name})`}
          subtitle={`Free Fire UID: ${selectedUser.ffUid} • Member since ${selectedUser.createdAt}`}
          maxWidth="max-w-3xl"
        >
          <div className="space-y-6">
            {/* Top Navigation Tabs */}
            <div className="flex border-b border-[#23233A] space-x-6">
              <button
                onClick={() => setActiveUserTab('profile')}
                className={`pb-2.5 text-xs font-bold border-b-2 transition-all ${
                  activeUserTab === 'profile'
                    ? 'border-[#C8FF3D] text-[#C8FF3D]'
                    : 'border-transparent text-[#9498AE]'
                }`}
              >
                Profile & Moderation
              </button>
              <button
                onClick={() => setActiveUserTab('ledger')}
                className={`pb-2.5 text-xs font-bold border-b-2 transition-all ${
                  activeUserTab === 'ledger'
                    ? 'border-[#C8FF3D] text-[#C8FF3D]'
                    : 'border-transparent text-[#9498AE]'
                }`}
              >
                Coin Ledger ({userTx.length})
              </button>
              <button
                onClick={() => setActiveUserTab('tournaments')}
                className={`pb-2.5 text-xs font-bold border-b-2 transition-all ${
                  activeUserTab === 'tournaments'
                    ? 'border-[#C8FF3D] text-[#C8FF3D]'
                    : 'border-transparent text-[#9498AE]'
                }`}
              >
                Tournament Entries ({userRegs.length})
              </button>
            </div>

            {/* TAB 1: Profile & Moderation */}
            {activeUserTab === 'profile' && (
              <div className="space-y-6">
                {/* User Info Stats Row */}
                <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                  <div className="bg-[#0A0A0F] border border-[#23233A] rounded-[16px] p-3 text-center">
                    <span className="text-[10px] text-[#9498AE] uppercase block">Coin Balance</span>
                    <span className="text-base font-extrabold text-[#FFB300]">🪙 {selectedUser.coinBalance}</span>
                  </div>
                  <div className="bg-[#0A0A0F] border border-[#23233A] rounded-[16px] p-3 text-center">
                    <span className="text-[10px] text-[#9498AE] uppercase block">Matches Played</span>
                    <span className="text-base font-extrabold text-[#F5F5FA]">{selectedUser.matchesPlayed}</span>
                  </div>
                  <div className="bg-[#0A0A0F] border border-[#23233A] rounded-[16px] p-3 text-center">
                    <span className="text-[10px] text-[#9498AE] uppercase block">Total Wins</span>
                    <span className="text-base font-extrabold text-[#C8FF3D]">{selectedUser.wins}</span>
                  </div>
                  <div className="bg-[#0A0A0F] border border-[#23233A] rounded-[16px] p-3 text-center">
                    <span className="text-[10px] text-[#9498AE] uppercase block">Total Kills</span>
                    <span className="text-base font-extrabold text-[#7B5CFF]">{selectedUser.kills}</span>
                  </div>
                </div>

                {/* Verification & Compliance */}
                <div className="p-4 bg-[#0A0A0F] border border-[#23233A] rounded-[18px] flex items-center justify-between">
                  <div>
                    <span className="text-xs font-bold text-[#F5F5FA] block">
                      Free Fire Level 30+ Anti-Smurf Verification
                    </span>
                    <span className="text-[11px] text-[#9498AE]">
                      {selectedUser.isVerified ? 'Verified • Eligible for Pro and Open Divisions' : 'Unverified • Restricted to Rookie Division'}
                    </span>
                  </div>
                  <button
                    onClick={handleToggleVerification}
                    className="px-3.5 py-1.5 rounded-xl bg-[#23233A] hover:bg-[#2E2E4A] text-xs font-bold text-[#C8FF3D]"
                  >
                    {selectedUser.isVerified ? 'Revoke Verification' : 'Verify Account'}
                  </button>
                </div>

                {/* Moderation Controls */}
                <div className="border border-[#23233A] rounded-[18px] p-4 bg-[#0A0A0F]/60">
                  <h4 className="text-xs font-extrabold text-[#F5F5FA] uppercase mb-3 flex items-center gap-1.5">
                    <Shield className="w-4 h-4 text-[#FF7A8A]" /> Account Moderation Actions
                  </h4>

                  {moderationAction ? (
                    <form onSubmit={handleApplyModeration} className="space-y-3">
                      <div className="text-xs font-bold text-[#FF7A8A]">
                        Action: Apply {moderationAction.toUpperCase()} to {selectedUser.inGameName}
                      </div>
                      <input
                        type="text"
                        required={moderationAction !== 'Active'}
                        value={moderationReason}
                        onChange={(e) => setModerationReason(e.target.value)}
                        placeholder="Mandatory reason (e.g. Trigger script usage reported in Cyberfray match #4)..."
                        className="w-full bg-[#14141F] border border-[#2E2E4A] focus:border-[#FF7A8A] text-[#F5F5FA] rounded-[14px] px-3 py-2 text-xs focus:outline-none"
                      />
                      <div className="flex gap-2 justify-end">
                        <button
                          type="button"
                          onClick={() => setModerationAction(null)}
                          className="px-3 py-1.5 text-xs font-bold text-[#9498AE]"
                        >
                          Cancel
                        </button>
                        <button
                          type="submit"
                          className="px-4 py-1.5 bg-[#FF7A8A] text-black font-extrabold rounded-xl text-xs"
                        >
                          CONFIRM ACTION
                        </button>
                      </div>
                    </form>
                  ) : (
                    <div className="flex flex-wrap gap-2">
                      <button
                        onClick={() => setModerationAction('Warned')}
                        className="px-3.5 py-2 bg-[#FFB300]/15 border border-[#FFB300]/40 text-[#FFB300] hover:bg-[#FFB300]/25 rounded-xl text-xs font-bold"
                      >
                        Issue Warning
                      </button>
                      <button
                        onClick={() => setModerationAction('Suspended')}
                        className="px-3.5 py-2 bg-[#FF7A8A]/15 border border-[#FF7A8A]/40 text-[#FF7A8A] hover:bg-[#FF7A8A]/25 rounded-xl text-xs font-bold"
                      >
                        Suspend 7 Days
                      </button>
                      <button
                        onClick={() => setModerationAction('Banned')}
                        className="px-3.5 py-2 bg-[#FF385C]/20 border border-[#FF385C]/50 text-[#FF385C] hover:bg-[#FF385C]/30 rounded-xl text-xs font-bold"
                      >
                        Permanent Ban
                      </button>
                      {selectedUser.status !== 'Active' && (
                        <button
                          onClick={() => setModerationAction('Active')}
                          className="px-3.5 py-2 bg-[#C8FF3D]/15 border border-[#C8FF3D]/40 text-[#C8FF3D] hover:bg-[#C8FF3D]/25 rounded-xl text-xs font-bold"
                        >
                          Restore to Active
                        </button>
                      )}
                    </div>
                  )}
                </div>
              </div>
            )}

            {/* TAB 2: Coin Ledger */}
            {activeUserTab === 'ledger' && (
              <div className="space-y-3 max-h-72 overflow-y-auto">
                {userTx.length === 0 ? (
                  <p className="text-xs text-[#9498AE] text-center py-6">No coin transactions on record for this user.</p>
                ) : (
                  userTx.map((tx) => (
                    <div
                      key={tx.id}
                      className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] flex items-center justify-between text-xs"
                    >
                      <div>
                        <span className="font-bold text-[#F5F5FA] block">{tx.reason}</span>
                        <span className="text-[10px] text-[#9498AE]">
                          {tx.createdAt} • by {tx.createdBy}
                        </span>
                      </div>
                      <span
                        className={`font-extrabold text-sm ${
                          tx.amount > 0 ? 'text-[#C8FF3D]' : 'text-[#FF7A8A]'
                        }`}
                      >
                        {tx.amount > 0 ? `+${tx.amount}` : tx.amount}
                      </span>
                    </div>
                  ))
                )}
              </div>
            )}

            {/* TAB 3: Tournament Entries */}
            {activeUserTab === 'tournaments' && (
              <div className="space-y-3 max-h-72 overflow-y-auto">
                {userRegs.length === 0 ? (
                  <p className="text-xs text-[#9498AE] text-center py-6">No tournament registrations on record.</p>
                ) : (
                  userRegs.map((reg) => (
                    <div
                      key={reg.id}
                      className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] flex items-center justify-between text-xs"
                    >
                      <div>
                        <span className="font-bold text-[#F5F5FA] block">{reg.tournamentName || reg.tournamentId}</span>
                        <span className="text-[10px] text-[#9498AE]">Registered: {reg.registeredAt}</span>
                      </div>
                      <Badge variant={reg.status === 'CheckedIn' ? 'lime' : 'blue'}>{reg.status}</Badge>
                    </div>
                  ))
                )}
              </div>
            )}
          </div>
        </Modal>
      )}
    </div>
  );
};
