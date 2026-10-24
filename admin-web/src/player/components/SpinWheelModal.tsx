import React, { useState } from 'react';
import { X, Sparkles, Trophy } from 'lucide-react';

interface SpinWheelModalProps {
  onClose: () => void;
  onWin: (coins: number, label: string) => void;
  hasSpunToday: boolean;
}

const SEGMENTS = [
  { label: '25 Coins', coins: 25, color: '#7B5CFF' },
  { label: '50 Coins', coins: 50, color: '#C8FF3D' },
  { label: '10 Coins', coins: 10, color: '#FF6A00' },
  { label: '100 Coins', coins: 100, color: '#FFB300' },
  { label: '30 Coins', coins: 30, color: '#00E5FF' },
  { label: '200 Coins', coins: 200, color: '#E040FB' },
  { label: '15 Coins', coins: 15, color: '#FF5252' },
  { label: '75 Coins', coins: 75, color: '#76FF03' },
];

export const SpinWheelModal: React.FC<SpinWheelModalProps> = ({
  onClose,
  onWin,
  hasSpunToday,
}) => {
  const [rotation, setRotation] = useState<number>(0);
  const [isSpinning, setIsSpinning] = useState(false);
  const [wonPrize, setWonPrize] = useState<{ label: string; coins: number } | null>(null);

  const handleSpin = () => {
    if (isSpinning || hasSpunToday) return;

    setIsSpinning(true);
    setWonPrize(null);

    // Pick random prize index
    const prizeIndex = Math.floor(Math.random() * SEGMENTS.length);
    const selected = SEGMENTS[prizeIndex];

    const segmentAngle = 360 / SEGMENTS.length;
    // 5 full rotations + landing angle
    const targetRotation = rotation + 1800 + (360 - (prizeIndex * segmentAngle + segmentAngle / 2));

    setRotation(targetRotation);

    setTimeout(() => {
      setIsSpinning(false);
      setWonPrize(selected);
      onWin(selected.coins, selected.label);
    }, 4500);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4">
      <div className="w-full max-w-sm bg-[#14141F] border border-[#23233A] rounded-[24px] overflow-hidden p-5 flex flex-col items-center text-center shadow-2xl relative">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="w-10 h-10 rounded-full bg-[#FFB300]/20 flex items-center justify-center text-[#FFB300] mb-2">
          <Sparkles className="w-5 h-5" />
        </div>

        <h3 className="text-lg font-black text-white">Daily Fortune Wheel</h3>
        <p className="text-xs text-[#9498AE] max-w-xs mb-4">
          Spin once daily to score loyalty coins, tickets, and badges.
        </p>

        {/* Wheel container */}
        <div className="relative w-64 h-64 my-2 flex items-center justify-center">
          {/* Wheel pointer top indicator */}
          <div className="absolute top-0 left-1/2 -translate-x-1/2 -translate-y-2 z-20 w-0 h-0 border-l-[12px] border-l-transparent border-r-[12px] border-r-transparent border-t-[20px] border-t-[#C8FF3D] filter drop-shadow-[0_2px_8px_rgba(200,255,61,0.8)]" />

          {/* Rotating wheel circle */}
          <div
            className="w-full h-full rounded-full border-4 border-[#23233A] overflow-hidden relative shadow-[0_0_30px_rgba(123,92,255,0.3)] transition-transform ease-out"
            style={{
              transform: `rotate(${rotation}deg)`,
              transitionDuration: isSpinning ? '4.5s' : '0s',
            }}
          >
            {SEGMENTS.map((seg, idx) => {
              const angle = (360 / SEGMENTS.length) * idx;
              return (
                <div
                  key={idx}
                  className="absolute w-1/2 h-full right-0 top-0 origin-left flex items-center justify-end pr-3"
                  style={{
                    transform: `rotate(${angle}deg)`,
                    backgroundColor: idx % 2 === 0 ? '#1C1C2E' : '#14141F',
                  }}
                >
                  <span
                    className="text-[11px] font-extrabold uppercase tracking-tight"
                    style={{ color: seg.color }}
                  >
                    {seg.label}
                  </span>
                </div>
              );
            })}
          </div>

          {/* Wheel Center Button */}
          <div className="absolute z-10 w-14 h-14 rounded-full bg-[#0A0A0F] border-2 border-[#C8FF3D] flex items-center justify-center shadow-lg">
            <Trophy className="w-6 h-6 text-[#C8FF3D]" />
          </div>
        </div>

        {/* Reward celebration banner */}
        {wonPrize && (
          <div className="w-full mt-4 p-3 bg-[#1C1C2E] border border-[#C8FF3D]/40 rounded-2xl animate-bounce-in">
            <span className="text-xs text-[#9498AE] block">Congratulations! You Won</span>
            <span className="text-lg font-black text-[#C8FF3D] block">{wonPrize.label}</span>
          </div>
        )}

        {/* Spin CTA */}
        <div className="w-full mt-5">
          <button
            onClick={handleSpin}
            disabled={isSpinning || hasSpunToday}
            className={`w-full py-3.5 rounded-2xl font-extrabold text-sm flex items-center justify-center gap-2 transition-all ${
              hasSpunToday
                ? 'bg-[#1C1C2E] text-slate-500 border border-[#23233A] cursor-not-allowed'
                : isSpinning
                ? 'bg-[#C8FF3D]/50 text-black cursor-wait'
                : 'bg-[#C8FF3D] hover:bg-[#b5f02c] text-black shadow-[0_0_20px_rgba(200,255,61,0.35)] active:scale-95'
            }`}
          >
            {isSpinning ? 'Spinning the Wheel...' : hasSpunToday ? 'Come Back Tomorrow!' : 'SPIN FOR FREE'}
          </button>
        </div>
      </div>
    </div>
  );
};
