import React, { useState } from 'react';
import { PlayerMatch, GameMode, MapType, TournamentRegistration } from '../types';
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
  Clock,
  Sparkles,
  Shield,
  Layers,
  Send,
  RefreshCw,
  HelpCircle,
  X,
  Award,
  LifeBuoy,
  Flag,
  UserCheck,
  History,
  Search,
  Filter,
  Check,
  RotateCcw,
  FileSpreadsheet,
  Info
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
    | 'dashboard'
    | 'tournaments'
    | 'rooms'
    | 'registrations'
    | 'results'
    | 'payouts'
    | 'support'
    | 'reports'
    | 'users'
    | 'audit'
    | 'announcements'
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
    }, 600);
  };

  // --- 1. Tournaments Form State ---
  const [title, setTitle] = useState('');
  const [sponsor, setSponsor] = useState('');
  const [mode, setMode] = useState<GameMode>('Squad');
  const [map, setMap] = useState<MapType>('Bermuda');
  const [selectedDate, setSelectedDate] = useState('Today');
  const [selectedHour, setSelectedHour] = useState('09');
  const [selectedMin, setSelectedMin] = useState('00');
  const [selectedAmPm, setSelectedAmPm] = useState('PM');
  const [prizePool, setPrizePool] = useState('10000');
  const [maxSlots, setMaxSlots] = useState('48');
  const [minLevel, setMinLevel] = useState(30);
  const [perKillPoints, setPerKillPoints] = useState(2);
  const [rulesText, setRulesText] = useState(
    'Mobile device only. Emulators strictly prohibited. No hacks, teaming, or third-party scripts. Minimum Free Fire Level 30 required.'
  );

  // --- 2. Room Credentials State ---
  const [selectedRoomMatchId, setSelectedRoomMatchId] = useState<string>(matches[0]?.id || 'm1');
  const [roomId, setRoomId] = useState('');
  const [roomPassword, setRoomPassword] = useState('');

  // --- 3. Registrations State ---
  const [registrations, setRegistrations] = useState<TournamentRegistration[]>(() =>
    TournamentService.getPlayerRegistrations()
  );
  const [regTournamentId, setRegTournamentId] = useState<string>(matches[0]?.id || 'm1');
  const [manualIgn, setManualIgn] = useState('');
  const [manualUid, setManualUid] = useState('');

  // --- 4. Results State ---
  const [resultMatchId, setResultMatchId] = useState<string>(matches[0]?.id || 'm1');
  const [firstIgn, setFirstIgn] = useState('SOUL_IMMORTAL');
  const [firstKills, setFirstKills] = useState('12');
  const [firstPrize, setFirstPrize] = useState('Trophy + Kit (Sponsor value ₹5,000)');
  const [secondIgn, setSecondIgn] = useState('NEON_SOUL');
  const [secondKills, setSecondKills] = useState('9');
  const [secondPrize, setSecondPrize] = useState('Pro Gaming Headset (Sponsor value ₹3,000)');
  const [thirdIgn, setThirdIgn] = useState('VIPER_STRIKE');
  const [thirdKills, setThirdKills] = useState('6');
  const [thirdPrize, setThirdPrize] = useState('Esports Jersey (Sponsor value ₹1,500)');
  const [showPreviewModal, setShowPreviewModal] = useState(false);

  // --- 5. Prize Tracker State ---
  const [prizeList, setPrizeList] = useState([
    {
      id: 'p1',
      title: 'Midnight Blitz Champions League',
      rank: '2nd Place',
      sponsor: 'OnePlus Gaming',
      item: 'Pro Gaming Headset (Sponsor value ₹3,000)',
      status: 'Delivered by sponsor',
      winner: 'NEON_SOUL',
    },
    {
      id: 'p2',
      title: 'Kalahari Survival Clash Weekend',
      rank: '1st Place (Booyah)',
      sponsor: 'Cosmic Byte',
      item: 'Mechanical RGB Keyboard + Mouse (Sponsor value ₹5,000)',
      status: 'Sponsor notified',
      winner: 'SOUL_IMMORTAL',
    },
    {
      id: 'p3',
      title: 'CYBERFRAY 2077 GRAND FINALS',
      rank: '3rd Place',
      sponsor: 'Asus ROG Gaming',
      item: 'Official Esports Jersey + Cap (Sponsor value ₹2,000)',
      status: 'Recorded',
      winner: 'VIPER_STRIKE',
    },
  ]);

  // --- 6. Support Tickets State ---
  const [supportTickets, setSupportTickets] = useState([
    {
      id: 't1',
      user: 'NEON_SOUL',
      subject: 'Room credentials auto-reveal query',
      message: 'Room credentials auto-revealed on time, thank you!',
      status: 'Closed',
      reply: 'Auto-revealed 15m prior to start as scheduled.',
    },
    {
      id: 't2',
      user: 'VIPER_STRIKE',
      subject: 'Prize tracking details',
      message: 'When will the sponsor ship the mechanical keyboard?',
      status: 'Open',
      reply: '',
    },
  ]);
  const [replyInputs, setReplyInputs] = useState<{ [id: string]: string }>({});

  // --- 7. Reports & Disputes State ---
  const [disputeReports, setDisputeReports] = useState([
    {
      id: 'rep_1',
      tournament: 'CYBERFRAY 2077 GRAND FINALS',
      reporter: 'NEON_SOUL',
      accused: 'ROGUE_CHEATER',
      reason: 'Suspected emulator / script usage in Round 2',
      details: 'Player was executing instant 360 snipes through smoke cover.',
      status: 'Pending',
    },
    {
      id: 'rep_2',
      tournament: 'Midnight Blitz Champions League',
      reporter: 'VIPER_STRIKE',
      accused: 'SHADOW_KILL',
      reason: 'Unfair cross-team collusion near Clock Tower',
      details: 'Match telemetry reviewed by referee. Warning issued.',
      status: 'Resolved',
    },
  ]);

  // --- 8. Users Moderation State ---
  const [userList, setUserList] = useState([
    { id: 'u1', ign: 'NEON_SOUL', uid: '994821034', status: 'Active', warnings: 0 },
    { id: 'u2', ign: 'SOUL_IMMORTAL', uid: '849201123', status: 'Active', warnings: 0 },
    { id: 'u3', ign: 'VIPER_STRIKE', uid: '771920441', status: 'Warned', warnings: 1 },
    { id: 'u4', ign: 'ROGUE_CHEATER', uid: '119203948', status: 'Banned', warnings: 3 },
  ]);
  const [userSearch, setUserSearch] = useState('');
  const [userFilter, setUserFilter] = useState('All');

  // --- 9. Audit Log State ---
  const [auditLogs, setAuditLogs] = useState([
    { id: 'a1', action: 'Published results for CyberFray 2077', admin: 'admin@demo.com', time: 'Today, 08:35 PM' },
    { id: 'a2', action: 'Updated room credentials for Kalahari Duo', admin: 'organizer@demo.com', time: 'Today, 06:10 PM' },
    { id: 'a3', action: 'Resolved dispute report #rep_2', admin: 'admin@demo.com', time: 'Yesterday, 11:45 PM' },
  ]);

  // --- 10. Announcements State ---
  const [announceTitle, setAnnounceTitle] = useState('');
  const [announceMsg, setAnnounceMsg] = useState('');
  const [announceAudience, setAnnounceAudience] = useState('All Players (Broadcast)');
  const [announcementsList, setAnnouncementsList] = useState([
    { id: 'ann_1', title: 'Grand Finals Kick-off Reminder', audience: 'All Players', time: '1 hour ago' },
  ]);

  // Confirmation Modals State
  const [banConfirmUser, setBanConfirmUser] = useState<any>(null);
  const [cancelConfirmMatch, setCancelConfirmMatch] = useState<PlayerMatch | null>(null);
  const [removeConfirmReg, setRemoveConfirmReg] = useState<TournamentRegistration | null>(null);

  // Tabs List (All 11 Tabs)
  const tabs = [
    { id: 'dashboard', label: 'Dashboard', icon: Layers },
    { id: 'tournaments', label: 'Tournaments', icon: PlusCircle },
    { id: 'rooms', label: 'Room Manager', icon: Key },
    { id: 'registrations', label: 'Registrations', icon: Users },
    { id: 'results', label: 'Results', icon: Trophy },
    { id: 'payouts', label: 'Prize Tracker', icon: Award },
    { id: 'support', label: 'Support', icon: LifeBuoy },
    { id: 'reports', label: 'Reports', icon: Flag },
    { id: 'users', label: 'Users', icon: UserCheck },
    { id: 'audit', label: 'Audit Log', icon: History },
    { id: 'announcements', label: 'Announcements', icon: Megaphone },
  ] as const;

  return (
    <div className="fixed inset-0 z-50 bg-[#0A0A0F] text-[#F5F5FA] flex flex-col overflow-hidden animate-fade-in font-sora">
      {/* Top Header */}
      <div className="bg-[#14141F] border-b border-[#23233A] px-4 py-3 flex items-center justify-between shrink-0 shadow-lg">
        <div className="flex items-center gap-3">
          <button
            onClick={onClose}
            className="w-9 h-9 rounded-xl bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] flex items-center justify-center text-white transition-all active:scale-95"
            aria-label="Back to App"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-sm font-black text-white uppercase tracking-wider flex items-center gap-1.5">
                <Shield className="w-4 h-4 text-[#C8FF3D]" /> Organizer Console
              </h1>
              <span className="px-2 py-0.5 bg-[#C8FF3D] text-black text-xs font-black rounded-full uppercase tracking-wider">
                DEMO
              </span>
            </div>
            <span className="text-xs text-[#9498AE]">100% Free Entry • Sponsor Funded Platform</span>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => setShowAdminGuide(true)}
            className="w-9 h-9 rounded-xl bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] flex items-center justify-center text-[#C8FF3D] transition-all"
            title="Organizer Guide"
          >
            <HelpCircle className="w-4 h-4" />
          </button>

          <button
            onClick={handleSyncNow}
            disabled={isSyncing}
            className="flex items-center gap-1.5 px-3 py-1.5 bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] rounded-xl text-xs font-bold text-white transition-all disabled:opacity-50"
          >
            <RefreshCw className={`w-3.5 h-3.5 text-[#C8FF3D] ${isSyncing ? 'animate-spin' : ''}`} />
            <span>Sync Now</span>
          </button>

          <button
            onClick={onClose}
            className="px-3 py-1.5 bg-[#C8FF3D] text-black font-extrabold text-xs rounded-xl shadow-[0_0_12px_rgba(200,255,61,0.25)] hover:bg-[#b5f02c] transition-all"
          >
            Exit
          </button>
        </div>
      </div>

      {/* Horizontal Scrolling Tabs (All 11 Tabs with right padding) */}
      <div className="bg-[#0E0E17] border-b border-[#23233A] px-3 py-2 flex items-center gap-1.5 overflow-x-auto no-scrollbar shrink-0 pr-8">
        {tabs.map((tab) => {
          const Icon = tab.icon;
          const isActive = activeSection === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => setActiveSection(tab.id as any)}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-xl text-xs font-bold whitespace-nowrap transition-all shrink-0 ${
                isActive
                  ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.3)] font-black'
                  : 'bg-[#14141F] text-[#9498AE] hover:text-white border border-[#23233A]'
              }`}
            >
              <Icon className="w-3.5 h-3.5" />
              <span>{tab.label}</span>
            </button>
          );
        })}
      </div>

      {/* Section Content Area (Scrollable with pb-28) */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4 pb-28">
        {/* 1. DASHBOARD */}
        {activeSection === 'dashboard' && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl">
                <span className="text-xs text-[#9498AE] block">Active Tournaments</span>
                <span className="text-2xl font-black text-white mt-1 block">
                  {matches.filter((m) => m.status !== 'Completed').length}
                </span>
                <span className="text-xs text-[#C8FF3D] font-bold mt-1 block">100% Free Entry</span>
              </div>
              <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl">
                <span className="text-xs text-[#9498AE] block">Registered Contestants</span>
                <span className="text-2xl font-black text-white mt-1 block">{registrations.length}</span>
                <span className="text-xs text-[#7B5CFF] font-bold mt-1 block">Verified UIDs</span>
              </div>
              <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl">
                <span className="text-xs text-[#9498AE] block">Prizes Recorded</span>
                <span className="text-2xl font-black text-[#FFB800] mt-1 block">{prizeList.length}</span>
                <span className="text-xs text-[#FFB800] font-bold mt-1 block">Sponsor-Funded</span>
              </div>
              <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl">
                <span className="text-xs text-[#9498AE] block">Open Disputes</span>
                <span className="text-2xl font-black text-[#FF4D6D] mt-1 block">
                  {disputeReports.filter((r) => r.status === 'Pending').length}
                </span>
                <span className="text-xs text-[#FF4D6D] font-bold mt-1 block">Telemetry Radar</span>
              </div>
            </div>

            {/* Production Architecture Notice Card */}
            <div className="bg-[#14141F] border border-[#23233A] rounded-2xl p-4 flex items-start gap-3">
              <Info className="w-5 h-5 text-[#C8FF3D] shrink-0 mt-0.5" />
              <div>
                <h4 className="text-xs font-black text-white uppercase tracking-wider">
                  Production Cloud Architecture Notice
                </h4>
                <p className="text-xs text-[#9498AE] mt-1 leading-relaxed">
                  In production, organizer operations are conducted via a separate hosted web panel with Firebase Auth
                  Custom Claims (role: organizer / admin). Results publishing and sponsor prize delivery writes are
                  executed securely via Cloud Functions with zero-trust validation.
                </p>
              </div>
            </div>

            {/* Quick Actions */}
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider">Quick Actions</h3>
              <div className="grid grid-cols-2 gap-2.5">
                <button
                  onClick={() => setActiveSection('tournaments')}
                  className="p-3 bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] rounded-xl text-left transition-all"
                >
                  <PlusCircle className="w-4 h-4 text-[#C8FF3D] mb-1.5" />
                  <span className="text-xs font-bold text-white block">Create Tournament</span>
                  <span className="text-xs text-[#9498AE]">Set mode, map, schedule</span>
                </button>
                <button
                  onClick={() => setActiveSection('rooms')}
                  className="p-3 bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] rounded-xl text-left transition-all"
                >
                  <Key className="w-4 h-4 text-[#FFB800] mb-1.5" />
                  <span className="text-xs font-bold text-white block">Room Manager</span>
                  <span className="text-xs text-[#9498AE]">Set custom Room ID & Pass</span>
                </button>
              </div>
            </div>
          </div>
        )}

        {/* 2. TOURNAMENTS (CREATE & SCHEDULE) */}
        {activeSection === 'tournaments' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <PlusCircle className="w-4 h-4 text-[#C8FF3D]" /> Schedule Tournament (100% Free Entry)
              </h3>

              <div className="space-y-2.5 text-xs">
                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Tournament Title</label>
                  <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="e.g. CYBERFRAY 2077 GRAND FINALS"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>

                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Sponsor Name</label>
                  <input
                    type="text"
                    value={sponsor}
                    onChange={(e) => setSponsor(e.target.value)}
                    placeholder="e.g. Asus ROG Gaming"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Game Mode</label>
                    <div className="flex gap-1.5">
                      {(['Solo', 'Duo', 'Squad'] as const).map((m) => (
                        <button
                          key={m}
                          type="button"
                          onClick={() => setMode(m)}
                          className={`flex-1 py-1.5 rounded-lg text-xs font-bold border transition-all ${
                            mode === m
                              ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          {m}
                        </button>
                      ))}
                    </div>
                  </div>

                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Map</label>
                    <select
                      value={map}
                      onChange={(e) => setMap(e.target.value as any)}
                      className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                    >
                      <option value="Bermuda">Bermuda</option>
                      <option value="Purgatory">Purgatory</option>
                      <option value="Kalahari">Kalahari</option>
                      <option value="Alpine">Alpine</option>
                    </select>
                  </div>
                </div>

                {/* Real Date Selector */}
                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Kick-Off Date</label>
                  <div className="flex gap-1.5 overflow-x-auto no-scrollbar">
                    {['Today', 'Tomorrow', 'In 2 Days', 'Saturday'].map((d) => (
                      <button
                        key={d}
                        type="button"
                        onClick={() => setSelectedDate(d)}
                        className={`px-3 py-1.5 rounded-lg text-xs font-bold border shrink-0 ${
                          selectedDate === d
                            ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                            : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                        }`}
                      >
                        {d}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Real Time Selector */}
                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Kick-Off Time</label>
                  <div className="flex items-center gap-2">
                    <div className="flex gap-1">
                      {['07', '08', '09', '10', '11'].map((hr) => (
                        <button
                          key={hr}
                          type="button"
                          onClick={() => setSelectedHour(hr)}
                          className={`px-2 py-1 rounded text-xs font-bold border ${
                            selectedHour === hr
                              ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          {hr}
                        </button>
                      ))}
                    </div>
                    <span className="text-white font-bold">:</span>
                    <div className="flex gap-1">
                      {['00', '15', '30', '45'].map((mn) => (
                        <button
                          key={mn}
                          type="button"
                          onClick={() => setSelectedMin(mn)}
                          className={`px-2 py-1 rounded text-xs font-bold border ${
                            selectedMin === mn
                              ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          {mn}
                        </button>
                      ))}
                    </div>
                    <div className="flex gap-1">
                      {['PM', 'AM'].map((ap) => (
                        <button
                          key={ap}
                          type="button"
                          onClick={() => setSelectedAmPm(ap)}
                          className={`px-2 py-1 rounded text-xs font-bold border ${
                            selectedTimeAmPm === ap
                              ? 'bg-[#7B5CFF] text-white border-[#7B5CFF]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          {ap}
                        </button>
                      ))}
                    </div>
                  </div>
                </div>

                {/* Min Level & Per-kill points */}
                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Min Free Fire Level</label>
                    <div className="flex gap-1.5">
                      {[20, 25, 30, 40].map((lvl) => (
                        <button
                          key={lvl}
                          type="button"
                          onClick={() => setMinLevel(lvl)}
                          className={`flex-1 py-1 rounded text-xs font-bold border ${
                            minLevel === lvl
                              ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          {lvl}+
                        </button>
                      ))}
                    </div>
                  </div>

                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Per-Kill Points</label>
                    <div className="flex gap-1.5">
                      {[1, 2, 3].map((pt) => (
                        <button
                          key={pt}
                          type="button"
                          onClick={() => setPerKillPoints(pt)}
                          className={`flex-1 py-1 rounded text-xs font-bold border ${
                            perKillPoints === pt
                              ? 'bg-[#7B5CFF] text-white border-[#7B5CFF]'
                              : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                          }`}
                        >
                          +{pt} pt
                        </button>
                      ))}
                    </div>
                  </div>
                </div>

                {/* Placement Table Display */}
                <div className="bg-[#0A0A0F] p-2.5 rounded-xl border border-[#262640] text-xs space-y-1">
                  <span className="font-bold text-[#C8FF3D] block">Placement Points Table:</span>
                  <span className="text-[#9498AE] block">
                    1st: 12 pts | 2nd: 9 pts | 3rd: 8 pts | 4th: 7 pts | 5th: 6 pts | 6th+: 1 pt
                  </span>
                </div>

                <div className="grid grid-cols-2 gap-2">
                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Sponsor Prize Pool (₹)</label>
                    <input
                      type="number"
                      value={prizePool}
                      onChange={(e) => setPrizePool(e.target.value)}
                      className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                    />
                  </div>
                  <div>
                    <label className="text-xs text-[#9498AE] block mb-1">Max Slots</label>
                    <input
                      type="number"
                      value={maxSlots}
                      onChange={(e) => setMaxSlots(e.target.value)}
                      className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                    />
                  </div>
                </div>

                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Rules & Fair Play Text</label>
                  <textarea
                    rows={2}
                    value={rulesText}
                    onChange={(e) => setRulesText(e.target.value)}
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl p-2.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>

                <button
                  type="button"
                  onClick={() => {
                    if (!title.trim()) {
                      onShowToast('error', 'Please enter tournament title');
                      return;
                    }
                    const newMatch: PlayerMatch = {
                      id: `t_${Date.now()}`,
                      title,
                      sponsorName: sponsor || 'Official Esports Partner',
                      mode,
                      map,
                      prizePool: parseInt(prizePool) || 10000,
                      maxSlots: parseInt(maxSlots) || 48,
                      joinedSlots: 1,
                      startTime: new Date().toISOString(),
                      schedule: `${selectedDate}, ${selectedHour}:${selectedMin} ${selectedTimeAmPm}`,
                      status: 'Upcoming',
                      entryFee: 0,
                      isFreeEntry: true,
                      userJoined: false,
                    };
                    TournamentService.addMatch(newMatch);
                    onRefreshMatches();
                    onShowToast('success', `Created '${title}' (100% Free Entry)!`);
                    setTitle('');
                    setSponsor('');
                  }}
                  className="w-full py-2.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.3)] hover:bg-[#b5f02c] transition-all"
                >
                  Publish Free Tournament
                </button>
              </div>
            </div>

            {/* Existing Tournaments List */}
            <div className="space-y-2">
              <h4 className="text-xs font-black text-white uppercase tracking-wider">Scheduled Tournaments</h4>
              {matches.map((m) => (
                <div
                  key={m.id}
                  className="bg-[#14141F] border border-[#23233A] p-3.5 rounded-2xl flex items-center justify-between"
                >
                  <div>
                    <h5 className="text-xs font-bold text-white">{m.title}</h5>
                    <span className="text-xs text-[#9498AE] block">
                      {m.mode} • {m.map} • {m.schedule}
                    </span>
                    <span className="text-xs text-[#C8FF3D] font-bold block">
                      Sponsor: {m.sponsorName} • ₹{m.prizePool.toLocaleString()} (Sponsor-funded)
                    </span>
                  </div>
                  <button
                    onClick={() => setCancelConfirmMatch(m)}
                    className="px-2.5 py-1 text-xs font-bold text-[#FF4D6D] bg-[#FF4D6D]/15 border border-[#FF4D6D]/30 rounded-lg hover:bg-[#FF4D6D]/25"
                  >
                    Cancel
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 3. ROOM MANAGER */}
        {activeSection === 'rooms' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <Key className="w-4 h-4 text-[#C8FF3D]" /> Free Fire Custom Room Credentials
              </h3>
              <p className="text-xs text-[#9498AE]">
                Enter room ID and password. Credentials automatically reveal 15 minutes before kick-off.
              </p>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Select Tournament</label>
                <select
                  value={selectedRoomMatchId}
                  onChange={(e) => setSelectedRoomMatchId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title} ({m.schedule})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Custom Room ID</label>
                <input
                  type="text"
                  value={roomId}
                  onChange={(e) => setRoomId(e.target.value)}
                  placeholder="e.g. 8849102"
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Room Password</label>
                <input
                  type="text"
                  value={roomPassword}
                  onChange={(e) => setRoomPassword(e.target.value)}
                  placeholder="e.g. ffpro99"
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-2 pt-1">
                <button
                  type="button"
                  onClick={() => {
                    if (!roomId.trim()) {
                      onShowToast('error', 'Please enter Room ID');
                      return;
                    }
                    TournamentService.setRoomCredentials(selectedRoomMatchId, roomId, roomPassword, false);
                    onRefreshMatches();
                    onShowToast('success', 'Credentials registered! Scheduled to auto-reveal 15m prior.');
                    setRoomId('');
                    setRoomPassword('');
                  }}
                  className="py-2.5 bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] text-white font-bold text-xs rounded-xl"
                >
                  Auto-Reveal (15m)
                </button>
                <button
                  type="button"
                  onClick={() => {
                    if (!roomId.trim()) {
                      onShowToast('error', 'Please enter Room ID');
                      return;
                    }
                    TournamentService.setRoomCredentials(selectedRoomMatchId, roomId, roomPassword, true);
                    onRefreshMatches();
                    onShowToast('success', 'Credentials revealed immediately to players!');
                    setRoomId('');
                    setRoomPassword('');
                  }}
                  className="py-2.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
                >
                  Reveal Immediately ⚡
                </button>
              </div>
            </div>
          </div>
        )}

        {/* 4. REGISTRATIONS */}
        {activeSection === 'registrations' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <div className="flex items-center justify-between">
                <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                  <Users className="w-4 h-4 text-[#C8FF3D]" /> Manual Contestant Entry
                </h3>
                <button
                  type="button"
                  onClick={() => {
                    const csv = registrations
                      .map((r) => `${r.inGameName},${r.ffUid},${r.tournamentTitle},${r.status}`)
                      .join('\n');
                    navigator.clipboard.writeText(csv);
                    onShowToast('info', `Exported ${registrations.length} registrations to clipboard (CSV)!`);
                  }}
                  className="flex items-center gap-1.5 px-3 py-1 bg-[#1B1B2B] border border-[#262640] rounded-lg text-xs font-bold text-[#C8FF3D]"
                >
                  <FileSpreadsheet className="w-3.5 h-3.5" />
                  <span>Export CSV</span>
                </button>
              </div>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Select Tournament</label>
                <select
                  value={regTournamentId}
                  onChange={(e) => setRegTournamentId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title}
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Player IGN</label>
                  <input
                    type="text"
                    value={manualIgn}
                    onChange={(e) => setManualIgn(e.target.value)}
                    placeholder="e.g. VIPER_STRIKE"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>
                <div>
                  <label className="text-xs text-[#9498AE] block mb-1">Free Fire UID (8-12 digits)</label>
                  <input
                    type="text"
                    value={manualUid}
                    onChange={(e) => setManualUid(e.target.value)}
                    placeholder="e.g. 771920441"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>
              </div>

              <button
                type="button"
                onClick={() => {
                  if (!manualIgn.trim() || !manualUid.trim()) {
                    onShowToast('error', 'Enter IGN and Free Fire UID');
                    return;
                  }
                  const tMatch = matches.find((m) => m.id === regTournamentId);
                  const newReg: TournamentRegistration = {
                    id: `reg_${Date.now()}`,
                    tournamentId: regTournamentId,
                    tournamentTitle: tMatch?.title || 'Tournament',
                    userId: `u_${Date.now()}`,
                    inGameName: manualIgn,
                    ffUid: manualUid,
                    registeredAt: 'Just now',
                    status: 'Confirmed',
                  };
                  setRegistrations([newReg, ...registrations]);
                  onShowToast('success', `Registered '${manualIgn}'!`);
                  setManualIgn('');
                  setManualUid('');
                }}
                className="w-full py-2 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
              >
                Confirm Registration
              </button>
            </div>

            {/* Registrations List */}
            <div className="space-y-2">
              <h4 className="text-xs font-black text-white uppercase tracking-wider">
                Contestants Roster ({registrations.length})
              </h4>
              {registrations.length === 0 ? (
                <div className="bg-[#14141F] border border-[#23233A] p-6 rounded-2xl text-center">
                  <Users className="w-8 h-8 text-[#9498AE] mx-auto mb-2" />
                  <p className="text-xs text-[#9498AE]">No registrations recorded yet.</p>
                </div>
              ) : (
                registrations.map((reg) => (
                  <div
                    key={reg.id}
                    className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl flex items-center justify-between"
                  >
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-bold text-white">{reg.inGameName}</span>
                        <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] text-xs font-bold rounded-md">
                          {reg.status}
                        </span>
                      </div>
                      <span className="text-xs text-[#9498AE] block">
                        UID: {reg.ffUid} • {reg.tournamentTitle}
                      </span>
                    </div>
                    <div className="flex items-center gap-1.5">
                      {reg.status !== 'Approved' && (
                        <button
                          onClick={() => {
                            setRegistrations(
                              registrations.map((r) => (r.id === reg.id ? { ...r, status: 'Approved' } : r))
                            );
                            onShowToast('success', `Approved ${reg.inGameName}`);
                          }}
                          className="p-1.5 bg-[#C8FF3D]/15 hover:bg-[#C8FF3D]/25 text-[#C8FF3D] rounded-lg"
                          title="Approve"
                        >
                          <Check className="w-4 h-4" />
                        </button>
                      )}
                      <button
                        onClick={() => setRemoveConfirmReg(reg)}
                        className="p-1.5 bg-[#FF4D6D]/15 hover:bg-[#FF4D6D]/25 text-[#FF4D6D] rounded-lg"
                        title="Remove"
                      >
                        <X className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        )}

        {/* 5. RESULTS */}
        {activeSection === 'results' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <Trophy className="w-4 h-4 text-[#C8FF3D]" /> Publish Official Match Results
              </h3>
              <p className="text-xs text-[#9498AE]">
                Submit final Booyah placements (1st to 12th) with verified kills. Prizes are sponsor-fulfilled.
              </p>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Select Tournament</label>
                <select
                  value={resultMatchId}
                  onChange={(e) => setResultMatchId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title}
                    </option>
                  ))}
                </select>
              </div>

              {/* 1st Place */}
              <div className="bg-[#0A0A0F] border border-[#FFB800]/40 p-3 rounded-xl space-y-2">
                <span className="text-xs font-black text-[#FFB800] uppercase block">🥇 1st Place (Champion)</span>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={firstIgn}
                    onChange={(e) => setFirstIgn(e.target.value)}
                    placeholder="1st IGN"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                  <input
                    type="number"
                    value={firstKills}
                    onChange={(e) => setFirstKills(e.target.value)}
                    placeholder="Kills"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>
                <input
                  type="text"
                  value={firstPrize}
                  onChange={(e) => setFirstPrize(e.target.value)}
                  placeholder="Sponsor Prize Label"
                  className="w-full bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              {/* 2nd Place */}
              <div className="bg-[#0A0A0F] border border-slate-600 p-3 rounded-xl space-y-2">
                <span className="text-xs font-black text-slate-300 uppercase block">🥈 2nd Place</span>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={secondIgn}
                    onChange={(e) => setSecondIgn(e.target.value)}
                    placeholder="2nd IGN"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                  <input
                    type="number"
                    value={secondKills}
                    onChange={(e) => setSecondKills(e.target.value)}
                    placeholder="Kills"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>
                <input
                  type="text"
                  value={secondPrize}
                  onChange={(e) => setSecondPrize(e.target.value)}
                  placeholder="Sponsor Prize Label"
                  className="w-full bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              {/* 3rd Place */}
              <div className="bg-[#0A0A0F] border border-amber-800 p-3 rounded-xl space-y-2">
                <span className="text-xs font-black text-amber-600 uppercase block">🥉 3rd Place</span>
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={thirdIgn}
                    onChange={(e) => setThirdIgn(e.target.value)}
                    placeholder="3rd IGN"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                  <input
                    type="number"
                    value={thirdKills}
                    onChange={(e) => setThirdKills(e.target.value)}
                    placeholder="Kills"
                    className="bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                  />
                </div>
                <input
                  type="text"
                  value={thirdPrize}
                  onChange={(e) => setThirdPrize(e.target.value)}
                  placeholder="Sponsor Prize Label"
                  className="w-full bg-[#14141F] border border-[#262640] rounded-lg px-2.5 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-2 pt-1">
                <button
                  type="button"
                  onClick={() => setShowPreviewModal(true)}
                  className="py-2.5 bg-[#1B1B2B] hover:bg-[#262640] border border-[#262640] text-white font-bold text-xs rounded-xl"
                >
                  Preview Standings
                </button>
                <button
                  type="button"
                  onClick={() => {
                    onShowToast('success', 'Published official match standings to contestant app!');
                  }}
                  className="py-2.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
                >
                  Publish to App
                </button>
              </div>
            </div>
          </div>
        )}

        {/* 6. PRIZE TRACKER */}
        {activeSection === 'payouts' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <Award className="w-4 h-4 text-[#C8FF3D]" /> Sponsor Prize Logistics Tracker
              </h3>
              <p className="text-xs text-[#9498AE]">
                Prizes are sponsor-funded and fulfilled outside the app. No cash payouts or UPI.
              </p>
            </div>

            <div className="space-y-3">
              {prizeList.map((prize) => (
                <div key={prize.id} className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <h4 className="text-xs font-bold text-white">{prize.title}</h4>
                      <span className="text-xs text-[#9498AE]">
                        Winner: <strong className="text-white">{prize.winner}</strong> ({prize.rank})
                      </span>
                    </div>
                    <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] text-xs font-bold rounded-md">
                      {prize.status}
                    </span>
                  </div>

                  <span className="text-xs text-[#C8FF3D] font-bold block">
                    {prize.item} • Sponsor: {prize.sponsor}
                  </span>

                  {/* 3-Step Flow */}
                  <div className="flex items-center justify-between text-xs pt-2 border-t border-[#23233A]">
                    <span className="flex items-center gap-1 text-[#C8FF3D] font-bold">
                      <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" /> 1. Recorded
                    </span>
                    <span className="text-slate-500">→</span>
                    <span
                      className={`flex items-center gap-1 font-bold ${
                        prize.status === 'Sponsor notified' || prize.status === 'Delivered by sponsor'
                          ? 'text-[#C8FF3D]'
                          : 'text-[#9498AE]'
                      }`}
                    >
                      {prize.status === 'Sponsor notified' || prize.status === 'Delivered by sponsor' ? (
                        <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
                      ) : (
                        <span className="w-4 h-4 rounded-full border border-[#9498AE] inline-block" />
                      )}
                      2. Notified
                    </span>
                    <span className="text-slate-500">→</span>
                    <span
                      className={`flex items-center gap-1 font-bold ${
                        prize.status === 'Delivered by sponsor' ? 'text-[#C8FF3D]' : 'text-[#9498AE]'
                      }`}
                    >
                      {prize.status === 'Delivered by sponsor' ? (
                        <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
                      ) : (
                        <span className="w-4 h-4 rounded-full border border-[#9498AE] inline-block" />
                      )}
                      3. Delivered
                    </span>
                  </div>

                  {/* Action Button */}
                  {prize.status === 'Recorded' && (
                    <button
                      onClick={() => {
                        setPrizeList(
                          prizeList.map((p) => (p.id === prize.id ? { ...p, status: 'Sponsor notified' } : p))
                        );
                        onShowToast('success', `Notified sponsor ${prize.sponsor}!`);
                      }}
                      className="w-full py-2 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
                    >
                      Mark: Sponsor Notified
                    </button>
                  )}
                  {prize.status === 'Sponsor notified' && (
                    <button
                      onClick={() => {
                        setPrizeList(
                          prizeList.map((p) => (p.id === prize.id ? { ...p, status: 'Delivered by sponsor' } : p))
                        );
                        onShowToast('success', `Prize marked Delivered by Sponsor!`);
                      }}
                      className="w-full py-2 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
                    >
                      Mark: Delivered by Sponsor ✓
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 7. SUPPORT TICKETS */}
        {activeSection === 'support' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <LifeBuoy className="w-4 h-4 text-[#C8FF3D]" /> Help & Dispute Desk
              </h3>
              <p className="text-xs text-[#9498AE]">Referee inquiries and room credential assistance.</p>
            </div>

            <div className="space-y-3">
              {supportTickets.map((ticket) => (
                <div key={ticket.id} className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-white">{ticket.subject}</span>
                    <span
                      className={`px-2 py-0.5 text-xs font-bold rounded-md ${
                        ticket.status === 'Open' ? 'bg-[#FF4D6D]/15 text-[#FF4D6D]' : 'bg-[#C8FF3D]/15 text-[#C8FF3D]'
                      }`}
                    >
                      {ticket.status}
                    </span>
                  </div>
                  <span className="text-xs text-[#C8FF3D] block">From: {ticket.user}</span>
                  <p className="text-xs text-[#9498AE]">{ticket.message}</p>

                  {ticket.reply && (
                    <div className="bg-[#0A0A0F] p-2.5 rounded-xl border border-[#262640] text-xs text-white">
                      <strong className="text-[#C8FF3D]">Organizer Reply:</strong> {ticket.reply}
                    </div>
                  )}

                  {ticket.status === 'Open' && (
                    <div className="flex gap-2 pt-1">
                      <input
                        type="text"
                        value={replyInputs[ticket.id] || ''}
                        onChange={(e) => setReplyInputs({ ...replyInputs, [ticket.id]: e.target.value })}
                        placeholder="Write reply..."
                        className="flex-1 bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-1.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                      />
                      <button
                        onClick={() => {
                          const reply = replyInputs[ticket.id];
                          if (!reply) return;
                          setSupportTickets(
                            supportTickets.map((t) => (t.id === ticket.id ? { ...t, status: 'Closed', reply } : t))
                          );
                          onShowToast('success', 'Replied and closed ticket');
                        }}
                        className="px-3 py-1.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl"
                      >
                        Reply
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 8. REPORTS / DISPUTES */}
        {activeSection === 'reports' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <Flag className="w-4 h-4 text-[#C8FF3D]" /> Telemetry Radar & Match Disputes
              </h3>
              <p className="text-xs text-[#9498AE]">Contestant reports raised during 30-minute protest window.</p>
            </div>

            <div className="space-y-3">
              {disputeReports.map((rep) => (
                <div key={rep.id} className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-bold text-white">Report by: {rep.reporter}</span>
                    <span
                      className={`px-2 py-0.5 text-xs font-bold rounded-md ${
                        rep.status === 'Resolved'
                          ? 'bg-[#C8FF3D]/15 text-[#C8FF3D]'
                          : rep.status === 'Dismissed'
                          ? 'bg-slate-700 text-slate-300'
                          : 'bg-[#FF4D6D]/15 text-[#FF4D6D]'
                      }`}
                    >
                      {rep.status}
                    </span>
                  </div>
                  <span className="text-xs text-[#9498AE] block">
                    Accused: <strong className="text-white">{rep.accused}</strong> • {rep.tournament}
                  </span>
                  <span className="text-xs text-[#FF4D6D] font-bold block">{rep.reason}</span>
                  <p className="text-xs text-[#9498AE]">{rep.details}</p>

                  {rep.status === 'Pending' && (
                    <div className="flex gap-2 pt-1">
                      <button
                        onClick={() => {
                          setDisputeReports(
                            disputeReports.map((r) => (r.id === rep.id ? { ...r, status: 'Resolved' } : r))
                          );
                          onShowToast('success', 'Resolved dispute & issued referee penalty');
                        }}
                        className="flex-1 py-1.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl"
                      >
                        Resolve & Warn
                      </button>
                      <button
                        onClick={() => {
                          setDisputeReports(
                            disputeReports.map((r) => (r.id === rep.id ? { ...r, status: 'Dismissed' } : r))
                          );
                          onShowToast('info', 'Dismissed dispute');
                        }}
                        className="flex-1 py-1.5 bg-[#1B1B2B] text-white font-bold text-xs rounded-xl border border-[#262640]"
                      >
                        Dismiss
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 9. USERS MODERATION */}
        {activeSection === 'users' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <UserCheck className="w-4 h-4 text-[#C8FF3D]" /> Player Accounts & Fair Play Status
              </h3>

              <div className="relative">
                <Search className="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#9498AE]" />
                <input
                  type="text"
                  value={userSearch}
                  onChange={(e) => setUserSearch(e.target.value)}
                  placeholder="Search by IGN or Free Fire UID..."
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <div className="flex gap-1.5">
                {['All', 'Active', 'Warned', 'Banned'].map((f) => (
                  <button
                    key={f}
                    onClick={() => setUserFilter(f)}
                    className={`px-3 py-1 rounded-lg text-xs font-bold border transition-all ${
                      userFilter === f
                        ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                        : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                    }`}
                  >
                    {f}
                  </button>
                ))}
              </div>
            </div>

            <div className="space-y-2">
              {userList
                .filter((u) => {
                  const matchQuery =
                    u.ign.toLowerCase().includes(userSearch.toLowerCase()) || u.uid.includes(userSearch);
                  const matchFilter = userFilter === 'All' || u.status === userFilter;
                  return matchQuery && matchFilter;
                })
                .map((user) => (
                  <div
                    key={user.id}
                    className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl flex items-center justify-between"
                  >
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="text-xs font-bold text-white">{user.ign}</span>
                        <span
                          className={`px-2 py-0.5 text-xs font-bold rounded-md ${
                            user.status === 'Active'
                              ? 'bg-[#C8FF3D]/15 text-[#C8FF3D]'
                              : user.status === 'Warned'
                              ? 'bg-[#FFB800]/15 text-[#FFB800]'
                              : 'bg-[#FF4D6D]/15 text-[#FF4D6D]'
                          }`}
                        >
                          {user.status}
                        </span>
                      </div>
                      <span className="text-xs text-[#9498AE] block">
                        UID: {user.uid} • Warnings: {user.warnings}
                      </span>
                    </div>

                    <div className="flex items-center gap-1.5">
                      {user.status === 'Banned' ? (
                        <button
                          onClick={() => {
                            setUserList(userList.map((u) => (u.id === user.id ? { ...u, status: 'Active' } : u)));
                            onShowToast('success', `Unbanned ${user.ign}`);
                          }}
                          className="px-2.5 py-1 bg-[#C8FF3D] text-black font-black text-xs rounded-lg"
                        >
                          Unban
                        </button>
                      ) : (
                        <>
                          <button
                            onClick={() => {
                              setUserList(
                                userList.map((u) =>
                                  u.id === user.id ? { ...u, status: 'Warned', warnings: u.warnings + 1 } : u
                                )
                              );
                              onShowToast('info', `Issued warning to ${user.ign}`);
                            }}
                            className="px-2.5 py-1 bg-[#FFB800]/15 text-[#FFB800] border border-[#FFB800]/30 font-bold text-xs rounded-lg"
                          >
                            Warn
                          </button>
                          <button
                            onClick={() => setBanConfirmUser(user)}
                            className="px-2.5 py-1 bg-[#FF4D6D]/15 text-[#FF4D6D] border border-[#FF4D6D]/30 font-bold text-xs rounded-lg"
                          >
                            Ban
                          </button>
                        </>
                      )}
                    </div>
                  </div>
                ))}
            </div>
          </div>
        )}

        {/* 10. AUDIT LOG */}
        {activeSection === 'audit' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-2">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <History className="w-4 h-4 text-[#C8FF3D]" /> Organizer Action Audit Trail
              </h3>
              <p className="text-xs text-[#9498AE]">Tamper-evident logs of administrative operations.</p>
            </div>

            <div className="space-y-2">
              {auditLogs.map((log) => (
                <div
                  key={log.id}
                  className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl flex items-center justify-between"
                >
                  <div>
                    <span className="text-xs font-bold text-white block">{log.action}</span>
                    <span className="text-xs text-[#C8FF3D]">By: {log.admin}</span>
                  </div>
                  <span className="text-xs text-[#9498AE]">{log.time}</span>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* 11. ANNOUNCEMENTS */}
        {activeSection === 'announcements' && (
          <div className="space-y-4">
            <div className="bg-[#14141F] border border-[#23233A] p-4 rounded-2xl space-y-3">
              <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-2">
                <Megaphone className="w-4 h-4 text-[#C8FF3D]" /> Send In-App Announcement
              </h3>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Target Audience</label>
                <div className="flex gap-2">
                  {['All Players (Broadcast)', 'Selected Tournament'].map((aud) => (
                    <button
                      key={aud}
                      type="button"
                      onClick={() => setAnnounceAudience(aud)}
                      className={`flex-1 py-1.5 rounded-lg text-xs font-bold border transition-all ${
                        announceAudience === aud
                          ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                          : 'bg-[#0A0A0F] text-[#9498AE] border-[#262640]'
                      }`}
                    >
                      {aud}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Headline</label>
                <input
                  type="text"
                  value={announceTitle}
                  onChange={(e) => setAnnounceTitle(e.target.value)}
                  placeholder="e.g. Server maintenance scheduled"
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <div>
                <label className="text-xs text-[#9498AE] block mb-1">Message Body</label>
                <textarea
                  rows={3}
                  value={announceMsg}
                  onChange={(e) => setAnnounceMsg(e.target.value)}
                  placeholder="Details of the announcement..."
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl p-2.5 text-white text-xs focus:border-[#C8FF3D] outline-none"
                />
              </div>

              <button
                type="button"
                onClick={() => {
                  if (!announceTitle.trim() || !announceMsg.trim()) {
                    onShowToast('error', 'Enter headline and message');
                    return;
                  }
                  setAnnouncementsList([
                    { id: `ann_${Date.now()}`, title: announceTitle, audience: announceAudience, time: 'Just now' },
                    ...announcementsList,
                  ]);
                  onShowToast('success', `Broadcast dispatched to ${announceAudience}!`);
                  setAnnounceTitle('');
                  setAnnounceMsg('');
                }}
                className="w-full py-2.5 bg-[#C8FF3D] text-black font-black text-xs rounded-xl hover:bg-[#b5f02c]"
              >
                Broadcast Announcement 📢
              </button>
            </div>

            <div className="space-y-2">
              <h4 className="text-xs font-black text-white uppercase tracking-wider">Dispatched Announcements</h4>
              {announcementsList.map((ann) => (
                <div
                  key={ann.id}
                  className="bg-[#14141F] border border-[#23233A] p-3 rounded-2xl flex items-center justify-between"
                >
                  <div>
                    <span className="text-xs font-bold text-white block">{ann.title}</span>
                    <span className="text-xs text-[#7B5CFF]">Target: {ann.audience}</span>
                  </div>
                  <span className="text-xs text-[#9498AE]">{ann.time}</span>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>

      {/* Admin Guide Modal */}
      {showAdminGuide && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-2xl p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-black text-white flex items-center gap-2">
                <HelpCircle className="w-4 h-4 text-[#C8FF3D]" /> Organizer Quick Guide
              </h3>
              <button onClick={() => setShowAdminGuide(false)} className="text-[#9498AE] hover:text-white">
                <X className="w-4 h-4" />
              </button>
            </div>
            <p className="text-xs text-[#9498AE] leading-relaxed">
              1. <strong>Tournaments:</strong> Schedule free-entry matches with sponsor prize pools.
              <br />
              2. <strong>Room Manager:</strong> Enter room credentials (auto-reveals 15m prior to kick-off).
              <br />
              3. <strong>Registrations:</strong> Review and approve contestant rosters, export CSV.
              <br />
              4. <strong>Results:</strong> Enter placement kills and publish official rankings.
              <br />
              5. <strong>Prize Tracker:</strong> Update status from Recorded to Delivered by sponsor.
            </p>
            <button
              onClick={() => setShowAdminGuide(false)}
              className="w-full py-2 bg-[#C8FF3D] text-black font-black text-xs rounded-xl"
            >
              Got It
            </button>
          </div>
        </div>
      )}

      {/* Preview Standings Modal */}
      {showPreviewModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-2xl p-5 shadow-2xl space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-black text-white flex items-center gap-2">
                <Trophy className="w-4 h-4 text-[#C8FF3D]" /> Official Standings Preview
              </h3>
              <button onClick={() => setShowPreviewModal(false)} className="text-[#9498AE] hover:text-white">
                <X className="w-4 h-4" />
              </button>
            </div>
            <div className="bg-[#0A0A0F] border border-[#262640] p-3 rounded-xl space-y-2 text-xs">
              <div className="text-[#FFB800] font-bold">
                🥇 1st: {firstIgn} • {firstKills} Kills ({firstPrize})
              </div>
              <div className="text-slate-300 font-bold">
                🥈 2nd: {secondIgn} • {secondKills} Kills ({secondPrize})
              </div>
              <div className="text-amber-600 font-bold">
                🥉 3rd: {thirdIgn} • {thirdKills} Kills ({thirdPrize})
              </div>
            </div>
            <button
              onClick={() => setShowPreviewModal(false)}
              className="w-full py-2 bg-[#C8FF3D] text-black font-black text-xs rounded-xl"
            >
              Close Preview
            </button>
          </div>
        </div>
      )}

      {/* Ban Confirm Modal */}
      {banConfirmUser && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#FF4D6D]/40 rounded-2xl p-5 shadow-2xl space-y-3">
            <h3 className="text-sm font-black text-[#FF4D6D]">Confirm Account Ban</h3>
            <p className="text-xs text-[#9498AE]">
              Are you sure you want to ban player <strong className="text-white">{banConfirmUser.ign}</strong> (UID:{' '}
              {banConfirmUser.uid})? They will be barred from joining custom rooms.
            </p>
            <div className="flex gap-2">
              <button
                onClick={() => setBanConfirmUser(null)}
                className="flex-1 py-2 bg-[#1B1B2B] text-white font-bold text-xs rounded-xl border border-[#262640]"
              >
                Cancel
              </button>
              <button
                onClick={() => {
                  setUserList(
                    userList.map((u) => (u.id === banConfirmUser.id ? { ...u, status: 'Banned' } : u))
                  );
                  onShowToast('info', `Banned player ${banConfirmUser.ign}`);
                  setBanConfirmUser(null);
                }}
                className="flex-1 py-2 bg-[#FF4D6D] text-white font-bold text-xs rounded-xl"
              >
                Ban Account
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Cancel Tournament Confirm Modal */}
      {cancelConfirmMatch && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#FF4D6D]/40 rounded-2xl p-5 shadow-2xl space-y-3">
            <h3 className="text-sm font-black text-[#FF4D6D]">Cancel Tournament?</h3>
            <p className="text-xs text-[#9498AE]">
              Are you sure you want to cancel <strong className="text-white">{cancelConfirmMatch.title}</strong>? All
              contestants will be notified.
            </p>
            <div className="flex gap-2">
              <button
                onClick={() => setCancelConfirmMatch(null)}
                className="flex-1 py-2 bg-[#1B1B2B] text-white font-bold text-xs rounded-xl border border-[#262640]"
              >
                Keep Active
              </button>
              <button
                onClick={() => {
                  TournamentService.cancelMatch(cancelConfirmMatch.id);
                  onRefreshMatches();
                  onShowToast('info', `Cancelled ${cancelConfirmMatch.title}`);
                  setCancelConfirmMatch(null);
                }}
                className="flex-1 py-2 bg-[#FF4D6D] text-white font-bold text-xs rounded-xl"
              >
                Confirm Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Remove Registration Confirm Modal */}
      {removeConfirmReg && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-md p-4 animate-fade-in">
          <div className="w-full max-w-sm bg-[#14141F] border border-[#FF4D6D]/40 rounded-2xl p-5 shadow-2xl space-y-3">
            <h3 className="text-sm font-black text-[#FF4D6D]">Remove Contestant?</h3>
            <p className="text-xs text-[#9498AE]">
              Remove player <strong className="text-white">{removeConfirmReg.inGameName}</strong> from{' '}
              {removeConfirmReg.tournamentTitle}?
            </p>
            <div className="flex gap-2">
              <button
                onClick={() => setRemoveConfirmReg(null)}
                className="flex-1 py-2 bg-[#1B1B2B] text-white font-bold text-xs rounded-xl border border-[#262640]"
              >
                Cancel
              </button>
              <button
                onClick={() => {
                  setRegistrations(registrations.filter((r) => r.id !== removeConfirmReg.id));
                  onShowToast('info', `Removed ${removeConfirmReg.inGameName}`);
                  setRemoveConfirmReg(null);
                }}
                className="flex-1 py-2 bg-[#FF4D6D] text-white font-bold text-xs rounded-xl"
              >
                Remove
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
