import React, { useState } from 'react';
import { X, CheckCircle2, AlertCircle, ArrowUpRight } from 'lucide-react';
import { WalletBalance } from '../types';

interface WithdrawModalProps {
  wallet: WalletBalance;
  linkedUpi: string;
  isKycVerified: boolean;
  onClose: () => void;
  onSuccess: (amount: number, upiId: string) => void;
}

export const WithdrawModal: React.FC<WithdrawModalProps> = ({
  wallet,
  linkedUpi,
  isKycVerified,
  onClose,
  onSuccess,
}) => {
  const [amount, setAmount] = useState<number>(Math.min(wallet.winnings, 200));
  const [upiId, setUpiId] = useState(linkedUpi);
  const [isProcessing, setIsProcessing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const minWithdrawal = 100;

  const handleWithdraw = (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    if (amount < minWithdrawal) {
      setError(`Minimum withdrawal is ₹${minWithdrawal}.`);
      return;
    }

    if (amount > wallet.winnings) {
      setError(`You can only withdraw from your Winnings balance (₹${wallet.winnings}).`);
      return;
    }

    if (!upiId.includes('@')) {
      setError('Please enter a valid UPI VPA address (e.g. name@oksbi).');
      return;
    }

    setIsProcessing(true);
    setTimeout(() => {
      setIsProcessing(false);
      onSuccess(amount, upiId);
    }, 1200);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/80 backdrop-blur-sm p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between bg-[#0E0E17]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#FF6A00]/20 flex items-center justify-center text-[#FF6A00]">
              <ArrowUpRight className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-bold text-white">Withdraw Winnings</h3>
              <p className="text-[11px] text-[#9498AE]">Instant transfer directly to your UPI ID</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleWithdraw} className="p-4 space-y-4 overflow-y-auto">
          {/* KYC Status Banner */}
          <div className="p-3 bg-[#1C1C2E] border border-[#23233A] rounded-2xl flex items-center justify-between text-xs">
            <div className="flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 text-[#C8FF3D]" />
              <span className="text-white font-medium">KYC Verification:</span>
            </div>
            <span className={`px-2 py-0.5 rounded-full text-[10px] font-bold ${isKycVerified ? 'bg-[#C8FF3D]/20 text-[#C8FF3D]' : 'bg-yellow-500/20 text-yellow-400'}`}>
              {isKycVerified ? 'Verified & Active' : 'Pending Verification'}
            </span>
          </div>

          {/* Withdrawable Balance Box */}
          <div className="bg-[#1C1C2E] border border-[#23233A] p-4 rounded-2xl text-center">
            <span className="text-[11px] text-[#9498AE] uppercase tracking-wider block mb-1">
              Withdrawable Winnings Balance
            </span>
            <div className="text-2xl font-black text-[#FFB300]">₹{wallet.winnings}</div>
            <p className="text-[11px] text-[#9498AE] mt-1">
              Minimum payout: ₹{minWithdrawal} • No TDS deduction on ₹0-₹10,000 net winnings
            </p>
          </div>

          {/* Amount input */}
          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">
              Amount to Withdraw (₹)
            </label>
            <input
              type="number"
              min={minWithdrawal}
              max={wallet.winnings}
              value={amount}
              onChange={(e) => setAmount(Number(e.target.value))}
              className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-sm font-bold text-white focus:outline-none focus:border-[#C8FF3D]"
              placeholder="e.g. 200"
            />
          </div>

          {/* UPI Address input */}
          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">
              Destination UPI ID (VPA)
            </label>
            <input
              type="text"
              required
              value={upiId}
              onChange={(e) => setUpiId(e.target.value)}
              className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-sm font-mono text-white focus:outline-none focus:border-[#C8FF3D]"
              placeholder="e.g. yourname@oksbi"
            />
          </div>

          {error && (
            <div className="p-3 bg-red-500/10 border border-red-500/30 rounded-xl flex items-center gap-2 text-xs text-red-400">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          {/* Action */}
          <div className="pt-2">
            <button
              type="submit"
              disabled={wallet.winnings < minWithdrawal || isProcessing}
              className={`w-full py-3.5 rounded-2xl font-extrabold text-sm flex items-center justify-center gap-2 transition-all ${
                wallet.winnings < minWithdrawal
                  ? 'bg-[#1C1C2E] text-slate-500 cursor-not-allowed border border-[#23233A]'
                  : 'bg-[#FF6A00] hover:bg-[#e05e00] text-white shadow-[0_0_20px_rgba(255,106,0,0.35)] active:scale-[0.98]'
              }`}
            >
              {isProcessing ? (
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : (
                <span>Confirm Instant Payout of ₹{amount}</span>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
