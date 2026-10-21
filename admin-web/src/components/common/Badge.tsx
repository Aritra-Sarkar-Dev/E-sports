import React from 'react';

interface BadgeProps {
  children: React.ReactNode;
  variant?: 'lime' | 'violet' | 'amber' | 'red' | 'blue' | 'gray';
  size?: 'sm' | 'md';
}

export const Badge: React.FC<BadgeProps> = ({ children, variant = 'lime', size = 'sm' }) => {
  const variantStyles = {
    lime: 'bg-[#C8FF3D]/15 text-[#C8FF3D] border-[#C8FF3D]/40',
    violet: 'bg-[#7B5CFF]/15 text-[#B39DDB] border-[#7B5CFF]/40',
    amber: 'bg-[#FFB300]/15 text-[#FFCA28] border-[#FFB300]/40',
    red: 'bg-[#FF7A8A]/15 text-[#FF7A8A] border-[#FF7A8A]/40',
    blue: 'bg-[#00E5FF]/15 text-[#00E5FF] border-[#00E5FF]/40',
    gray: 'bg-[#23233A] text-[#9498AE] border-[#2E2E4A]',
  };

  const sizeStyles = {
    sm: 'text-xs px-2.5 py-0.5 rounded-full',
    md: 'text-sm px-3 py-1 rounded-full',
  };

  return (
    <span
      className={`inline-flex items-center font-bold border tracking-wide uppercase ${variantStyles[variant]} ${sizeStyles[size]}`}
    >
      {children}
    </span>
  );
};
