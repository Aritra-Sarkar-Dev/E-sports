import { CoinRule, CoinTransaction, StoreItem, UserAccount } from '../types';
import { INITIAL_COIN_RULES, INITIAL_COIN_TRANSACTIONS, INITIAL_STORE_ITEMS, INITIAL_USERS } from '../data/mockData';
import { db } from './firebase';
import { collection, doc, getDoc, getDocs, runTransaction, query, where, orderBy, setDoc } from 'firebase/firestore';

const STORAGE_KEY_COIN_RULES = 'ff_arena_admin_coin_rules';
const STORAGE_KEY_COIN_TX = 'ff_arena_admin_coin_transactions';
const STORAGE_KEY_STORE_ITEMS = 'ff_arena_admin_store_items';
const STORAGE_KEY_USERS = 'ff_arena_admin_users';

function getStored<T>(key: string, defaultValue: T): T {
  try {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : defaultValue;
  } catch {
    return defaultValue;
  }
}

function setStored<T>(key: string, value: T): void {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (e) {
    console.error('Storage error', e);
  }
}

export class CoinService {
  /**
   * Get all coin rules
   */
  static async getCoinRules(): Promise<CoinRule[]> {
    return getStored<CoinRule[]>(STORAGE_KEY_COIN_RULES, INITIAL_COIN_RULES);
  }

  /**
   * Update a coin rule (SuperAdmin only)
   */
  static async updateCoinRule(ruleId: string, coins: number, isEnabled: boolean): Promise<CoinRule> {
    const rules = await this.getCoinRules();
    const index = rules.findIndex((r) => r.id === ruleId);
    if (index === -1) throw new Error('Rule not found');

    const updated = { ...rules[index], coins, isEnabled };
    rules[index] = updated;
    setStored(STORAGE_KEY_COIN_RULES, rules);

    // Sync to Firestore if available
    try {
      await setDoc(doc(db, 'coinRules', ruleId), updated, { merge: true });
    } catch (e) {
      console.warn('Firestore coinRule sync skipped', e);
    }

    return updated;
  }

  /**
   * Get ledger transactions
   */
  static async getAllTransactions(): Promise<CoinTransaction[]> {
    return getStored<CoinTransaction[]>(STORAGE_KEY_COIN_TX, INITIAL_COIN_TRANSACTIONS);
  }

  /**
   * Get transactions for specific user
   */
  static async getUserTransactions(uid: string): Promise<CoinTransaction[]> {
    const all = await this.getAllTransactions();
    return all.filter((tx) => tx.uid === uid);
  }

  /**
   * Manual coin adjustment by Admin with mandatory reason.
   * Atomically updates user balance and writes to coinTransactions ledger.
   * NEVER allows balance to drop below 0.
   */
  static async adminAdjustCoins(
    uid: string,
    amount: number,
    reason: string,
    adminEmail: string
  ): Promise<{ newBalance: number; tx: CoinTransaction }> {
    if (!reason || reason.trim().length < 5) {
      throw new Error('A detailed reason (at least 5 characters) is required for audit compliance.');
    }
    if (amount === 0) {
      throw new Error('Adjustment amount cannot be zero.');
    }

    const users = getStored<UserAccount[]>(STORAGE_KEY_USERS, INITIAL_USERS);
    const userIndex = users.findIndex((u) => u.uid === uid);
    if (userIndex === -1) throw new Error('User not found.');

    const user = users[userIndex];
    const newBalance = user.coinBalance + amount;

    if (newBalance < 0) {
      throw new Error(`Insufficient coin balance! Current balance is ${user.coinBalance}, cannot deduct ${Math.abs(amount)}.`);
    }

    // Update user balance
    user.coinBalance = newBalance;
    users[userIndex] = user;
    setStored(STORAGE_KEY_USERS, users);

    // Create immutable ledger entry
    const newTx: CoinTransaction = {
      id: `ctx_${Date.now()}`,
      uid,
      playerName: user.inGameName,
      type: 'admin_adjust',
      amount,
      reason,
      createdAt: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', month: 'short', day: 'numeric' }),
      createdBy: adminEmail,
    };

    const allTx = await this.getAllTransactions();
    allTx.unshift(newTx);
    setStored(STORAGE_KEY_COIN_TX, allTx);

    // Firestore atomic batch write attempt
    try {
      await runTransaction(db, async (transaction) => {
        const userRef = doc(db, 'users', uid);
        const txRef = doc(collection(db, 'coinTransactions'));
        const userDoc = await transaction.get(userRef);
        const currentFsBalance = userDoc.exists() ? (userDoc.data().coinBalance || 0) : user.coinBalance;
        if (currentFsBalance + amount < 0) {
          throw new Error('Firestore transaction aborted: negative balance prevented.');
        }
        transaction.set(userRef, { coinBalance: currentFsBalance + amount }, { merge: true });
        transaction.set(txRef, { ...newTx, id: txRef.id });
      });
    } catch (e) {
      console.warn('Firestore transaction fallback to local state', e);
    }

    return { newBalance, tx: newTx };
  }

  /**
   * Store items management
   */
  static async getStoreItems(): Promise<StoreItem[]> {
    return getStored<StoreItem[]>(STORAGE_KEY_STORE_ITEMS, INITIAL_STORE_ITEMS);
  }

  static async saveStoreItem(item: StoreItem): Promise<StoreItem> {
    const items = await this.getStoreItems();
    const index = items.findIndex((i) => i.id === item.id);
    if (index >= 0) {
      items[index] = item;
    } else {
      items.push(item);
    }
    setStored(STORAGE_KEY_STORE_ITEMS, items);

    try {
      await setDoc(doc(db, 'storeItems', item.id), item, { merge: true });
    } catch (e) {
      console.warn('Firestore store item sync skipped', e);
    }

    return item;
  }
}
