import React, { useState } from 'react';
import { PlayerMatch, GameMode, MapType, TournamentRegistration, MatchPlacementResult } from '../types';
import { TournamentService } from '../../services/tournamentService';
import {
  ArrowLeft,
  PlusCircle,
  Key,
  Users,
  Trophy,
  Megaphone,
  CheckCircle2,
  AlertCircle,
  Calendar,
  Clock,
  Sparkles,
  Shield,
  Layers,
  ChevronDown,
  Send,
  Eye,
  RefreshCw,
  UserPlus,
  HelpCircle,
  X,
  Award
} from 'lucide-react';

interface OrganizerConsoleProps {
  onClose: () => void;
  matches: PlayerMatch[];
  onRefreshMatches: () => void;
  onShowToast: (type: 'success' | 'error' | 'info', message: string) => void;
}

export const OrganizerConsole: React.FC<OrganizerConsoleProps> = ({
  onClose,
  matches,
  onRefreshMatches,
  onShowToast,
}) => {
  const [activeSection, setActiveSection] = useState<
    'dashboard' | 'create' | 'rooms' | 'registrations' | 'results' | 'announcements'
  >('dashboard');
  const [showAdminGuide, setShowAdminGuide] = useState(false);
  const [isSyncing, setIsSyncing] = useState(false);

  const handleSyncNow = () => {
    setIsSyncing(true);
    setTimeout(() => {
      onRefreshMatches();
      setRegistrations(TournamentService.getPlayerRegistrations());
      setIsSyncing(false);
      onShowToast('success', 'Tournament data & live registrations synchronized!');
    }, 700);
  };

  // --- Section 1: Create Tournament State ---
  const [title, setTitle] = useState('');
  const [sponsor, setSponsor] = useState('');
  const [mode, setMode] = useState<GameMode>('Squad');
  const [map, setMap] = useState<MapType>('Bermuda');
  const [startTime, setStartTime] = useState(() => {
    const d = new Date(Date.now() + 3 * 3600 * 1000);
    return d.toISOString().slice(0, 16);
  });
  const [sponsorPrize, setSponsorPrize] = useState('10000');
  const [slots, setSlots] = useState('48');

  // --- Section 2: Set Room Credentials State ---
  const [selectedRoomMatchId, setSelectedRoomMatchId] = useState<string>(
    matches.length > 0 ? matches[0].id : ''
  );
  const [roomIdInput, setRoomIdInput] = useState('');
  const [roomPasswordInput, setRoomPasswordInput] = useState('');
  const [autoRevealMins, setAutoRevealMins] = useState('15');
  const [revealImmediately, setRevealImmediately] = useState(false);

  // Sync room inputs when selected match changes
  const currentRoomMatch = matches.find((m) => m.id === selectedRoomMatchId);

  const handleSelectRoomMatch = (mId: string) => {
    setSelectedRoomMatchId(mId);
    const m = matches.find((match) => match.id === mId);
    if (m) {
      setRoomIdInput(m.roomId || '');
      setRoomPasswordInput(m.roomPassword || '');
    }
  };

  // --- Section 3: View Registrations State ---
  const [selectedRegMatchId, setSelectedRegMatchId] = useState<string>(
    matches.length > 0 ? matches[0].id : ''
  );
  const [registrations, setRegistrations] = useState<TournamentRegistration[]>(() =>
    TournamentService.getPlayerRegistrations()
  );
  const [newRegIgn, setNewRegIgn] = useState('');
  const [newRegUid, setNewRegUid] = useState('');
  const [showAddRegModal, setShowAddRegModal] = useState(false);

  const filteredRegistrations = registrations.filter(
    (r) => !selectedRegMatchId || r.tournamentId === selectedRegMatchId
  );

  // --- Section 4: Publish Results State ---
  const [selectedResultMatchId, setSelectedResultMatchId] = useState<string>(
    matches.length > 0 ? matches[0].id : ''
  );
  const [firstIgn, setFirstIgn] = useState('');
  const [firstUid, setFirstUid] = useState('');
  const [firstKills, setFirstKills] = useState('12');
  const [firstPrize, setFirstPrize] = useState('₹5,000 Trophy + Sponsor Kit');

  const [secondIgn, setSecondIgn] = useState('');
  const [secondUid, setSecondUid] = useState('');
  const [secondKills, setSecondKills] = useState('8');
  const [secondPrize, setSecondPrize] = useState('₹3,000 Trophy');

  const [thirdIgn, setThirdIgn] = useState('');
  const [thirdUid, setThirdUid] = useState('');
  const [thirdKills, setThirdKills] = useState('5');
  const [thirdPrize, setThirdPrize] = useState('₹2,000 Trophy');

  // --- Section 5: Send Announcement State ---
  const [notifTitle, setNotifTitle] = useState('');
  const [notifMessage, setNotifMessage] = useState('');
  const [notifType, setNotifType] = useState<'system' | 'room' | 'match' | 'reward'>('system');

  // Handlers
  const handleCreateTournament = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) {
      onShowToast('error', 'Please enter a tournament title.');
      return;
    }

    const prizeNum = parseInt(sponsorPrize, 10) || 5000;
    const slotsNum = parseInt(slots, 10) || 48;

    TournamentService.createPlayerTournament({
      title: title.trim(),
      sponsorName: sponsor.trim() || 'Official Sponsor',
      mode,
      map,
      startTime: new Date(startTime).toISOString(),
      prizePool: prizeNum,
      maxSlots: slotsNum,
    });

    onRefreshMatches();
    onShowToast('success', `Tournament "${title}" created successfully!`);
    setTitle('');
    setSponsor('');
    setActiveSection('rooms');
  };

  const handleSaveRoomCredentials = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedRoomMatchId) {
      onShowToast('error', 'Select a tournament first.');
      return;
    }
    if (!roomIdInput.trim() || !roomPasswordInput.trim()) {
      onShowToast('error', 'Please provide both Room ID and Room Password.');
      return;
    }

    const mins = parseInt(autoRevealMins, 10) || 15;
    TournamentService.setMatchRoomCredentials(
      selectedRoomMatchId,
      roomIdInput.trim(),
      roomPasswordInput.trim(),
      mins,
      revealImmediately
    );

    onRefreshMatches();
    onShowToast('success', `Room ID & Password saved for match!`);
  };

  const handleAddRegistration = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newRegIgn.trim() || !newRegUid.trim()) {
      onShowToast('error', 'Enter player IGN and FF UID.');
      return;
    }
    const t = matches.find((m) => m.id === selectedRegMatchId);
    const newReg: TournamentRegistration = {
      id: `reg_${Date.now()}`,
      tournamentId: selectedRegMatchId,
      tournamentTitle: t?.title || 'Tournament',
      userId: `usr_${newRegUid.trim()}`,
      inGameName: newRegIgn.trim(),
      ffUid: newRegUid.trim(),
      teamMembers: t?.mode === 'Squad' ? ['Squad_Mate_1', 'Squad_Mate_2'] : [],
      registeredAt: 'Just now',
      status: 'Confirmed',
    };

    TournamentService.addPlayerRegistration(newReg);
    setRegistrations(TournamentService.getPlayerRegistrations());
    onRefreshMatches();
    setShowAddRegModal(false);
    setNewRegIgn('');
    setNewRegUid('');
    onShowToast('success', `Player ${newReg.inGameName} registered successfully!`);
  };

  const handlePublishResults = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedResultMatchId) {
      onShowToast('error', 'Select a tournament first.');
      return;
    }

    const placements: MatchPlacementResult[] = [
      {
        rank: 1,
        inGameName: firstIgn.trim() || '⚡SOUL_MORTAL⚡',
        ffUid: firstUid.trim() || '994821034',
        kills: parseInt(firstKills, 10) || 12,
        sponsorPrizeRecord: firstPrize.trim() || '₹5,000 Trophy',
      },
      {
        rank: 2,
        inGameName: secondIgn.trim() || '⚡NEON_SOUL⚡',
        ffUid: secondUid.trim() || '994821034',
        kills: parseInt(secondKills, 10) || 8,
        sponsorPrizeRecord: secondPrize.trim() || '₹3,000 Trophy',
      },
      {
        rank: 3,
        inGameName: thirdIgn.trim() || '🔥VIPER_GOD🔥',
        ffUid: thirdUid.trim() || '884910231',
        kills: parseInt(thirdKills, 10) || 5,
        sponsorPrizeRecord: thirdPrize.trim() || '₹2,000 Trophy',
      },
    ];

    TournamentService.publishMatchResults(selectedResultMatchId, placements);
    onRefreshMatches();
    onShowToast('success', `Official results published & notifications broadcast!`);
  };

  const handleSendAnnouncement = (e: React.FormEvent) => {
    e.preventDefault();
    if (!notifTitle.trim() || !notifMessage.trim()) {
      onShowToast('error', 'Please provide a title and announcement message.');
      return;
    }

    TournamentService.sendAnnouncement(notifTitle.trim(), notifMessage.trim(), notifType);
    onShowToast('success', 'Announcement sent to all players!');
    setNotifTitle('');
    setNotifMessage('');
  };

  return (
    <div className="fixed inset-0 z-50 bg-[#0A0A0F] text-[#F5F5FA] font-sora flex flex-col overflow-hidden animate-fade-in">
      {/* Top Bar */}
      <div className="bg-[#14141F] border-b border-[#23233A] px-4 py-3 flex items-center justify-between shrink-0 shadow-md">
        <div className="flex items-center gap-3">
          <button
            onClick={onClose}
            className="w-8 h-8 rounded-xl bg-[#1C1C2E] border border-[#23233A] flex items-center justify-center text-[#9498AE] hover:text-white transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-sm font-black text-white">Organizer Console</h1>
              <span className="px-2 py-0.5 bg-[#C8FF3D] text-black text-[9px] font-black rounded-full uppercase tracking-wider">
                Active
              </span>
            </div>
            <p className="text-[10px] text-[#9498AE]">Free Fire Custom Rooms & Sponsor Cups</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handleSyncNow}
            disabled={isSyncing}
            className="flex items-center gap-1.5 px-3 py-1.5 bg-[#1C1C2E] hover:bg-[#23233A] text-xs font-bold text-[#C8FF3D] border border-[#23233A] rounded-xl transition-all"
            title="Sync latest tournament data from backend"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${isSyncing ? 'animate-spin' : ''}`} />
            <span>{isSyncing ? 'Syncing...' : 'Sync Now'}</span>
          </button>
          <button
            onClick={() => setShowAdminGuide(true)}
            className="p-2 bg-[#1C1C2E] hover:bg-[#23233A] text-[#9498AE] hover:text-white border border-[#23233A] rounded-xl transition-colors"
            title="Admin Guide"
          >
            <HelpCircle className="w-4 h-4" />
          </button>
          <button
            onClick={onClose}
            className="px-2.5 py-1 text-xs font-bold text-[#9498AE] hover:text-white"
          >
            Close
          </button>
        </div>
      </div>

      {/* Horizontal Section Switcher (Scrollable) */}
      <div className="bg-[#0E0E17] border-b border-[#23233A] px-3 py-2 flex items-center gap-1.5 overflow-x-auto no-scrollbar shrink-0">
        {[
          { id: 'dashboard', label: 'Dashboard', icon: Layers },
          { id: 'create', label: 'Create', icon: PlusCircle },
          { id: 'rooms', label: 'Room ID & Pass', icon: Key },
          { id: 'registrations', label: 'Registrations', icon: Users },
          { id: 'results', label: 'Publish Results', icon: Trophy },
          { id: 'announcements', label: 'Announcement', icon: Megaphone },
        ].map((sec) => {
          const Icon = sec.icon;
          const isActive = activeSection === sec.id;
          return (
            <button
              key={sec.id}
              onClick={() => setActiveSection(sec.id as any)}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition-all ${
                isActive
                  ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.3)]'
                  : 'bg-[#14141F] text-[#9498AE] hover:text-white border border-[#23233A]'
              }`}
            >
              <Icon className="w-3.5 h-3.5" />
              <span>{sec.label}</span>
            </button>
          );
        })}
      </div>

      {/* Section Content Area (Scrollable) */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4 max-w-lg mx-auto w-full pb-20">
        {/* ============================================================== */}
        {/* SECTION 0: DASHBOARD (COUNTS & METRICS)                       */}
        {/* ============================================================== */}
        {activeSection === 'dashboard' && (
          <div className="space-y-4 animate-fade-in">
            {/* 4 Overview Count Cards */}
            <div className="grid grid-cols-2 gap-2.5">
              <div className="bg-[#14141F] border border-[#262640] p-3.5 rounded-2xl space-y-1">
                <div className="flex items-center justify-between text-[#A7A7C0]">
                  <span className="text-[10px] font-bold uppercase tracking-wider">Tournaments</span>
                  <Trophy className="w-4 h-4 text-[#C8FF3D]" />
                </div>
                <div className="text-2xl font-black text-white">{matches.length}</div>
                <span className="text-[9px] text-[#C8FF3D] font-bold">
                  {matches.filter((m) => m.status === 'Live').length} Live Now
                </span>
              </div>

              <div className="bg-[#14141F] border border-[#262640] p-3.5 rounded-2xl space-y-1">
                <div className="flex items-center justify-between text-[#A7A7C0]">
                  <span className="text-[10px] font-bold uppercase tracking-wider">Registered</span>
                  <Users className="w-4 h-4 text-[#7B5CFF]" />
                </div>
                <div className="text-2xl font-black text-white">{registrations.length}</div>
                <span className="text-[9px] text-[#7B5CFF] font-bold">Active Contestants</span>
              </div>

              <div className="bg-[#14141F] border border-[#262640] p-3.5 rounded-2xl space-y-1">
                <div className="flex items-center justify-between text-[#A7A7C0]">
                  <span className="text-[10px] font-bold uppercase tracking-wider">Sponsor Pools</span>
                  <Sparkles className="w-4 h-4 text-[#FFB800]" />
                </div>
                <div className="text-2xl font-black text-white">
                  ₹{matches.reduce((sum, m) => sum + m.prizePool, 0).toLocaleString()}
                </div>
                <span className="text-[9px] text-[#A7A7C0]">100% Sponsor Funded</span>
              </div>

              <div className="bg-[#14141F] border border-[#262640] p-3.5 rounded-2xl space-y-1">
                <div className="flex items-center justify-between text-[#A7A7C0]">
                  <span className="text-[10px] font-bold uppercase tracking-wider">Room Security</span>
                  <Key className="w-4 h-4 text-[#00E5FF]" />
                </div>
                <div className="text-2xl font-black text-white">
                  {matches.filter((m) => m.roomId).length}/{matches.length}
                </div>
                <span className="text-[9px] text-[#00E5FF] font-bold">Rooms Configured</span>
              </div>
            </div>

            {/* Quick Actions */}
            <div className="grid grid-cols-2 gap-2">
              <button
                onClick={() => setActiveSection('create')}
                className="py-2.5 px-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 transition-all"
              >
                <PlusCircle className="w-4 h-4" />
                <span>Create Cup</span>
              </button>
              <button
                onClick={() => setActiveSection('rooms')}
                className="py-2.5 px-3 bg-[#1B1B2B] hover:bg-[#262640] text-white border border-[#262640] font-bold text-xs rounded-xl flex items-center justify-center gap-1.5 transition-all"
              >
                <Key className="w-4 h-4 text-[#C8FF3D]" />
                <span>Set Room ID</span>
              </button>
            </div>

            {/* Tournaments Management List */}
            <div className="space-y-2">
              <div className="flex items-center justify-between">
                <span className="text-xs font-black text-white uppercase tracking-wider">
                  Managed Tournaments ({matches.length})
                </span>
                <button
                  onClick={onRefreshMatches}
                  className="text-[11px] text-[#C8FF3D] flex items-center gap-1 font-bold"
                >
                  <RefreshCw className="w-3 h-3" /> Refresh
                </button>
              </div>

              <div className="space-y-2">
                {matches.map((m) => (
                  <div
                    key={m.id}
                    className="bg-[#14141F] border border-[#262640] rounded-2xl p-3.5 space-y-2"
                  >
                    <div className="flex items-start justify-between">
                      <div>
                        <div className="flex items-center gap-1.5 mb-0.5">
                          <span className={`px-2 py-0.5 text-[9px] font-black rounded-full uppercase ${
                            m.status === 'Live'
                              ? 'bg-[#FF4D6D] text-white'
                              : m.status === 'Completed'
                              ? 'bg-slate-700 text-slate-300'
                              : 'bg-[#C8FF3D]/20 text-[#C8FF3D]'
                          }`}>
                            {m.status}
                          </span>
                          <span className="text-[10px] text-[#7B5CFF] font-bold">
                            {m.sponsorName}
                          </span>
                        </div>
                        <h4 className="text-xs font-black text-white">{m.title}</h4>
                      </div>

                      <div className="text-right">
                        <span className="text-xs font-black text-white block">
                          ₹{m.prizePool.toLocaleString()}
                        </span>
                        <span className="text-[9px] text-[#A7A7C0]">
                          {m.joinedSlots}/{m.maxSlots} Slots
                        </span>
                      </div>
                    </div>

                    <div className="flex items-center justify-between pt-1 border-t border-[#262640] text-[10px]">
                      <div className="flex items-center gap-2 text-[#A7A7C0]">
                        <span>Room: <strong className={m.roomId ? 'text-[#C8FF3D]' : 'text-slate-500'}>{m.roomId || 'Not set'}</strong></span>
                        <span>•</span>
                        <span>{m.mode} ({m.map})</span>
                      </div>

                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => {
                            setSelectedRoomMatchId(m.id);
                            setRoomIdInput(m.roomId || '');
                            setRoomPasswordInput(m.roomPassword || '');
                            setActiveSection('rooms');
                          }}
                          className="text-[#C8FF3D] font-bold hover:underline"
                        >
                          Config Room
                        </button>
                        <button
                          onClick={() => {
                            setSelectedResultMatchId(m.id);
                            setActiveSection('results');
                          }}
                          className="text-[#7B5CFF] font-bold hover:underline"
                        >
                          Results
                        </button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* ============================================================== */}
        {/* SECTION 1: CREATE TOURNAMENT                                   */}
        {/* ============================================================== */}
        {activeSection === 'create' && (
          <form onSubmit={handleCreateTournament} className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 space-y-3 shadow-lg">
              <div className="flex items-center justify-between pb-2 border-b border-[#23233A]">
                <div className="flex items-center gap-2">
                  <div className="w-7 h-7 rounded-lg bg-[#C8FF3D]/20 flex items-center justify-center text-[#C8FF3D]">
                    <PlusCircle className="w-4 h-4" />
                  </div>
                  <div>
                    <h2 className="text-xs font-black text-white">Create New Tournament</h2>
                    <span className="text-[10px] text-[#C8FF3D] font-bold">100% Free Entry • Sponsor Funded</span>
                  </div>
                </div>
              </div>

              {/* Tournament Title */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Tournament Title *
                </label>
                <input
                  type="text"
                  required
                  placeholder="e.g. CYBERFRAY 2077 GRAND FINALS"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Sponsor Name */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Sponsor Name *
                </label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Asus ROG Gaming / Red Bull"
                  value={sponsor}
                  onChange={(e) => setSponsor(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Mode & Map */}
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="text-[11px] font-bold text-[#9498AE] block mb-1">Mode</label>
                  <select
                    value={mode}
                    onChange={(e) => setMode(e.target.value as GameMode)}
                    className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                  >
                    <option value="Solo">Solo</option>
                    <option value="Duo">Duo</option>
                    <option value="Squad">Squad</option>
                  </select>
                </div>

                <div>
                  <label className="text-[11px] font-bold text-[#9498AE] block mb-1">Map</label>
                  <select
                    value={map}
                    onChange={(e) => setMap(e.target.value as MapType)}
                    className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                  >
                    <option value="Bermuda">Bermuda</option>
                    <option value="Purgatory">Purgatory</option>
                    <option value="Kalahari">Kalahari</option>
                    <option value="Alpine">Alpine</option>
                  </select>
                </div>
              </div>

              {/* Kick-off Time */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1 flex items-center gap-1.5">
                  <Clock className="w-3.5 h-3.5 text-[#C8FF3D]" /> Kick-Off Date & Time *
                </label>
                <input
                  type="datetime-local"
                  required
                  value={startTime}
                  onChange={(e) => setStartTime(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Sponsor Prize & Slots */}
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                    Sponsor Prize (₹)
                  </label>
                  <input
                    type="number"
                    min="1000"
                    step="500"
                    required
                    value={sponsorPrize}
                    onChange={(e) => setSponsorPrize(e.target.value)}
                    className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                  />
                  <span className="text-[9px] text-[#C8FF3D] mt-0.5 block">Sponsor-funded pool</span>
                </div>

                <div>
                  <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                    Max Slots
                  </label>
                  <input
                    type="number"
                    min="12"
                    max="100"
                    required
                    value={slots}
                    onChange={(e) => setSlots(e.target.value)}
                    className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                  />
                  <span className="text-[9px] text-[#9498AE] mt-0.5 block">Free Entry for all</span>
                </div>
              </div>

              {/* Submit CTA */}
              <button
                type="submit"
                className="w-full py-3 mt-2 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-black text-xs rounded-xl shadow-lg transition-transform active:scale-95 flex items-center justify-center gap-2"
              >
                <PlusCircle className="w-4 h-4" />
                <span>Create & Publish Tournament</span>
              </button>
            </div>
          </form>
        )}

        {/* ============================================================== */}
        {/* SECTION 2: SET ROOM ID AND PASSWORD                            */}
        {/* ============================================================== */}
        {activeSection === 'rooms' && (
          <form onSubmit={handleSaveRoomCredentials} className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 space-y-3 shadow-lg">
              <div className="flex items-center justify-between pb-2 border-b border-[#23233A]">
                <div className="flex items-center gap-2">
                  <div className="w-7 h-7 rounded-lg bg-[#7B5CFF]/20 flex items-center justify-center text-[#7B5CFF]">
                    <Key className="w-4 h-4" />
                  </div>
                  <div>
                    <h2 className="text-xs font-black text-white">Set Room ID & Password</h2>
                    <span className="text-[10px] text-[#9498AE]">Manage in-game custom room credentials</span>
                  </div>
                </div>
              </div>

              {/* Tournament Selector */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Select Upcoming Tournament
                </label>
                <select
                  value={selectedRoomMatchId}
                  onChange={(e) => handleSelectRoomMatch(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title} ({m.mode} • {m.status})
                    </option>
                  ))}
                </select>
              </div>

              {currentRoomMatch && (
                <div className="p-2.5 bg-[#0A0A0F] border border-[#23233A] rounded-xl text-xs space-y-1">
                  <div className="flex justify-between text-[#9498AE] text-[10px]">
                    <span>Current Status:</span>
                    <span className={`font-bold ${currentRoomMatch.isRoomRevealed ? 'text-[#C8FF3D]' : 'text-[#FFB300]'}`}>
                      {currentRoomMatch.isRoomRevealed ? 'Revealed to Joined Players' : 'Hidden from Players'}
                    </span>
                  </div>
                  {currentRoomMatch.roomId && (
                    <div className="flex justify-between text-white font-mono text-[11px]">
                      <span>Saved ID: <strong className="text-[#C8FF3D]">{currentRoomMatch.roomId}</strong></span>
                      <span>Pass: <strong className="text-[#C8FF3D]">{currentRoomMatch.roomPassword}</strong></span>
                    </div>
                  )}
                </div>
              )}

              {/* Room ID */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Free Fire Custom Room ID *
                </label>
                <input
                  type="text"
                  required
                  placeholder="e.g. 8849102"
                  value={roomIdInput}
                  onChange={(e) => setRoomIdInput(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D] font-mono"
                />
              </div>

              {/* Room Password */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Room Password *
                </label>
                <input
                  type="text"
                  required
                  placeholder="e.g. ffpro99"
                  value={roomPasswordInput}
                  onChange={(e) => setRoomPasswordInput(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D] font-mono"
                />
              </div>

              {/* Auto Reveal Timer */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Auto-reveal to joined players (minutes prior to match)
                </label>
                <input
                  type="number"
                  min="5"
                  max="60"
                  value={autoRevealMins}
                  onChange={(e) => setAutoRevealMins(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Immediate reveal toggle */}
              <label className="flex items-center gap-2 p-2 bg-[#0A0A0F] rounded-xl border border-[#23233A] cursor-pointer">
                <input
                  type="checkbox"
                  checked={revealImmediately}
                  onChange={(e) => setRevealImmediately(e.target.checked)}
                  className="rounded text-[#C8FF3D] focus:ring-0"
                />
                <span className="text-[11px] text-white font-medium">
                  Reveal to players right now (instant push notification)
                </span>
              </label>

              {/* Save CTA */}
              <button
                type="submit"
                className="w-full py-3 bg-[#7B5CFF] hover:bg-[#6849e6] text-white font-black text-xs rounded-xl shadow-lg transition-transform active:scale-95 flex items-center justify-center gap-2"
              >
                <Key className="w-4 h-4" />
                <span>Save & Broadcast Room Credentials</span>
              </button>
            </div>
          </form>
        )}

        {/* ============================================================== */}
        {/* SECTION 3: VIEW REGISTRATIONS                                  */}
        {/* ============================================================== */}
        {activeSection === 'registrations' && (
          <div className="space-y-3">
            <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 space-y-3 shadow-lg">
              <div className="flex items-center justify-between pb-2 border-b border-[#23233A]">
                <div className="flex items-center gap-2">
                  <div className="w-7 h-7 rounded-lg bg-[#00E5FF]/20 flex items-center justify-center text-[#00E5FF]">
                    <Users className="w-4 h-4" />
                  </div>
                  <div>
                    <h2 className="text-xs font-black text-white">View Registrations</h2>
                    <span className="text-[10px] text-[#9498AE]">Confirmed players & team rosters</span>
                  </div>
                </div>

                <button
                  onClick={() => setShowAddRegModal(true)}
                  className="flex items-center gap-1 px-2.5 py-1 bg-[#C8FF3D] text-black font-extrabold text-[10px] rounded-lg shadow"
                >
                  <UserPlus className="w-3 h-3" />
                  <span>+ Add Player</span>
                </button>
              </div>

              {/* Match Filter */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Filter by Tournament
                </label>
                <select
                  value={selectedRegMatchId}
                  onChange={(e) => setSelectedRegMatchId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  <option value="">All Tournaments ({registrations.length})</option>
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title}
                    </option>
                  ))}
                </select>
              </div>

              {/* Roster Cards */}
              <div className="space-y-2 pt-1">
                {filteredRegistrations.length === 0 ? (
                  <div className="text-center py-6 text-xs text-[#9498AE]">
                    No registrations found for this match yet.
                  </div>
                ) : (
                  filteredRegistrations.map((reg) => (
                    <div
                      key={reg.id}
                      className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-2xl flex items-center justify-between"
                    >
                      <div>
                        <div className="flex items-center gap-2">
                          <span className="text-xs font-black text-white">{reg.inGameName}</span>
                          <span className="px-1.5 py-0.5 bg-[#C8FF3D]/20 text-[#C8FF3D] text-[9px] font-bold rounded">
                            {reg.status}
                          </span>
                        </div>
                        <div className="text-[10px] text-[#9498AE] font-mono mt-0.5">
                          UID: {reg.ffUid} • {reg.registeredAt}
                        </div>
                        {reg.teamMembers && reg.teamMembers.length > 0 && (
                          <div className="text-[9px] text-slate-400 mt-1">
                            Teammates: {reg.teamMembers.join(', ')}
                          </div>
                        )}
                      </div>

                      <div className="text-right">
                        <span className="text-[10px] text-[#C8FF3D] font-bold block">Free Entry</span>
                        <span className="text-[9px] text-[#9498AE]">Slot Verified</span>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* Quick Add Player Modal */}
            {showAddRegModal && (
              <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm p-4">
                <div className="w-full max-w-sm bg-[#14141F] border border-[#23233A] rounded-[24px] p-5 shadow-2xl space-y-3">
                  <h3 className="text-xs font-black text-white">Manual Player Registration</h3>
                  <form onSubmit={handleAddRegistration} className="space-y-2.5">
                    <div>
                      <label className="text-[10px] text-[#9498AE] block mb-1">In-Game Name (IGN)</label>
                      <input
                        type="text"
                        required
                        placeholder="e.g. ⚡VIPER_STRIKE⚡"
                        value={newRegIgn}
                        onChange={(e) => setNewRegIgn(e.target.value)}
                        className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white"
                      />
                    </div>
                    <div>
                      <label className="text-[10px] text-[#9498AE] block mb-1">Free Fire Numeric UID</label>
                      <input
                        type="text"
                        required
                        placeholder="e.g. 98412033"
                        value={newRegUid}
                        onChange={(e) => setNewRegUid(e.target.value)}
                        className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white"
                      />
                    </div>
                    <div className="flex gap-2 pt-2">
                      <button
                        type="button"
                        onClick={() => setShowAddRegModal(false)}
                        className="flex-1 py-2 bg-[#1C1C2E] text-[#9498AE] rounded-xl text-xs font-bold"
                      >
                        Cancel
                      </button>
                      <button
                        type="submit"
                        className="flex-1 py-2 bg-[#C8FF3D] text-black font-black rounded-xl text-xs shadow"
                      >
                        Confirm Slot
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </div>
        )}

        {/* ============================================================== */}
        {/* SECTION 4: PUBLISH RESULTS                                     */}
        {/* ============================================================== */}
        {activeSection === 'results' && (
          <form onSubmit={handlePublishResults} className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 space-y-3 shadow-lg">
              <div className="flex items-center justify-between pb-2 border-b border-[#23233A]">
                <div className="flex items-center gap-2">
                  <div className="w-7 h-7 rounded-lg bg-[#FFB300]/20 flex items-center justify-center text-[#FFB300]">
                    <Trophy className="w-4 h-4" />
                  </div>
                  <div>
                    <h2 className="text-xs font-black text-white">Publish Match Results</h2>
                    <span className="text-[10px] text-[#9498AE]">Record official placements, kills & sponsor prizes</span>
                  </div>
                </div>
              </div>

              {/* Tournament Selector */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Select Completed / Live Match
                </label>
                <select
                  value={selectedResultMatchId}
                  onChange={(e) => setSelectedResultMatchId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title} ({m.status})
                    </option>
                  ))}
                </select>
              </div>

              {/* 1st Place */}
              <div className="p-3 bg-[#0A0A0F] border border-[#FFB300]/40 rounded-2xl space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-[11px] font-black text-[#FFB300] flex items-center gap-1">
                    🥇 1st Place (Booyah Champion)
                  </span>
                  <span className="text-[9px] text-[#9498AE]">Rank #1</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    placeholder="Player / Team IGN"
                    value={firstIgn}
                    onChange={(e) => setFirstIgn(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                  <input
                    type="number"
                    placeholder="Kills"
                    value={firstKills}
                    onChange={(e) => setFirstKills(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                </div>
                <input
                  type="text"
                  placeholder="Sponsor Prize Record (e.g. ₹5,000 Trophy)"
                  value={firstPrize}
                  onChange={(e) => setFirstPrize(e.target.value)}
                  className="w-full bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-[#C8FF3D]"
                />
              </div>

              {/* 2nd Place */}
              <div className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-2xl space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-[11px] font-black text-slate-300 flex items-center gap-1">
                    🥈 2nd Place Runner-Up
                  </span>
                  <span className="text-[9px] text-[#9498AE]">Rank #2</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    placeholder="Player / Team IGN"
                    value={secondIgn}
                    onChange={(e) => setSecondIgn(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                  <input
                    type="number"
                    placeholder="Kills"
                    value={secondKills}
                    onChange={(e) => setSecondKills(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                </div>
                <input
                  type="text"
                  placeholder="Sponsor Prize Record (e.g. ₹3,000 Trophy)"
                  value={secondPrize}
                  onChange={(e) => setSecondPrize(e.target.value)}
                  className="w-full bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-[#C8FF3D]"
                />
              </div>

              {/* 3rd Place */}
              <div className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-2xl space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-[11px] font-black text-amber-600 flex items-center gap-1">
                    🥉 3rd Place
                  </span>
                  <span className="text-[9px] text-[#9498AE]">Rank #3</span>
                </div>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    placeholder="Player / Team IGN"
                    value={thirdIgn}
                    onChange={(e) => setThirdIgn(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                  <input
                    type="number"
                    placeholder="Kills"
                    value={thirdKills}
                    onChange={(e) => setThirdKills(e.target.value)}
                    className="bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-white"
                  />
                </div>
                <input
                  type="text"
                  placeholder="Sponsor Prize Record (e.g. ₹2,000 Trophy)"
                  value={thirdPrize}
                  onChange={(e) => setThirdPrize(e.target.value)}
                  className="w-full bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1.5 text-xs text-[#C8FF3D]"
                />
              </div>

              {/* Publish Button */}
              <button
                type="submit"
                className="w-full py-3 bg-[#FFB300] hover:bg-[#e6a100] text-black font-black text-xs rounded-xl shadow-lg transition-transform active:scale-95 flex items-center justify-center gap-2"
              >
                <Trophy className="w-4 h-4" />
                <span>Publish Official Scorecard & Finish Match</span>
              </button>
            </div>
          </form>
        )}

        {/* ============================================================== */}
        {/* SECTION 5: SEND ANNOUNCEMENT                                   */}
        {/* ============================================================== */}
        {activeSection === 'announcements' && (
          <form onSubmit={handleSendAnnouncement} className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] rounded-[24px] p-4 space-y-3 shadow-lg">
              <div className="flex items-center justify-between pb-2 border-b border-[#23233A]">
                <div className="flex items-center gap-2">
                  <div className="w-7 h-7 rounded-lg bg-[#FF6A00]/20 flex items-center justify-center text-[#FF6A00]">
                    <Megaphone className="w-4 h-4" />
                  </div>
                  <div>
                    <h2 className="text-xs font-black text-white">Send Push Announcement</h2>
                    <span className="text-[10px] text-[#9498AE]">Broadcast news, room alerts, or rules update</span>
                  </div>
                </div>
              </div>

              {/* Title */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Announcement Title *
                </label>
                <input
                  type="text"
                  required
                  placeholder="e.g. Room ID Unlocked / Match Postponed"
                  value={notifTitle}
                  onChange={(e) => setNotifTitle(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Notification Category */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Category Tag
                </label>
                <select
                  value={notifType}
                  onChange={(e) => setNotifType(e.target.value as any)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  <option value="system">System Notice</option>
                  <option value="room">Room Credential Alert</option>
                  <option value="match">Match Event</option>
                  <option value="reward">Sponsor Prize Announcement</option>
                </select>
              </div>

              {/* Message */}
              <div>
                <label className="text-[11px] font-bold text-[#9498AE] block mb-1">
                  Message Body *
                </label>
                <textarea
                  rows={4}
                  required
                  placeholder="Type your message to all players..."
                  value={notifMessage}
                  onChange={(e) => setNotifMessage(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#23233A] rounded-xl px-3 py-2 text-xs text-white placeholder-slate-600 focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              {/* Send CTA */}
              <button
                type="submit"
                className="w-full py-3 bg-[#FF6A00] hover:bg-[#e55f00] text-white font-black text-xs rounded-xl shadow-lg transition-transform active:scale-95 flex items-center justify-center gap-2"
              >
                <Send className="w-4 h-4" />
                <span>Broadcast Announcement Now</span>
              </button>
            </div>
          </form>
        )}
      </div>

      {/* Admin Guide Modal */}
      {showAdminGuide && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-[28px] p-5 shadow-2xl space-y-4 max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between border-b border-[#23233A] pb-3">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D]">
                  <HelpCircle className="w-4 h-4" />
                </div>
                <div>
                  <h3 className="text-sm font-black text-white">Organizer Console Admin Guide</h3>
                  <p className="text-[10px] text-[#9498AE]">Quick start guide for tournament directors</p>
                </div>
              </div>
              <button
                onClick={() => setShowAdminGuide(false)}
                className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            <div className="space-y-3 text-xs">
              <div className="p-3 bg-[#1C1C2E] border border-[#23233A] rounded-2xl space-y-1">
                <div className="flex items-center gap-2 text-white font-bold">
                  <Trophy className="w-4 h-4 text-[#C8FF3D]" />
                  <span>1. Tournaments & Create</span>
                </div>
                <p className="text-[11px] text-[#9498AE] pl-6 leading-relaxed">
                  Create and configure free-entry sponsor cups. Specify prize pools, battle royale or clash squad modes, maps (Bermuda, Purgatory, etc.), start times, and slot limits.
                </p>
              </div>

              <div className="p-3 bg-[#1C1C2E] border border-[#23233A] rounded-2xl space-y-1">
                <div className="flex items-center gap-2 text-white font-bold">
                  <Key className="w-4 h-4 text-[#00E5FF]" />
                  <span>2. Room Manager (Room ID & Password)</span>
                </div>
                <p className="text-[11px] text-[#9498AE] pl-6 leading-relaxed">
                  Set the custom Free Fire room ID and password for matches. Configure auto-reveal timers (e.g. 15 minutes before kick-off) so registered players receive credentials securely.
                </p>
              </div>

              <div className="p-3 bg-[#1C1C2E] border border-[#23233A] rounded-2xl space-y-1">
                <div className="flex items-center gap-2 text-white font-bold">
                  <Award className="w-4 h-4 text-[#FFB800]" />
                  <span>3. Publish Results</span>
                </div>
                <p className="text-[11px] text-[#9498AE] pl-6 leading-relaxed">
                  Record Booyah podium standings (1st, 2nd, 3rd place), elimination counts, and sponsor prize awards. This updates player leaderboards and prize history logs.
                </p>
              </div>

              <div className="p-3 bg-[#1C1C2E] border border-[#23233A] rounded-2xl space-y-1">
                <div className="flex items-center gap-2 text-white font-bold">
                  <Users className="w-4 h-4 text-[#7B5CFF]" />
                  <span>4. Registrations & Announcements</span>
                </div>
                <p className="text-[11px] text-[#9498AE] pl-6 leading-relaxed">
                  Inspect active participant rosters, review UIDs, and broadcast push announcements directly to player inboxes.
                </p>
              </div>
            </div>

            <button
              onClick={() => setShowAdminGuide(false)}
              className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow transition-all"
            >
              Got it, Let's Manage
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
