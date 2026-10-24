import React, { useState } from 'react';
import { PlayerMatch } from '../types';
import {
  Search,
  Filter,
  Users,
  Clock,
  Key,
  Copy,
  Check,
  Trophy,
  Flame,
  ShieldAlert,
  ArrowUpDown,
  SlidersHorizontal,
  RotateCcw,
  X
} from 'lucide-react';

interface MatchesTabProps {
  matches: PlayerMatch[];
  onOpenMatchDetail: (match: PlayerMatch) => void;
  onJoinMatch: (match: PlayerMatch) => void;
  onOpenReportModal: (matchId: string) => void;
  onCopyText: (text: string) => void;
  initialSubTab?: 'Upcoming' | 'Live' | 'Completed' | 'My Matches';
}

export const MatchesTab: React.FC<MatchesTabProps> = ({
  matches,
  onOpenMatchDetail,
  onJoinMatch,
  onOpenReportModal,
  onCopyText,
  initialSubTab = 'Upcoming',
}) => {
  const [activeSubTab, setActiveSubTab] = useState<'Upcoming' | 'Live' | 'Completed' | 'My Matches'>(initialSubTab);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedMode, setSelectedMode] = useState<string>('All');
  const [selectedMap, setSelectedMap] = useState<string>('All');
  const [selectedFee, setSelectedFee] = useState<string>('All');
  const [sortBy, setSortBy] = useState<'prize_desc' | 'time_asc' | 'entry_asc'>('time_asc');
  const [showFilterToggle, setShowFilterToggle] = useState<boolean>(false);
  const [copiedId, setCopiedId] = useState<string | null>(null);

  const handleCopy = (text: string, id: string) => {
    onCopyText(text);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  // Filter logic
  const filteredMatches = matches.filter((m) => {
    // Tab filter
    if (activeSubTab === 'My Matches') {
      if (!m.userJoined) return false;
    } else {
      if (m.status !== activeSubTab) return false;
    }

    // Search query
    if (searchQuery.trim()) {
      const q = searchQuery.toLowerCase();
      const matchText = `${m.title} ${m.map} ${m.mode} ${m.sponsorName}`.toLowerCase();
      if (!matchText.includes(q)) return false;
    }

    // Mode filter
    if (selectedMode !== 'All' && m.mode !== selectedMode) return false;

    // Map filter
    if (selectedMap !== 'All' && m.map !== selectedMap) return false;

    // Fee filter
    if (selectedFee === 'Free' && m.entryFee !== 0) return false;
    if (selectedFee === 'Paid' && m.entryFee === 0) return false;
    if (selectedFee === '50+' && m.entryFee < 50) return false;

    return true;
  });

  // Sort logic
  filteredMatches.sort((a, b) => {
    if (sortBy === 'prize_desc') return b.prizePool - a.prizePool;
    if (sortBy === 'entry_asc') return a.entryFee - b.entryFee;
    return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
  });

  return (
    <div className="space-y-4 pb-12 animate-fade-in">
      {/* Top Section Header */}
      <div className="px-4 pt-3 pb-1">
        <h2 className="text-lg font-black text-white flex items-center gap-2">
          <Trophy className="w-5 h-5 text-[#C8FF3D]" /> Tournament Lobby
        </h2>
        <p className="text-xs text-[#9498AE]">Verified Free Fire MAX custom matches & prize pools</p>
      </div>

      {/* Sub Tabs: Upcoming | Live | Completed | My Matches */}
      <div className="px-4">
        <div className="flex bg-[#14141F] p-1 rounded-2xl border border-[#23233A]">
          {(['Upcoming', 'Live', 'Completed', 'My Matches'] as const).map((tab) => {
            const isSelected = activeSubTab === tab;
            return (
              <button
                key={tab}
                onClick={() => setActiveSubTab(tab)}
                className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
                  isSelected
                    ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)]'
                    : 'text-[#9498AE] hover:text-white'
                }`}
              >
                {tab}
                {tab === 'My Matches' && matches.filter((m) => m.userJoined).length > 0 && (
                  <span className={`ml-1 text-[9px] px-1.5 py-0.2 rounded-full font-extrabold ${
                    isSelected ? 'bg-black text-[#C8FF3D]' : 'bg-[#C8FF3D]/20 text-[#C8FF3D]'
                  }`}>
                    {matches.filter((m) => m.userJoined).length}
                  </span>
                )}
              </button>
            );
          })}
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="px-4 space-y-2.5">
        {/* Search input + Filter Toggle Button */}
        <div className="flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-[#9498AE]" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search tournament, map, or sponsor..."
              className="w-full bg-[#14141F] border border-[#23233A] rounded-2xl pl-10 pr-4 py-2.5 text-xs text-white placeholder-[#9498AE] focus:outline-none focus:border-[#C8FF3D]"
            />
          </div>

          {/* Dedicated Filter Toggle Button */}
          <button
            onClick={() => setShowFilterToggle(!showFilterToggle)}
            className={`flex items-center gap-1.5 px-3.5 py-2.5 rounded-2xl border text-xs font-extrabold transition-all shrink-0 ${
              showFilterToggle || selectedMap !== 'All' || sortBy !== 'time_asc'
                ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_15px_rgba(200,255,61,0.25)]'
                : 'bg-[#14141F] text-white border-[#23233A] hover:border-slate-500'
            }`}
          >
            <SlidersHorizontal className="w-3.5 h-3.5" />
            <span>Filter</span>
            {(selectedMap !== 'All' || sortBy !== 'time_asc') && (
              <span className="w-2 h-2 rounded-full bg-[#FF6A00] animate-pulse" />
            )}
          </button>
        </div>

        {/* Expandable Filter & Sort Drawer / Panel */}
        {showFilterToggle && (
          <div className="bg-[#14141F] border border-[#C8FF3D]/40 rounded-2xl p-3.5 space-y-3 shadow-2xl animate-slide-up">
            <div className="flex items-center justify-between border-b border-[#23233A] pb-2">
              <span className="text-xs font-black text-white flex items-center gap-1.5 uppercase tracking-wider">
                <Filter className="w-3.5 h-3.5 text-[#C8FF3D]" /> Filter & Sort Matches
              </span>
              {(selectedMap !== 'All' || sortBy !== 'time_asc' || selectedFee !== 'All' || selectedMode !== 'All') && (
                <button
                  onClick={() => {
                    setSelectedMap('All');
                    setSortBy('time_asc');
                    setSelectedFee('All');
                    setSelectedMode('All');
                  }}
                  className="flex items-center gap-1 text-[10px] text-[#FF6A00] font-bold hover:underline"
                >
                  <RotateCcw className="w-3 h-3" /> Reset All
                </button>
              )}
            </div>

            {/* Sort By section: Entry Fee Low-High, Prize Pool High-Low */}
            <div className="space-y-1.5">
              <span className="text-[10px] font-bold text-[#9498AE] uppercase tracking-wider block">
                Sort Tournaments
              </span>
              <div className="grid grid-cols-2 gap-2 text-xs">
                <button
                  onClick={() => setSortBy('entry_asc')}
                  className={`p-2 rounded-xl border text-xs font-bold text-left transition-all ${
                    sortBy === 'entry_asc'
                      ? 'bg-[#C8FF3D]/15 border-[#C8FF3D] text-[#C8FF3D]'
                      : 'bg-[#0E0E17] border-[#23233A] text-slate-300 hover:text-white'
                  }`}
                >
                  <span className="block text-[9px] text-[#9498AE]">Entry Fee</span>
                  <span>Low to High ↑</span>
                </button>

                <button
                  onClick={() => setSortBy('prize_desc')}
                  className={`p-2 rounded-xl border text-xs font-bold text-left transition-all ${
                    sortBy === 'prize_desc'
                      ? 'bg-[#FFB300]/15 border-[#FFB300] text-[#FFB300]'
                      : 'bg-[#0E0E17] border-[#23233A] text-slate-300 hover:text-white'
                  }`}
                >
                  <span className="block text-[9px] text-[#9498AE]">Prize Pool</span>
                  <span>High to Low ↓</span>
                </button>
              </div>
            </div>

            {/* Specifically Filter by Map: Bermuda / Purgatory */}
            <div className="space-y-1.5">
              <span className="text-[10px] font-bold text-[#9498AE] uppercase tracking-wider block">
                Filter by Map (Battleground)
              </span>
              <div className="grid grid-cols-3 gap-1.5 text-xs">
                {[
                  { id: 'All', label: 'All Maps' },
                  { id: 'Bermuda', label: '🏝️ Bermuda' },
                  { id: 'Purgatory', label: '🌋 Purgatory' },
                  { id: 'Kalahari', label: '🏜️ Kalahari' },
                  { id: 'Alpine', label: '❄️ Alpine' },
                ].map((mapItem) => (
                  <button
                    key={mapItem.id}
                    onClick={() => setSelectedMap(mapItem.id)}
                    className={`py-1.5 px-2 rounded-xl border text-[11px] font-bold truncate transition-all ${
                      selectedMap === mapItem.id
                        ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] font-black shadow'
                        : 'bg-[#0E0E17] border-[#23233A] text-slate-300 hover:text-white'
                    }`}
                  >
                    {mapItem.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Close / Apply button */}
            <button
              onClick={() => setShowFilterToggle(false)}
              className="w-full py-2 bg-[#1C1C2E] hover:bg-[#23233A] text-white border border-[#23233A] rounded-xl text-xs font-bold transition-all text-center"
            >
              Apply & Close
            </button>
          </div>
        )}

        {/* Quick Map Filter Chips (Single-tap Bermuda & Purgatory) */}
        <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-1 text-xs">
          <span className="text-[10px] text-[#9498AE] font-bold uppercase shrink-0 mr-1">
            Map:
          </span>

          <button
            onClick={() => setSelectedMap('All')}
            className={`px-2.5 py-1 rounded-xl text-[11px] font-bold whitespace-nowrap transition-all border ${
              selectedMap === 'All'
                ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                : 'bg-[#14141F] text-[#9498AE] border-[#23233A] hover:text-white'
            }`}
          >
            All
          </button>

          <button
            onClick={() => setSelectedMap(selectedMap === 'Bermuda' ? 'All' : 'Bermuda')}
            className={`px-3 py-1 rounded-xl text-[11px] font-bold whitespace-nowrap transition-all border flex items-center gap-1 ${
              selectedMap === 'Bermuda'
                ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_10px_rgba(200,255,61,0.25)]'
                : 'bg-[#14141F] text-white border-[#23233A] hover:border-[#C8FF3D]'
            }`}
          >
            <span>Bermuda</span>
            {selectedMap === 'Bermuda' && <Check className="w-3 h-3" />}
          </button>

          <button
            onClick={() => setSelectedMap(selectedMap === 'Purgatory' ? 'All' : 'Purgatory')}
            className={`px-3 py-1 rounded-xl text-[11px] font-bold whitespace-nowrap transition-all border flex items-center gap-1 ${
              selectedMap === 'Purgatory'
                ? 'bg-[#FF6A00] text-white border-[#FF6A00] shadow-[0_0_10px_rgba(255,106,0,0.25)]'
                : 'bg-[#14141F] text-white border-[#23233A] hover:border-[#FF6A00]'
            }`}
          >
            <span>Purgatory</span>
            {selectedMap === 'Purgatory' && <Check className="w-3 h-3" />}
          </button>

          {/* Quick Sort Toggle Button */}
          <div className="ml-auto flex items-center gap-1 shrink-0">
            <button
              onClick={() => {
                if (sortBy === 'time_asc') setSortBy('entry_asc');
                else if (sortBy === 'entry_asc') setSortBy('prize_desc');
                else setSortBy('time_asc');
              }}
              className="flex items-center gap-1 bg-[#14141F] border border-[#23233A] rounded-xl px-2.5 py-1 text-[11px] font-bold text-[#C8FF3D]"
            >
              <ArrowUpDown className="w-3 h-3" />
              <span>
                {sortBy === 'entry_asc' ? 'Fee: Low ↑' : sortBy === 'prize_desc' ? 'Prize: High ↓' : 'Default Sort'}
              </span>
            </button>
          </div>
        </div>
      </div>

      {/* Matches List */}
      <div className="px-4 space-y-3.5">
        {filteredMatches.length === 0 ? (
          <div className="py-16 text-center bg-[#14141F] border border-[#23233A] rounded-[24px] p-6 space-y-3">
            <Trophy className="w-12 h-12 mx-auto text-[#23233A]" />
            <h4 className="text-sm font-bold text-white">No Tournaments Found</h4>
            <p className="text-xs text-[#9498AE] max-w-xs mx-auto">
              {activeSubTab === 'My Matches'
                ? 'You have not joined any tournaments yet. Explore the Upcoming tab to register!'
                : 'Try adjusting your filters or search keywords to discover available battles.'}
            </p>
          </div>
        ) : (
          filteredMatches.map((m) => {
            const isFull = m.joinedSlots >= m.maxSlots;
            const fillPercent = Math.min(100, Math.round((m.joinedSlots / m.maxSlots) * 100));

            return (
              <div
                key={m.id}
                onClick={() => onOpenMatchDetail(m)}
                className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden shadow-xl hover:border-[#C8FF3D]/50 transition-all cursor-pointer group"
              >
                {/* Hero Map Header */}
                <div className="relative h-28 overflow-hidden">
                  <img
                    src={m.mapImage}
                    alt={m.map}
                    className="w-full h-full object-cover brightness-60 group-hover:scale-105 transition-transform duration-500"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-[#14141F] via-[#14141F]/40 to-transparent" />

                  {/* Top Badges */}
                  <div className="absolute top-2.5 left-3 right-3 flex items-center justify-between">
                    <div className="flex items-center gap-1.5">
                      <span className="px-2.5 py-0.5 bg-[#C8FF3D] text-black text-[10px] font-black rounded-full uppercase tracking-wider">
                        {m.mode}
                      </span>
                      <span className="px-2 py-0.5 bg-black/60 text-white text-[10px] font-bold rounded-full border border-white/10 backdrop-blur-md">
                        {m.map}
                      </span>
                    </div>

                    <span className="text-[10px] font-bold text-slate-300 bg-black/60 px-2 py-0.5 rounded-full border border-white/10 flex items-center gap-1">
                      <Clock className="w-3 h-3 text-[#C8FF3D]" />
                      {new Date(m.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </span>
                  </div>

                  {/* Title & Sponsor */}
                  <div className="absolute bottom-2 left-3 right-3">
                    <h3 className="text-sm font-bold text-white group-hover:text-[#C8FF3D] transition-colors line-clamp-1">
                      {m.title}
                    </h3>
                    <p className="text-[10px] text-[#9498AE]">Sponsored by {m.sponsorName}</p>
                  </div>
                </div>

                {/* Card Body & Stats */}
                <div className="p-3.5 space-y-3">
                  {/* Prize, Fee, Kill reward columns */}
                  <div className="grid grid-cols-3 gap-2 py-2 px-3 bg-[#1C1C2E] rounded-xl border border-[#23233A]/60 text-center">
                    <div>
                      <span className="text-[9px] text-[#9498AE] block uppercase">Prize Pool</span>
                      <span className="text-xs font-black text-[#FFB300]">₹{m.prizePool}</span>
                    </div>
                    <div>
                      <span className="text-[9px] text-[#9498AE] block uppercase">Entry Fee</span>
                      <span className={`text-xs font-black ${m.entryFee === 0 ? 'text-[#C8FF3D]' : 'text-white'}`}>
                        {m.entryFee === 0 ? 'FREE' : `₹${m.entryFee}`}
                      </span>
                    </div>
                    <div>
                      <span className="text-[9px] text-[#9498AE] block uppercase">Per Kill</span>
                      <span className="text-xs font-black text-[#FF6A00]">₹{m.perKillPrize}</span>
                    </div>
                  </div>

                  {/* Progress Slots Bar */}
                  <div className="space-y-1">
                    <div className="flex justify-between items-center text-[10px]">
                      <span className="text-[#9498AE] flex items-center gap-1">
                        <Users className="w-3 h-3 text-[#C8FF3D]" /> Registered
                      </span>
                      <span className="font-bold text-white">
                        {m.joinedSlots} / {m.maxSlots} ({fillPercent}%)
                      </span>
                    </div>
                    <div className="w-full bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                      <div
                        className={`h-full rounded-full transition-all duration-300 ${
                          fillPercent >= 90
                            ? 'bg-gradient-to-r from-[#FF6A00] to-red-500'
                            : 'bg-gradient-to-r from-[#7B5CFF] to-[#C8FF3D]'
                        }`}
                        style={{ width: `${fillPercent}%` }}
                      />
                    </div>
                  </div>

                  {/* Joined Room Reveal Container (if My Matches or userJoined) */}
                  {m.userJoined && (
                    <div className="p-2.5 bg-[#0E0E17] border border-[#7B5CFF]/30 rounded-xl space-y-1.5">
                      <div className="flex items-center justify-between">
                        <span className="text-[10px] font-bold text-white flex items-center gap-1">
                          <Key className="w-3.5 h-3.5 text-[#FFB300]" /> Room Credentials:
                        </span>
                        {m.isRoomRevealed ? (
                          <span className="text-[9px] px-1.5 py-0.2 bg-[#C8FF3D]/20 text-[#C8FF3D] font-bold rounded">
                            UNLOCKED
                          </span>
                        ) : (
                          <span className="text-[9px] px-1.5 py-0.2 bg-white/10 text-[#9498AE] rounded">
                            Revealing 10m before start
                          </span>
                        )}
                      </div>

                      {m.isRoomRevealed && m.roomId ? (
                        <div className="flex items-center justify-between text-xs font-mono bg-[#1C1C2E] p-2 rounded-lg">
                          <div>
                            <span className="text-[#9498AE] text-[9px] block">Room ID / Pass:</span>
                            <span className="font-bold text-[#C8FF3D]">{m.roomId}</span>
                            <span className="text-[#9498AE] mx-1">|</span>
                            <span className="font-bold text-[#FFB300]">{m.roomPassword}</span>
                          </div>
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              handleCopy(`Room: ${m.roomId} Pass: ${m.roomPassword}`, m.id);
                            }}
                            className="p-1.5 bg-[#23233A] hover:bg-[#2E2E4A] rounded-md text-white"
                          >
                            {copiedId === m.id ? <Check className="w-3.5 h-3.5 text-[#C8FF3D]" /> : <Copy className="w-3.5 h-3.5" />}
                          </button>
                        </div>
                      ) : null}

                      {/* Completed match result banner */}
                      {m.status === 'Completed' && (
                        <div className="flex items-center justify-between text-[11px] pt-1 border-t border-white/5">
                          <span className="text-[#9498AE]">
                            Rank #{m.myRank || 2} • {m.myKills || 6} Kills
                          </span>
                          <span className="font-bold text-[#FFB300]">
                            +₹{m.myWinnings || 1620} Won
                          </span>
                        </div>
                      )}

                      {/* Submit dispute or proof */}
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onOpenReportModal(m.id);
                        }}
                        className="w-full py-1 text-[10px] font-bold text-[#9498AE] hover:text-white flex items-center justify-center gap-1"
                      >
                        <ShieldAlert className="w-3 h-3 text-red-400" /> Submit Screenshot / Dispute
                      </button>
                    </div>
                  )}

                  {/* Action Button */}
                  {!m.userJoined && (
                    <div className="pt-1">
                      {isFull ? (
                        <button
                          disabled
                          className="w-full py-2.5 bg-[#1C1C2E] text-slate-500 border border-[#23233A] rounded-xl text-xs font-bold cursor-not-allowed"
                        >
                          Tournament Full
                        </button>
                      ) : (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            onJoinMatch(m);
                          }}
                          className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] transition-all active:scale-[0.98]"
                        >
                          Join Match ({m.entryFee === 0 ? 'FREE' : `₹${m.entryFee}`})
                        </button>
                      )}
                    </div>
                  )}
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};
