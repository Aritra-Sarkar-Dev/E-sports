import React from 'react';
import { PlayerNotification } from '../types';
import { X, Bell, Key, Shield, Award, Check, Sparkles } from 'lucide-react';

interface NotificationsModalProps {
  notifications: PlayerNotification[];
  onClose: () => void;
  onMarkAllRead: () => void;
}

export const NotificationsModal: React.FC<NotificationsModalProps> = ({
  notifications,
  onClose,
  onMarkAllRead,
}) => {
  const getIcon = (type: PlayerNotification['type']) => {
    switch (type) {
      case 'room':
        return <Key className="w-4 h-4 text-[#C8FF3D]" />;
      case 'reward':
        return <Award className="w-4 h-4 text-[#FFB800]" />;
      case 'match':
        return <Sparkles className="w-4 h-4 text-[#7B5CFF]" />;
      case 'system':
      default:
        return <Shield className="w-4 h-4 text-[#C8FF3D]" />;
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-xl bg-[#7B5CFF]/20 flex items-center justify-center text-[#7B5CFF]">
              <Bell className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-black text-white">Match & Room Alerts</h3>
              <p className="text-[10px] text-[#A7A7C0]">Room passwords & result verifications</p>
            </div>
          </div>
          <div className="flex items-center gap-1">
            <button
              onClick={onMarkAllRead}
              className="px-2.5 py-1 text-[11px] font-bold text-[#C8FF3D] hover:bg-white/5 rounded-lg flex items-center gap-1"
            >
              <Check className="w-3.5 h-3.5" /> Mark read
            </button>
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        </div>

        <div className="p-4 overflow-y-auto space-y-2.5 flex-1">
          {notifications.length === 0 ? (
            <div className="py-12 text-center text-[#A7A7C0]">
              <Bell className="w-10 h-10 mx-auto text-[#262640] mb-2" />
              <p className="text-xs">No notifications right now</p>
            </div>
          ) : (
            notifications.map((item) => (
              <div
                key={item.id}
                className={`p-3.5 rounded-2xl border transition-all ${
                  item.isRead
                    ? 'bg-[#14141F] border-[#262640]'
                    : 'bg-[#1B1B2B] border-[#7B5CFF]/40 shadow-[0_0_15px_rgba(123,92,255,0.1)]'
                }`}
              >
                <div className="flex items-start gap-3">
                  <div className="w-8 h-8 rounded-xl bg-[#0A0A0F] border border-[#262640] flex items-center justify-center shrink-0 mt-0.5">
                    {getIcon(item.type)}
                  </div>
                  <div className="flex-1">
                    <div className="flex items-center justify-between mb-0.5">
                      <h4 className="text-xs font-bold text-white">{item.title}</h4>
                      <span className="text-[10px] text-[#A7A7C0]">{item.time}</span>
                    </div>
                    <p className="text-xs text-[#A7A7C0] leading-relaxed">{item.message}</p>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};
