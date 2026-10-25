import React, { useState } from 'react';
import { PlayerProfile } from '../types';
import { AuthService } from '../../services/authService';
import { ShieldCheck, User, Mail, Lock, Gamepad2, Hash, X, Check, ArrowRight } from 'lucide-react';

interface AuthModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (user: PlayerProfile) => void;
}

export const AuthModal: React.FC<AuthModalProps> = ({ isOpen, onClose, onSuccess }) => {
  const [mode, setMode] = useState<'login' | 'signup'>('login');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [inGameName, setInGameName] = useState('');
  const [ffUid, setFfUid] = useState('');
  const [accepted18Plus, setAccepted18Plus] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (mode === 'login') {
        if (!email) throw new Error('Please enter your email.');
        const user = await AuthService.login(email, password);
        onSuccess(user);
        onClose();
      } else {
        if (!name || !email || !inGameName || !ffUid) {
          throw new Error('Please complete all required profile fields.');
        }
        if (!accepted18Plus) {
          throw new Error('You must confirm 18+ age and agree to fair-play rules.');
        }
        const user = await AuthService.signup({
          name,
          email,
          password,
          inGameName,
          ffUid,
        });
        onSuccess(user);
        onClose();
      }
    } catch (err: any) {
      setError(err.message || 'Authentication failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDemoLogin = async () => {
    setLoading(true);
    try {
      const user = await AuthService.login('aritra@esports.gg', 'demo123');
      onSuccess(user);
      onClose();
    } catch (err: any) {
      setError('Demo login failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4 animate-fade-in">
      <div className="w-full max-w-sm bg-[#14141F] border border-[#262640] rounded-[28px] overflow-hidden shadow-2xl flex flex-col relative max-h-[92vh]">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-xl bg-[#C8FF3D]/20 border border-[#C8FF3D]/40 flex items-center justify-center text-[#C8FF3D]">
              <Gamepad2 className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-black text-white">
                {mode === 'login' ? 'Player Sign In' : 'Create Player Account'}
              </h3>
              <span className="text-[10px] text-[#C8FF3D] font-bold block">
                Free-Entry Esports Hub
              </span>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Tab switch */}
        <div className="flex border-b border-[#262640] bg-[#0A0A0F] p-1">
          <button
            type="button"
            onClick={() => { setMode('login'); setError(null); }}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              mode === 'login'
                ? 'bg-[#14141F] text-[#C8FF3D] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Log In
          </button>
          <button
            type="button"
            onClick={() => { setMode('signup'); setError(null); }}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              mode === 'signup'
                ? 'bg-[#14141F] text-[#C8FF3D] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            New Account
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-4 space-y-3 overflow-y-auto flex-1">
          {error && (
            <div className="p-2.5 bg-red-500/10 border border-red-500/30 rounded-xl text-xs text-red-400">
              {error}
            </div>
          )}

          {mode === 'signup' && (
            <>
              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Full Name
                </label>
                <div className="relative">
                  <User className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#A7A7C0]" />
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="e.g. Aritra Sarkar"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
                  />
                </div>
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Free Fire In-Game Name (IGN)
                </label>
                <div className="relative">
                  <Gamepad2 className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#C8FF3D]" />
                  <input
                    type="text"
                    required
                    value={inGameName}
                    onChange={(e) => setInGameName(e.target.value)}
                    placeholder="e.g. ⚡NEON_SOUL⚡"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
                  />
                </div>
              </div>

              <div>
                <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
                  Free Fire Numeric UID
                </label>
                <div className="relative">
                  <Hash className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#7B5CFF]" />
                  <input
                    type="text"
                    required
                    value={ffUid}
                    onChange={(e) => setFfUid(e.target.value)}
                    placeholder="e.g. 994821034"
                    className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white font-mono placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
                  />
                </div>
              </div>
            </>
          )}

          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
              Email Address
            </label>
            <div className="relative">
              <Mail className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#A7A7C0]" />
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="player@esports.gg"
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
              />
            </div>
          </div>

          <div>
            <label className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block mb-1">
              Password
            </label>
            <div className="relative">
              <Lock className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#A7A7C0]" />
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full bg-[#0A0A0F] border border-[#262640] rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-[#A7A7C0] focus:outline-none focus:border-[#C8FF3D]"
              />
            </div>
          </div>

          {mode === 'signup' && (
            <label className="flex items-start gap-2 pt-1 text-[11px] text-[#A7A7C0] cursor-pointer">
              <input
                type="checkbox"
                checked={accepted18Plus}
                onChange={(e) => setAccepted18Plus(e.target.checked)}
                className="mt-0.5 rounded border-[#262640] text-[#C8FF3D] focus:ring-0 bg-[#0A0A0F]"
              />
              <span>I confirm I am 18+ years old and agree to the Fair Play Anti-Cheat rules. Free entry only.</span>
            </label>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold text-xs rounded-xl shadow-[0_0_15px_rgba(200,255,61,0.25)] flex items-center justify-center gap-1.5 transition-all active:scale-95 mt-2"
          >
            <span>{loading ? 'Please wait...' : mode === 'login' ? 'Log In to Lobby' : 'Create Free Account'}</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>

          {/* Quick Demo Login */}
          <button
            type="button"
            onClick={handleDemoLogin}
            disabled={loading}
            className="w-full py-2 bg-[#1B1B2B] hover:bg-[#262640] text-[#C8FF3D] border border-[#C8FF3D]/30 font-bold text-xs rounded-xl transition-all text-center"
          >
            ⚡ Quick Demo Sign In (Aritra - NeonSoul)
          </button>
        </form>
      </div>
    </div>
  );
};
