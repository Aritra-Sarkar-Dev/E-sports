import React, { useState } from 'react';
import { ArrowLeft, HelpCircle, ChevronDown, ChevronUp, Send, CheckCircle2, MessageSquare, AlertCircle } from 'lucide-react';
import { PlayerMatch } from '../types';

interface SupportTicket {
  id: string;
  category: string;
  matchTitle: string;
  description: string;
  status: 'Open' | 'Investigating' | 'Resolved';
  time: string;
}

const INITIAL_FAQS = [
  {
    q: 'How do custom room matches work?',
    a: 'Once you reserve a free slot in an upcoming tournament, the custom room ID and password will automatically unlock inside the match screen 15 minutes before kick-off. Copy the credentials and enter the room in Free Fire MAX.',
  },
  {
    q: 'Are tournaments really 100% free to enter?',
    a: 'Yes, absolutely! There are no entry fees, deposits, or paywalls. All tournament prize pools are funded by commercial brand sponsors. Entry is strictly open and free.',
  },
  {
    q: 'Can I withdraw or cash out Loyalty Coins?',
    a: 'No. Loyalty Coins are virtual rewards earned strictly through gameplay (streaks, missions, matches). They cannot be bought, cannot be converted into cash, and cannot be withdrawn. They are only spendable on cosmetic items like profile frames, badges, and extra free-entry tickets.',
  },
  {
    q: 'What should I do if an opponent uses hacks or cheats?',
    a: 'Submit a ticket below with the room ID, approximate timestamp, and description. You can also paste an unlisted video recording link. Our tournament referee team will review telemetry and disqualify violators.',
  },
  {
    q: 'How are sponsor-funded prizes awarded?',
    a: 'Once referees verify official match standings and kill tallies, the results and sponsor prize records are published directly in the tournament lobby and your Prize History. The organizer contacts winners directly for physical kits or vouchers.',
  },
];

const INITIAL_TICKETS: SupportTicket[] = [
  {
    id: 'TCK-8492',
    category: 'Teaming / Fair Play',
    matchTitle: 'CYBERFRAY 2077 GRAND FINALS',
    description: 'Reported team collusion in circle 4 between player A and B.',
    status: 'Resolved',
    time: '2 days ago',
  },
  {
    id: 'TCK-5104',
    category: 'Room Password Assistance',
    matchTitle: 'SUNDAY ROOKIE SHOWDOWN',
    description: 'Room password delay reported before match kick-off.',
    status: 'Resolved',
    time: '4 days ago',
  },
];

interface SupportModalProps {
  isOpen: boolean;
  onClose: () => void;
  matches: PlayerMatch[];
  onShowToast: (type: 'success' | 'error' | 'info', message: string) => void;
}

