import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import { AdminLayout } from './components/layout/AdminLayout';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { TournamentsPage } from './pages/TournamentsPage';
import { RoomManagementPage } from './pages/RoomManagementPage';
import { RegistrationsPage } from './pages/RegistrationsPage';
import { ResultsPage } from './pages/ResultsPage';
import { PrizePayoutsPage } from './pages/PrizePayoutsPage';
import { CoinsPage } from './pages/CoinsPage';
import { UsersPage } from './pages/UsersPage';
import { ReportsPage } from './pages/ReportsPage';
import { AnnouncementsPage } from './pages/AnnouncementsPage';
import { LeaderboardAdminPage } from './pages/LeaderboardAdminPage';
import { SystemSettingsPage } from './pages/SystemSettingsPage';
import { AuditLogsPage } from './pages/AuditLogsPage';

import { PlayerApp } from './player/PlayerApp';
import { Smartphone, Sliders } from 'lucide-react';

const AppContent: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [viewMode, setViewMode] = useState<'player' | 'admin'>('player');
  const [currentTab, setCurrentTab] = useState<string>('dashboard');

  if (viewMode === 'player') {
    return <PlayerApp onSwitchToAdmin={() => setViewMode('admin')} />;
  }

  // Admin Mode
  if (!currentAdmin) {
    return (
      <div className="relative">
        <div className="fixed top-4 right-4 z-50">
          <button
            onClick={() => setViewMode('player')}
            className="flex items-center gap-1.5 px-3 py-1.5 bg-[#C8FF3D] text-black font-extrabold text-xs rounded-full shadow-lg"
          >
            <Smartphone className="w-3.5 h-3.5" /> Back to Player App
          </button>
        </div>
        <LoginPage />
      </div>
    );
  }

  const renderContent = () => {
    switch (currentTab) {
      case 'dashboard':
        return <DashboardPage onNavigate={setCurrentTab} />;
      case 'tournaments':
        return <TournamentsPage />;
      case 'rooms':
        return <RoomManagementPage />;
      case 'registrations':
        return <RegistrationsPage />;
      case 'results':
        return <ResultsPage />;
      case 'payouts':
        return <PrizePayoutsPage />;
      case 'coins':
        return <CoinsPage />;
      case 'users':
        return <UsersPage />;
      case 'reports':
        return <ReportsPage />;
      case 'notifications':
        return <AnnouncementsPage />;
      case 'leaderboard':
        return <LeaderboardAdminPage />;
      case 'settings':
        return <SystemSettingsPage />;
      case 'audit':
        return <AuditLogsPage />;
      default:
        return <DashboardPage onNavigate={setCurrentTab} />;
    }
  };

  return (
    <div className="relative">
      {/* Quick return to Player App bar */}
      <div className="bg-[#17113D] border-b border-[#7B5CFF]/30 px-4 py-2 flex items-center justify-between text-xs sticky top-0 z-50">
        <span className="text-[#9498AE] flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-[#C8FF3D] animate-ping" />
          <strong className="text-white">Tournament Organizer Portal (Admin Console)</strong>
        </span>
        <button
          onClick={() => setViewMode('player')}
          className="flex items-center gap-1.5 px-3 py-1 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold rounded-full shadow transition-all active:scale-95"
        >
          <Smartphone className="w-3.5 h-3.5" /> Return to Player App
        </button>
      </div>

      <AdminLayout currentTab={currentTab} onSelectTab={setCurrentTab}>
        {renderContent()}
      </AdminLayout>
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default App;
