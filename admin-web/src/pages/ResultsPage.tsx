import React, { useState, useEffect } from 'react';
import {
  Award,
  CheckCircle,
  ExternalLink,
  Plus,
  Send,
  ShieldCheck,
  CheckCircle2,
  AlertCircle,
  Coins,
} from 'lucide-react';
import { MatchResult, Tournament } from '../types';
import { TournamentService } from '../services/tournamentService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { useAuth } from '../context/AuthContext';

export const ResultsPage: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [selectedTournamentId, setSelectedTournamentId] = useState<string>('tourn_05');
  const [results, setResults] = useState<MatchResult[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isPublishing, setIsPublishing] = useState(false);
  const [actionNotice, setActionNotice] = useState<string | null>(null);

  // Form for entry
  const [formPlayer, setFormPlayer] = useState('⚡ViperSniper⚡ (Team Alpha)');
  const [formFfUid, setFormFfUid] = useState('839472819');
  const [formRank, setFormRank] = useState(1);
  const [formKills, setFormKills] = useState(9);
  const [formPoints, setFormPoints] = useState(29);
  const [formProof, setFormProof] = useState('https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800');
  const [formVerified, setFormVerified] = useState(true);

  useEffect(() => {
    loadData();
  }, [selectedTournamentId]);

  const loadData = async () => {
    const tList = await TournamentService.getTournaments();
    setTournaments(tList);
    const rList = await TournamentService.getResults(selectedTournamentId);
    setResults(rList);
  };

  const handleSaveResult = async (e: React.FormEvent) => {
    e.preventDefault();
    const newRes: MatchResult = {
      id: `res_${Date.now()}`,
      tournamentId: selectedTournamentId,
      userId: `usr_${formFfUid}`,
      playerName: formPlayer,
      ffUid: formFfUid,
      rank: Number(formRank),
      kills: Number(formKills),
      points: Number(formPoints),
      proofUrl: formProof,
      isVerified: formVerified,
      isPublished: false,
    };

    await TournamentService.saveResult(newRes);
    await loadData();
    setIsModalOpen(false);
    showNotice(`Scorecard for ${newRes.playerName} added.`);
  };

  const handlePublishAll = async () => {
    if (results.length === 0) return;
    if (
      confirm(
        'PUBLISH CERTIFIED RESULTS?\n\nThis will:\n1. Lock match results and mark tournament Completed.\n2. Update leaderboard standings.\n3. Atomically award Loyalty Coins to players based on Coin Rules.'
      )
    ) {
      setIsPublishing(true);
      try {
        await TournamentService.publishTournamentResults(
          selectedTournamentId,
          currentAdmin?.email || 'admin@ffarena.in'
        );
        await loadData();
        showNotice('🏆 Results published! Loyalty coins awarded and leaderboard updated.');
      } finally {
        setIsPublishing(false);
      }
    }
  };

  const showNotice = (msg: string) => {
    setActionNotice(msg);
    setTimeout(() => setActionNotice(null), 4000);
  };

  const currentTournament = tournaments.find((t) => t.id === selectedTournamentId);
  const allVerified = results.length > 0 && results.every((r) => r.isVerified);
  const isPublished = results.length > 0 && results.every((r) => r.isPublished);

  return (
    <div className="space-y-6">
      {actionNotice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] flex items-center gap-3 text-xs font-bold text-[#C8FF3D] animate-fade-in">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{actionNotice}</span>
        </div>
      )}

      {/* Header and Controls */}
      <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4">
        {/* Tournament Selector */}
        <div className="flex items-center gap-3">
          <select
            value={selectedTournamentId}
            onChange={(e) => setSelectedTournamentId(e.target.value)}
            className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-xs focus:outline-none min-w-[280px]"
          >
            {tournaments.map((t) => (
              <option key={t.id} value={t.id}>
                {t.name} ({t.status})
              </option>
            ))}
          </select>

          {isPublished ? (
            <Badge variant="lime">PUBLISHED & REWARDED</Badge>
          ) : allVerified ? (
            <Badge variant="violet">ALL REFEREE VERIFIED</Badge>
          ) : (
            <Badge variant="amber">PENDING VERIFICATION</Badge>
          )}
        </div>

        {/* Action Buttons */}
        <div className="flex items-center gap-3">
          <button
            onClick={() => setIsModalOpen(true)}
            className="inline-flex items-center justify-center gap-2 bg-[#14141F] hover:bg-[#23233A] text-[#F5F5FA] border border-[#23233A] font-bold px-4 py-2.5 rounded-[18px] text-xs transition-colors"
          >
            <Plus className="w-4 h-4 text-[#9498AE]" />
            <span>ENTER SCORECARD</span>
          </button>

          <button
            onClick={handlePublishAll}
            disabled={results.length === 0 || isPublished || isPublishing}
            className={`inline-flex items-center justify-center gap-2 font-extrabold px-5 py-2.5 rounded-[18px] text-xs transition-all ${
              isPublished
                ? 'bg-[#23233A] text-[#9498AE] cursor-not-allowed'
                : 'bg-[#C8FF3D] hover:bg-[#b8f02d] text-black shadow-md shadow-[#C8FF3D]/10'
            }`}
          >
            <Send className="w-4 h-4 stroke-2" />
            <span>{isPublished ? 'RESULTS PUBLISHED' : 'PUBLISH RESULTS & COINS'}</span>
          </button>
        </div>
      </div>

      {/* Results Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
        <div className="p-4 bg-[#0A0A0F] border-b border-[#23233A] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Award className="w-4 h-4 text-[#C8FF3D]" />
            <h3 className="font-extrabold text-[#F5F5FA] text-sm">
              Certified Match Placements ({results.length} Entries)
            </h3>
          </div>
          <span className="text-xs text-[#9498AE]">
            Publishing awards +200 Booyah coins, +10/kill, +40 match completion
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F]/50 text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
              <tr>
                <th className="px-6 py-4">Rank</th>
                <th className="px-4 py-4">Player / Team</th>
                <th className="px-4 py-4">Free Fire UID</th>
                <th className="px-4 py-4">Kills</th>
                <th className="px-4 py-4">Points</th>
                <th className="px-4 py-4">Proof Link</th>
                <th className="px-4 py-4">Referee Verification</th>
                <th className="px-6 py-4 text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {results.length === 0 ? (
                <tr>
                  <td colSpan={8} className="px-6 py-12 text-center text-[#9498AE]">
                    No scores entered yet for this tournament. Click "Enter Scorecard" to add verified match results.
                  </td>
                </tr>
              ) : (
                results.map((r) => (
                  <tr key={r.id} className="hover:bg-[#1A1A28]/50 transition-colors">
                    <td className="px-6 py-4 font-extrabold text-sm">
                      <span
                        className={
                          r.rank === 1
                            ? 'text-[#FFD700]'
                            : r.rank === 2
                            ? 'text-[#C0C0C0]'
                            : r.rank === 3
                            ? 'text-[#CD7F32]'
                            : 'text-[#F5F5FA]'
                        }
                      >
                        #{r.rank} {r.rank === 1 ? '🏆 Booyah' : ''}
                      </span>
                    </td>
                    <td className="px-4 py-4 font-bold text-[#F5F5FA]">{r.playerName}</td>
                    <td className="px-4 py-4 font-mono text-[#C8FF3D]">{r.ffUid}</td>
                    <td className="px-4 py-4 font-extrabold text-[#F5F5FA]">{r.kills} Kills</td>
                    <td className="px-4 py-4 font-extrabold text-[#7B5CFF]">{r.points} Pts</td>
                    <td className="px-4 py-4">
                      {r.proofUrl ? (
                        <a
                          href={r.proofUrl}
                          target="_blank"
                          rel="noreferrer"
                          className="inline-flex items-center gap-1 text-xs text-[#00E5FF] hover:underline"
                        >
                          View Proof <ExternalLink className="w-3 h-3" />
                        </a>
                      ) : (
                        <span className="text-gray-500">None</span>
                      )}
                    </td>
                    <td className="px-4 py-4">
                      {r.isVerified ? (
                        <span className="inline-flex items-center gap-1 text-[#C8FF3D] font-bold text-xs">
                          <CheckCircle className="w-3.5 h-3.5" /> Verified
                        </span>
                      ) : (
                        <span className="text-[#FFB300] font-bold text-xs">Unverified</span>
                      )}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <Badge variant={r.isPublished ? 'lime' : 'amber'}>
                        {r.isPublished ? 'Published' : 'Draft'}
                      </Badge>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Enter Result Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Enter Match Scorecard & Proof"
        subtitle="Verify match proof screenshot before certifying"
        maxWidth="max-w-md"
      >
        <form onSubmit={handleSaveResult} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
              Player / Squad Name *
            </label>
            <input
              type="text"
              required
              value={formPlayer}
              onChange={(e) => setFormPlayer(e.target.value)}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-sm focus:outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
              Free Fire Numerical UID *
            </label>
            <input
              type="text"
              required
              value={formFfUid}
              onChange={(e) => setFormFfUid(e.target.value)}
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-sm focus:outline-none font-mono"
            />
          </div>

          <div className="grid grid-cols-3 gap-3">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Rank</label>
              <input
                type="number"
                min="1"
                max="50"
                value={formRank}
                onChange={(e) => setFormRank(Number(e.target.value))}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-sm focus:outline-none"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Kills</label>
              <input
                type="number"
                min="0"
                value={formKills}
                onChange={(e) => setFormKills(Number(e.target.value))}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-sm focus:outline-none"
              />
            </div>
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Points</label>
              <input
                type="number"
                min="0"
                value={formPoints}
                onChange={(e) => setFormPoints(Number(e.target.value))}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-sm focus:outline-none"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
              Proof Link (Screenshot or Stream URL)
            </label>
            <input
              type="url"
              value={formProof}
              onChange={(e) => setFormProof(e.target.value)}
              placeholder="https://imgur.com/... or https://youtube.com/..."
              className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-sm focus:outline-none"
            />
          </div>

          <div className="flex items-center gap-2 pt-1">
            <input
              type="checkbox"
              id="verifiedChk"
              checked={formVerified}
              onChange={(e) => setFormVerified(e.target.checked)}
              className="rounded text-[#C8FF3D] focus:ring-0 bg-[#0A0A0F] border-[#2E2E4A]"
            />
            <label htmlFor="verifiedChk" className="text-xs font-bold text-[#F5F5FA]">
              Certified by Chief Esports Referee (Anti-cheat check passed)
            </label>
          </div>

          <div className="flex items-center justify-end space-x-3 pt-3 border-t border-[#23233A]">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2 text-xs font-bold text-[#9498AE]"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black hover:bg-[#b8f02d]"
            >
              SAVE SCORECARD
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
