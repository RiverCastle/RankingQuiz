// =============================================
// 인증 확인 및 초기화
// =============================================
const accessToken = sessionStorage.getItem('adminToken');

let currentReportId = null;
let currentQuizId = null;
let allReports = [];

document.addEventListener('DOMContentLoaded', () => {
  if (!accessToken) {
    window.location.href = '/admin/login';
    return;
  }
  document.getElementById('reportsContent').classList.remove('hidden');
  loadReports();
});

// =============================================
// 신고 목록 조회
// =============================================
async function loadReports() {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/reports`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('권한이 없습니다.');
    allReports = await res.json();
    renderReports(allReports);
    updateStats(allReports);
  } catch (e) {
    showToast('목록 조회 실패: ' + e.message);
  }
}

function updateStats(reports) {
  document.getElementById('count-total').textContent = reports.length;
  document.getElementById('count-unread').textContent = reports.filter(r => r.status === 'UNREAD').length;
  document.getElementById('count-resolved').textContent = reports.filter(r => r.status === 'RESOLVED').length;
}

function renderReports(reports) {
  const list = document.getElementById('reportsList');
  const empty = document.getElementById('emptyState');
  list.innerHTML = '';

  if (reports.length === 0) {
    empty.classList.remove('hidden');
    return;
  }
  empty.classList.add('hidden');

  reports.forEach(report => {
    const card = document.createElement('div');
    card.className = 'card-glass rounded-2xl p-4 cursor-pointer hover:bg-white/5 transition-all';
    card.onclick = () => openModal(report.id);

    const statusClass = report.status === 'UNREAD'
      ? 'bg-neon-blue/10 border border-neon-blue/30 text-neon-blue'
      : 'bg-neon-purple/10 border border-neon-purple/30 text-neon-purple';
    const statusLabel = statusLabelOf(report.status);
    const categoryLabel = categoryLabelOf(report.category);
    const dateStr = formatDate(report.reportedAt);

    card.innerHTML = `
      <div class="flex items-start justify-between gap-3 mb-2">
        <span class="inline-block px-2.5 py-0.5 rounded-full text-xs font-bold ${statusClass}">${statusLabel}</span>
        <span class="text-white/30 text-xs flex-shrink-0">${dateStr}</span>
      </div>
      <p class="text-white/80 text-sm leading-relaxed line-clamp-2 mb-2">${escapeHtml(report.question)}</p>
      <div class="flex items-center gap-3 text-xs text-white/40">
        <span>📂 ${categoryLabel}</span>
        <span>👤 ${escapeHtml(report.reporterId ?? '알 수 없음')}</span>
      </div>
    `;
    list.appendChild(card);
  });
}

// =============================================
// 모달 열기 / 닫기
// =============================================
async function openModal(id) {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/reports/${id}`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('상세 조회 실패');
    const report = await res.json();

    currentReportId = report.id;
    currentQuizId = report.quizId;

    const statusClass = report.status === 'UNREAD'
      ? 'bg-neon-blue/10 border border-neon-blue/30 text-neon-blue'
      : 'bg-neon-purple/10 border border-neon-purple/30 text-neon-purple';

    document.getElementById('modal-status-badge').className =
      `inline-block px-3 py-1 rounded-full text-xs font-bold border ${statusClass}`;
    document.getElementById('modal-status-badge').textContent = statusLabelOf(report.status);
    document.getElementById('modal-meta').textContent =
      `${categoryLabelOf(report.category)} · ${formatDate(report.reportedAt)}`;
    document.getElementById('modal-question').textContent = report.question ?? '-';
    document.getElementById('modal-original-answer').textContent = report.originalAnswer ?? '-';
    document.getElementById('modal-user-answer').textContent = report.userAnswer ?? '(없음)';
    document.getElementById('modal-reporter').textContent = report.reporterId ?? '알 수 없음';

    const isResolved = report.status === 'RESOLVED';
    document.getElementById('btn-resolve').disabled = isResolved;
    document.getElementById('btn-resolve').classList.toggle('opacity-40', isResolved);

    document.getElementById('reportModal').classList.remove('hidden');
  } catch (e) {
    showToast('상세 조회 실패: ' + e.message);
  }
}

function closeModal() {
  document.getElementById('reportModal').classList.add('hidden');
  currentReportId = null;
  currentQuizId = null;
}

// =============================================
// 액션 버튼
// =============================================
function goToEdit() {
  if (currentQuizId == null) return;
  window.location.href = `/admin?quizId=${currentQuizId}`;
}

async function resolveReport() {
  if (currentReportId == null) return;
  await patchStatus(currentReportId, 'RESOLVED');
}

async function deleteReport() {
  if (currentReportId == null) return;
  await patchStatus(currentReportId, 'DELETED');
}

async function patchStatus(id, status) {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/reports/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`
      },
      body: JSON.stringify({ status })
    });
    if (!res.ok) throw new Error('상태 변경 실패');

    const label = status === 'RESOLVED' ? '해결 완료' : '삭제';
    showToast(`✅ ${label} 처리되었습니다.`);
    closeModal();
    await loadReports();
  } catch (e) {
    showToast('처리 실패: ' + e.message);
  }
}

// =============================================
// 유틸리티
// =============================================
function statusLabelOf(status) {
  switch (status) {
    case 'UNREAD':    return '미확인';
    case 'RESOLVED':  return '해결 완료';
    case 'DELETED':   return '삭제됨';
    default:          return status;
  }
}

function categoryLabelOf(category) {
  switch (category) {
    case 'ENG_VOCA': return '단어 퀴즈';
    case 'BIBLE':    return '성경 퀴즈';
    default:         return category ?? '-';
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}

function escapeHtml(str) {
  if (!str) return '';
  return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

let toastTimer;
function showToast(message) {
  const toast = document.getElementById('toast');
  clearTimeout(toastTimer);
  toast.textContent = message;
  toast.classList.remove('hidden', 'opacity-0');
  toastTimer = setTimeout(() => {
    toast.classList.add('hidden');
  }, 2500);
}
