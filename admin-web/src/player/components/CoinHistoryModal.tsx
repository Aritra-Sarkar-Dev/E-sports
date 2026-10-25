import React, { useState } from 'react';
import { ArrowLeft, Coins, ArrowUpRight, ArrowDownLeft, Filter, Sparkles, Info } from 'lucide-react';
import { CoinLedgerEntry } from '../types';
import { CoinService } from '../../services/coinService';

interface CoinHistoryModalProps {
  isOpen: boolean;
  onClose: () => void;
  currentBalance: number;
}

export const CoinHistoryModal: React.FC<CoinHistoryModalProps> = ({
  isOpen,
  onClose,
  currentBalance,
}) => {
  const [filter, setFilter] = useState<'all' | 'earned' | 'spent'>('all');
  const ledger = CoinService.getPlayerLedger();

  if (!isOpen) return null;

  const filteredEntries = ledger.filter((entry) => {
    if (filter === 'earned') return entry.amount > 0;
    if (filter === 'spent') return entry.amount < 0;
    return true;
  });

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
                <Coins className="w-4 h-4 text-[#FFB800]" /> Coin History & Ledger
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Loyalty rewards & cosmetic redemptions</p>
            </div>
          </div>
          <div className="flex items-center gap-1 px-3 py-1 bg-[#FFB800]/15 text-[#FFB800] border border-[#FFB800]/30 rounded-full text-xs font-black">
            <span>{currentBalance}</span>
            <Coins className="w-3.5 h-3.5" />
          </div>
        </div>

        {/* Informational Policy Banner */}
        <div className="p-3.5 bg-[#0A0A0F] border-b border-[#262640]">
          <div className="flex items-start gap-2 p-2.5 bg-[#1B1B2B] border border-[#262640] rounded-xl text-[10px] text-[#A7A7C0]">
            <Info className="w-3.5 h-3.5 text-[#FFB800] shrink-0 mt-0.5" />
            <span>
              Loyalty coins are non-purchasable, non-cashable virtual credits earned via streaks, matches, and missions. Redeemable only for cosmetic badges, avatar frames, and bonus tickets.
            </span>
          </div>

          {/* Filter Chips */}
          <div className="flex gap-2 mt-2.5">
            {(['all', 'earned', 'spent'] as const).map((mode) => (
              <button
                key={mode}
                onClick={() => setFilter(mode)}
                className={`flex-1 py-1.5 rounded-xl text-xs font-bold capitalize transition-all border ${
                  filter === mode
                    ? 'bg-[#FFB800] text-black border-[#FFB800] shadow-[0_0_10px_rgba(255,184,0,0.25)]'
                    : 'bg-[#14141F] text-[#A7A7C0] border-[#262640] hover:text-white'
                }`}
              >
                {mode === 'all' ? 'All Activity' : mode === 'earned' ? '+ Earned' : '- Spent'}
              </button>
            ))}
          </div>
        </div>

        {/* Ledger Entries List */}
        <div className="p-4 overflow-y-auto space-y-2.5 flex-1">
          {filteredEntries.length === 0 ? (
            <div className="py-12 text-center text-[#A7A7C0]">
              <Coins className="w-10 h-10 mx-auto text-[#262640] mb-2" />
              <p className="text-xs">No coin transactions found</p>
            </div>
          ) : (
            filteredEntries.map((item) => {
              const isEarn = item.amount > 0;
              return (
                <div
                  key={item.id}
                  className="p-3 rounded-2xl bg-[#1B1B2B] border border-[#262640] flex items-center justify-between transition-all hover:border-[#FFB800]/30"
                >
                  <div className="flex items-center gap-3">
                    <div
                      className={`w-9 h-9 rounded-xl flex items-center justify-center shrink-0 border ${
                        isEarn
                          ? 'bg-[#FFB800]/15 border-[#FFB800]/40 text-[#FFB800]'
                          : 'bg-[#7B5CFF]/15 border-[#7B5CFF]/40 text-[#7B5CFF]'
                      }`}
                    >
                      {isEarn ? (
                        <ArrowDownLeft className="w-4 h-4" />
                      ) : (
                        <ArrowUpRight className="w-4 h-4" />
                      )}
                    </div>
                    <div>
                      <h4 className="text-xs font-bold text-white line-clamp-1">{item.reason}</h4>
                      <div className="flex items-center gap-1.5 text-[10px] text-[#A7A7C0] mt-0.5">
                        <span className="capitalize">{item.type.replace('_', ' ')}</span>
                        <span>•</span>
                        <span>{item.date}</span>
                      </div>
                    </div>
                  </div>

                  <div className="text-right shrink-0">
                    <span
                      className={`text-sm font-black ${
                        isEarn ? 'text-[#FFB800]' : 'text-[#A7A7C0]'
                      }`}
                    >
                      {isEarn ? `+${item.amount}` : item.amount}
                    </span>
                    <span className="text-[9px] text-[#A7A7C0] block">Coins</span>
                  </div>
                </div>
              );
            })
          )}
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F] text-center">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-[#1B1B2B] hover:bg-[#262640] text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
          >
            Back to Profile
          </button>
        </div>
      </div>
    </div>
  );
};
