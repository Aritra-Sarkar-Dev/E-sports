import React, { useState } from 'react';
import {
  PlayerProfile,
  PlayerMatch,
  WalletBalance,
  WalletTransaction,
  Mission,
  RewardStoreItem,
  LeaderboardPlayer,
  PlayerNotification
} from './types';
import {
  INITIAL_PLAYER_PROFILE,
  INITIAL_WALLET,
  INITIAL_COIN_BALANCE,
  INITIAL_MATCHES,
  INITIAL_TRANSACTIONS,
  INITIAL_LEADERBOARD,
  INITIAL_MISSIONS,
  INITIAL_STORE_ITEMS,
  INITIAL_NOTIFICATIONS
} from './mockPlayerData';
import { HomeTab } from './tabs/HomeTab';
import { MatchesTab } from './tabs/MatchesTab';
import { WalletTab } from './tabs/WalletTab';
import { RewardsTab } from './tabs/RewardsTab';
import { ProfileTab } from './tabs/ProfileTab';
import { MatchDetailModal } from './components/MatchDetailModal';
import { JoinMatchModal } from './components/JoinMatchModal';
import { AddMoneyModal } from './components/AddMoneyModal';
import { WithdrawModal } from './components/WithdrawModal';
import { SpinWheelModal } from './components/SpinWheelModal';
import { LeaderboardModal } from './components/LeaderboardModal';
import { NotificationsModal } from './components/NotificationsModal';
import { ReportModal } from './components/ReportModal';
import { ToastContainer, ToastMessage } from './components/Toast';
import {
  Home,
  Trophy,
  Wallet,
  Award,
  User,
  Sliders,
  Sparkles
} from 'lucide-react';

interface PlayerAppProps {
  onSwitchToAdmin: () => void;
}

