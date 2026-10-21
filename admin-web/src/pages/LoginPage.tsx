import React, { useState } from 'react';
import { Shield, Lock, Mail, AlertCircle } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('admin@ffarena.in');
  const [password, setPassword] = useState('admin123');
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);
    try {
      await login(email, password);
    } catch (err: any) {
      setError(err.message || 'Authentication failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#0A0A0F] text-[#F5F5FA] font-sora glow-bg flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-[28px] p-8 shadow-2xl relative overflow-hidden">
        {/* Top Glow Accent */}
        <div className="absolute -top-24 -right-24 w-48 h-48 bg-[#7B5CFF]/20 rounded-full blur-3xl pointer-events-none" />

        {/* Logo */}
        <div className="flex flex-col items-center text-center mb-8">
          <div className="w-16 h-16 rounded-[22px] bg-gradient-to-br from-[#C8FF3D] to-[#7B5CFF] flex items-center justify-center font-extrabold text-black text-3xl shadow-xl shadow-[#C8FF3D]/20 mb-4">
            FF
          </div>
          <h2 className="text-2xl font-extrabold text-[#F5F5FA] tracking-tight">FF Arena Admin</h2>
          <p className="text-xs text-[#9498AE] mt-1">Free Fire MAX Esports Command Center</p>
          <div className="mt-2 inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-[#23233A] text-[11px] font-semibold text-[#C8FF3D]">
            <Shield className="w-3.5 h-3.5" />
            <span>Strict Administrator Authentication</span>
          </div>
        </div>

        {error && (
          <div className="mb-6 p-3.5 bg-[#FF7A8A]/10 border border-[#FF7A8A]/30 rounded-[16px] flex items-start gap-3 text-xs text-[#FF7A8A]">
            <AlertCircle className="w-4 h-4 shrink-0 mt-0.5" />
            <span>{error}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
              Admin Email
            </label>
            <div className="relative">
              <Mail className="w-4 h-4 text-[#9498AE] absolute left-4 top-3.5" />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                placeholder="admin@ffarena.in"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-11 pr-4 py-3 text-sm focus:outline-none transition-colors"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-[#9498AE] uppercase tracking-wider mb-2">
              Password
            </label>
            <div className="relative">
              <Lock className="w-4 h-4 text-[#9498AE] absolute left-4 top-3.5" />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                placeholder="••••••••"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-11 pr-4 py-3 text-sm focus:outline-none transition-colors"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-[#C8FF3D] hover:bg-[#b8f02d] text-[#0A0A0F] font-extrabold py-3.5 rounded-[18px] transition-all transform active:scale-[0.98] shadow-lg shadow-[#C8FF3D]/15 mt-2"
          >
            {isLoading ? 'Verifying Roles...' : 'ACCESS ADMIN PANEL'}
          </button>
        </form>

        {/* Demo Credentials Helper */}
        <div className="mt-8 pt-6 border-t border-[#23233A] text-center">
          <p className="text-[11px] font-semibold text-[#9498AE] mb-3 uppercase tracking-wider">
            Quick Demo Accounts (Pre-configured)
          </p>
          <div className="grid grid-cols-3 gap-2 text-xs">
            <button
              onClick={() => {
                setEmail('admin@ffarena.in');
                setPassword('admin123');
              }}
              className="px-2 py-1.5 bg-[#23233A] hover:bg-[#2E2E4A] rounded-xl text-[#F5F5FA] font-bold text-[11px]"
            >
              SuperAdmin
            </button>
            <button
              onClick={() => {
                setEmail('mod@ffarena.in');
                setPassword('mod123');
              }}
              className="px-2 py-1.5 bg-[#23233A] hover:bg-[#2E2E4A] rounded-xl text-[#F5F5FA] font-bold text-[11px]"
            >
              Moderator
            </button>
            <button
              onClick={() => {
                setEmail('support@ffarena.in');
                setPassword('support123');
              }}
              className="px-2 py-1.5 bg-[#23233A] hover:bg-[#2E2E4A] rounded-xl text-[#F5F5FA] font-bold text-[11px]"
            >
              Support
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
