import React, { useState, useEffect } from 'react';
import {
  Coins,
  Settings2,
  Sliders,
  History,
  ShoppingBag,
  Plus,
  ArrowUpRight,
  ArrowDownRight,
  CheckCircle2,
  AlertCircle,
  ShieldAlert,
  Search,
} from 'lucide-react';
import { CoinRule, CoinTransaction, StoreItem, UserAccount } from '../types';
import { CoinService } from '../services/coinService';
import { AdminService } from '../services/adminService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';
import { useAuth } from '../context/AuthContext';

export const CoinsPage: React.FC = () => {
  const { currentAdmin } = useAuth();
  const [activeTab, setActiveTab] = useState<'rules' | 'adjust' | 'ledger' | 'store'>('rules');

  // Rules state
  const [rules, setRules] = useState<CoinRule[]>([]);
  const [editingRule, setEditingRule] = useState<CoinRule | null>(null);
  const [editCoins, setEditCoins] = useState<number>(0);
  const [editEnabled, setEditEnabled] = useState<boolean>(true);

  // Manual Adjust state
  const [users, setUsers] = useState<UserAccount[]>([]);
  const [selectedUid, setSelectedUid] = useState<string>('usr_001');
  const [adjustAmount, setAdjustAmount] = useState<number>(100);
  const [adjustReason, setAdjustReason] = useState<string>('');
  const [adjustError, setAdjustError] = useState<string | null>(null);

  // Ledger state
  const [transactions, setTransactions] = useState<CoinTransaction[]>([]);
  const [ledgerFilter, setLedgerFilter] = useState<string>('all');
  const [ledgerSearch, setLedgerSearch] = useState<string>('');

  // Store Items state
  const [storeItems, setStoreItems] = useState<StoreItem[]>([]);
  const [isStoreModalOpen, setIsStoreModalOpen] = useState<boolean>(false);
  const [newStoreName, setNewStoreName] = useState<string>('');
  const [newStoreCategory, setNewStoreCategory] = useState<'badge' | 'frame' | 'theme' | 'ticket'>('badge');
  const [newStoreCost, setNewStoreCost] = useState<number>(500);

  const [notice, setNotice] = useState<string | null>(null);

  useEffect(() => {
    loadAllData();
  }, []);

  const loadAllData = async () => {
    const r = await CoinService.getCoinRules();
    setRules(r);
    const tx = await CoinService.getAllTransactions();
    setTransactions(tx);
    const u = await AdminService.getUsers();
    setUsers(u);
    const s = await CoinService.getStoreItems();
    setStoreItems(s);
  };

  const showNotice = (msg: string) => {
    setNotice(msg);
    setTimeout(() => setNotice(null), 3500);
  };

  // Rule Save
  const handleSaveRule = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingRule) return;

    await CoinService.updateCoinRule(editingRule.id, editCoins, editEnabled);
    await loadAllData();
    setEditingRule(null);
    showNotice(`Coin Rule '${editingRule.name}' updated to ${editCoins} coins.`);
  };

  // Manual Adjust Save
  const handleManualAdjust = async (e: React.FormEvent) => {
    e.preventDefault();
    setAdjustError(null);

    try {
      const res = await CoinService.adminAdjustCoins(
        selectedUid,
        adjustAmount,
        adjustReason,
        currentAdmin?.email || 'admin@ffarena.in'
      );
      await loadAllData();
      setAdjustReason('');
      showNotice(`Adjusted ${adjustAmount > 0 ? `+${adjustAmount}` : adjustAmount} coins. New balance: 🪙 ${res.newBalance}.`);
    } catch (err: any) {
      setAdjustError(err.message || 'Adjustment failed');
    }
  };

  // Store Item Save
  const handleSaveStoreItem = async (e: React.FormEvent) => {
    e.preventDefault();
    const item: StoreItem = {
      id: `item_${Date.now()}`,
      name: newStoreName,
      category: newStoreCategory,
      costCoins: Number(newStoreCost),
      previewUrl: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=200',
      isAvailable: true,
    };
    await CoinService.saveStoreItem(item);
    await loadAllData();
    setIsStoreModalOpen(false);
    setNewStoreName('');
    showNotice(`Store asset '${item.name}' published.`);
  };

  const selectedUserForAdjust = users.find((u) => u.uid === selectedUid);

  return (
    <div className="space-y-6">
      {/* Notice Banner */}
      {notice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] text-xs font-bold text-[#C8FF3D] animate-fade-in flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{notice}</span>
        </div>
      )}

      {/* Compliance Rule Header */}
      <div className="p-4 bg-[#14141F] border border-[#23233A] rounded-[22px] flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-[#FFB300]/15 flex items-center justify-center text-[#FFB300]">
            <Coins className="w-5 h-5 stroke-2" />
          </div>
          <div>
            <span className="font-extrabold text-[#F5F5FA] text-sm block">Non-Cash Loyalty Coin System</span>
            <span className="text-xs text-[#9498AE]">
              Coins cannot be bought, withdrawn, or converted to currency (India Gaming Law Compliant)
            </span>
          </div>
        </div>
        <div className="hidden sm:flex items-center gap-2">
          <Badge variant="lime">Atomic Firestore Ledger</Badge>
          <Badge variant="amber">Zero Negative Balance Rule</Badge>
        </div>
      </div>

      {/* Tabs Row */}
      <div className="flex border-b border-[#23233A] space-x-6">
        {[
          { id: 'rules', label: '1. Coin Rules', icon: Sliders },
          { id: 'adjust', label: '2. Manual Adjust', icon: Settings2 },
          { id: 'ledger', label: '3. User Coin Ledger', icon: History },
          { id: 'store', label: '4. Store Items Manager', icon: ShoppingBag },
        ].map((tab) => {
          const Icon = tab.icon;
          const isActive = activeTab === tab.id;
          return (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id as any)}
              className={`pb-3 text-xs font-extrabold flex items-center gap-2 border-b-2 transition-all ${
                isActive ? 'border-[#C8FF3D] text-[#C8FF3D]' : 'border-transparent text-[#9498AE] hover:text-[#F5F5FA]'
              }`}
            >
              <Icon className="w-4 h-4" />
              <span>{tab.label}</span>
            </button>
          );
        })}
      </div>

      {/* TAB 1: Coin Rules */}
      {activeTab === 'rules' && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span className="text-xs text-[#9498AE] font-semibold">
              Configure loyalty points awarded for tournament participation, milestones, and logins.
            </span>
            <span className="text-xs text-[#C8FF3D] font-bold">Requires SuperAdmin permissions</span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {rules.map((rule) => (
              <div
                key={rule.id}
                className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 flex flex-col justify-between hover:border-[#7B5CFF]/40 transition-all"
              >
                <div>
                  <div className="flex items-center justify-between mb-2">
                    <Badge variant={rule.isEnabled ? 'lime' : 'gray'}>
                      {rule.isEnabled ? 'Active Rule' : 'Disabled'}
                    </Badge>
                    <span className="font-extrabold text-lg text-[#C8FF3D]">+{rule.coins} 🪙</span>
                  </div>
                  <h4 className="font-extrabold text-[#F5F5FA] text-sm mb-1">{rule.name}</h4>
                  <p className="text-xs text-[#9498AE] leading-relaxed">{rule.description}</p>
                </div>

                <div className="mt-5 pt-3 border-t border-[#23233A] flex items-center justify-between">
                  <span className="text-[10px] font-mono text-[#7B5CFF]">{rule.actionKey}</span>
                  <button
                    onClick={() => {
                      setEditingRule(rule);
                      setEditCoins(rule.coins);
                      setEditEnabled(rule.isEnabled);
                    }}
                    className="px-3 py-1 bg-[#23233A] hover:bg-[#2E2E4A] text-xs font-bold text-[#F5F5FA] rounded-xl transition-colors"
                  >
                    Edit Rule
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* TAB 2: Manual Adjust */}
      {activeTab === 'adjust' && (
        <div className="max-w-xl mx-auto bg-[#14141F] border border-[#23233A] rounded-[22px] p-6 space-y-5">
          <div>
            <h3 className="font-extrabold text-base text-[#F5F5FA]">Manual Coin Adjustment</h3>
            <p className="text-xs text-[#9498AE] mt-0.5">
              Credits or debits player coin balances. Every change is immutably logged to the ledger.
            </p>
          </div>

          {adjustError && (
            <div className="p-3.5 bg-[#FF7A8A]/10 border border-[#FF7A8A]/30 rounded-[16px] flex items-center gap-2.5 text-xs text-[#FF7A8A]">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{adjustError}</span>
            </div>
          )}

          <form onSubmit={handleManualAdjust} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                Target Player *
              </label>
              <select
                value={selectedUid}
                onChange={(e) => setSelectedUid(e.target.value)}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2.5 text-xs focus:outline-none"
              >
                {users.map((u) => (
                  <option key={u.uid} value={u.uid}>
                    {u.inGameName} ({u.name}) — Balance: 🪙 {u.coinBalance} (UID: {u.ffUid})
                  </option>
                ))}
              </select>
            </div>

            {selectedUserForAdjust && (
              <div className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] flex items-center justify-between text-xs">
                <span className="text-[#9498AE]">Current Player Balance</span>
                <span className="font-extrabold text-[#FFB300] text-sm">
                  🪙 {selectedUserForAdjust.coinBalance} Coins
                </span>
              </div>
            )}

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                Adjustment Amount (Positive to Credit, Negative to Deduct) *
              </label>
              <input
                type="number"
                required
                value={adjustAmount}
                onChange={(e) => setAdjustAmount(Number(e.target.value))}
                placeholder="e.g. 150 or -50"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#C8FF3D] font-extrabold rounded-[16px] px-4 py-2.5 text-sm focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1.5">
                Mandatory Reason for Audit Trail *
              </label>
              <textarea
                rows={3}
                required
                value={adjustReason}
                onChange={(e) => setAdjustReason(e.target.value)}
                placeholder="e.g. Referee compensation for technical server disconnect during Match #4"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] p-3 text-xs focus:outline-none"
              />
            </div>

            <button
              type="submit"
              className="w-full bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold py-3.5 rounded-[18px] text-xs transition-all shadow-md shadow-[#C8FF3D]/10"
            >
              EXECUTE ATOMIC BALANCE ADJUSTMENT
            </button>
          </form>
        </div>
      )}

      {/* TAB 3: User Coin Ledger */}
      {activeTab === 'ledger' && (
        <div className="space-y-4">
          <div className="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4">
            <div className="relative flex-1 max-w-md">
              <Search className="w-4 h-4 text-[#9498AE] absolute left-3.5 top-3" />
              <input
                type="text"
                value={ledgerSearch}
                onChange={(e) => setLedgerSearch(e.target.value)}
                placeholder="Search ledger by player or reason..."
                className="w-full bg-[#14141F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-10 pr-4 py-2 text-xs focus:outline-none"
              />
            </div>

            <select
              value={ledgerFilter}
              onChange={(e) => setLedgerFilter(e.target.value)}
              className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-3.5 py-2 text-xs focus:outline-none"
            >
              <option value="all">All Transaction Types</option>
              <option value="earn">Earn (Rewards)</option>
              <option value="spend">Spend (Store Items)</option>
              <option value="admin_adjust">Admin Manual Adjustments</option>
            </select>
          </div>

          <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs text-[#9498AE]">
                <thead className="bg-[#0A0A0F] text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
                  <tr>
                    <th className="px-6 py-4">Player</th>
                    <th className="px-4 py-4">Type</th>
                    <th className="px-4 py-4">Amount</th>
                    <th className="px-6 py-4">Reason & Reference</th>
                    <th className="px-4 py-4">Timestamp</th>
                    <th className="px-6 py-4 text-right">Auditor</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#23233A]">
                  {transactions
                    .filter((t) => {
                      const matchesType = ledgerFilter === 'all' || t.type === ledgerFilter;
                      const matchesSearch =
                        (t.playerName && t.playerName.toLowerCase().includes(ledgerSearch.toLowerCase())) ||
                        t.reason.toLowerCase().includes(ledgerSearch.toLowerCase());
                      return matchesType && matchesSearch;
                    })
                    .map((tx) => (
                      <tr key={tx.id} className="hover:bg-[#1A1A28]/50 transition-colors">
                        <td className="px-6 py-4 font-bold text-[#F5F5FA]">
                          {tx.playerName || tx.uid}
                        </td>
                        <td className="px-4 py-4">
                          <Badge variant={tx.type === 'earn' ? 'lime' : tx.type === 'spend' ? 'violet' : 'amber'}>
                            {tx.type}
                          </Badge>
                        </td>
                        <td className="px-4 py-4 font-extrabold text-sm">
                          <span className={tx.amount > 0 ? 'text-[#C8FF3D]' : 'text-[#FF7A8A]'}>
                            {tx.amount > 0 ? `+${tx.amount}` : tx.amount}
                          </span>
                        </td>
                        <td className="px-6 py-4 text-[#F5F5FA] font-medium">{tx.reason}</td>
                        <td className="px-4 py-4">{tx.createdAt}</td>
                        <td className="px-6 py-4 text-right font-mono text-[11px] text-[#9498AE]">
                          {tx.createdBy}
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      {/* TAB 4: Store Items Manager */}
      {activeTab === 'store' && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <span className="text-xs text-[#9498AE]">
              Loyalty Store Catalog: Players redeem non-cash coins for badges, frames, and tournament passes.
            </span>
            <button
              onClick={() => setIsStoreModalOpen(true)}
              className="inline-flex items-center gap-1.5 bg-[#C8FF3D] hover:bg-[#b8f02d] text-black font-extrabold px-4 py-2 rounded-[16px] text-xs transition-all"
            >
              <Plus className="w-4 h-4 stroke-2" />
              <span>ADD STORE ITEM</span>
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {storeItems.map((item) => (
              <div
                key={item.id}
                className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 flex flex-col justify-between"
              >
                <div>
                  <div className="h-32 w-full bg-[#0A0A0F] rounded-[16px] overflow-hidden mb-3 border border-[#23233A] relative">
                    <img
                      src={item.previewUrl}
                      alt={item.name}
                      className="w-full h-full object-cover"
                    />
                    <div className="absolute top-2 right-2">
                      <Badge variant="violet">{item.category}</Badge>
                    </div>
                  </div>
                  <h4 className="font-extrabold text-[#F5F5FA] text-sm">{item.name}</h4>
                </div>

                <div className="mt-4 pt-3 border-t border-[#23233A] flex items-center justify-between">
                  <span className="font-extrabold text-sm text-[#FFB300]">🪙 {item.costCoins}</span>
                  <span className="text-[11px] font-bold text-[#C8FF3D]">In Stock</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Edit Rule Modal */}
      {editingRule && (
        <Modal
          isOpen={true}
          onClose={() => setEditingRule(null)}
          title={`Edit Rule: ${editingRule.name}`}
          subtitle="Configure coins granted per trigger"
          maxWidth="max-w-md"
        >
          <form onSubmit={handleSaveRule} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
                Coins Awarded *
              </label>
              <input
                type="number"
                min="1"
                required
                value={editCoins}
                onChange={(e) => setEditCoins(Number(e.target.value))}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#C8FF3D] font-extrabold rounded-[16px] px-4 py-2 text-sm focus:outline-none"
              />
            </div>

            <div className="flex items-center gap-2 pt-1">
              <input
                type="checkbox"
                id="ruleEnabled"
                checked={editEnabled}
                onChange={(e) => setEditEnabled(e.target.checked)}
                className="rounded text-[#C8FF3D] bg-[#0A0A0F] border-[#2E2E4A]"
              />
              <label htmlFor="ruleEnabled" className="text-xs font-bold text-[#F5F5FA]">
                Rule Enabled (Automatically issued by esports score engine)
              </label>
            </div>

            <div className="flex justify-end gap-2 pt-3 border-t border-[#23233A]">
              <button
                type="button"
                onClick={() => setEditingRule(null)}
                className="px-4 py-2 text-xs font-bold text-[#9498AE]"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black"
              >
                SAVE RULE
              </button>
            </div>
          </form>
        </Modal>
      )}

      {/* Add Store Item Modal */}
      {isStoreModalOpen && (
        <Modal
          isOpen={true}
          onClose={() => setIsStoreModalOpen(false)}
          title="Add New Loyalty Store Asset"
          subtitle="Non-cash items redeemable via loyalty points"
          maxWidth="max-w-md"
        >
          <form onSubmit={handleSaveStoreItem} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Item Title *</label>
              <input
                type="text"
                required
                value={newStoreName}
                onChange={(e) => setNewStoreName(e.target.value)}
                placeholder="e.g. Cyber Dragon Profile Avatar Frame"
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-4 py-2 text-xs focus:outline-none"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Category</label>
              <select
                value={newStoreCategory}
                onChange={(e) => setNewStoreCategory(e.target.value as any)}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] text-[#F5F5FA] rounded-[16px] px-3 py-2 text-xs focus:outline-none"
              >
                <option value="badge">Badge</option>
                <option value="frame">Avatar Frame</option>
                <option value="theme">Profile Card Theme</option>
                <option value="ticket">Free Entry Tournament Pass</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">Coin Price *</label>
              <input
                type="number"
                min="10"
                required
                value={newStoreCost}
                onChange={(e) => setNewStoreCost(Number(e.target.value))}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#FFB300] font-extrabold rounded-[16px] px-4 py-2 text-sm focus:outline-none"
              />
            </div>

            <div className="flex justify-end gap-2 pt-3 border-t border-[#23233A]">
              <button
                type="button"
                onClick={() => setIsStoreModalOpen(false)}
                className="px-4 py-2 text-xs font-bold text-[#9498AE]"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black"
              >
                ADD TO STORE
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
};
