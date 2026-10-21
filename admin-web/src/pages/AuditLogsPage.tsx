import React, { useState, useEffect } from 'react';
import { FileText, Search, ShieldCheck, Download, Filter } from 'lucide-react';
import { AuditLog } from '../types';
import { AdminService } from '../services/adminService';
import { Badge } from '../components/common/Badge';

export const AuditLogsPage: React.FC = () => {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [search, setSearch] = useState('');
  const [roleFilter, setRoleFilter] = useState('all');

  useEffect(() => {
    loadLogs();
  }, []);

  const loadLogs = async () => {
    const data = await AdminService.getAuditLogs();
    setLogs(data);
  };

  const filtered = logs.filter((log) => {
    const matchSearch =
      log.action.toLowerCase().includes(search.toLowerCase()) ||
      log.targetEntity.toLowerCase().includes(search.toLowerCase()) ||
      log.adminEmail.toLowerCase().includes(search.toLowerCase()) ||
      log.details.toLowerCase().includes(search.toLowerCase());
    const matchRole = roleFilter === 'all' || log.adminRole === roleFilter;
    return matchSearch && matchRole;
  });

  const handleExportCsv = () => {
    const headers = ['Timestamp', 'Admin Email', 'Admin Role', 'Action', 'Target Entity', 'Details'];
    const rows = filtered.map((l) => [
      `"${l.timestamp}"`,
      `"${l.adminEmail}"`,
      `"${l.adminRole}"`,
      `"${l.action}"`,
      `"${l.targetEntity}"`,
      `"${l.details.replace(/"/g, '""')}"`,
    ]);
    const csvContent = [headers.join(','), ...rows.map((r) => r.join(','))].join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `FFArena_AuditLogs_${Date.now()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="space-y-6">
      {/* Top Banner */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] p-5 shadow-xl flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div className="flex items-center space-x-3">
          <div className="p-3 bg-[#C8FF3D]/10 text-[#C8FF3D] rounded-[16px]">
            <ShieldCheck className="w-6 h-6" />
          </div>
          <div>
            <h2 className="font-extrabold text-[#F5F5FA] text-base">Security Audit Log</h2>
            <p className="text-xs text-[#9498AE]">Immutable tracking of administrative modifications and coin adjustments</p>
          </div>
        </div>

        <button
          onClick={handleExportCsv}
          className="flex items-center space-x-2 bg-[#23233A] hover:bg-[#2E2E4A] text-[#F5F5FA] px-4 py-2.5 rounded-[16px] text-xs font-bold transition-all self-start sm:self-auto"
        >
          <Download className="w-4 h-4 text-[#C8FF3D]" />
          <span>Export Audit CSV</span>
        </button>
      </div>

      {/* Filter and Search */}
      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search className="w-4 h-4 text-[#9498AE] absolute left-4 top-3" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search action, target entity, or admin email..."
            className="w-full bg-[#14141F] border border-[#23233A] focus:border-[#C8FF3D] text-[#F5F5FA] rounded-[16px] pl-11 pr-4 py-2.5 text-xs focus:outline-none"
          />
        </div>

        <select
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value)}
          className="bg-[#14141F] border border-[#23233A] text-[#F5F5FA] rounded-[16px] px-4 py-2.5 text-xs focus:outline-none"
        >
          <option value="all">All Roles</option>
          <option value="SuperAdmin">SuperAdmin Only</option>
          <option value="Moderator">Moderator Only</option>
          <option value="Support">Support Only</option>
        </select>
      </div>

      {/* Audit Log Table */}
      <div className="bg-[#14141F] border border-[#23233A] rounded-[22px] overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse text-xs">
            <thead>
              <tr className="bg-[#0A0A0F]/80 text-[#9498AE] uppercase text-[10px] tracking-wider border-b border-[#23233A]">
                <th className="py-3 px-4 font-bold">Timestamp</th>
                <th className="py-3 px-4 font-bold">Administrator</th>
                <th className="py-3 px-4 font-bold">Role</th>
                <th className="py-3 px-4 font-bold">Action Taken</th>
                <th className="py-3 px-4 font-bold">Target Resource</th>
                <th className="py-3 px-4 font-bold">Operational Context</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#23233A]">
              {filtered.map((log) => (
                <tr key={log.id} className="hover:bg-[#1A1A2A] transition-colors">
                  <td className="py-3 px-4 text-[#9498AE] font-mono text-[11px] whitespace-nowrap">
                    {log.timestamp}
                  </td>
                  <td className="py-3 px-4 font-bold text-[#F5F5FA]">{log.adminEmail}</td>
                  <td className="py-3 px-4">
                    <Badge
                      variant={
                        log.adminRole === 'SuperAdmin'
                          ? 'lime'
                          : log.adminRole === 'Moderator'
                          ? 'violet'
                          : 'gray'
                      }
                    >
                      {log.adminRole}
                    </Badge>
                  </td>
                  <td className="py-3 px-4 font-extrabold text-[#F5F5FA]">{log.action}</td>
                  <td className="py-3 px-4 font-mono text-[#00E5FF] text-[11px]">{log.targetEntity}</td>
                  <td className="py-3 px-4 text-[#9498AE] max-w-xs truncate" title={log.details}>
                    {log.details}
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan={6} className="py-8 text-center text-[#9498AE]">
                    No audit records match your filters.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
