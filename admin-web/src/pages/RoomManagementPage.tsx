import React, { useState, useEffect } from 'react';
import { KeyRound, Lock, Eye, Copy, Check, Clock, AlertCircle, ShieldCheck } from 'lucide-react';
import { Tournament } from '../types';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';

export const RoomManagementPage: React.FC = () => {
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [selectedTournament, setSelectedTournament] = useState<Tournament | null>(null);
  const [roomId, setRoomId] = useState('');
  const [roomPassword, setRoomPassword] = useState('');
  const [autoRevealMinutes, setAutoRevealMinutes] = useState(10);
  const [simulateUnlocked, setSimulateUnlocked] = useState(false);
  const [copiedField, setCopiedField] = useState<string | null>(null);
  const [saveSuccess, setSaveSuccess] = useState(false);

  useEffect(() => {
    loadTournaments();
  }, []);

  const loadTournaments = async () => {
    const list = await TournamentService.getTournaments();
    setTournaments(list);
    if (list.length > 0 && !selectedTournament) {
      handleSelectTournament(list[0]);
    }
  };

  const handleSelectTournament = (t: Tournament) => {
    setSelectedTournament(t);
    setRoomId(t.roomId || '');
    setRoomPassword(t.roomPassword || '');
    setAutoRevealMinutes(t.autoRevealMinutes || 10);
    setSimulateUnlocked(t.isRoomRevealed || false);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedTournament) return;

    await TournamentService.updateRoomCredentials(
      selectedTournament.id,
      roomId,
      roomPassword,
      autoRevealMinutes
    );

    await loadTournaments();
    setSaveSuccess(true);
    setTimeout(() => setSaveSuccess(false), 3000);
  };

  const copyText = (text: string, field: string) => {
    navigator.clipboard.writeText(text);
    setCopiedField(field);
    setTimeout(() => setCopiedField(null), 2000);
  };

  return (
    <div className="space-y-6">
      {saveSuccess && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-xs font-bold text-[#C8FF3D] animate-fade-in">
          <ShieldCheck className="w-4 h-4 shrink-0" />
          <span>Room credentials updated and auto-reveal timer armed!</span>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        {/* Left Column: Tournament Selector & Credentials Form */}
        <div className="lg:col-span-7 space-y-6">
          {/* Tournament Selector Card */}
          <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6">
            <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
              Select Tournament to Manage
            </label>
            <select
              value={selectedTournament?.id || ''}
              onChange={(e) => {
                const found = tournaments.find((t) => t.id === e.target.value);
                if (found) handleSelectTournament(found);
              }}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-3 text-sm focus:outline-none"
            >
              {tournaments.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.name} ({t.mode} • {t.division}) — {t.status}
                </option>
              ))}
            </select>
          </div>

          {/* Credentials Configuration Form */}
          {selectedTournament && (
            <form onSubmit={handleSave} className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 space-y-4">
              <div className="flex items-center justify-between pb-3 border-b border-[#23233A]">
                <div>
                  <h3 className="font-extrabold text-[#F5F5FA] text-base">Custom Room Credentials</h3>
                  <p className="text-xs text-[#9498AE]">Set Free Fire MAX custom match credentials</p>
                </div>
                <Badge variant={selectedTournament.status === 'Live' ? 'red' : 'violet'}>
                  {selectedTournament.status}
                </Badge>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                    Room ID *
                  </label>
                  <div className="relative">
                    <KeyRound className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
                    <input
                      type="text"
                      required
                      value={roomId}
                      onChange={(e) => setRoomId(e.target.value)}
                      placeholder="e.g. 982341"
                      className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2.5 text-sm focus:outline-none font-mono"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                    Room Password *
                  </label>
                  <div className="relative">
                    <Lock className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
                    <input
                      type="text"
                      required
                      value={roomPassword}
                      onChange={(e) => setRoomPassword(e.target.value)}
                      placeholder="e.g. rog99"
                      className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2.5 text-sm focus:outline-none font-mono"
                    />
                  </div>
                </div>
              </div>

              {/* Auto-reveal time */}
              <div>
                <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                  Auto-Reveal Time (Minutes Before Match Start)
                </label>
                <div className="flex items-center space-x-3">
                  <input
                    type="number"
                    min="1"
                    max="60"
                    value={autoRevealMinutes}
                    onChange={(e) => setAutoRevealMinutes(Number(e.target.value))}
                    className="w-32 bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
                  />
                  <span className="text-xs text-[#9498AE]">
                    Default is 10 mins. Prevents players leaking credentials too early.
                  </span>
                </div>
              </div>

              <div className="p-3.5 bg-[#7B5CFF]/10 border border-[#7B5CFF]/30 rounded-[16px] flex items-start gap-2.5 text-xs text-[#B39DDB]">
                <Clock className="w-4 h-4 shrink-0 mt-0.5 text-[#7B5CFF]" />
                <span>
                  Match starts at{' '}
                  <strong className="text-[#F5F5FA]">
                    {new Date(selectedTournament.startDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  </strong>
                  . Room ID will automatically unlock for confirmed participants{' '}
                  <strong className="text-[#C8FF3D]">{autoRevealMinutes} minutes prior</strong>.
                </span>
              </div>

              <button
                type="submit"
                className="w-full bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold py-3 rounded-[18px] text-xs transition-all shadow-md shadow-[#C8FF3D]/10"
              >
                SAVE CREDENTIALS & ARM AUTO-REVEAL
              </button>
            </form>
          )}
        </div>

        {/* Right Column: Interactive Player Preview */}
        <div className="lg:col-span-5 space-y-4">
          <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6">
            <div className="flex items-center justify-between pb-3 border-b border-[#23233A] mb-4">
              <div className="flex items-center gap-2">
                <Eye className="w-4 h-4 text-[#C8FF3D]" />
                <h3 className="font-extrabold text-[#F5F5FA] text-sm">Player In-App View Preview</h3>
              </div>
              <button
                type="button"
                onClick={() => setSimulateUnlocked(!simulateUnlocked)}
                className="text-[11px] font-bold px-2.5 py-1 rounded-full bg-[#23233A] text-[#C8FF3D] hover:bg-[#2E2E4A] transition-colors"
              >
                Toggle: {simulateUnlocked ? 'Unlocked State' : 'Locked State'}
              </button>
            </div>

            {/* Mobile Card Mockup */}
            <div className="bg-[#0A0A0F] border border-[#23233A] rounded-[20px] p-5 relative overflow-hidden">
              <div className="flex items-center justify-between mb-3">
                <Badge variant="blue">{selectedTournament?.mode || 'SQUAD'}</Badge>
                <span className="text-[11px] text-[#9498AE] font-semibold">Today, 08:30 PM</span>
              </div>

              <h4 className="font-extrabold text-[#F5F5FA] text-base mb-3 truncate">
                {selectedTournament?.name || 'Tournament Title'}
              </h4>

              {simulateUnlocked ? (
                /* UNLOCKED STATE (what player sees 10m before) */
                <div className="bg-[#C8FF3D]/10 border border-[#C8FF3D] rounded-[16px] p-4 space-y-3">
                  <div className="flex items-center justify-between">
                    <span className="text-[10px] font-extrabold tracking-wider uppercase text-[#C8FF3D] flex items-center gap-1">
                      <span className="w-2 h-2 rounded-full bg-[#C8FF3D] animate-ping" />
                      ROOM CREDENTIALS UNLOCKED
                    </span>
                    <span className="text-[10px] text-[#9498AE]">Verified Slot</span>
                  </div>

                  <div className="flex items-center justify-between pt-1">
                    <div>
                      <span className="text-[10px] font-bold text-[#9498AE] uppercase block">Room ID</span>
                      <span className="text-base font-extrabold text-[#F5F5FA] font-mono">
                        {roomId || '892341'}
                      </span>
                    </div>
                    <button
                      onClick={() => copyText(roomId || '892341', 'room')}
                      className="px-3 py-1.5 bg-[#C8FF3D] text-black text-xs font-extrabold rounded-xl flex items-center gap-1 shadow-sm"
                    >
                      {copiedField === 'room' ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                      <span>{copiedField === 'room' ? 'COPIED' : 'COPY'}</span>
                    </button>
                  </div>

                  <div className="flex items-center justify-between pt-1 border-t border-[#C8FF3D]/20">
                    <div>
                      <span className="text-[10px] font-bold text-[#9498AE] uppercase block">Password</span>
                      <span className="text-sm font-extrabold text-[#F5F5FA] font-mono">
                        {roomPassword || 'ffa99'}
                      </span>
                    </div>
                    <button
                      onClick={() => copyText(roomPassword || 'ffa99', 'pass')}
                      className="px-3 py-1.5 bg-[#23233A] hover:bg-[#2E2E4A] text-[#F5F5FA] text-xs font-bold rounded-xl flex items-center gap-1"
                    >
                      {copiedField === 'pass' ? <Check className="w-3.5 h-3.5" /> : <Copy className="w-3.5 h-3.5" />}
                      <span>{copiedField === 'pass' ? 'COPIED' : 'COPY'}</span>
                    </button>
                  </div>
                </div>
              ) : (
                /* LOCKED STATE (what player sees until 10 mins prior) */
                <div className="bg-[#14141F] border border-[#23233A] rounded-[16px] p-4 flex items-center gap-3.5">
                  <div className="w-10 h-10 rounded-full bg-[#23233A] flex items-center justify-center text-[#9498AE] shrink-0">
                    <Lock className="w-5 h-5" />
                  </div>
                  <div>
                    <span className="text-[11px] font-bold text-[#F5F5FA] block uppercase tracking-wide">
                      ROOM ID & PASSWORD LOCKED
                    </span>
                    <span className="text-[11px] text-[#9498AE]">
                      Unlocks {autoRevealMinutes} minutes before start for registered players.
                    </span>
                  </div>
                </div>
              )}

              <div className="mt-4 pt-3 border-t border-[#23233A] flex items-center justify-between text-[11px] text-[#9498AE]">
                <span>Map: Bermuda Remastered</span>
                <span className="text-[#C8FF3D] font-bold">Gun Skins: OFF</span>
              </div>
            </div>

            <p className="text-[11px] text-[#9498AE] mt-3 text-center">
              Players view this inside the Free Fire MAX app under the "My Matches" tab.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};