export const SupportModal: React.FC<SupportModalProps> = ({
  isOpen,
  onClose,
  matches,
  onShowToast,
}) => {
  const [activeTab, setActiveTab] = useState<'faq' | 'ticket' | 'history'>('faq');
  const [openFaqIndex, setOpenFaqIndex] = useState<number | null>(0);

  // Ticket form state
  const [category, setCategory] = useState('Room Problem');
  const [matchId, setMatchId] = useState(matches[0]?.id || 'm1');
  const [roomId, setRoomId] = useState('');
  const [description, setDescription] = useState('');
  const [proofUrl, setProofUrl] = useState('');
  const [tickets, setTickets] = useState<SupportTicket[]>(INITIAL_TICKETS);

  if (!isOpen) return null;

  const handleSubmitTicket = (e: React.FormEvent) => {
    e.preventDefault();
    if (!description.trim()) {
      onShowToast('error', 'Please describe the problem.');
      return;
    }

    const selMatch = matches.find((m) => m.id === matchId);
    const newTicket: SupportTicket = {
      id: `TCK-${Math.floor(1000 + Math.random() * 9000)}`,
      category,
      matchTitle: selMatch ? selMatch.title : 'Custom Room',
      description,
      status: 'Open',
      time: 'Just now',
    };

    setTickets((prev) => [newTicket, ...prev]);
    setDescription('');
    setProofUrl('');
    setRoomId('');
    setActiveTab('history');
    onShowToast('success', `Ticket ${newTicket.id} submitted! Referees will investigate.`);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[92vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2.5">
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white transition-colors"
              title="Back"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
            <div>
              <h3 className="text-sm font-black text-white flex items-center gap-1.5">
                <HelpCircle className="w-4 h-4 text-[#00E5FF]" /> Help & Support
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Room assistance, dispute tickets & FAQs</p>
            </div>
          </div>
          <span className="px-2 py-0.5 bg-[#00E5FF]/15 text-[#00E5FF] font-black text-[9px] rounded-full border border-[#00E5FF]/30">
            24/7 ACTIVE
          </span>
        </div>

        {/* Sub-tabs */}
        <div className="flex border-b border-[#262640] bg-[#0A0A0F] p-1">
          <button
            onClick={() => setActiveTab('faq')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeTab === 'faq'
                ? 'bg-[#14141F] text-[#00E5FF] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            FAQ
          </button>
          <button
            onClick={() => setActiveTab('ticket')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeTab === 'ticket'
                ? 'bg-[#14141F] text-[#C8FF3D] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Submit Dispute
          </button>
          <button
            onClick={() => setActiveTab('history')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeTab === 'history'
                ? 'bg-[#14141F] text-[#7B5CFF] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            My Tickets ({tickets.length})
          </button>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto space-y-3 flex-1 text-xs">
          {activeTab === 'faq' && (
            <div className="space-y-2.5">
              {INITIAL_FAQS.map((faq, idx) => {
                const isOpen = openFaqIndex === idx;
                return (
                  <div
                    key={idx}
                    className="bg-[#1B1B2B] border border-[#262640] rounded-2xl overflow-hidden transition-all"
                  >
                    <button
                      onClick={() => setOpenFaqIndex(isOpen ? null : idx)}
                      className="w-full p-3.5 flex items-center justify-between text-left font-bold text-white hover:text-[#00E5FF] transition-colors"
                    >
                      <span className="pr-2">{faq.q}</span>
                      {isOpen ? (
                        <ChevronUp className="w-4 h-4 text-[#00E5FF] shrink-0" />
                      ) : (
                        <ChevronDown className="w-4 h-4 text-[#A7A7C0] shrink-0" />
                      )}
                    </button>
                    {isOpen && (
                      <div className="px-3.5 pb-3.5 pt-0 text-[11px] text-[#A7A7C0] leading-relaxed border-t border-[#262640]/50 mt-1">
                        {faq.a}
                      </div>
                    )}
                  </div>
                );
              })}
            </div>
          )}

          {activeTab === 'ticket' && (
            <form onSubmit={handleSubmitTicket} className="space-y-3">
              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Issue Category
                </label>
                <select
                  value={category}
                  onChange={(e) => setCategory(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  <option value="Room Problem">Room ID / Password Not Working</option>
                  <option value="Teaming / Fair Play">Suspected Hacker / Anti-Cheat Violation</option>
                  <option value="Incorrect Result">Result Placement Dispute</option>
                  <option value="Sponsor Prize Query">Sponsor Prize Fulfillment Inquiry</option>
                  <option value="Other">Other Match Issue</option>
                </select>
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Tournament
                </label>
                <select
                  value={matchId}
                  onChange={(e) => setMatchId(e.target.value)}
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                >
                  {matches.map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.title} ({m.mode} • {m.map})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Custom Room ID (Optional)
                </label>
                <input
                  type="text"
                  value={roomId}
                  onChange={(e) => setRoomId(e.target.value)}
                  placeholder="e.g. 884910"
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white font-mono focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Problem Description
                </label>
                <textarea
                  required
                  rows={3}
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Explain what happened in detail..."
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl p-3 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Video / Screenshot Proof URL (Optional)
                </label>
                <input
                  type="url"
                  value={proofUrl}
                  onChange={(e) => setProofUrl(e.target.value)}
                  placeholder="https://imgur.com/... or Google Drive link"
                  className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl px-3 py-2 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
                />
              </div>

              <button
                type="submit"
                className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 transition-all active:scale-95 mt-2"
              >
                <Send className="w-3.5 h-3.5" />
                <span>Submit Ticket to Referees</span>
              </button>
            </form>
          )}

          {activeTab === 'history' && (
            <div className="space-y-2.5">
              {tickets.length === 0 ? (
                <div className="py-12 text-center text-[#A7A7C0]">
                  <MessageSquare className="w-10 h-10 mx-auto text-[#262640] mb-2" />
                  <p className="text-xs">No active dispute tickets</p>
                </div>
              ) : (
                tickets.map((t) => (
                  <div
                    key={t.id}
                    className="p-3.5 bg-[#1B1B2B] border border-[#262640] rounded-2xl space-y-1.5"
                  >
                    <div className="flex items-center justify-between">
                      <span className="font-mono text-xs font-black text-white">{t.id}</span>
                      <span
                        className={`text-[9px] font-extrabold px-2 py-0.5 rounded-full border ${
                          t.status === 'Resolved'
                            ? 'bg-[#C8FF3D]/15 text-[#C8FF3D] border-[#C8FF3D]/30'
                            : t.status === 'Investigating'
                            ? 'bg-[#FFB800]/15 text-[#FFB800] border-[#FFB800]/30'
                            : 'bg-[#7B5CFF]/15 text-[#7B5CFF] border-[#7B5CFF]/30'
                        }`}
                      >
                        {t.status}
                      </span>
                    </div>

                    <div className="text-[11px] font-bold text-white">{t.matchTitle}</div>
                    <p className="text-[10px] text-[#A7A7C0] leading-relaxed">{t.description}</p>
                    <div className="flex items-center justify-between text-[9px] text-[#A7A7C0] pt-1 border-t border-[#262640]/40">
                      <span>{t.category}</span>
                      <span>{t.time}</span>
                    </div>
                  </div>
                ))
              )}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2 bg-[#1B1B2B] hover:bg-[#262640] text-[#A7A7C0] hover:text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
          >
            Close Support
          </button>
        </div>
      </div>
    </div>
  );
};
