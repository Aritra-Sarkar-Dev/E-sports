import React from 'react';
import {
  Users,
  Activity,
  Flame,
  UserCheck,
  AlertTriangle,
  Coins,
  ArrowUpRight,
  TrendingUp,
  Clock,
  ShieldCheck,
} from 'lucide-react';
import { StatsCard } from '../components/common/StatsCard';
import { Badge } from '../components/common/Badge';

interface DashboardPageProps {
  onNavigate: (tab: string) => void;
}

export const DashboardPage: React.FC<DashboardPageProps> = ({ onNavigate }) => {
  return (
    <div className="space-y-6">
      {/* Metrics Row (6 key cards) */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
        <StatsCard
          title="Total Users"
          value="1,420"
          change="+14% this wk"
          icon={Users}
          accentColor="#00E5FF"
        />
        <StatsCard
          title="Active Today"
          value="384"
          change="Live now"
          icon={Activity}
          accentColor="#C8FF3D"
        />
        <StatsCard
          title="Live Arenas"
          value="1"
          change="Bermuda Blitz"
          icon={Flame}
          accentColor="#FF385C"
        />
        <StatsCard
          title="Registrations"
          value="86"
          change="+22 today"
          icon={UserCheck}
          accentColor="#7B5CFF"
        />
        <StatsCard
          title="Open Reports"
          value="2"
          change="Requires review"
          isPositive={false}
          icon={AlertTriangle}
          accentColor="#FF7A8A"
        />
        <StatsCard
          title="Coins Issued"
          value="24.8K"
          change="Non-cash rewards"
          icon={Coins}
          accentColor="#FFB300"
        />
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Line Chart: Daily Registrations */}
        <div className="lg:col-span-2 bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 flex flex-col justify-between">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h3 className="text-base font-bold text-[#F5F5FA] flex items-center gap-2">
                <TrendingUp className="w-4 h-4 text-[#C8FF3D]" /> Daily Tournament Registrations
              </h3>
              <p className="text-xs text-[#9498AE]">Last 7 days registration volume across all modes</p>
            </div>
            <span className="text-xs font-bold text-[#C8FF3D] bg-[#C8FF3D]/10 px-3 py-1 rounded-full border border-[#C8FF3D]/30">
              Avg 72 / day
            </span>
          </div>

          {/* SVG Line Chart */}
          <div className="h-56 w-full pt-4">
            <svg className="w-full h-full overflow-visible" viewBox="0 0 700 200">
              <defs>
                <linearGradient id="lineGrad" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stopColor="#C8FF3D" stopOpacity="0.35" />
                  <stop offset="100%" stopColor="#C8FF3D" stopOpacity="0.0" />
                </linearGradient>
              </defs>
              {/* Horizontal guide lines */}
              <line x1="0" y1="40" x2="700" y2="40" stroke="#23233A" strokeDasharray="4" />
              <line x1="0" y1="100" x2="700" y2="100" stroke="#23233A" strokeDasharray="4" />
              <line x1="0" y1="160" x2="700" y2="160" stroke="#23233A" strokeDasharray="4" />

              {/* Area */}
              <polygon
                points="0,170 100,140 200,120 300,150 400,90 500,60 600,45 700,50 700,200 0,200"
                fill="url(#lineGrad)"
              />
              {/* Line */}
              <polyline
                fill="none"
                stroke="#C8FF3D"
                strokeWidth="3.5"
                strokeLinecap="round"
                strokeLinejoin="round"
                points="0,170 100,140 200,120 300,150 400,90 500,60 600,45 700,50"
              />
              {/* Coordinates dots */}
              {[
                { x: 0, y: 170, label: 'Mon (42)' },
                { x: 100, y: 140, label: 'Tue (55)' },
                { x: 200, y: 120, label: 'Wed (64)' },
                { x: 300, y: 150, label: 'Thu (50)' },
                { x: 400, y: 90, label: 'Fri (82)' },
                { x: 500, y: 60, label: 'Sat (95)' },
                { x: 600, y: 45, label: 'Sun (108)' },
                { x: 700, y: 50, label: 'Today (86)' },
              ].map((pt, idx) => (
                <g key={idx}>
                  <circle cx={pt.x} cy={pt.y} r="5" fill="#0A0A0F" stroke="#C8FF3D" strokeWidth="2.5" />
                  <text x={pt.x} y="195" fill="#9498AE" fontSize="11" textAnchor="middle">
                    {pt.label.split(' ')[0]}
                  </text>
                </g>
              ))}
            </svg>
          </div>
        </div>

        {/* Bar Chart: Tournaments by Mode */}
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 flex flex-col justify-between">
          <div>
            <h3 className="text-base font-bold text-[#F5F5FA]">Tournaments by Mode</h3>
            <p className="text-xs text-[#9498AE] mt-0.5">Distribution across battle formats</p>
          </div>

          <div className="space-y-4 my-6">
            <div>
              <div className="flex justify-between text-xs font-semibold mb-1">
                <span className="text-[#F5F5FA]">Squad 4v4 (Battle Royale)</span>
                <span className="text-[#C8FF3D] font-bold">14 events (58%)</span>
              </div>
              <div className="h-3 w-full bg-[#0A0A0F] rounded-full overflow-hidden border border-[#23233A]">
                <div className="h-full bg-gradient-to-r from-[#C8FF3D] to-[#7B5CFF] rounded-full" style={{ width: '58%' }} />
              </div>
            </div>

            <div>
              <div className="flex justify-between text-xs font-semibold mb-1">
                <span className="text-[#F5F5FA]">Solo Blitz</span>
                <span className="text-[#00E5FF] font-bold">6 events (25%)</span>
              </div>
              <div className="h-3 w-full bg-[#0A0A0F] rounded-full overflow-hidden border border-[#23233A]">
                <div className="h-full bg-[#00E5FF] rounded-full" style={{ width: '25%' }} />
              </div>
            </div>

            <div>
              <div className="flex justify-between text-xs font-semibold mb-1">
                <span className="text-[#F5F5FA]">Duo Tactical</span>
                <span className="text-[#FFB300] font-bold">4 events (17%)</span>
              </div>
              <div className="h-3 w-full bg-[#0A0A0F] rounded-full overflow-hidden border border-[#23233A]">
                <div className="h-full bg-[#FFB300] rounded-full" style={{ width: '17%' }} />
              </div>
            </div>
          </div>

          <div className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] flex items-center justify-between text-xs">
            <span className="text-[#9498AE]">Next Scheduled Start</span>
            <span className="font-bold text-[#C8FF3D]">In 42 mins (Pro Squad)</span>
          </div>
        </div>
      </div>

      {/* Quick Ops Control Row */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Next Big Match Status */}
        <div className="bg-[#14141F] border border-[#7B5CFF]/40 rounded-[22px] p-6 relative overflow-hidden">
          <div className="flex items-center justify-between mb-3">
            <Badge variant="violet">LIVE ROOM STAGING</Badge>
            <Clock className="w-4 h-4 text-[#7B5CFF]" />
          </div>
          <h4 className="font-extrabold text-[#F5F5FA] text-lg">CYBERFRAY 2077 GRAND FINALS</h4>
          <p className="text-xs text-[#9498AE] mt-1">Room ID: 982341 • Password: rog99</p>
          <div className="mt-4 pt-4 border-t border-[#23233A] flex items-center justify-between">
            <span className="text-xs text-[#C8FF3D] font-bold">Auto-reveals in 32 mins</span>
            <button
              onClick={() => onNavigate('rooms')}
              className="text-xs font-bold text-[#C8FF3D] hover:underline flex items-center gap-1"
            >
              Manage Room <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        {/* Loyalty Coin Health */}
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6">
          <div className="flex items-center justify-between mb-3">
            <Badge variant="amber">LOYALTY COIN ECONOMY</Badge>
            <Coins className="w-4 h-4 text-[#FFB300]" />
          </div>
          <h4 className="font-extrabold text-[#F5F5FA] text-lg">Non-Cash Gamification</h4>
          <p className="text-xs text-[#9498AE] mt-1">
            Zero deposits, zero withdrawals. Coins are loyalty points awarded for active esports play.
          </p>
          <div className="mt-4 pt-4 border-t border-[#23233A] flex items-center justify-between">
            <span className="text-xs text-[#9498AE]">6 Active Earn Rules</span>
            <button
              onClick={() => onNavigate('coins')}
              className="text-xs font-bold text-[#C8FF3D] hover:underline flex items-center gap-1"
            >
              Coin Rules <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        {/* Fair Play & Compliance */}
        <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-6">
          <div className="flex items-center justify-between mb-3">
            <Badge variant="lime">INDIA LAW COMPLIANT</Badge>
            <ShieldCheck className="w-4 h-4 text-[#C8FF3D]" />
          </div>
          <h4 className="font-extrabold text-[#F5F5FA] text-lg">Free Entry Certification</h4>
          <p className="text-xs text-[#9498AE] mt-1">
            Prizes are 100% sponsored. No cash wallet, entry stake, or gambling mechanics exist.
          </p>
          <div className="mt-4 pt-4 border-t border-[#23233A] flex items-center justify-between">
            <span className="text-xs text-[#C8FF3D] font-bold">enablePaidEntry = false</span>
            <button
              onClick={() => onNavigate('settings')}
              className="text-xs font-bold text-[#C8FF3D] hover:underline flex items-center gap-1"
            >
              System Config <ArrowUpRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
