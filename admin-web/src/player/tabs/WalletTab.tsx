import React, { useState } from 'react';
import { WalletBalance, WalletTransaction, TransactionType } from '../types';
import {
  Wallet,
  ArrowDownLeft,
  ArrowUpRight,
  ShieldCheck,
  CheckCircle2,
  Clock,
  Sparkles,
  Filter
} from 'lucide-react';

interface WalletTabProps {
  wallet: WalletBalance;
  transactions: WalletTransaction[];
  isKycVerified: boolean;
  onOpenAddMoney: () => void;
  onOpenWithdraw: () => void;
}

export const WalletTab: React.FC<WalletTabProps> = ({
  wallet,
  transactions,
  isKycVerified,
  onOpenAddMoney,
  onOpenWithdraw,
}) => {
  const [selectedFilter, setSelectedFilter] = useState<'All' | TransactionType>('All');

  const filteredTransactions = transactions.filter((tx) => {
    if (selectedFilter === 'All') return true;
    return tx.type === selectedFilter;
  });

  return (
    <div className="space-y-4 pb-12 animate-fade-in">
      {/* Header */}
      <div className="px-4 pt-3 pb-1">
        <h2 className="text-lg font-black text-white flex items-center gap-2">
          <Wallet className="w-5 h-5 text-[#C8FF3D]" /> Cash Wallet
        </h2>
        <p className="text-xs text-[#9498AE]">Manage tournament deposits, winnings & UPI payouts</p>
      </div>

      {/* 3-Pool Balance Hero Card */}
      <div className="px-4">
        <div className="relative rounded-[24px] overflow-hidden bg-gradient-to-br from-[#1C1C2E] via-[#17113D] to-[#0A0A0F] border border-[#7B5CFF]/40 p-5 shadow-2xl">
          {/* Background glowing orb */}
          <div className="absolute top-0 right-0 w-32 h-32 bg-[#C8FF3D]/10 rounded-full blur-2xl pointer-events-none" />

          {/* Total balance row */}
          <div className="flex items-center justify-between mb-4">
            <div>
              <span className="text-[11px] text-[#9498AE] uppercase tracking-wider block">
                Total Wallet Balance
              </span>
              <div className="text-3xl font-black text-white tracking-tight flex items-center gap-1">
                <span>₹{wallet.total}</span>
                <Sparkles className="w-4 h-4 text-[#C8FF3D]" />
              </div>
            </div>

            <div className="flex items-center gap-1.5 px-2.5 py-1 bg-white/5 border border-white/10 rounded-full text-[10px] font-bold text-[#C8FF3D]">
              <ShieldCheck className="w-3.5 h-3.5" /> 100% Safe Escrow
            </div>
          </div>

          {/* 3 Separated Pools Grid */}
          <div className="grid grid-cols-3 gap-2 py-3 px-2 bg-[#0A0A0F]/60 rounded-2xl border border-white/5 text-center mb-5">
            {/* Deposit */}
            <div>
              <span className="text-[10px] text-[#9498AE] block">Deposit</span>
              <span className="text-sm font-extrabold text-white">₹{wallet.deposit}</span>
              <span className="text-[8px] text-[#9498AE] block">For entries</span>
            </div>

            {/* Winnings (Golden withdrawable) */}
            <div className="border-x border-[#23233A]">
              <span className="text-[10px] text-[#FFB300] font-bold block">Winnings</span>
              <span className="text-sm font-black text-[#FFB300]">₹{wallet.winnings}</span>
              <span className="text-[8px] text-[#C8FF3D] font-bold block">Withdrawable</span>
            </div>

            {/* Bonus */}
            <div>
              <span className="text-[10px] text-[#9498AE] block">Bonus</span>
              <span className="text-sm font-extrabold text-[#7B5CFF]">₹{wallet.bonus}</span>
              <span className="text-[8px] text-[#9498AE] block">Check-ins</span>
            </div>
          </div>

          {/* CTA Buttons: Add Cash & Withdraw */}
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={onOpenAddMoney}
              className="py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 active:scale-95 transition-all"
            >
              <ArrowDownLeft className="w-4 h-4" /> Add Cash
            </button>

            <button
              onClick={onOpenWithdraw}
              className="py-3 bg-[#1C1C2E] hover:bg-[#23233A] text-white border border-[#23233A] font-extrabold text-xs rounded-xl flex items-center justify-center gap-1.5 active:scale-95 transition-all"
            >
              <ArrowUpRight className="w-4 h-4 text-[#FF6A00]" /> Withdraw UPI
            </button>
          </div>
        </div>
      </div>

      {/* KYC Status Strip */}
      <div className="px-4">
        <div className="flex items-center justify-between p-3 bg-[#14141F] border border-[#23233A] rounded-2xl text-xs">
          <div className="flex items-center gap-2.5">
            <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
            <div>
              <span className="text-white font-bold block text-xs">KYC Verification</span>
              <span className="text-[10px] text-[#9498AE]">Linked UPI: aritra@oksbi</span>
            </div>
          </div>
          <span className="px-2 py-0.5 bg-[#C8FF3D]/15 text-[#C8FF3D] rounded-full text-[10px] font-black">
            VERIFIED
          </span>
        </div>
      </div>

      {/* Transaction History */}
      <div className="px-4 space-y-3 pt-2">
        <div className="flex items-center justify-between">
          <h3 className="text-xs font-black text-white uppercase tracking-wider flex items-center gap-1.5">
            <Clock className="w-4 h-4 text-[#9498AE]" /> Transaction Log
          </h3>
          <span className="text-[11px] text-[#9498AE]">
            {filteredTransactions.length} records
          </span>
        </div>

        {/* Filter Pills */}
        <div className="flex gap-1.5 overflow-x-auto no-scrollbar pb-1 text-xs">
          {[
            { id: 'All', label: 'All' },
            { id: 'deposit', label: 'Deposits' },
            { id: 'winning', label: 'Winnings' },
            { id: 'entry_fee', label: 'Entry Fees' },
            { id: 'withdrawal', label: 'Withdrawals' },
          ].map((f) => (
            <button
              key={f.id}
              onClick={() => setSelectedFilter(f.id as any)}
              className={`px-3 py-1.5 rounded-xl font-bold whitespace-nowrap transition-all border ${
                selectedFilter === f.id
                  ? 'bg-[#C8FF3D] text-black border-[#C8FF3D] shadow-[0_0_10px_rgba(200,255,61,0.2)]'
                  : 'bg-[#14141F] text-[#9498AE] border-[#23233A] hover:text-white'
              }`}
            >
              {f.label}
            </button>
          ))}
        </div>

        {/* List of Transactions */}
        <div className="space-y-2">
          {filteredTransactions.map((tx) => {
            const isPositive = tx.amount > 0;
            return (
              <div
                key={tx.id}
                className="flex items-center justify-between p-3.5 bg-[#14141F] border border-[#23233A] rounded-2xl hover:border-slate-600 transition-all"
              >
                <div className="flex items-center gap-3">
                  <div
                    className={`w-9 h-9 rounded-2xl flex items-center justify-center shrink-0 ${
                      isPositive
                        ? 'bg-[#C8FF3D]/15 text-[#C8FF3D]'
                        : 'bg-[#FF6A00]/15 text-[#FF6A00]'
                    }`}
                  >
                    {isPositive ? <ArrowDownLeft className="w-4 h-4" /> : <ArrowUpRight className="w-4 h-4" />}
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-white line-clamp-1">{tx.title}</h4>
                    <span className="text-[10px] text-[#9498AE]">{tx.date}</span>
                  </div>
                </div>

                <div className="text-right shrink-0">
                  <span
                    className={`text-xs font-black block ${
                      isPositive ? 'text-[#C8FF3D]' : 'text-[#FF7A8A]'
                    }`}
                  >
                    {isPositive ? `+₹${tx.amount}` : `-₹${Math.abs(tx.amount)}`}
                  </span>
                  <span className="text-[9px] text-[#9498AE] font-mono">{tx.status}</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
