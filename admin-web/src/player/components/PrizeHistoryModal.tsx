import React from 'react';
import { ArrowLeft, Trophy, Award, CheckCircle2, ShieldCheck, Info } from 'lucide-react';

export interface SponsorPrizeItem {
  id: string;
  tournamentTitle: string;
  rank: number;
  prizeName: string;
  date: string;
  sponsorName: string;
  status: 'Recorded';
  details: string;
}

export const INITIAL_SPONSOR_PRIZES: SponsorPrizeItem[] = [
  {
    id: 'sp_1',
    tournamentTitle: 'CYBERFRAY 2077 GRAND FINALS',
    rank: 1,
    prizeName: 'Logitech G Pro X Headset (Sponsor-funded)',
    date: 'Oct 22, 2026',
    sponsorName: 'Asus ROG Gaming',
    status: 'Recorded',
    details: 'Booyah Champion placement verified by tournament ref.',
  },
  {
    id: 'sp_2',
    tournamentTitle: 'CLASH SQUAD 4v4 INVITATIONAL',
    rank: 2,
    prizeName: '₹3,500 Amazon Voucher (Sponsor-funded)',
    date: 'Oct 18, 2026',
    sponsorName: 'Monster Energy',
    status: 'Recorded',
    details: 'Runner-up placement in Bo7 grand finals.',
  },
  {
    id: 'sp_3',
    tournamentTitle: 'SUNDAY ROOKIE SHOWDOWN',
    rank: 1,
    prizeName: 'Booyah Pass Season 12 (Sponsor-funded)',
    date: 'Oct 14, 2026',
    sponsorName: 'Red Bull Esports',
    status: 'Recorded',
    details: '1st place in Solo Bermuda bracket.',
  },
  {
    id: 'sp_4',
    tournamentTitle: 'NIGHTFALL SQUAD ELITE CUP',
    rank: 3,
    prizeName: 'HyperX Pulsefire Gaming Mouse (Sponsor-funded)',
    date: 'Oct 09, 2026',
    sponsorName: 'HyperX India',
    status: 'Recorded',
    details: 'Podium 3rd finish with 14 squad eliminations.',
  },
  {
    id: 'sp_5',
    tournamentTitle: 'KALAHARI RUMBLE SOLO WOLF',
    rank: 1,
    prizeName: 'Custom Jersey & Pro Kit (Sponsor-funded)',
    date: 'Sep 29, 2026',
    sponsorName: 'Puma Esports',
    status: 'Recorded',
    details: '1v1 Lone Wolf tournament champion.',
  },
];

interface PrizeHistoryModalProps {
  isOpen: boolean;
  onClose: () => void;
  prizes?: SponsorPrizeItem[];
}

export const PrizeHistoryModal: React.FC<PrizeHistoryModalProps> = ({
  isOpen,
  onClose,
  prizes = INITIAL_SPONSOR_PRIZES,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
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
                <Trophy className="w-4 h-4 text-[#C8FF3D]" /> Prize History
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Official record of sponsor-funded prizes won</p>
            </div>
          </div>
          <span className="px-2.5 py-1 bg-[#C8FF3D]/15 text-[#C8FF3D] font-black text-[10px] rounded-full border border-[#C8FF3D]/30">
            {prizes.length} Recorded
          </span>
        </div>

        {/* Informational Policy Notice */}
        <div className="p-4 bg-[#0A0A0F] border-b border-[#262640]">
          <div className="flex items-start gap-2.5 p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl text-[11px] text-[#A7A7C0] leading-relaxed">
            <Info className="w-4 h-4 text-[#7B5CFF] shrink-0 mt-0.5" />
            <span>
              All tournaments are <strong className="text-white">100% Free Entry</strong>. Sponsor-funded prizes are officially recorded by tournament organizers upon result verification. There is zero real-money deposit, withdrawal, or cash wagering in this platform.
            </span>
          </div>
        </div>

        {/* Prizes List */}
        <div className="p-4 overflow-y-auto space-y-3 flex-1">
          {prizes.map((item) => (
            <div
              key={item.id}
              className="bg-[#1B1B2B] border border-[#262640] hover:border-[#C8FF3D]/40 rounded-2xl p-3.5 transition-all space-y-2 group"
            >
              <div className="flex items-start justify-between">
                <div>
                  <div className="flex items-center gap-2">
                    <span className="text-xs font-black text-white group-hover:text-[#C8FF3D] transition-colors">
                      {item.tournamentTitle}
                    </span>
                  </div>
                  <span className="text-[10px] text-[#A7A7C0] block mt-0.5">
                    Sponsored by {item.sponsorName} • {item.date}
                  </span>
                </div>
                <div className="flex items-center gap-1.5 shrink-0">
                  <span className="text-xs">
                    {item.rank === 1 ? '🥇' : item.rank === 2 ? '🥈' : '🥉'}
                  </span>
                  <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] font-black text-[10px] rounded-full border border-[#C8FF3D]/30">
                    Rank #{item.rank}
                  </span>
                </div>
              </div>

              <div className="p-2.5 bg-[#0A0A0F] rounded-xl border border-[#262640] flex items-center justify-between">
                <div>
                  <span className="text-[9px] text-[#A7A7C0] uppercase tracking-wider block">
                    Sponsor Prize Awarded
                  </span>
                  <span className="text-xs font-black text-white">{item.prizeName}</span>
                </div>
                <span className="flex items-center gap-1 text-[10px] text-[#C8FF3D] font-extrabold bg-[#C8FF3D]/10 px-2 py-0.5 rounded-full border border-[#C8FF3D]/20">
                  <CheckCircle2 className="w-3 h-3" />
                  {item.status}
                </span>
              </div>

              <p className="text-[10px] text-[#A7A7C0] italic">
                {item.details}
              </p>
            </div>
          ))}
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F] text-center">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-[#1B1B2B] hover:bg-[#262640] text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
          >
            Close Prize History
          </button>
        </div>
      </div>
    </div>
  );
};