export const PlayerApp: React.FC<PlayerAppProps> = ({ onSwitchToAdmin }) => {
  const [currentTab, setCurrentTab] = useState<'home' | 'matches' | 'wallet' | 'rewards' | 'profile'>('home');
  const [profile, setProfile] = useState<PlayerProfile>(INITIAL_PLAYER_PROFILE);
  const [wallet, setWallet] = useState<WalletBalance>(INITIAL_WALLET);
  const [coins, setCoins] = useState<number>(INITIAL_COIN_BALANCE);
  const [matches, setMatches] = useState<PlayerMatch[]>(INITIAL_MATCHES);
  const [transactions, setTransactions] = useState<WalletTransaction[]>(INITIAL_TRANSACTIONS);
  const [missions, setMissions] = useState<Mission[]>(INITIAL_MISSIONS);
  const [storeItems, setStoreItems] = useState<RewardStoreItem[]>(INITIAL_STORE_ITEMS);
  const [notifications, setNotifications] = useState<PlayerNotification[]>(INITIAL_NOTIFICATIONS);
  const [hasCheckedInToday, setHasCheckedInToday] = useState(false);
  const [hasSpunToday, setHasSpunToday] = useState(false);

  // Modals state
  const [selectedMatchForDetail, setSelectedMatchForDetail] = useState<PlayerMatch | null>(null);
  const [selectedMatchForJoin, setSelectedMatchForJoin] = useState<PlayerMatch | null>(null);
  const [showAddMoney, setShowAddMoney] = useState(false);
  const [showWithdraw, setShowWithdraw] = useState(false);
  const [showSpinWheel, setShowSpinWheel] = useState(false);
  const [showLeaderboard, setShowLeaderboard] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showReportModal, setShowReportModal] = useState(false);
  const [reportDefaultMatchId, setReportDefaultMatchId] = useState<string>('m1');

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

  // Join Match Handler
  const handleJoinSuccess = (match: PlayerMatch, teamMembers: string[]) => {
    // Deduct entry fee
    if (match.entryFee > 0) {
      setWallet((prev) => {
        let remaining = match.entryFee;
        let newBonus = prev.bonus;
        let newDeposit = prev.deposit;
        let newWinnings = prev.winnings;

        if (newBonus >= remaining) {
          newBonus -= remaining;
          remaining = 0;
        } else {
          remaining -= newBonus;
          newBonus = 0;
        }

        if (remaining > 0 && newDeposit >= remaining) {
          newDeposit -= remaining;
          remaining = 0;
        } else if (remaining > 0) {
          remaining -= newDeposit;
          newDeposit = 0;
          newWinnings = Math.max(0, newWinnings - remaining);
        }

        return {
          deposit: newDeposit,
          winnings: newWinnings,
          bonus: newBonus,
          total: newDeposit + newWinnings + newBonus,
        };
      });

      // Add to transactions
      const newTx: WalletTransaction = {
        id: `tx_${Date.now()}`,
        type: 'entry_fee',
        title: `Match Entry: ${match.title}`,
        amount: -match.entryFee,
        date: 'Just now',
        status: 'Completed',
        refId: match.id,
      };
      setTransactions((prev) => [newTx, ...prev]);
    }

    // Update match state
    setMatches((prev) =>
      prev.map((m) =>
        m.id === match.id
          ? {
              ...m,
              joinedSlots: m.joinedSlots + 1,
              userJoined: true,
              isRoomRevealed: true,
              roomId: m.roomId || '884910',
              roomPassword: m.roomPassword || 'ffpro99',
            }
          : m
      )
    );

    setSelectedMatchForJoin(null);
    setSelectedMatchForDetail(null);
    addToast('success', `Joined ${match.title}! Slot confirmed.`);
  };

  // Add Money Handler
  const handleAddMoneySuccess = (amount: number, upiApp: string) => {
    setWallet((prev) => ({
      ...prev,
      deposit: prev.deposit + amount,
      total: prev.total + amount,
    }));

    const newTx: WalletTransaction = {
      id: `tx_${Date.now()}`,
      type: 'deposit',
      title: `UPI Add Money (${upiApp})`,
      amount,
      date: 'Just now',
      status: 'Completed',
      refId: `upi_${Date.now()}`,
    };
    setTransactions((prev) => [newTx, ...prev]);

    setShowAddMoney(false);
    addToast('success', `₹${amount} deposited successfully via ${upiApp}!`);
  };

  // Withdraw Handler
  const handleWithdrawSuccess = (amount: number, upiId: string) => {
    setWallet((prev) => ({
      ...prev,
      winnings: prev.winnings - amount,
      total: prev.total - amount,
    }));

    const newTx: WalletTransaction = {
      id: `tx_${Date.now()}`,
      type: 'withdrawal',
      title: `Instant UPI Payout to ${upiId}`,
      amount: -amount,
      date: 'Just now',
      status: 'Completed',
      refId: `wth_${Date.now()}`,
    };
    setTransactions((prev) => [newTx, ...prev]);

    setShowWithdraw(false);
    addToast('success', `₹${amount} payout initiated to ${upiId}!`);
  };

  // Daily Check In Handler
  const handleDailyCheckIn = () => {
    if (hasCheckedInToday) return;
    setHasCheckedInToday(true);
    setCoins((prev) => prev + 60);
    setWallet((prev) => ({
      ...prev,
      bonus: prev.bonus + 10,
      total: prev.total + 10,
    }));
    addToast('success', '+60 Loyalty Coins and ₹10 Cash Bonus claimed!');
  };

  // Spin Wheel Win Handler
  const handleSpinWin = (winCoins: number, label: string) => {
    setHasSpunToday(true);
    setCoins((prev) => prev + winCoins);
    addToast('success', `Prize claimed: ${label}!`);
  };

  // Claim Mission Handler
  const handleClaimMission = (missionId: string) => {
    const mis = missions.find((m) => m.id === missionId);
    if (!mis || mis.isClaimed) return;

    setMissions((prev) =>
      prev.map((m) => (m.id === missionId ? { ...m, isClaimed: true } : m))
    );
    setCoins((prev) => prev + mis.rewardCoins);
    addToast('success', `Claimed +${mis.rewardCoins} Loyalty Coins!`);
  };

  // Redeem Store Item
  const handleRedeemItem = (item: RewardStoreItem) => {
    if (coins < item.coins) {
      addToast('error', 'Insufficient coins to redeem this item.');
      return;
    }
    setCoins((prev) => prev - item.coins);
    addToast('success', `Redeemed ${item.title}! Check your inventory.`);
  };

  // Copy helper
  const handleCopyText = (text: string) => {
    navigator.clipboard?.writeText(text);
    addToast('info', 'Copied to clipboard!');
  };

  // Dispute Report submit
  const handleReportSubmit = (data: { category: string; matchId: string; description: string; proofUrl: string }) => {
    setShowReportModal(false);
    addToast('success', `Dispute ticket #${Math.floor(1000 + Math.random() * 9000)} submitted to referee.`);
  };

  const unreadNotifsCount = notifications.filter((n) => !n.isRead).length;

  return (
    <div className="min-h-screen bg-[#0A0A0F] text-[#F5F5FA] font-sora selection:bg-[#C8FF3D] selection:text-black flex justify-center">
      {/* Toast Alert Manager */}
      <ToastContainer toasts={toasts} onDismiss={dismissToast} />

      {/* Mobile-first centered frame container (360px - 430px) */}
      <div className="w-full max-w-[430px] min-h-screen bg-[#0A0A0F] border-x border-[#23233A]/50 relative flex flex-col shadow-2xl">
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
              wallet={wallet}
              coins={coins}
              matches={matches}
              leaderboard={INITIAL_LEADERBOARD}
              unreadNotifsCount={unreadNotifsCount}
              onOpenNotifications={() => setShowNotifications(true)}
              onOpenWallet={() => setCurrentTab('wallet')}
              onOpenSpinWheel={() => setShowSpinWheel(true)}
              onOpenLeaderboard={() => setShowLeaderboard(true)}
              onOpenMatchDetail={(m) => setSelectedMatchForDetail(m)}
              onSelectCategory={(cat) => setCurrentTab('matches')}
              onDailyCheckIn={handleDailyCheckIn}
              hasCheckedInToday={hasCheckedInToday}
              onShareReferral={() => handleCopyText(`Join me on FF Arena with my referral code ${profile.referralCode} to get ₹25 free!`)}
              onOpenSupport={() => setShowReportModal(true)}
            />
          )}

          {currentTab === 'matches' && (
            <MatchesTab
              matches={matches}
              onOpenMatchDetail={(m) => setSelectedMatchForDetail(m)}
              onJoinMatch={(m) => setSelectedMatchForJoin(m)}
              onOpenReportModal={(matchId) => {
                setReportDefaultMatchId(matchId);
                setShowReportModal(true);
              }}
              onCopyText={handleCopyText}
            />
          )}

          {currentTab === 'wallet' && (
            <WalletTab
              wallet={wallet}
              transactions={transactions}
              isKycVerified={profile.isKycVerified}
              onOpenAddMoney={() => setShowAddMoney(true)}
              onOpenWithdraw={() => setShowWithdraw(true)}
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
              onUpdateProfile={(updated) => setProfile((p) => ({ ...p, ...updated }))}
              onCopyText={handleCopyText}
              onSwitchToAdmin={onSwitchToAdmin}
              onLogout={() => addToast('info', 'Logged out of player account.')}
              onOpenSupport={() => setShowReportModal(true)}
            />
          )}
        </div>

        {/* Floating Pill Bottom Navigation (5 tabs with glow) */}
        <div className="fixed bottom-3 left-1/2 -translate-x-1/2 z-40 w-[94%] max-w-[400px]">
          <nav className="bg-[#14141F]/90 backdrop-blur-xl border border-[#23233A] rounded-full p-1.5 flex items-center justify-between shadow-[0_8px_32px_rgba(0,0,0,0.8)]">
            {[
              { id: 'home', label: 'Home', icon: Home },
              { id: 'matches', label: 'Matches', icon: Trophy },
              { id: 'wallet', label: 'Wallet', icon: Wallet },
              { id: 'rewards', label: 'Rewards', icon: Award },
              { id: 'profile', label: 'Profile', icon: User },
            ].map((tab) => {
              const Icon = tab.icon;
              const isActive = currentTab === tab.id;
              return (
                <button
                  key={tab.id}
                  onClick={() => setCurrentTab(tab.id as any)}
                  className={`flex flex-col items-center justify-center flex-1 py-1.5 px-1 rounded-full transition-all duration-300 relative ${
                    isActive
                      ? 'text-[#C8FF3D] font-black'
                      : 'text-[#9498AE] hover:text-white font-medium'
                  }`}
                >
                  {/* Active glow backing pill */}
                  {isActive && (
                    <div className="absolute inset-0 bg-[#C8FF3D]/15 rounded-full border border-[#C8FF3D]/40 shadow-[0_0_15px_rgba(200,255,61,0.25)] -z-10" />
                  )}

                  <Icon className={`w-5 h-5 transition-transform duration-200 ${isActive ? 'scale-110' : ''}`} />
                  <span className="text-[10px] mt-0.5 tracking-tight">{tab.label}</span>
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
          wallet={wallet}
          onClose={() => setSelectedMatchForJoin(null)}
          onSuccess={handleJoinSuccess}
          onOpenAddMoney={() => {
            setSelectedMatchForJoin(null);
            setShowAddMoney(true);
          }}
        />

        {showAddMoney && (
          <AddMoneyModal
            onClose={() => setShowAddMoney(false)}
            onSuccess={handleAddMoneySuccess}
          />
        )}

        {showWithdraw && (
          <WithdrawModal
            wallet={wallet}
            linkedUpi={profile.linkedUpiId}
            isKycVerified={profile.isKycVerified}
            onClose={() => setShowWithdraw(false)}
            onSuccess={handleWithdrawSuccess}
          />
        )}

        {showSpinWheel && (
          <SpinWheelModal
            hasSpunToday={hasSpunToday}
            onClose={() => setShowSpinWheel(false)}
            onWin={handleSpinWin}
          />
        )}

        {showLeaderboard && (
          <LeaderboardModal
            players={INITIAL_LEADERBOARD}
            onClose={() => setShowLeaderboard(false)}
          />
        )}

        {showNotifications && (
          <NotificationsModal
            notifications={notifications}
            onClose={() => setShowNotifications(false)}
            onMarkAllRead={() => {
              setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
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
      </div>
    </div>
  );
};
