import React, { useState, useEffect, useMemo } from 'react';
import {
  Users,
  Search,
  Download,
  Trash2,
  Plus,
  AlertCircle,
  CheckCircle2,
  XCircle,
  Clock,
  Filter,
  ArrowUpDown,
  ArrowUp,
  ArrowDown,
  ShieldAlert,
  ChevronDown,
  Sparkles,
  RotateCcw,
} from 'lucide-react';
import { Registration, RegistrationStatus, Tournament } from '../types';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';

type SortField = 'status' | 'registeredAt' | 'inGameName' | 'ffUid' | 'tournamentName';
type SortDirection = 'asc' | 'desc';

// Status sorting weight: Pending first for asc, or rejected first for desc
const STATUS_PRIORITY_ASC: Record<RegistrationStatus, number> = {
  Pending: 1,
  Approved: 2,
  Confirmed: 3,
  CheckedIn: 4,
  Rejected: 5,
  Disqualified: 6,
};

export const RegistrationsPage: React.FC = () => {
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<string>('all');
  const [registrations, setRegistrations] = useState<Registration[]>([]);
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [search, setSearch] = useState('');
  
  // Sorting state
  const [sortField, setSortField] = useState<SortField>('status');
  const [sortDirection, setSortDirection] = useState<SortDirection>('asc');

  // Add modal state
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [newFfUid, setNewFfUid] = useState('');
  const [newIgn, setNewIgn] = useState('');
  const [newStatus, setNewStatus] = useState<RegistrationStatus>('Approved');
  const [newAdminNote, setNewAdminNote] = useState('');
  const [addError, setAddError] = useState<string | null>(null);

  // Status edit modal state
  const [editingRegistration, setEditingRegistration] = useState<Registration | null>(null);
  const [editStatus, setEditStatus] = useState<RegistrationStatus>('Approved');
  const [editNote, setEditNote] = useState('');

  const [actionNotice, setActionNotice] = useState<string | null>(null);

  useEffect(() => {
    loadData();
  }, [selectedTournamentId]);

  const loadData = async () => {
    const tList = await TournamentService.getTournaments();
    setTournaments(tList);
    const regs = await TournamentService.getRegistrations(
      selectedTournamentId === 'all' ? undefined : selectedTournamentId
    );
    setRegistrations(regs);
  };

  const showNotice = (msg: string) => {
    setActionNotice(msg);
    setTimeout(() => setActionNotice(null), 3500);
  };

  const handleRemove = async (regId: string) => {
    if (confirm('Are you sure you want to remove this player from the tournament?')) {
      await TournamentService.removeRegistration(regId);
      await loadData();
      showNotice('Participant removed and slot freed.');
    }
  };

  const handleQuickStatusChange = async (regId: string, status: RegistrationStatus) => {
    await TournamentService.updateRegistrationStatus(regId, status);
    await loadData();
    showNotice(`Registration status updated to ${status}.`);
  };

  const handleSaveStatusModal = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingRegistration) return;
    await TournamentService.updateRegistrationStatus(
      editingRegistration.id,
      editStatus,
      editNote.trim() || undefined
    );
    await loadData();
    setEditingRegistration(null);
    showNotice(`Registration updated: ${editStatus}`);
  };

  const handleAddPlayer = async (e: React.FormEvent) => {
    e.preventDefault();
    setAddError(null);

    if (selectedTournamentId === 'all') {
      setAddError('Please select a specific tournament before adding a player.');
      return;
    }

    // Free Fire UID numeric validation
    if (!/^\d{6,14}$/.test(newFfUid.trim())) {
      setAddError('Free Fire UID must be between 6 and 14 digits.');
      return;
    }

    // BUSINESS RULE: Prevent one UID joining twice in the same tournament
    const existing = registrations.find(
      (r) => r.tournamentId === selectedTournamentId && r.ffUid.trim() === newFfUid.trim()
    );

    if (existing) {
      setAddError(`Free Fire UID ${newFfUid} is ALREADY registered in this tournament!`);
      return;
    }

    const targetTournament = tournaments.find((t) => t.id === selectedTournamentId);

    const newReg: Registration = {
      id: `reg_${Date.now()}`,
      tournamentId: selectedTournamentId,
      tournamentName: targetTournament?.name || 'Tournament',
      userId: `usr_${newFfUid.trim()}`,
      ffUid: newFfUid.trim(),
      inGameName: newIgn.trim() || `Player_${newFfUid.slice(-4)}`,
      status: newStatus,
      registeredAt: 'Just now (Manual Entry)',
      adminNote: newAdminNote.trim() || undefined,
    };

    const currentList = await TournamentService.getRegistrations();
    currentList.unshift(newReg);
    localStorage.setItem('ff_arena_admin_registrations', JSON.stringify(currentList));

    await loadData();
    setIsAddModalOpen(false);
    setNewFfUid('');
    setNewIgn('');
    setNewAdminNote('');
    setNewStatus('Approved');
    showNotice(`Player ${newReg.inGameName} registered as ${newStatus}.`);
  };

  const handleSort = (field: SortField) => {
    if (sortField === field) {
      setSortDirection((prev) => (prev === 'asc' ? 'desc' : 'asc'));
    } else {
      setSortField(field);
      setSortDirection('asc');
    }
  };

  // Status counts based on tournament filter & search
  const statusCounts = useMemo(() => {
    const counts = {
      ALL: registrations.length,
      Pending: 0,
      Approved: 0,
      CheckedIn: 0,
      Rejected: 0,
    };

    registrations.forEach((r) => {
      if (r.status === 'Pending') counts.Pending++;
      else if (r.status === 'Approved' || r.status === 'Confirmed') counts.Approved++;
      else if (r.status === 'CheckedIn') counts.CheckedIn++;
      else if (r.status === 'Rejected' || r.status === 'Disqualified') counts.Rejected++;
    });

    return counts;
  }, [registrations]);

  // Filtered and sorted registrations
  const filteredAndSorted = useMemo(() => {
    // 1. Filter
    let list = registrations.filter((r) => {
      // Search
      const matchesSearch =
        r.inGameName.toLowerCase().includes(search.toLowerCase()) ||
        r.ffUid.includes(search) ||
        (r.tournamentName && r.tournamentName.toLowerCase().includes(search.toLowerCase())) ||
        (r.adminNote && r.adminNote.toLowerCase().includes(search.toLowerCase()));

      if (!matchesSearch) return false;

      // Status Filter
      if (statusFilter === 'ALL') return true;
      if (statusFilter === 'Pending') return r.status === 'Pending';
      if (statusFilter === 'Approved') return r.status === 'Approved' || r.status === 'Confirmed';
      if (statusFilter === 'CheckedIn') return r.status === 'CheckedIn';
      if (statusFilter === 'Rejected') return r.status === 'Rejected' || r.status === 'Disqualified';
      return r.status === statusFilter;
    });

    // 2. Sort
    list = [...list].sort((a, b) => {
      if (sortField === 'status') {
        const pA = STATUS_PRIORITY_ASC[a.status] || 99;
        const pB = STATUS_PRIORITY_ASC[b.status] || 99;
        if (pA !== pB) {
          return sortDirection === 'asc' ? pA - pB : pB - pA;
        }
        return a.inGameName.localeCompare(b.inGameName);
      }

      if (sortField === 'inGameName') {
        const res = a.inGameName.localeCompare(b.inGameName);
        return sortDirection === 'asc' ? res : -res;
      }

      if (sortField === 'ffUid') {
        const res = a.ffUid.localeCompare(b.ffUid);
        return sortDirection === 'asc' ? res : -res;
      }

      if (sortField === 'tournamentName') {
        const nameA = a.tournamentName || '';
        const nameB = b.tournamentName || '';
        const res = nameA.localeCompare(nameB);
        return sortDirection === 'asc' ? res : -res;
      }

      if (sortField === 'registeredAt') {
        const res = a.registeredAt.localeCompare(b.registeredAt);
        return sortDirection === 'asc' ? res : -res;
      }

      return 0;
    });

    return list;
  }, [registrations, search, statusFilter, sortField, sortDirection]);

  const exportCSV = () => {
    if (filteredAndSorted.length === 0) {
      showNotice('No registrations found with the current filters to download.');
      return;
    }

    const headers = [
      'Registration ID',
      'Tournament ID',
      'Tournament Name',
      'Free Fire UID',
      'In-Game Name',
      'Status',
      'Admin Note',
      'Registered At',
      'Export Timestamp',
    ];

    const exportTime = new Date().toISOString();
    const rows = filteredAndSorted.map((r) => [
      `"${r.id.replace(/"/g, '""')}"`,
      `"${r.tournamentId.replace(/"/g, '""')}"`,
      `"${(r.tournamentName || '').replace(/"/g, '""')}"`,
      `"${r.ffUid.replace(/"/g, '""')}"`,
      `"${r.inGameName.replace(/"/g, '""')}"`,
      `"${r.status.replace(/"/g, '""')}"`,
      `"${(r.adminNote || '').replace(/"/g, '""')}"`,
      `"${r.registeredAt.replace(/"/g, '""')}"`,
      `"${exportTime}"`,
    ]);

    // Include UTF-8 BOM (\uFEFF) for full Excel and international character support
    const csvContent =
      '\uFEFF' +
      [headers.join(','), ...rows.map((row) => row.join(','))].join('\r\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;

    const tournamentTag = selectedTournamentId !== 'all' ? `_${selectedTournamentId}` : '';
    const statusTag = statusFilter !== 'ALL' ? `_${statusFilter.toLowerCase()}` : '';
    const dateStr = new Date().toISOString().slice(0, 10);
    link.download = `ff_arena_registrations${tournamentTag}${statusTag}_${dateStr}.csv`;

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);

    showNotice(`Downloaded CSV: ${filteredAndSorted.length} registrations exported.`);
  };

  const getStatusBadge = (status: RegistrationStatus) => {
    switch (status) {
      case 'Pending':
        return (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-[#FFB300]/15 text-[#FFCA28] border border-[#FFB300]/40">
            <Clock className="w-3 h-3 animate-pulse" />
            <span>Pending</span>
          </span>
        );
      case 'Approved':
      case 'Confirmed':
        return (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-[#C8FF3D]/15 text-[#C8FF3D] border border-[#C8FF3D]/40">
            <CheckCircle2 className="w-3 h-3" />
            <span>{status === 'Confirmed' ? 'Confirmed' : 'Approved'}</span>
          </span>
        );
      case 'CheckedIn':
        return (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-[#7B5CFF]/15 text-[#B39DDB] border border-[#7B5CFF]/40">
            <Sparkles className="w-3 h-3" />
            <span>Checked In</span>
          </span>
        );
      case 'Rejected':
        return (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-[#FF7A8A]/15 text-[#FF7A8A] border border-[#FF7A8A]/40">
            <XCircle className="w-3 h-3" />
            <span>Rejected</span>
          </span>
        );
      case 'Disqualified':
        return (
          <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-[#FF5252]/15 text-[#FF8A80] border border-[#FF5252]/40">
            <ShieldAlert className="w-3 h-3" />
            <span>Disqualified</span>
          </span>
        );
      default:
        return <Badge variant="gray">{status}</Badge>;
    }
  };

  const renderSortIcon = (field: SortField) => {
    if (sortField !== field) {
      return <ArrowUpDown className="w-3.5 h-3.5 text-[#9498AE]/40 group-hover:text-[#9498AE] transition-colors" />;
    }
    return sortDirection === 'asc' ? (
      <ArrowUp className="w-3.5 h-3.5 text-[#C8FF3D]" />
    ) : (
      <ArrowDown className="w-3.5 h-3.5 text-[#C8FF3D]" />
    );
  };

  return (
    <div className="space-y-6">
      {actionNotice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-xs font-bold text-[#C8FF3D] animate-fade-in shadow-lg shadow-[#C8FF3D]/5">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{actionNotice}</span>
        </div>
      )}

      {/* Overview Metric Cards */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 sm:gap-4">
        <div
          onClick={() => setStatusFilter('ALL')}
          className={`cursor-pointer p-4 rounded-[20px] border transition-all ${
            statusFilter === 'ALL'
              ? 'bg-[#14141F] border-[#C8FF3D] shadow-lg shadow-[#C8FF3D]/5 ring-1 ring-[#C8FF3D]/20'
              : 'bg-[#14141F] border-[#23233A] hover:border-[#383852]'
          }`}
        >
          <div className="flex items-center justify-between text-xs text-[#9498AE]">
            <span className="font-semibold uppercase tracking-wider">Total</span>
            <Users className="w-4 h-4 text-[#9498AE]" />
          </div>
          <div className="mt-2 text-2xl font-black text-[#F5F5FA]">{statusCounts.ALL}</div>
          <div className="text-[11px] text-[#9498AE] mt-0.5">All registrations</div>
        </div>

        <div
          onClick={() => setStatusFilter('Pending')}
          className={`cursor-pointer p-4 rounded-[20px] border transition-all ${
            statusFilter === 'Pending'
              ? 'bg-[#14141F] border-[#FFB300] shadow-lg shadow-[#FFB300]/10 ring-1 ring-[#FFB300]/30'
              : 'bg-[#14141F] border-[#23233A] hover:border-[#FFB300]/50'
          }`}
        >
          <div className="flex items-center justify-between text-xs text-[#FFCA28]">
            <span className="font-semibold uppercase tracking-wider">Pending Review</span>
            <Clock className="w-4 h-4 text-[#FFCA28] animate-pulse" />
          </div>
          <div className="mt-2 text-2xl font-black text-[#FFCA28]">{statusCounts.Pending}</div>
          <div className="text-[11px] text-[#9498AE] mt-0.5">Require admin action</div>
        </div>

        <div
          onClick={() => setStatusFilter('Approved')}
          className={`cursor-pointer p-4 rounded-[20px] border transition-all ${
            statusFilter === 'Approved'
              ? 'bg-[#14141F] border-[#C8FF3D] shadow-lg shadow-[#C8FF3D]/10 ring-1 ring-[#C8FF3D]/30'
              : 'bg-[#14141F] border-[#23233A] hover:border-[#C8FF3D]/50'
          }`}
        >
          <div className="flex items-center justify-between text-xs text-[#C8FF3D]">
            <span className="font-semibold uppercase tracking-wider">Approved / Confirmed</span>
            <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
          </div>
          <div className="mt-2 text-2xl font-black text-[#C8FF3D]">{statusCounts.Approved}</div>
          <div className="text-[11px] text-[#9498AE] mt-0.5">Confirmed slot holders</div>
        </div>

        <div
          onClick={() => setStatusFilter('Rejected')}
          className={`cursor-pointer p-4 rounded-[20px] border transition-all ${
            statusFilter === 'Rejected'
              ? 'bg-[#14141F] border-[#FF7A8A] shadow-lg shadow-[#FF7A8A]/10 ring-1 ring-[#FF7A8A]/30'
              : 'bg-[#14141F] border-[#23233A] hover:border-[#FF7A8A]/50'
          }`}
        >
          <div className="flex items-center justify-between text-xs text-[#FF7A8A]">
            <span className="font-semibold uppercase tracking-wider">Rejected</span>
            <XCircle className="w-4 h-4 text-[#FF7A8A]" />
          </div>
          <div className="mt-2 text-2xl font-black text-[#FF7A8A]">{statusCounts.Rejected}</div>
          <div className="text-[11px] text-[#9498AE] mt-0.5">Ineligible or duplicate</div>
        </div>
      </div>

      {/* Filters Toolbar */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-4 sm:p-5 space-y-4">
        {/* Status Filter Tabs / Chips */}
        <div className="flex flex-wrap items-center gap-2 border-b border-[#23233A] pb-3">
          <span className="text-xs font-bold uppercase tracking-wider text-[#9498AE] flex items-center gap-1.5 mr-2">
            <Filter className="w-3.5 h-3.5 text-[#C8FF3D]" />
            Status:
          </span>

          <button
            onClick={() => setStatusFilter('ALL')}
            className={`px-3 py-1.5 rounded-full text-xs font-bold transition-all ${
              statusFilter === 'ALL'
                ? 'bg-[#C8FF3D] text-black shadow-md shadow-[#C8FF3D]/20'
                : 'bg-[#0A0A0F] text-[#9498AE] hover:text-[#F5F5FA] border border-[#23233A]'
            }`}
          >
            All ({statusCounts.ALL})
          </button>

          <button
            onClick={() => setStatusFilter('Pending')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all ${
              statusFilter === 'Pending'
                ? 'bg-[#FFB300] text-black shadow-md shadow-[#FFB300]/20'
                : 'bg-[#0A0A0F] text-[#FFCA28] border border-[#FFB300]/30 hover:bg-[#FFB300]/10'
            }`}
          >
            <Clock className="w-3 h-3" />
            Pending ({statusCounts.Pending})
          </button>

          <button
            onClick={() => setStatusFilter('Approved')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all ${
              statusFilter === 'Approved'
                ? 'bg-[#C8FF3D] text-black shadow-md shadow-[#C8FF3D]/20'
                : 'bg-[#0A0A0F] text-[#C8FF3D] border border-[#C8FF3D]/30 hover:bg-[#C8FF3D]/10'
            }`}
          >
            <CheckCircle2 className="w-3 h-3" />
            Approved ({statusCounts.Approved})
          </button>

          <button
            onClick={() => setStatusFilter('CheckedIn')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all ${
              statusFilter === 'CheckedIn'
                ? 'bg-[#7B5CFF] text-white shadow-md shadow-[#7B5CFF]/20'
                : 'bg-[#0A0A0F] text-[#B39DDB] border border-[#7B5CFF]/30 hover:bg-[#7B5CFF]/10'
            }`}
          >
            <Sparkles className="w-3 h-3" />
            Checked In ({statusCounts.CheckedIn})
          </button>

          <button
            onClick={() => setStatusFilter('Rejected')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-bold transition-all ${
              statusFilter === 'Rejected'
                ? 'bg-[#FF7A8A] text-black shadow-md shadow-[#FF7A8A]/20'
                : 'bg-[#0A0A0F] text-[#FF7A8A] border border-[#FF7A8A]/30 hover:bg-[#FF7A8A]/10'
            }`}
          >
            <XCircle className="w-3 h-3" />
            Rejected ({statusCounts.Rejected})
          </button>

          {(statusFilter !== 'ALL' || search || selectedTournamentId !== 'all') && (
            <button
              onClick={() => {
                setStatusFilter('ALL');
                setSearch('');
                setSelectedTournamentId('all');
              }}
              className="ml-auto inline-flex items-center gap-1 text-[11px] font-bold text-[#9498AE] hover:text-[#F5F5FA] px-2.5 py-1 rounded-lg hover:bg-[#23233A] transition-colors"
            >
              <RotateCcw className="w-3 h-3" />
              Reset Filters
            </button>
          )}
        </div>

        {/* Tournament, Search & Sorting Controls */}
        <div className="flex flex-col lg:flex-row items-stretch lg:items-center justify-between gap-3">
          <div className="flex flex-1 flex-wrap items-center gap-3">
            {/* Tournament Filter */}
            <div className="min-w-[180px]">
              <select
                value={selectedTournamentId}
                onChange={(e) => setSelectedTournamentId(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none focus:border-[#C8FF3D]"
              >
                <option value="all">All Tournaments</option>
                {tournaments.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.name} ({t.joinedSlots}/{t.maxSlots})
                  </option>
                ))}
              </select>
            </div>

            {/* Search */}
            <div className="relative flex-1 min-w-[220px]">
              <Search className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
              <input
                type="text"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Search player IGN, Free Fire UID, note..."
                className="w-full bg-[#0A0A0F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2.5 text-xs focus:outline-none"
              />
              {search && (
                <button
                  onClick={() => setSearch('')}
                  className="absolute right-3 top-2.5 text-[#9498AE] hover:text-[#F5F5FA] text-xs"
                >
                  Clear
                </button>
              )}
            </div>

            {/* Quick Sort Dropdown */}
            <div className="flex items-center gap-2">
              <span className="text-xs text-[#9498AE] font-semibold whitespace-nowrap">Sort:</span>
              <select
                value={`${sortField}-${sortDirection}`}
                onChange={(e) => {
                  const [field, dir] = e.target.value.split('-');
                  setSortField(field as SortField);
                  setSortDirection(dir as SortDirection);
                }}
                className="bg-[#0A0A0F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3 py-2.5 text-xs focus:outline-none focus:border-[#C8FF3D]"
              >
                <option value="status-asc">Status: Pending First</option>
                <option value="status-desc">Status: Rejected First</option>
                <option value="inGameName-asc">IGN: A to Z</option>
                <option value="inGameName-desc">IGN: Z to A</option>
                <option value="ffUid-asc">UID: Ascending</option>
                <option value="registeredAt-desc">Time: Newest</option>
                <option value="registeredAt-asc">Time: Oldest</option>
              </select>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex items-center gap-3 pt-2 lg:pt-0">
            <button
              onClick={exportCSV}
              disabled={filteredAndSorted.length === 0}
              title={
                filteredAndSorted.length === 0
                  ? 'No registrations match current filter'
                  : `Export ${filteredAndSorted.length} filtered registrations to CSV`
              }
              className="inline-flex items-center justify-center gap-2 bg-[#0A0A0F] hover:bg-[#23233A] hover:border-[#C8FF3D]/50 disabled:opacity-40 disabled:hover:bg-[#0A0A0F] text-[#F5F5FA] border border-[#23233A] font-bold px-4 py-2.5 rounded-[18px] text-xs transition-all shadow-sm"
            >
              <Download className="w-4 h-4 text-[#C8FF3D]" />
              <span>Download CSV ({filteredAndSorted.length})</span>
            </button>

            <button
              onClick={() => {
                setAddError(null);
                setIsAddModalOpen(true);
              }}
              className="inline-flex items-center justify-center gap-2 bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold px-5 py-2.5 rounded-[18px] text-xs transition-all shadow-md shadow-[#C8FF3D]/10"
            >
              <Plus className="w-4 h-4 stroke-2" />
              <span>ADD PLAYER</span>
            </button>
          </div>
        </div>
      </div>

      {/* Registrations Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F] text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A] select-none">
              <tr>
                <th
                  onClick={() => handleSort('inGameName')}
                  className="px-6 py-4 cursor-pointer hover:text-[#C8FF3D] group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span>Player & IGN</span>
                    {renderSortIcon('inGameName')}
                  </div>
                </th>
                <th
                  onClick={() => handleSort('ffUid')}
                  className="px-4 py-4 cursor-pointer hover:text-[#C8FF3D] group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span>Free Fire UID</span>
                    {renderSortIcon('ffUid')}
                  </div>
                </th>
                <th
                  onClick={() => handleSort('tournamentName')}
                  className="px-4 py-4 cursor-pointer hover:text-[#C8FF3D] group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span>Tournament</span>
                    {renderSortIcon('tournamentName')}
                  </div>
                </th>
                <th
                  onClick={() => handleSort('status')}
                  className="px-4 py-4 cursor-pointer hover:text-[#C8FF3D] group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span>Status (Click to Sort)</span>
                    {renderSortIcon('status')}
                  </div>
                </th>
                <th
                  onClick={() => handleSort('registeredAt')}
                  className="px-4 py-4 cursor-pointer hover:text-[#C8FF3D] group transition-colors"
                >
                  <div className="flex items-center gap-2">
                    <span>Registered</span>
                    {renderSortIcon('registeredAt')}
                  </div>
                </th>
                <th className="px-6 py-4 text-right">Admin Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {filteredAndSorted.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-14 text-center">
                    <div className="max-w-xs mx-auto space-y-3">
                      <div className="w-12 h-12 rounded-full bg-[#23233A]/60 flex items-center justify-center mx-auto text-[#9498AE]">
                        <Users className="w-6 h-6" />
                      </div>
                      <p className="font-bold text-[#F5F5FA]">No matching registrations</p>
                      <p className="text-xs text-[#9498AE]">
                        {statusFilter !== 'ALL'
                          ? `No registrations with status "${statusFilter}". Try switching filters.`
                          : 'Try adjusting your search query or selected tournament.'}
                      </p>
                      <button
                        onClick={() => {
                          setStatusFilter('ALL');
                          setSearch('');
                          setSelectedTournamentId('all');
                        }}
                        className="mt-2 px-4 py-1.5 rounded-full text-xs font-bold bg-[#23233A] text-[#F5F5FA] hover:bg-[#2E2E4A]"
                      >
                        Clear Filters
                      </button>
                    </div>
                  </td>
                </tr>
              ) : (
                filteredAndSorted.map((r) => (
                  <tr key={r.id} className="hover:bg-[#1A1A28]/50 transition-colors group">
                    {/* Player IGN */}
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 rounded-full bg-[#7B5CFF]/20 border border-[#7B5CFF]/30 flex items-center justify-center text-[#7B5CFF] font-bold shrink-0">
                          {r.inGameName.slice(0, 2).replace(/[^A-Za-z0-9]/g, '') || 'FF'}
                        </div>
                        <div>
                          <div className="font-extrabold text-[#F5F5FA] text-sm group-hover:text-[#C8FF3D] transition-colors">
                            {r.inGameName}
                          </div>
                          {r.adminNote && (
                            <div className="text-[11px] text-[#FFCA28] mt-0.5 truncate max-w-[200px]">
                              Note: {r.adminNote}
                            </div>
                          )}
                        </div>
                      </div>
                    </td>

                    {/* FF UID */}
                    <td className="px-4 py-4 font-mono font-bold text-[#C8FF3D]">
                      {r.ffUid}
                    </td>

                    {/* Tournament */}
                    <td className="px-4 py-4 text-[#F5F5FA] font-medium max-w-[220px] truncate">
                      {r.tournamentName || r.tournamentId}
                    </td>

                    {/* Status with Quick Click */}
                    <td className="px-4 py-4">
                      <div className="flex items-center gap-2">
                        {getStatusBadge(r.status)}
                      </div>
                    </td>

                    {/* Registered Date */}
                    <td className="px-4 py-4 text-[#9498AE] whitespace-nowrap">
                      {r.registeredAt}
                    </td>

                    {/* Actions */}
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-1.5">
                        {/* Quick Approve / Reject for Pending */}
                        {r.status === 'Pending' && (
                          <>
                            <button
                              onClick={() => handleQuickStatusChange(r.id, 'Approved')}
                              title="Approve Player Slot"
                              className="px-2.5 py-1 text-[11px] font-extrabold rounded-lg bg-[#C8FF3D]/20 text-[#C8FF3D] border border-[#C8FF3D]/40 hover:bg-[#C8FF3D] hover:text-black transition-all"
                            >
                              Approve
                            </button>
                            <button
                              onClick={() => handleQuickStatusChange(r.id, 'Rejected')}
                              title="Reject Player Slot"
                              className="px-2.5 py-1 text-[11px] font-extrabold rounded-lg bg-[#FF7A8A]/20 text-[#FF7A8A] border border-[#FF7A8A]/40 hover:bg-[#FF7A8A] hover:text-black transition-all"
                            >
                              Reject
                            </button>
                          </>
                        )}

                        {/* Status Edit Modal Trigger */}
                        <button
                          onClick={() => {
                            setEditingRegistration(r);
                            setEditStatus(r.status);
                            setEditNote(r.adminNote || '');
                          }}
                          title="Change status or add note"
                          className="px-2.5 py-1 text-[11px] font-semibold text-[#9498AE] hover:text-[#F5F5FA] bg-[#0A0A0F] border border-[#23233A] hover:border-[#383852] rounded-lg transition-colors"
                        >
                          Status
                        </button>

                        {/* Delete registration */}
                        <button
                          onClick={() => handleRemove(r.id)}
                          title="Remove Player from Tournament"
                          className="p-1.5 text-[#9498AE] hover:text-[#FF7A8A] hover:bg-[#23233A] rounded-lg transition-colors ml-1"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Table footer info */}
        <div className="p-4 bg-[#0A0A0F] border-t border-[#23233A] flex flex-col sm:flex-row items-center justify-between text-xs text-[#9498AE] gap-3">
          <div>
            Showing <span className="font-bold text-[#F5F5FA]">{filteredAndSorted.length}</span> of{' '}
            <span className="font-bold text-[#F5F5FA]">{registrations.length}</span> registrations
            {statusFilter !== 'ALL' && (
              <span> (Filtered by: <span className="text-[#C8FF3D] font-bold">{statusFilter}</span>)</span>
            )}
          </div>
          <div className="flex items-center gap-4 text-[11px]">
            <div className="flex items-center gap-1.5">
              <span>Sorted by:</span>
              <span className="font-bold text-[#C8FF3D] capitalize">
                {sortField === 'status' ? 'Status' : sortField} ({sortDirection === 'asc' ? 'Ascending' : 'Descending'})
              </span>
            </div>

            <button
              onClick={exportCSV}
              disabled={filteredAndSorted.length === 0}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-[#14141F] hover:bg-[#23233A] border border-[#23233A] hover:border-[#C8FF3D]/40 text-[#F5F5FA] font-bold text-xs disabled:opacity-40 transition-all"
            >
              <Download className="w-3.5 h-3.5 text-[#C8FF3D]" />
              <span>Download CSV</span>
            </button>
          </div>
        </div>
      </div>

      {/* Change Status Modal */}
      <Modal
        isOpen={!!editingRegistration}
        onClose={() => setEditingRegistration(null)}
        title="Update Registration Status"
        subtitle={`Player: ${editingRegistration?.inGameName} (UID: ${editingRegistration?.ffUid})`}
        maxWidth="max-w-md"
      >
        <form onSubmit={handleSaveStatusModal} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Registration Status *
            </label>
            <select
              value={editStatus}
              onChange={(e) => setEditStatus(e.target.value as RegistrationStatus)}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none"
            >
              <option value="Pending">Pending (Under Review)</option>
              <option value="Approved">Approved (Slot Confirmed)</option>
              <option value="Confirmed">Confirmed</option>
              <option value="CheckedIn">Checked In (Ready for Room)</option>
              <option value="Rejected">Rejected (Ineligible/Blocked)</option>
              <option value="Disqualified">Disqualified (Rules Violation)</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Admin Note / Reason
            </label>
            <textarea
              rows={3}
              value={editNote}
              onChange={(e) => setEditNote(e.target.value)}
              placeholder="e.g. KYC verified, Level 40+ proof confirmed, or reason for rejection..."
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-xs focus:outline-none"
            />
          </div>

          <div className="flex items-center justify-end space-x-3 pt-3 border-t border-[#23233A]">
            <button
              type="button"
              onClick={() => setEditingRegistration(null)}
              className="px-4 py-2 text-xs font-bold text-[#9498AE] hover:text-[#F5F5FA]"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black hover:bg-[#b8f02d]"
            >
              SAVE STATUS
            </button>
          </div>
        </form>
      </Modal>

      {/* Add Player Modal */}
      <Modal
        isOpen={isAddModalOpen}
        onClose={() => setIsAddModalOpen(false)}
        title="Register Player for Tournament"
        subtitle="Enforces duplicate UID prevention rule (Free Fire MAX 18+ Only)"
        maxWidth="max-w-md"
      >
        <form onSubmit={handleAddPlayer} className="space-y-4">
          {addError && (
            <div className="p-3 bg-[#FF7A8A]/10 border border-[#FF7A8A]/30 rounded-[16px] flex items-center gap-2 text-xs text-[#FF7A8A]">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{addError}</span>
            </div>
          )}

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Target Tournament *
            </label>
            <select
              value={selectedTournamentId}
              onChange={(e) => setSelectedTournamentId(e.target.value)}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none"
            >
              <option value="all" disabled>
                Choose a tournament
              </option>
              {tournaments.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Free Fire Numerical UID (6-14 digits) *
            </label>
            <input
              type="text"
              required
              value={newFfUid}
              onChange={(e) => setNewFfUid(e.target.value)}
              placeholder="e.g. 839472819"
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none font-mono"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              In-Game Name (IGN)
            </label>
            <input
              type="text"
              value={newIgn}
              onChange={(e) => setNewIgn(e.target.value)}
              placeholder="e.g. ⚡ViperSniper⚡"
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Initial Registration Status
            </label>
            <select
              value={newStatus}
              onChange={(e) => setNewStatus(e.target.value as RegistrationStatus)}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none"
            >
              <option value="Approved">Approved (Confirmed Slot)</option>
              <option value="Pending">Pending (Review Required)</option>
              <option value="CheckedIn">Checked In</option>
              <option value="Rejected">Rejected</option>
            </select>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
              Admin Note (Optional)
            </label>
            <input
              type="text"
              value={newAdminNote}
              onChange={(e) => setNewAdminNote(e.target.value)}
              placeholder="e.g. Level 45 verified, Discord tag: @viper"
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-xs focus:outline-none"
            />
          </div>

          <div className="flex items-center justify-end space-x-3 pt-3 border-t border-[#23233A]">
            <button
              type="button"
              onClick={() => setIsAddModalOpen(false)}
              className="px-4 py-2 text-xs font-bold text-[#9498AE] hover:text-[#F5F5FA]"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black hover:bg-[#b8f02d]"
            >
              CONFIRM REGISTRATION
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
