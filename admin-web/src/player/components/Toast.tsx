import React from 'react';
import { CheckCircle2, AlertCircle, Info, X } from 'lucide-react';

export interface ToastMessage {
  id: string;
  type: 'success' | 'error' | 'info';
  message: string;
}

interface ToastProps {
  toasts: ToastMessage[];
  onDismiss: (id: string) => void;
}

export const ToastContainer: React.FC<ToastProps> = ({ toasts, onDismiss }) => {
  if (toasts.length === 0) return null;

  return (
    <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 flex flex-col gap-2 w-[90%] max-w-sm pointer-events-none">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          className={`pointer-events-auto flex items-center justify-between gap-3 px-4 py-3 rounded-2xl shadow-xl backdrop-blur-md border text-sm transition-all animate-bounce-in ${
            toast.type === 'success'
              ? 'bg-[#14141F]/95 border-[#C8FF3D]/40 text-[#C8FF3D]'
              : toast.type === 'error'
              ? 'bg-[#14141F]/95 border-[#FF7A8A]/40 text-[#FF7A8A]'
              : 'bg-[#14141F]/95 border-[#7B5CFF]/40 text-[#F5F5FA]'
          }`}
        >
          <div className="flex items-center gap-2.5">
            {toast.type === 'success' && <CheckCircle2 className="w-5 h-5 text-[#C8FF3D] shrink-0" />}
            {toast.type === 'error' && <AlertCircle className="w-5 h-5 text-[#FF7A8A] shrink-0" />}
            {toast.type === 'info' && <Info className="w-5 h-5 text-[#7B5CFF] shrink-0" />}
            <span className="font-medium text-white">{toast.message}</span>
          </div>
          <button
            onClick={() => onDismiss(toast.id)}
            className="p-1 hover:bg-white/10 rounded-lg text-slate-400 hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>
      ))}
    </div>
  );
};
