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

const AppContent: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [currentTab, setCurrentTab] = useState<string>('dashboard');

  if (!currentAdmin) {
    return <LoginPage />;
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
    <AdminLayout currentTab={currentTab} onSelectTab={setCurrentTab}>
      {renderContent()}
    </AdminLayout>
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
