import React, { createContext, useContext, useState, useEffect } from 'react';
import { AdminUser, AdminRole } from '../types';
import { INITIAL_ADMINS } from '../data/mockData';

interface AuthContextType {
  currentAdmin: AdminUser | null;
  login: (email: string, pass: string) => Promise<boolean>;
  logout: () => void;
  switchRole: (role: AdminRole) => void;
  hasRole: (allowedRoles: AdminRole[]) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const STORAGE_KEY_AUTH = 'ff_arena_admin_auth_user';

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [currentAdmin, setCurrentAdmin] = useState<AdminUser | null>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_AUTH);
      return saved ? JSON.parse(saved) : INITIAL_ADMINS[0]; // Default to SuperAdmin for instant testing
    } catch {
      return INITIAL_ADMINS[0];
    }
  });

  useEffect(() => {
    if (currentAdmin) {
      localStorage.setItem(STORAGE_KEY_AUTH, JSON.stringify(currentAdmin));
    } else {
      localStorage.removeItem(STORAGE_KEY_AUTH);
    }
  }, [currentAdmin]);

  const login = async (email: string, pass: string): Promise<boolean> => {
    // In production, verifies Firebase Auth custom claims (request.auth.token.role)
    const matched = INITIAL_ADMINS.find((a) => a.email.toLowerCase() === email.toLowerCase());
    if (matched) {
      setCurrentAdmin(matched);
      return true;
    }
    // Block non-admins
    throw new Error('Access denied. This account does not possess FF Arena administrator privileges.');
  };

  const logout = () => {
    setCurrentAdmin(null);
  };

  const switchRole = (role: AdminRole) => {
    const adminForRole = INITIAL_ADMINS.find((a) => a.role === role);
    if (adminForRole) {
      setCurrentAdmin(adminForRole);
    }
  };

  const hasRole = (allowedRoles: AdminRole[]): boolean => {
    if (!currentAdmin) return false;
    return allowedRoles.includes(currentAdmin.role);
  };

  return (
    <AuthContext.Provider value={{ currentAdmin, login, logout, switchRole, hasRole }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error('useAuth must be used within an AuthProvider');
  return context;
};
