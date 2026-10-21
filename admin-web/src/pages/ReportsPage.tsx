import React, { useState, useEffect } from 'react';
import { AlertTriangle, CheckCircle2, MessageSquare, Shield, Clock } from 'lucide-react';
import { Report, ReportStatus } from '../types';
import { AdminService } from '../services/adminService';
import { Badge } from '../components/common/Badge';
import { Modal } from '../components/common/Modal';

export const ReportsPage: React.FC = () => {
  const [reports, setReports] = useState<Report[]>([]);
  const [selectedReport, setSelectedReport] = useState<Report | null>(null);
  const [replyNotes, setReplyNotes] = useState('');
  const [reportStatus, setReportStatus] = useState<ReportStatus>('Open');
  const [notice, setNotice] = useState<string | null>(null);

  useEffect(() => {
    loadReports();
  }, []);

  const loadReports = async () => {
    const list = await AdminService.getReports();
    setReports(list);
  };

  const handleOpenReport = (r: Report) => {
    setSelectedReport(r);
    setReportStatus(r.status);
    setReplyNotes(r.adminNotes || '');
  };

  const handleSaveReport = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedReport) return;

    await AdminService.updateReport(selectedReport.id, reportStatus, replyNotes);
    await loadReports();
    setSelectedReport(null);
    setNotice(`Report #${selectedReport.id} marked as ${reportStatus}.`);
    setTimeout(() => setNotice(null), 3000);
  };

  return (
    <div className="space-y-6">
      {notice && (
        <div className="p-4 bg-[#C8FF3D]/10 border border-[#C8FF3D]/30 rounded-[18px] text-xs font-bold text-[#C8FF3D] animate-fade-in flex items-center gap-2">
          <CheckCircle2 className="w-4 h-4 shrink-0" />
          <span>{notice}</span>
        </div>
      )}

      {/* Reports Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden">
        <div className="p-4 bg-[#0A0A0F] border-b border-[#23233A] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <AlertTriangle className="w-4 h-4 text-[#FF7A8A]" />
            <h3 className="font-extrabold text-[#F5F5FA] text-sm">Player Inquiries & Anti-Cheat Disputes</h3>
          </div>
          <span className="text-xs text-[#9498AE]">Response SLA: Within 4 hours of match completion</span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs text-[#9498AE]">
            <thead className="bg-[#0A0A0F]/50 text-[#F5F5FA] uppercase font-bold tracking-wider text-[11px] border-b border-[#23233A]">
              <tr>
                <th className="px-6 py-4">Category</th>
                <th className="px-4 py-4">Reporter</th>
                <th className="px-4 py-4">Target Player</th>
                <th className="px-4 py-4">Tournament</th>
                <th className="px-4 py-4">Status</th>
                <th className="px-4 py-4">Timestamp</th>
                <th className="px-6 py-4 text-right">Review</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {reports.map((r) => (
                <tr key={r.id} className="hover:bg-[#1A1A28]/50 transition-colors">
                  <td className="px-6 py-4">
                    <Badge variant={r.category === 'cheating' ? 'red' : 'amber'}>
                      {r.category.replace('_', ' ')}
                    </Badge>
                  </td>
                  <td className="px-4 py-4 font-bold text-[#F5F5FA]">{r.reporterName}</td>
                  <td className="px-4 py-4 font-bold text-[#FF7A8A]">{r.reportedName || 'Tournament Host'}</td>
                  <td className="px-4 py-4 text-[#F5F5FA]">{r.tournamentName || 'General Arena'}</td>
                  <td className="px-4 py-4">
                    <Badge variant={r.status === 'Resolved' ? 'lime' : r.status === 'In review' ? 'violet' : 'amber'}>
                      {r.status}
                    </Badge>
                  </td>
                  <td className="px-4 py-4">{r.createdAt}</td>
                  <td className="px-6 py-4 text-right">
                    <button
                      onClick={() => handleOpenReport(r)}
                      className="px-3 py-1 bg-[#23233A] hover:bg-[#2E2E4A] text-xs font-bold text-[#F5F5FA] rounded-xl"
                    >
                      Inspect Ticket
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Review Modal */}
      {selectedReport && (
        <Modal
          isOpen={true}
          onClose={() => setSelectedReport(null)}
          title={`Dispute Ticket #${selectedReport.id}`}
          subtitle={`Category: ${selectedReport.category.toUpperCase()}`}
          maxWidth="max-w-md"
        >
          <form onSubmit={handleSaveReport} className="space-y-4">
            <div className="p-3 bg-[#0A0A0F] border border-[#23233A] rounded-[16px] space-y-2">
              <span className="text-[10px] uppercase font-bold text-[#9498AE] block">Player Grievance</span>
              <p className="text-xs text-[#F5F5FA] leading-relaxed">{selectedReport.details}</p>
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
                Ticket Status
              </label>
              <select
                value={reportStatus}
                onChange={(e) => setReportStatus(e.target.value as ReportStatus)}
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] px-3.5 py-2 text-xs focus:outline-none"
              >
                <option value="Open">Open</option>
                <option value="In review">In Review (Referee Examining Logs)</option>
                <option value="Resolved">Resolved</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-semibold text-[#9498AE] uppercase mb-1">
                Referee Internal Notes
              </label>
              <textarea
                rows={3}
                value={replyNotes}
                onChange={(e) => setReplyNotes(e.target.value)}
                placeholder="Log anti-cheat review findings or player resolution..."
                className="w-full bg-[#0A0A0F] border border-[#2E2E4A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] p-3 text-xs focus:outline-none"
              />
            </div>

            <div className="flex justify-end gap-2 pt-3 border-t border-[#23233A]">
              <button
                type="button"
                onClick={() => setSelectedReport(null)}
                className="px-4 py-2 text-xs font-bold text-[#9498AE]"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2.5 rounded-[18px] text-xs font-extrabold bg-[#C8FF3D] text-black"
              >
                UPDATE DISPUTE
              </button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
};
