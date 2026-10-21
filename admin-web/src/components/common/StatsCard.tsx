import React from 'react';
import { LucideIcon } from 'lucide-react';

interface StatsCardProps {
  title: string;
  value: string | number;
  change?: string;
  isPositive?: boolean;
  icon: LucideIcon;
  accentColor?: string;
}

export const StatsCard: React.FC<StatsCardProps> = ({
  title,
  value,
  change,
  isPositive = true,
  icon: Icon,
  accentColor = '#C8FF3D',
}) => {
  return (
    <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 hover:border-[#7B5CFF]/40 transition-all">
      <div className="flex items-center justify-between">
        <span className="text-xs font-semibold text-[#9498AE] uppercase tracking-wider">{title}</span>
        <div
          className="w-10 h-10 rounded-xl flex items-center justify-center"
          style={{ backgroundColor: `${accentColor}15`, color: accentColor }}
        >
          <Icon className="w-5 h-5 stroke-2" />
        </div>
      </div>
      <div className="mt-4 flex items-baseline justify-between">
        <span className="text-2xl font-extrabold text-[#F5F5FA]">{value}</span>
        {change && (
          <span className={`text-xs font-bold ${isPositive ? 'text-[#C8FF3D]' : 'text-[#FF7A8A]'}`}>
            {change}
          </span>
        )}
      </div>
    </div>
  );
};
