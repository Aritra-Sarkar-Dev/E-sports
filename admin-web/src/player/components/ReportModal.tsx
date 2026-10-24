import React, { useState } from 'react';
import { X, ShieldAlert, Send } from 'lucide-react';

interface ReportModalProps {
  onClose: () => void;
  onSubmit: (data: { category: string; matchId: string; description: string; proofUrl: string }) => void;
  defaultMatchId?: string;
}

export const ReportModal: React.FC<ReportModalProps> = ({ onClose, onSubmit, defaultMatchId }) => {
  const [category, setCategory] = useState<'hacker' | 'scorecard' | 'room_issue' | 'other'>('scorecard');
  const [matchId, setMatchId] = useState(defaultMatchId || 'm1');
  const [description, setDescription] = useState('');
  const [proofUrl, setProofUrl] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setTimeout(() => {
      setIsSubmitting(false);
      onSubmit({ category, matchId, description, proofUrl });
    }, 800);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center bg-black/85 backdrop-blur-md p-0 sm:p-4">
      <div className="w-full max-w-md bg-[#14141F] border border-[#23233A] rounded-t-[28px] sm:rounded-[24px] overflow-hidden max-h-[90vh] flex flex-col animate-slide-up shadow-2xl">
        <div className="p-4 border-b border-[#23233A] flex items-center justify-between bg-[#0E0E17]">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded-full bg-red-500/20 flex items-center justify-center text-red-400">
              <ShieldAlert className="w-4 h-4" />
            </div>
            <div>
              <h3 className="text-sm font-bold text-white">Dispute & Match Proof Center</h3>
              <p className="text-[11px] text-[#9498AE]">Referee assistance & screenshot proof</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 hover:bg-white/10 rounded-full text-[#9498AE] hover:text-white"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-4 space-y-4 overflow-y-auto">
          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">Issue Category</label>
            <div className="grid grid-cols-2 gap-2">
              {[
                { id: 'scorecard', label: 'Submit Scorecard' },
                { id: 'hacker', label: 'Report Cheater / Hacker' },
                { id: 'room_issue', label: 'Room Password Issue' },
                { id: 'other', label: 'Payout / Other Query' },
              ].map((c) => (
                <button
                  key={c.id}
                  type="button"
                  onClick={() => setCategory(c.id as any)}
                  className={`p-2.5 rounded-xl border text-xs font-bold text-left transition-all ${
                    category === c.id
                      ? 'bg-[#1C1C2E] border-[#C8FF3D] text-[#C8FF3D]'
                      : 'bg-[#0E0E17] border-[#23233A] text-slate-300'
                  }`}
                >
                  {c.label}
                </button>
              ))}
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">Match Reference ID</label>
            <input
              type="text"
              required
              value={matchId}
              onChange={(e) => setMatchId(e.target.value)}
              className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
              placeholder="e.g. m1 or CYBERFRAY 2077"
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">
              Screenshot / Video Proof Link (Drive, Imgur, or URL)
            </label>
            <input
              type="url"
              value={proofUrl}
              onChange={(e) => setProofUrl(e.target.value)}
              className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl px-3.5 py-2.5 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
              placeholder="https://drive.google.com/file/..."
            />
          </div>

          <div>
            <label className="text-xs font-semibold text-[#9498AE] block mb-1">Explanation / Remarks</label>
            <textarea
              required
              rows={3}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full bg-[#0E0E17] border border-[#23233A] rounded-xl p-3 text-xs text-white focus:outline-none focus:border-[#C8FF3D]"
              placeholder="Provide exact details for referee review..."
            />
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full py-3.5 bg-[#C8FF3D] hover:bg-[#b5f02c] text-black font-extrabold rounded-2xl shadow-[0_0_20px_rgba(200,255,61,0.35)] flex items-center justify-center gap-2 active:scale-95 text-xs"
          >
            {isSubmitting ? (
              <div className="w-4 h-4 border-2 border-black border-t-transparent rounded-full animate-spin" />
            ) : (
              <>
                <Send className="w-4 h-4" /> Submit Report to Referee
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
};
