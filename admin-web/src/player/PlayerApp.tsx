import React, { useState, useEffect } from 'react';
import {
  PlayerProfile,
  PlayerMatch,
  Mission,
  RewardStoreItem,
  LeaderboardPlayer,
  PlayerNotification
} from './types';
import {
  INITIAL_PLAYER_PROFILE,
  INITIAL_COIN_BALANCE,
  INITIAL_MATCHES,
  INITIAL_LEADERBOARD,
  INITIAL_MISSIONS,
  INITIAL_STORE_ITEMS,
  INITIAL_NOTIFICATIONS
} from './mockPlayerData';
import { HomeTab } from './tabs/HomeTab';
import { MatchesTab } from './tabs/MatchesTab';
import { RewardsTab } from './tabs/RewardsTab';
import { ProfileTab } from './tabs/ProfileTab';
import { MatchDetailModal } from './components/MatchDetailModal';
import { JoinMatchModal } from './components/JoinMatchModal';
import { SpinWheelModal } from './components/SpinWheelModal';
import { LeaderboardModal } from './components/LeaderboardModal';
import { NotificationsModal } from './components/NotificationsModal';
import { ReportModal } from './components/ReportModal';
import { OrganizerConsole } from './components/OrganizerConsole';
import { OnboardingModal } from './components/OnboardingModal';
import { AuthModal } from './components/AuthModal';
import { SquadModal } from './components/SquadModal';
import { ShareCardModal } from './components/ShareCardModal';
import { TournamentService } from '../services/tournamentService';
import { AuthService } from '../services/authService';
import { CoinService } from '../services/coinService';
import { NotificationService } from '../services/notificationService';
import { LeaderboardService } from '../services/leaderboardService';
import { ToastContainer, ToastMessage } from './components/Toast';
import {
  Home,
  Trophy,
  Award,
  User,
  Sliders,
  Sparkles
} from 'lucide-react';

interface PlayerAppProps {
  onSwitchToAdmin: () => void;
}

