import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { PlayerMatch, MapType, GameMode } from '../types';
import {
  Search,
  Filter,
  Users,
  Clock,
  Key,
  Copy,
  Check,
  Trophy,
  SlidersHorizontal,
  RotateCcw,
  ShieldCheck,
  ChevronRight,
  Sparkles,
  Lock
} from 'lucide-react';

interface MatchesTabProps {
  matches: PlayerMatch[];
  onOpenMatchDetail: (match: PlayerMatch) => void;
  onJoinMatch: (match: PlayerMatch) => void;
  onOpenReportModal: (matchId: string) => void;
  onCopyText: (text: string) => void;
  initialSubTab?: 'Upcoming' | 'Live' | 'Completed' | 'My Matches';
}

const MAP_ACCENTS: Record<MapType, string> = {
  Bermuda: '#C8FF3D',
  Purgatory: '#FF4D6D',
  Kalahari: '#FFB800',
  Alpine: '#00E5FF',
};

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
  const [sortBy, setSortBy] = useState<'prize_desc' | 'time_asc'>('time_asc');
  const [showFilterToggle, setShowFilterToggle] = useState<boolean>(false);
  const [copiedId, setCopiedId] = useState<string | null>(null);

  const handleCopy = (text: string, id: string, e: React.MouseEvent) => {
    e.stopPropagation();
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

    return true;
  });

  // Sort logic
  filteredMatches.sort((a, b) => {
    if (sortBy === 'prize_desc') return b.prizePool - a.prizePool;
    return new Date(a.startTime).getTime() - new Date(b.startTime).getTime();
  });

  const myMatchesCount = matches.filter((m) => m.userJoined).length;

  return (
    <div className="space-y-4 pb-32 animate-fade-in">
      {/* Top Section Header */}
      <div className="px-4 pt-3 pb-1">
        <h2 className="text-lg font-black text-white flex items-center gap-2">
          <Trophy className="w-5 h-5 text-[#C8FF3D]" /> Tournament Lobby
        </h2>
        <p className="text-xs text-[#A7A7C0]">Official Free Fire Custom Rooms & Sponsor Cups</p>
      </div>

      {/* Horizontal Scrolling Sub Tabs without clipping */}
      <div className="px-4">
        <div className="flex bg-[#14141F] p-1 rounded-2xl border border-[#262640] overflow-x-auto no-scrollbar gap-1 pr-6">
          {(['Upcoming', 'Live', 'Completed', 'My Matches'] as const).map((tab) => {
            const isSelected = activeSubTab === tab;
            return (
              <button
                key={tab}
                onClick={() => setActiveSubTab(tab)}
                className={`py-2 px-3 text-xs font-bold rounded-xl whitespace-nowrap transition-all shrink-0 flex-1 text-center ${
                  isSelected
                    ? 'bg-[#C8FF3D] text-black shadow-[0_0_12px_rgba(200,255,61,0.25)] font-black'
                    : 'text-[#A7A7C0] hover:text-white'
                }`}
              >
                {tab}
                {tab === 'My Matches' && myMatchesCount > 0 && (
                  <span className={`ml-1.5 text-xs px-1.5 py-0.2 rounded-full font-black ${
                    isSelected ? 'bg-black text-[#C8FF3D]' : 'bg-[#C8FF3D]/20 text-[#C8FF3D]'
                  }`}>
                    {myMatchesCount}
                  </span>
                )}
              </button>
            );
          })}
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="px-4 space-y-2.5">
        <div className="flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="w-4 h-4 absolute left-3.5 top-1/2 -translate-y-1/2 text-[#A7A7C0]" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search tournament, map, or sponsor..."
              className="w-full bg-[#14141F] border border-[#262640] rounded-2xl pl-10 pr-4 py-2.5 text-xs text-white placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
            />
          </div>

          <button
            onClick={() => setShowFilterToggle(!showFilterToggle)}
            className={`flex items-center gap-1.5 px-3.5 py-2.5 rounded-2xl border text-xs font-extrabold transition-all shrink-0 ${
              showFilterToggle || selectedMap !== 'All' || selectedMode !== 'All' || sortBy !== 'time_asc'
                ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_15px_rgba(200,255,61,0.25)]'
                : 'bg-[#14141F] text-white border-[#262640] hover:border-slate-500'
            }`}
          >
            <SlidersHorizontal className="w-3.5 h-3.5" />
            <span>Filter</span>
            {(selectedMap !== 'All' || selectedMode !== 'All' || sortBy !== 'time_asc') && (
              <span className="w-1.5 h-1.5 rounded-full bg-black" />
            )}
          </button>
        </div>

        {/* Expandable Filter & Sort Drawer */}
        {showFilterToggle && (
          <div className="bg-[#1B1B2B] border border-[#C8FF3D]/40 rounded-2xl p-3.5 space-y-3 shadow-2xl animate-slide-up">
            <div className="flex items-center justify-between border-b border-[#262640] pb-2">
              <span className="text-xs font-black text-white flex items-center gap-1.5 uppercase tracking-wider">
                <Filter className="w-3.5 h-3.5 text-[#C8FF3D]" /> Filter Tournaments
              </span>
              {(selectedMap !== 'All' || sortBy !== 'time_asc' || selectedMode !== 'All') && (
                <button
                  onClick={() => {
                    setSelectedMap('All');
                    setSelectedMode('All');
                    setSortBy('time_asc');
                  }}
                  className="flex items-center gap-1 text-xs text-[#FF4D6D] font-bold hover:underline"
                >
                  <RotateCcw className="w-3 h-3" /> Reset All
                </button>
              )}
            </div>

            {/* Sort: Time vs Prize Pool */}
            <div className="space-y-1.5">
              <span className="text-xs font-bold text-[#A7A7C0] uppercase tracking-wider block">
                Sort Tournaments
              </span>
              <div className="grid grid-cols-2 gap-2 text-xs">
                <button
                  onClick={() => setSortBy('time_asc')}
                  className={`p-2 rounded-xl border text-xs font-bold text-left transition-all ${
                    sortBy === 'time_asc'
                      ? 'bg-[#C8FF3D]/15 border-[#C8FF3D] text-[#C8FF3D]'
                      : 'bg-[#0A0A0F] border-[#262640] text-slate-300'
                  }`}
                >
                  <span className="block text-xs text-[#A7A7C0]">Time</span>
                  <span>Earliest First ↑</span>
                </button>

                <button
                  onClick={() => setSortBy('prize_desc')}
                  className={`p-2 rounded-xl border text-xs font-bold text-left transition-all ${
                    sortBy === 'prize_desc'
                      ? 'bg-[#C8FF3D]/15 border-[#C8FF3D] text-[#C8FF3D]'
                      : 'bg-[#0A0A0F] border-[#262640] text-slate-300'
                  }`}
                >
                  <span className="block text-xs text-[#A7A7C0]">Sponsor Pool</span>
                  <span>High to Low ↓</span>
                </button>
              </div>
            </div>

            {/* Filter by Map */}
            <div className="space-y-1.5">
              <span className="text-xs font-bold text-[#A7A7C0] uppercase tracking-wider block">
                Battleground Map
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
                    className={`py-1.5 px-2 rounded-xl border text-xs font-bold truncate transition-all ${
                      selectedMap === mapItem.id
                        ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] font-black'
                        : 'bg-[#0A0A0F] border-[#262640] text-slate-300'
                    }`}
                  >
                    {mapItem.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Close button */}
            <button
              onClick={() => setShowFilterToggle(false)}
              className="w-full py-2 bg-[#14141F] hover:bg-[#262640] text-white border border-[#262640] rounded-xl text-xs font-bold transition-all text-center"
            >
              Apply Filters
            </button>
          </div>
        )}

        {/* Quick Map Chips */}
        <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar py-0.5 pr-8">
          <span className="text-xs text-[#A7A7C0] font-bold uppercase shrink-0 mr-1">
            Map:
          </span>
          {['All', 'Bermuda', 'Purgatory', 'Kalahari', 'Alpine'].map((m) => (
            <button
              key={m}
              onClick={() => setSelectedMap(m)}
              className={`px-2.5 py-1 rounded-xl text-xs font-bold whitespace-nowrap transition-all border ${
                selectedMap === m
                  ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] font-black'
                  : 'bg-[#14141F] text-[#A7A7C0] border-[#262640] hover:text-white'
              }`}
            >
              {m}
            </button>
          ))}
        </div>
      </div>

      {/* Match Cards List */}
      <div className="px-4 space-y-3">
        {filteredMatches.length === 0 ? (
          <div className="py-16 text-center text-[#A7A7C0] bg-[#14141F] rounded-2xl border border-[#262640] p-6">
            <Trophy className="w-12 h-12 mx-auto text-[#262640] mb-3" />
            <h4 className="text-sm font-bold text-white mb-1">No Tournaments Found</h4>
            <p className="text-xs max-w-xs mx-auto">
              Try adjusting your map or game mode filters to view available Free Fire custom matches.
            </p>
          </div>
        ) : (
          <motion.div
            variants={{
              hidden: { opacity: 0 },
              show: {
                opacity: 1,
                transition: {
                  staggerChildren: 0.08,
                },
              },
            }}
            initial="hidden"
            animate="show"
            className="space-y-3"
          >
            {filteredMatches.map((match) => {
              const slotsPercent = Math.min(100, (match.joinedSlots / match.maxSlots) * 100);
              const remainingSlots = match.maxSlots - match.joinedSlots;
              const isUrgentSlots = remainingSlots > 0 && remainingSlots <= Math.ceil(match.maxSlots * 0.1);
              const mapAccent = MAP_ACCENTS[match.map] || '#C8FF3D';
              const isFull = match.joinedSlots >= match.maxSlots;

              return (
                <motion.div
                  key={match.id}
                  variants={{
                    hidden: { opacity: 0, y: 20 },
                    show: { opacity: 1, y: 0, transition: { duration: 0.35, ease: 'easeOut' } },
                  }}
                  onClick={() => onOpenMatchDetail(match)}
                  className="bg-[#14141F] border border-[#262640] rounded-2xl p-4 space-y-3 hover:border-slate-500 transition-all cursor-pointer group shadow-lg relative overflow-hidden"
                  style={{ borderLeftWidth: '4px', borderLeftColor: mapAccent }}
                >
                  {/* Header Row: Tags + Title */}
                  <div className="flex items-start justify-between gap-2">
                    <div className="flex-1">
                      <div className="flex items-center gap-1.5 flex-wrap mb-1">
                        <span className="px-2 py-0.5 bg-[#C8FF3D] text-black font-black text-xs rounded-full uppercase tracking-wider">
                          FREE ENTRY
                        </span>
                        <span className="px-2 py-0.5 bg-[#1B1B2B] text-white font-bold text-xs rounded-full border border-[#262640]">
                          {match.mode}
                        </span>
                        <span className="px-2 py-0.5 bg-[#1B1B2B] text-[#A7A7C0] font-bold text-xs rounded-full border border-[#262640]">
                          {match.map}
                        </span>
                        {match.status === 'Live' && (
                          <span className="px-2 py-0.5 bg-[#FF4D6D] text-white font-black text-xs rounded-full animate-pulse">
                            LIVE
                          </span>
                        )}
                      </div>

                      <h3 className="text-sm font-black text-white group-hover:text-[#C8FF3D] transition-colors leading-snug line-clamp-1">
                        {match.title}
                      </h3>
                      <span className="text-xs text-[#7B5CFF] font-semibold block mt-0.5">
                        Sponsored by {match.sponsorName}
                      </span>
                    </div>

                    {/* Prize Display */}
                    <div className="text-right shrink-0">
                      <span className="text-base font-black text-white drop-shadow-[0_0_8px_rgba(200,255,61,0.3)] block">
                        ₹{match.prizePool.toLocaleString()}
                      </span>
                      <span className="text-xs text-[#A7A7C0] block">
                        Sponsor Pool
                      </span>
                    </div>
                  </div>

                  {/* 3 Metric Pills */}
                  <div className="grid grid-cols-3 gap-2 text-center bg-[#0A0A0F] p-2 rounded-xl border border-[#262640] text-xs">
                    <div>
                      <span className="text-xs text-[#A7A7C0] block">Entry Fee</span>
                      <span className="font-black text-[#C8FF3D]">FREE</span>
                    </div>
                    <div>
                      <span className="text-xs text-[#A7A7C0] block">Elimination</span>
                      <span className="font-black text-[#7B5CFF]">+10 XP</span>
                    </div>
                    <div>
                      <span className="text-xs text-[#A7A7C0] block">Kick-Off</span>
                      <span className="font-bold text-white truncate block">
                        {new Date(match.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    </div>
                  </div>

                  {/* Slots meter & Action */}
                  <div className="flex items-center justify-between pt-1">
                    <div className="flex items-center gap-2 flex-1 mr-3">
                      <div className="flex-1 max-w-[120px] bg-[#0A0A0F] h-1.5 rounded-full overflow-hidden">
                        <div
                          className={`h-full ${isUrgentSlots ? 'bg-[#FF4D6D]' : 'bg-[#C8FF3D]'}`}
                          style={{ width: `${slotsPercent}%` }}
                        />
                      </div>
                      <span className={`text-xs font-mono shrink-0 ${isUrgentSlots ? 'text-[#FF4D6D] font-extrabold animate-pulse' : 'text-[#A7A7C0]'}`}>
                        {isUrgentSlots ? `${remainingSlots} slots left!` : `${match.joinedSlots}/${match.maxSlots}`}
                      </span>
                    </div>

                    {match.userJoined ? (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onOpenMatchDetail(match);
                        }}
                        className="px-3 py-1.5 bg-[#C8FF3D]/15 border border-[#C8FF3D]/40 text-[#C8FF3D] font-black text-xs rounded-xl flex items-center gap-1"
                      >
                        <span>Registered</span>
                        <ChevronRight className="w-3 h-3" />
                      </button>
                    ) : isFull ? (
                      <span className="text-xs text-[#A7A7C0] font-bold">Full</span>
                    ) : (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onJoinMatch(match);
                        }}
                        className="px-3 py-1.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow active:scale-95 transition-all flex items-center gap-1"
                      >
                        <span>Join Free</span>
                        <ChevronRight className="w-3 h-3" />
                      </button>
                    )}
                  </div>
                </motion.div>
              );
            })}
          </motion.div>
        )}
      </div>
    </div>
  );
};
