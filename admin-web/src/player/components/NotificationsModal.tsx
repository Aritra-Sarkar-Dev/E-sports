import React, { useState } from 'react';
import { PlayerNotification } from '../types';
import { X, Bell, Key, Shield, Award, Check, Sparkles, SlidersHorizontal, ArrowLeft } from 'lucide-react';

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
  const [activeSubTab, setActiveSubTab] = useState<'inbox' | 'preferences'>('inbox');
  const [toggles, setToggles] = useState({
    roomCredentials: true,
    matchReminders: true,
    resultsPublished: true,
    sponsorCups: true,
  });

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

  const handleToggle = (key: keyof typeof toggles) => {
    setToggles((prev) => ({ ...prev, [key]: !prev[key] }));
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4 animate-fade-in">
      <div className="w-full max-w-md bg-[#14141F] border border-[#262640] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        {/* Header */}
        <div className="p-4 border-b border-[#262640] flex items-center justify-between bg-[#1B1B2B]">
          <div className="flex items-center gap-2.5">
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white transition-colors"
              title="Back"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
            <div>
              <h3 className="text-sm font-black text-white flex items-center gap-1.5">
                <Bell className="w-4 h-4 text-[#7B5CFF]" /> Notifications & Alerts
              </h3>
              <p className="text-[10px] text-[#A7A7C0]">Room passwords & match notifications</p>
            </div>
          </div>
          <div className="flex items-center gap-1">
            {activeSubTab === 'inbox' && (
              <button
                onClick={onMarkAllRead}
                className="px-2.5 py-1 text-[11px] font-bold text-[#C8FF3D] hover:bg-white/5 rounded-lg flex items-center gap-1"
              >
                <Check className="w-3.5 h-3.5" /> Mark read
              </button>
            )}
            <button
              onClick={onClose}
              className="p-1.5 hover:bg-white/10 rounded-full text-[#A7A7C0] hover:text-white"
            >
              <X className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Tab switchers */}
        <div className="flex border-b border-[#262640] bg-[#0A0A0F] p-1">
          <button
            onClick={() => setActiveSubTab('inbox')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'inbox'
                ? 'bg-[#14141F] text-[#C8FF3D] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Inbox ({notifications.filter((n) => !n.isRead).length} new)
          </button>
          <button
            onClick={() => setActiveSubTab('preferences')}
            className={`flex-1 py-2 text-xs font-bold rounded-xl transition-all ${
              activeSubTab === 'preferences'
                ? 'bg-[#14141F] text-[#7B5CFF] border border-[#262640]'
                : 'text-[#A7A7C0] hover:text-white'
            }`}
          >
            Preferences & Toggles
          </button>
        </div>

        {/* Content */}
        <div className="p-4 overflow-y-auto space-y-2.5 flex-1 text-xs">
          {activeSubTab === 'inbox' ? (
            notifications.length === 0 ? (
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
            )
          ) : (
            <div className="space-y-3">
              <span className="text-[10px] font-bold text-[#A7A7C0] uppercase tracking-wider block">
                Notification Channels
              </span>

              {/* Toggle 1: Room Password */}
              <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
                <div>
                  <span className="text-xs font-bold text-white block">Room Password Reveal</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    Get instant ping when Room ID & Password unlock 15m before start
                  </span>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={toggles.roomCredentials}
                  onClick={() => handleToggle('roomCredentials')}
                  className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                    toggles.roomCredentials ? 'bg-[#C8FF3D]' : 'bg-[#262640]'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-black transition-transform ${
                      toggles.roomCredentials ? 'translate-x-6' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 2: Match Reminders */}
              <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
                <div>
                  <span className="text-xs font-bold text-white block">Match Reminders (30 mins prior)</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    Alerts for registered tournaments coming up today
                  </span>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={toggles.matchReminders}
                  onClick={() => handleToggle('matchReminders')}
                  className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                    toggles.matchReminders ? 'bg-[#C8FF3D]' : 'bg-[#262640]'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-black transition-transform ${
                      toggles.matchReminders ? 'translate-x-6' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 3: Results Published */}
              <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
                <div>
                  <span className="text-xs font-bold text-white block">Results & Sponsor Prizes</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    Notifications when referees verify Booyah standings and publish points
                  </span>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={toggles.resultsPublished}
                  onClick={() => handleToggle('resultsPublished')}
                  className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                    toggles.resultsPublished ? 'bg-[#C8FF3D]' : 'bg-[#262640]'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-black transition-transform ${
                      toggles.resultsPublished ? 'translate-x-6' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>

              {/* Toggle 4: Sponsor Announcements */}
              <div className="p-3 bg-[#1B1B2B] border border-[#262640] rounded-2xl flex items-center justify-between">
                <div>
                  <span className="text-xs font-bold text-white block">Sponsor Cups & Bonus Drops</span>
                  <span className="text-[10px] text-[#A7A7C0]">
                    New Free-Entry tournaments from Asus ROG, Monster & Red Bull
                  </span>
                </div>
                <button
                  type="button"
                  role="switch"
                  aria-checked={toggles.sponsorCups}
                  onClick={() => handleToggle('sponsorCups')}
                  className={`relative inline-flex h-6 w-11 items-center rounded-full transition-colors ${
                    toggles.sponsorCups ? 'bg-[#C8FF3D]' : 'bg-[#262640]'
                  }`}
                >
                  <span
                    className={`inline-block h-4 w-4 transform rounded-full bg-black transition-transform ${
                      toggles.sponsorCups ? 'translate-x-6' : 'translate-x-1'
                    }`}
                  />
                </button>
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-3.5 border-t border-[#262640] bg-[#14141F]">
          <button
            onClick={onClose}
            className="w-full py-2 bg-[#1B1B2B] text-[#A7A7C0] hover:text-white font-bold text-xs rounded-xl transition-colors"
          >
            Close Notifications
          </button>
        </div>
      </div>
    </div>
  );
};