export const PlayerApp: React.FC<PlayerAppProps> = ({ onSwitchToAdmin }) => {
  const [currentTab, setCurrentTab] = useState<'home' | 'matches' | 'rewards' | 'profile'>('home');
  const [matchesSubTab, setMatchesSubTab] = useState<'Upcoming' | 'Live' | 'Completed' | 'My Matches'>('Upcoming');

  const [profile, setProfile] = useState<PlayerProfile>(() => {
    return AuthService.getCurrentUser() || INITIAL_PLAYER_PROFILE;
  });

  const [coins, setCoins] = useState<number>(() => {
    return CoinService.getPlayerBalance();
  });

  const [matches, setMatches] = useState<PlayerMatch[]>(() => {
    return TournamentService.getPlayerMatches();
  });

  const [missions, setMissions] = useState<Mission[]>(INITIAL_MISSIONS);
  const [storeItems, setStoreItems] = useState<RewardStoreItem[]>(INITIAL_STORE_ITEMS);
  const [notifications, setNotifications] = useState<PlayerNotification[]>(() => {
    return NotificationService.getNotifications();
  });

  const [hasCheckedInToday, setHasCheckedInToday] = useState(false);
  const [hasSpunToday, setHasSpunToday] = useState(false);

  // Modals state
  const [selectedMatchForDetail, setSelectedMatchForDetail] = useState<PlayerMatch | null>(null);
  const [selectedMatchForJoin, setSelectedMatchForJoin] = useState<PlayerMatch | null>(null);
  const [showSpinWheel, setShowSpinWheel] = useState(false);
  const [showLeaderboard, setShowLeaderboard] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showReportModal, setShowReportModal] = useState(false);
  const [showOrganizerConsole, setShowOrganizerConsole] = useState(false);
  const [showOnboarding, setShowOnboarding] = useState(false);
  const [showAuthModal, setShowAuthModal] = useState(false);
  const [showSquadModal, setShowSquadModal] = useState(false);
  const [showShareCard, setShowShareCard] = useState(false);
  const [reportDefaultMatchId, setReportDefaultMatchId] = useState<string>('m1');

  // Check onboarding on initial mount
  useEffect(() => {
    const hasSeenOnboarding = localStorage.getItem('ff_arena_onboarding_completed');
    if (!hasSeenOnboarding) {
      setShowOnboarding(true);
    }
  }, []);

  const handleCloseOnboarding = () => {
    localStorage.setItem('ff_arena_onboarding_completed', 'true');
    setShowOnboarding(false);
  };

  // Toasts
  const [toasts, setToasts] = useState<ToastMessage[]>([]);

  const addToast = (type: 'success' | 'error' | 'info', message: string) => {
    const id = Date.now().toString();
    setToasts((prev) => [...prev, { id, type, message }]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((t) => t.id !== id));
    }, 3500);
  };

  const dismissToast = (id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  };

  // Join Free Tournament Handler
  const handleJoinSuccess = (match: PlayerMatch, teamMembers: string[]) => {
    // Update match state
    const updatedMatches = matches.map((m) =>
      m.id === match.id
        ? {
            ...m,
            joinedSlots: Math.min(m.maxSlots, m.joinedSlots + 1),
            userJoined: true,
            roomId: m.roomId || '884910',
            roomPassword: m.roomPassword || 'ffpro99',
          }
        : m
    );

    setMatches(updatedMatches);
    TournamentService.savePlayerMatches(updatedMatches);

    // Record registration in TournamentService
    TournamentService.addPlayerRegistration({
      id: `reg_${Date.now()}`,
      tournamentId: match.id,
      tournamentTitle: match.title,
      userId: profile.uid,
      inGameName: profile.inGameName,
      ffUid: profile.ffUid,
      teamMembers,
      registeredAt: 'Just now',
      status: 'Confirmed',
    });

    // Award +20 Loyalty coins for participation
    const { newBalance } = CoinService.earnPlayerCoins(
      20,
      'tournament_join',
      `Tournament Registration: ${match.title}`
    );
    setCoins(newBalance);

    // Send in-app notification
    const newNotif = NotificationService.addNotification(
      `Slot Confirmed: ${match.title}`,
      `You have reserved a free slot. Room credentials will unlock 15 minutes before kick-off.`,
      'room'
    );
    setNotifications((prev) => [newNotif, ...prev]);

    setSelectedMatchForJoin(null);
    setSelectedMatchForDetail(null);
    addToast('success', `Free slot reserved in ${match.title}! +20 Loyalty Coins earned.`);
  };

  // Daily Check In Handler
  const handleDailyCheckIn = () => {
    if (hasCheckedInToday) return;
    setHasCheckedInToday(true);
    const { newBalance } = CoinService.earnPlayerCoins(
      50,
      'daily_checkin',
      'Day 5 Login Streak Bonus'
    );
    setCoins(newBalance);
    addToast('success', '+50 Loyalty Coins claimed for your login streak!');
  };

  // Spin Wheel Win Handler
  const handleSpinWin = (winCoins: number, label: string) => {
    setHasSpunToday(true);
    const { newBalance } = CoinService.earnPlayerCoins(
      winCoins,
      'spin_wheel',
      `Daily Spin Win: ${label}`
    );
    setCoins(newBalance);
    addToast('success', `Prize claimed: ${label}! +${winCoins} coins added.`);
  };

  // Claim Mission Handler
  const handleClaimMission = (missionId: string) => {
    const mis = missions.find((m) => m.id === missionId);
    if (!mis || mis.isClaimed) return;

    setMissions((prev) =>
      prev.map((m) => (m.id === missionId ? { ...m, isClaimed: true } : m))
    );

    const { newBalance } = CoinService.earnPlayerCoins(
      mis.rewardCoins,
      'mission',
      `Completed: ${mis.title}`
    );
    setCoins(newBalance);
    addToast('success', `Claimed +${mis.rewardCoins} Loyalty Coins!`);
  };

  // Redeem Store Item
  const handleRedeemItem = (item: RewardStoreItem) => {
    if (coins < item.coins) {
      addToast('error', 'Insufficient coins to redeem this cosmetic item.');
      return;
    }
    try {
      const { newBalance } = CoinService.spendPlayerCoins(
        item.coins,
        `Redeemed: ${item.title}`
      );
      setCoins(newBalance);
      setStoreItems((prev) =>
        prev.map((i) => (i.id === item.id ? { ...i, isPurchased: true } : i))
      );
      addToast('success', `Redeemed ${item.title}! Equipped on your profile.`);
    } catch (err: any) {
      addToast('error', err.message || 'Redemption failed');
    }
  };

  // Copy helper
  const handleCopyText = (text: string) => {
    navigator.clipboard?.writeText(text);
    addToast('info', 'Copied to clipboard!');
  };

  // Dispute Report submit
  const handleReportSubmit = (data: { category: string; matchId: string; description: string; proofUrl: string }) => {
    setShowReportModal(false);
    addToast('success', `Fair-play ticket #${Math.floor(1000 + Math.random() * 9000)} submitted to referees.`);
  };

  const unreadNotifsCount = notifications.filter((n) => !n.isRead).length;

  return (
    <div className="min-h-screen bg-[#0A0A0F] text-[#FFFFFF] font-sora selection:bg-[#C8FF3D] selection:text-black flex justify-center">
      {/* Toast Alert Manager */}
      <ToastContainer toasts={toasts} onDismiss={dismissToast} />

      {/* Mobile-first centered frame container (360px - 430px) */}
      <div className="w-full max-w-[430px] min-h-screen bg-[#0A0A0F] border-x border-[#262640]/50 relative flex flex-col shadow-2xl">
        {/* Floating Quick Admin Switcher Button */}
        <div className="absolute top-2 right-2 z-40 opacity-30 hover:opacity-100 transition-opacity">
          <button
            onClick={onSwitchToAdmin}
            title="Switch to Admin Console"
            className="p-1.5 bg-[#14141F] border border-[#7B5CFF]/60 rounded-full text-[#7B5CFF] hover:bg-[#7B5CFF] hover:text-white transition-all shadow"
          >
            <Sliders className="w-3.5 h-3.5" />
          </button>
        </div>

        {/* Tab Content Rendering */}
        <div className="flex-1 pb-24 overflow-x-hidden">
          {currentTab === 'home' && (
            <HomeTab
              profile={profile}
              coins={coins}
              matches={matches}
              leaderboard={INITIAL_LEADERBOARD}
              unreadNotifsCount={unreadNotifsCount}
              onOpenNotifications={() => setShowNotifications(true)}
              onOpenRewards={() => setCurrentTab('rewards')}
              onOpenSpinWheel={() => setShowSpinWheel(true)}
              onOpenLeaderboard={() => setShowLeaderboard(true)}
              onOpenSquad={() => setShowSquadModal(true)}
              onOpenMatchDetail={(m) => setSelectedMatchForDetail(m)}
              onSelectCategory={(cat) => {
                if (cat === 'Live' || cat === 'Upcoming') {
                  setMatchesSubTab(cat as any);
                }
                setCurrentTab('matches');
              }}
              onDailyCheckIn={handleDailyCheckIn}
              hasCheckedInToday={hasCheckedInToday}
              onShareReferral={() => handleCopyText(`Join me on FF Arena with my referral code ${profile.referralCode} to both get 100 bonus coins!`)}
              onOpenSupport={() => setShowReportModal(true)}
            />
          )}

          {currentTab === 'matches' && (
            <MatchesTab
              matches={matches}
              initialSubTab={matchesSubTab}
              onOpenMatchDetail={(m) => setSelectedMatchForDetail(m)}
              onJoinMatch={(m) => setSelectedMatchForJoin(m)}
              onOpenReportModal={(matchId) => {
                setReportDefaultMatchId(matchId);
                setShowReportModal(true);
              }}
              onCopyText={handleCopyText}
            />
          )}

          {currentTab === 'rewards' && (
            <RewardsTab
              coins={coins}
              missions={missions}
              storeItems={storeItems}
              hasCheckedInToday={hasCheckedInToday}
              onDailyCheckIn={handleDailyCheckIn}
              onOpenSpinWheel={() => setShowSpinWheel(true)}
              onClaimMission={handleClaimMission}
              onRedeemStoreItem={handleRedeemItem}
            />
          )}

          {currentTab === 'profile' && (
            <ProfileTab
              profile={profile}
              recentMatches={matches.filter((m) => m.userJoined)}
              onUpdateProfile={(updated) => {
                const newProfile = AuthService.updateProfile(updated);
                setProfile(newProfile);
              }}
              onCopyText={handleCopyText}
              onSwitchToAdmin={onSwitchToAdmin}
              onOpenOrganizerConsole={() => setShowOrganizerConsole(true)}
              onOpenSquad={() => setShowSquadModal(true)}
              onOpenShareCard={() => setShowShareCard(true)}
              onLogout={() => {
                AuthService.logout();
                addToast('info', 'Logged out of player account.');
                setShowAuthModal(true);
              }}
              onOpenSupport={() => setShowReportModal(true)}
            />
          )}
        </div>

        {/* Floating Pill Bottom Navigation (Strictly 4 tabs: lime pill with label, inactive icon only) */}
        <div className="fixed bottom-3 left-1/2 -translate-x-1/2 z-40 w-[94%] max-w-[400px]">
          <nav className="bg-[#14141F]/95 backdrop-blur-xl border border-[#262640] rounded-full p-1.5 flex items-center justify-around shadow-[0_8px_32px_rgba(0,0,0,0.85)]">
            {[
              { id: 'home', label: 'Home', icon: Home },
              { id: 'matches', label: 'Matches', icon: Trophy },
              { id: 'rewards', label: 'Rewards', icon: Award },
              { id: 'profile', label: 'Profile', icon: User },
            ].map((tab) => {
              const Icon = tab.icon;
              const isActive = currentTab === tab.id;
              return (
                <button
                  key={tab.id}
                  onClick={() => setCurrentTab(tab.id as any)}
                  className={`flex items-center justify-center transition-all duration-300 min-h-[44px] ${
                    isActive
                      ? 'bg-[#C8FF3D] text-black font-extrabold px-3 py-1.5 rounded-full shadow-[0_0_16px_rgba(200,255,61,0.35)] gap-1.5'
                      : 'text-[#A7A7C0] hover:text-white px-2.5 py-1.5 rounded-full hover:bg-white/5 gap-1.5'
                  }`}
                  aria-label={tab.label}
                >
                  <Icon className="w-4 h-4 shrink-0" />
                  <span className={`text-[11px] font-bold tracking-wide leading-none ${isActive ? 'text-black font-black' : 'text-[#A7A7C0]'}`}>{tab.label}</span>
                </button>
              );
            })}
          </nav>
        </div>

        {/* Modals */}
        <MatchDetailModal
          match={selectedMatchForDetail}
          onClose={() => setSelectedMatchForDetail(null)}
          onJoin={(m) => {
            setSelectedMatchForDetail(null);
            setSelectedMatchForJoin(m);
          }}
          onCopyRoomCreds={handleCopyText}
        />

        <JoinMatchModal
          match={selectedMatchForJoin}
          profile={profile}
          onClose={() => setSelectedMatchForJoin(null)}
          onSuccess={handleJoinSuccess}
        />

        {showSpinWheel && (
          <SpinWheelModal
            hasSpunToday={hasSpunToday}
            onClose={() => setShowSpinWheel(false)}
            onWin={handleSpinWin}
          />
        )}

        {showLeaderboard && (
          <LeaderboardModal
            players={LeaderboardService.getWeekly()}
            onClose={() => setShowLeaderboard(false)}
          />
        )}

        {showNotifications && (
          <NotificationsModal
            notifications={notifications}
            onClose={() => setShowNotifications(false)}
            onMarkAllRead={() => {
              const read = NotificationService.markAllAsRead();
              setNotifications(read);
              addToast('info', 'All notifications marked as read');
            }}
          />
        )}

        {showReportModal && (
          <ReportModal
            defaultMatchId={reportDefaultMatchId}
            onClose={() => setShowReportModal(false)}
            onSubmit={handleReportSubmit}
          />
        )}

        {showSquadModal && (
          <SquadModal
            isOpen={showSquadModal}
            onClose={() => setShowSquadModal(false)}
            onShowToast={addToast}
          />
        )}

        {showShareCard && (
          <ShareCardModal
            isOpen={showShareCard}
            profile={profile}
            onClose={() => setShowShareCard(false)}
            onShowToast={addToast}
          />
        )}

        {showOnboarding && (
          <OnboardingModal
            isOpen={showOnboarding}
            onClose={handleCloseOnboarding}
          />
        )}

        {showAuthModal && (
          <AuthModal
            isOpen={showAuthModal}
            onClose={() => setShowAuthModal(false)}
            onSuccess={(u) => {
              setProfile(u);
              addToast('success', `Welcome, ${u.name}!`);
            }}
          />
        )}

        {showOrganizerConsole && (
          <OrganizerConsole
            onClose={() => setShowOrganizerConsole(false)}
            matches={matches}
            onRefreshMatches={() => {
              setMatches([...TournamentService.getPlayerMatches()]);
              setNotifications([...NotificationService.getNotifications()]);
            }}
            onShowToast={addToast}
          />
        )}
      </div>
    </div>
  );
};
