import React, { useState } from 'react';
import { X, CheckCircle2, Shield, Sparkles } from 'lucide-react';

interface AddMoneyModalProps {
  onClose: () => void;
  onSuccess: (amount: number, upiApp: string) => void;
}

export const AddMoneyModal: React.FC<AddMoneyModalProps> = ({ onClose, onSuccess }) => {
  const [amount, setAmount] = useState<number>(100);
  const [selectedApp, setSelectedApp] = useState<'Google Pay' | 'PhonePe' | 'Paytm' | 'BHIM'>('Google Pay');
  const [isProcessing, setIsProcessing] = useState(false);

  const presets = [50, 100, 200, 500];

  const handlePay = () => {
    if (amount < 10) return;
    setIsProcessing(true);
    setTimeout(() => {
      setIsProcessing(false);
      onSuccess(amount, selectedApp);
    }, 1000);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/80 backdrop-blur-sm p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between bg-[#0E0E17]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-[#C8FF3D]/10 flex items-center justify-center text-[#C8FF3D]">
              <Sparkles className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-bold text-white">Add Cash to Wallet</h3>
              <p className="text-[11px] text-[#9498AE]">Instant UPI deposit • 100% Secure</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-4 space-y-4 overflow-y-auto">
          {/* Amount Input */}
          <div className="bg-[#1C1C2E] border border-[#23233A] p-4 rounded-2xl text-center">
            <span className="text-[11px] text-[#9498AE] uppercase tracking-wider block mb-1">
              Deposit Amount
            </span>
            <div className="flex items-center justify-center gap-1">
              <span className="text-2xl font-extrabold text-[#C8FF3D]">₹</span>
              <input
                type="number"
                min="10"
                max="10000"
                value={amount}
                onChange={(e) => setAmount(Number(e.target.value))}
                className="w-32 bg-transparent text-3xl font-black text-white text-center focus:outline-none"
              />
            </div>

            {/* Presets */}
            <div className="grid grid-cols-4 gap-2 mt-4">
              {presets.map((preset) => (
                <button
                  key={preset}
                  type="button"
                  onClick={() => setAmount(preset)}
                  className={`py-1.5 text-xs font-bold rounded-xl border transition-all ${
                    amount === preset
                      ? 'bg-[#C8FF3D] text-black border-[#C8FF3D]'
                      : 'bg-[#0E0E17] text-white border-[#23233A] hover:border-slate-500'
                  }`}
                >
                  +₹{preset}
                </button>
              ))}
            </div>
          </div>

          {/* UPI App selector */}
          <div className="space-y-2">
            <span className="text-xs font-semibold text-[#9498AE] block">Select UPI Payment Method</span>
            <div className="grid grid-cols-2 gap-2.5">
              {(['Google Pay', 'PhonePe', 'Paytm', 'BHIM'] as const).map((app) => (
                <button
                  key={app}
                  type="button"
                  onClick={() => setSelectedApp(app)}
                  className={`flex items-center gap-2.5 p-3 rounded-2xl border text-left transition-all ${
                    selectedApp === app
                      ? 'bg-[#1C1C2E] border-[#C8FF3D] shadow-[0_0_15px_rgba(200,255,61,0.15)]'
                      : 'bg-[#0E0E17] border-[#23233A] hover:border-slate-600'
                  }`}
                >
                  <div className="w-7 h-7 rounded-full bg-white/10 flex items-center justify-center font-bold text-xs text-white">
                    {app[0]}
                  </div>
                  <div className="flex-1">
                    <span className="text-xs font-bold text-white block">{app}</span>
                    <span className="text-[10px] text-[#9498AE]">Instant UPI</span>
                  </div>
                  {selectedApp === app && <CheckCircle2 className="w-4 h-4 text-[#C8FF3D] shrink-0" />}
                </button>
              ))}
            </div>
          </div>

          {/* Security guarantee */}
          <div className="flex items-center gap-2 p-3 bg-[#1C1C2E]/60 rounded-xl text-xs text-[#9498AE]">
            <Shield className="w-4 h-4 text-[#C8FF3D] shrink-0" />
            <span>256-bit encrypted bank checkout. Funds reflect instantly.</span>
          </div>

          {/* Pay Button */}
          <button
            type="button"
            disabled={amount < 10 || isProcessing}
            onClick={handlePay}
            className={`w-full py-3.5 rounded-2xl font-extrabold text-sm flex items-center justify-center gap-2 transition-all ${
              amount < 10
                ? 'bg-[#1C1C2E] text-slate-500 cursor-not-allowed border border-[#23233A]'
                : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_20px_rgba(200,255,61,0.35)] active:scale-[0.98]'
            }`}
          >
            {isProcessing ? (
              <div className="w-5 h-5 border-2 border-black border-t-transparent rounded-full animate-spin" />
            ) : (
              <span>Proceed to Pay ₹{amount} via {selectedApp}</span>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};
