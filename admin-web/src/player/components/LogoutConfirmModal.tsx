import React from 'react';
import { LogOut, AlertCircle, X } from 'lucide-react';

interface LogoutConfirmModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export const LogoutConfirmModal: React.FC<LogoutConfirmModalProps> = ({
  isOpen,
  onClose,
  onConfirm,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/85 backdrop-blur-md p-4 animate-fade-in">
      <div className="w-full max-w-xs bg-[#14141F] border border-[#262640] rounded-[24px] p-5 shadow-2xl text-center space-y-3.5">
        <div className="w-12 h-12 rounded-2xl bg-red-500/15 border border-red-500/30 text-red-400 mx-auto flex items-center justify-center">
          <LogOut className="w-6 h-6" />
        </div>

        <div>
          <h3 className="text-sm font-black text-white">Log out of FF Arena?</h3>
          <p className="text-xs text-[#A7A7C0] mt-1 leading-relaxed">
            You will need to sign in again to access your custom room passwords and loyalty rewards.
          </p>
        </div>

        <div className="flex gap-2 pt-1">
          <button
            type="button"
            onClick={onClose}
            className="flex-1 py-2.5 bg-[#1B1B2B] hover:bg-[#262640] text-[#A7A7C0] hover:text-white font-bold text-xs rounded-xl border border-[#262640] transition-colors"
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={onConfirm}
            className="flex-1 py-2.5 bg-red-500 hover:bg-red-600 text-white font-extrabold text-xs rounded-xl transition-all shadow active:scale-95"
          >
            Log Out
          </button>
        </div>
      </div>
    </div>
  );
};
