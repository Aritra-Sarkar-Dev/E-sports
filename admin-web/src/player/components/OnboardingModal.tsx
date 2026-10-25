import React, { useState } from 'react';
import { Trophy, ShieldCheck, Zap, Sparkles, ChevronRight, X } from 'lucide-react';

interface OnboardingModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const SLIDES = [
  {
    id: 1,
    title: '100% FREE ENTRY ESPORTS',
    tagline: 'Zero Deposits • Zero Wagers • Pure Skill',
    desc: 'Compete in official Free Fire MAX custom rooms across Solo, Duo, and Squad modes without paying a single rupee. No entry fees ever.',
    icon: Trophy,
    accent: '#C8FF3D',
    image: 'https://images.unsplash.com/photo-1542751371-adc38448a05e?w=700&auto=format&fit=crop&q=80',
  },
  {
    id: 2,
    title: 'SPONSOR-FUNDED PRIZES',
    tagline: 'Brand Trophies & Rewards',
    desc: 'All prize pools are 100% funded by gaming sponsors like ASUS ROG, Logitech G & Monster Energy. Win real awards and earn loyalty coins for every kill.',
    icon: Zap,
    accent: '#7B5CFF',
    image: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?w=700&auto=format&fit=crop&q=80',
  },
  {
    id: 3,
    title: 'FAIR PLAY & INSTANT ROOMS',
    tagline: 'Automated Telemetry & Anti-Cheat',
    desc: 'Mobile-only tournament verification with emulators strictly barred. Custom room ID & password unlock 15 minutes before kick-off directly in the app.',
    icon: ShieldCheck,
    accent: '#C8FF3D',
    image: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=700&auto=format&fit=crop&q=80',
  },
];

export const OnboardingModal: React.FC<OnboardingModalProps> = ({ isOpen, onClose }) => {
  const [currentSlide, setCurrentSlide] = useState(0);

  if (!isOpen) return null;

  const slide = SLIDES[currentSlide];
  const Icon = slide.icon;
  const isLast = currentSlide === SLIDES.length - 1;

  const handleNext = () => {
    if (isLast) {
      onClose();
    } else {
      setCurrentSlide((prev) => prev + 1);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/90 backdrop-blur-md p-4 animate-fade-in">
      <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[28px] overflow-hidden shadow-2xl flex flex-col relative">
        {/* Skip button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 z-20 px-3 py-1 bg-black/60 hover:bg-black/80 rounded-full text-[11px] font-bold text-[#A7A7C0] hover:text-white backdrop-blur-md transition-all"
        >
          Skip
        </button>

        {/* Hero image header with gradient */}
        <div className="relative h-48 overflow-hidden">
          <img
            src={slide.image}
            alt={slide.title}
            className="w-full h-full object-cover brightness-60 scale-105 transition-transform duration-700"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-[#14141F] via-[#14141F]/40 to-transparent" />

          {/* Glowing Icon */}
          <div className="absolute bottom-3 left-5 w-12 h-12 rounded-2xl bg-[#0A0A0F]/80 border border-[#262640] flex items-center justify-center backdrop-blur-md shadow-[0_0_20px_rgba(200,255,61,0.2)]">
            <Icon className="w-6 h-6 text-[#C8FF3D]" />
          </div>
        </div>

        {/* Body content */}
        <div className="p-5 pt-3 space-y-4">
          <div>
            <span className="text-[10px] font-extrabold uppercase tracking-wider text-[#C8FF3D] block mb-1">
              {slide.tagline}
            </span>
            <h3 className="text-lg font-black text-white leading-tight">
              {slide.title}
            </h3>
            <p className="text-xs text-[#A7A7C0] mt-2 leading-relaxed">
              {slide.desc}
            </p>
          </div>

          {/* Dots Indicator */}
          <div className="flex items-center gap-1.5 justify-center py-1">
            {SLIDES.map((s, idx) => (
              <button
                key={s.id}
                onClick={() => setCurrentSlide(idx)}
                className={`h-1.5 rounded-full transition-all ${
                  idx === currentSlide
                    ? 'w-6 bg-[#C8FF3D] shadow-[0_0_8px_rgba(200,255,61,0.4)]'
                    : 'w-1.5 bg-[#262640]'
                }`}
              />
            ))}
          </div>

          {/* Action button */}
          <button
            onClick={handleNext}
            className="w-full py-3 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_20px_rgba(200,255,61,0.25)] flex items-center justify-center gap-2 active:scale-95 transition-all"
          >
            <span>{isLast ? 'Get Started' : 'Continue'}</span>
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>
    </div>
  );
};
