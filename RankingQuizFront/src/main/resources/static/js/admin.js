// =============================================
// 인증 확인 및 초기화
// =============================================
const accessToken = sessionStorage.getItem('adminToken');

document.addEventListener('DOMContentLoaded', () => {
  if (!accessToken) {
    window.location.href = '/admin/login';
    return;
  }

  document.getElementById('adminContent').classList.remove('hidden');
  initTabs();
  initRadioCards();
  initQuizTypeToggle();
  loadQuizStatus();
  loadSessionCounts();
  setInterval(loadSessionCounts, 15000);
  document.getElementById('tab-bulk').addEventListener('click', () => switchTab('bulk'));
  document.getElementById('tab-suggestion').addEventListener('click', () => {
    switchTab('suggestion');
    loadSuggestions();
  });
});

// =============================================
// 탭 전환
// =============================================
function initTabs() {
  const tabs = ['home', 'register'];
  tabs.forEach(name => {
    document.getElementById(`tab-${name}`).addEventListener('click', () => switchTab(name));
  });
}

function switchTab(name) {
  ['home', 'register', 'bulk', 'suggestion'].forEach(t => {
    document.getElementById(`tab-${t}`).classList.toggle('active', t === name);
    document.getElementById(`panel-${t}`).classList.toggle('hidden', t !== name);
  });
}

// =============================================
// 라디오 카드 UX
// =============================================
function initRadioCards() {
  document.querySelectorAll('.radio-card').forEach(card => {
    card.addEventListener('click', () => {
      const radio = card.querySelector('input[type="radio"]');
      const name = radio.name;
      document.querySelectorAll(`input[name="${name}"]`).forEach(r => {
        r.closest('.radio-card').classList.remove('selected');
      });
      radio.checked = true;
      card.classList.add('selected');
    });
  });

  // 초기 선택 상태 반영
  document.querySelectorAll('input[type="radio"]:checked').forEach(r => {
    r.closest('.radio-card').classList.add('selected');
  });
}

// =============================================
// 문제 유형 변경 시 보기 섹션 토글
// =============================================
function initQuizTypeToggle() {
  document.querySelectorAll('input[name="quizType"]').forEach(radio => {
    radio.addEventListener('change', () => {
      const isMultiple = radio.value === 'MULTIPLE_CHOICE';
      document.getElementById('multipleOptionsSection').classList.toggle('hidden', !isMultiple);
    });
  });
}

// =============================================
// 퀴즈 상태 조회
// =============================================
async function loadQuizStatus() {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-status`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('권한이 없습니다.');
    const data = await res.json();
    applyStatus('ENG_VOCA', data['ENG_VOCA']);
    applyStatus('BIBLE', data['BIBLE']);
  } catch (e) {
    showToast('상태 조회 실패: ' + e.message);
  }
}

function applyStatus(category, enabled) {
  const toggle = document.getElementById(`toggle-${category}`);
  if (toggle) toggle.checked = enabled;
}

// =============================================
// 접속자 수 조회 (15초 polling)
// =============================================
async function loadSessionCounts() {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-sessions/count`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('조회 실패');
    const data = await res.json();
    updateSessionCount('ENG_VOCA', data['ENG_VOCA']);
    updateSessionCount('BIBLE', data['BIBLE']);
  } catch (e) {
    console.error('접속자 수 조회 실패:', e.message);
  }
}

function updateSessionCount(category, count) {
  const el = document.getElementById(`session-count-${category}`);
  if (el) el.textContent = count ?? '-';
}

// =============================================
// 퀴즈 상태 토글
// =============================================
async function onToggleQuiz(category, enabled) {
  try {
    const res = await fetch(
      `${protocol}${BACKEND_BASE_URL}/quiz-status/${category}?enabled=${enabled}`,
      {
        method: 'PUT',
        headers: { Authorization: `Bearer ${accessToken}` }
      }
    );
    if (!res.ok) throw new Error('변경 실패');
    applyStatus(category, enabled);
    const label = category === 'ENG_VOCA' ? '단어 퀴즈' : '성경 퀴즈';
    showToast(`${label}가 ${enabled ? '활성화' : '비활성화'}되었습니다.`);
  } catch (e) {
    // 실패 시 토글 원복
    const toggle = document.getElementById(`toggle-${category}`);
    if (toggle) toggle.checked = !enabled;
    showToast('변경 실패: ' + e.message);
  }
}

// =============================================
// 토스트 메시지
// =============================================
let toastTimer;
function showToast(message) {
  const toast = document.getElementById('statusToast');
  clearTimeout(toastTimer);
  toast.textContent = message;
  toast.classList.remove('hidden', 'fade-out');
  toastTimer = setTimeout(() => {
    toast.classList.add('fade-out');
    setTimeout(() => toast.classList.add('hidden'), 300);
  }, 2500);
}

// =============================================
// 보기 추가/제거
// =============================================
function addOption() {
  const list = document.getElementById('optionsList');
  const count = list.querySelectorAll('.option-input').length + 1;
  const input = document.createElement('input');
  input.type = 'text';
  input.placeholder = `보기 ${count}`;
  input.className = 'option-input neon-border w-full bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-white placeholder-white/25 text-sm transition-all duration-200';
  list.appendChild(input);
}

function removeOption() {
  const list = document.getElementById('optionsList');
  const inputs = list.querySelectorAll('.option-input');
  if (inputs.length > 2) {
    inputs[inputs.length - 1].remove();
  }
}

// =============================================
// 문제 등록 제출
// =============================================
async function submitQuiz() {
  const statement = document.getElementById('statement').value.trim();
  const answer = document.getElementById('answer').value.trim();
  const timeLimit = parseInt(document.getElementById('timeLimit').value) || 10;
  const category = document.querySelector('input[name="category"]:checked')?.value;
  const quizType = document.querySelector('input[name="quizType"]:checked')?.value;
  const tagsRaw = document.getElementById('tags').value.trim();
  const tags = tagsRaw ? tagsRaw.split(',').map(t => t.trim()).filter(Boolean) : [];

  if (!statement || !answer || !category || !quizType) {
    showRegisterResult('문제 내용과 정답을 모두 입력해주세요.', false);
    return;
  }

  const payload = { statement, answer, timeLimit, category, quizType, tags };

  if (quizType === 'MULTIPLE_CHOICE') {
    const options = [...document.querySelectorAll('.option-input')]
      .map(i => i.value.trim())
      .filter(Boolean);
    if (options.length < 2) {
      showRegisterResult('객관식은 보기를 최소 2개 이상 입력해주세요.', false);
      return;
    }
    payload.multipleOptions = options;
  }

  const btn = document.getElementById('submitBtn');
  btn.disabled = true;
  btn.textContent = '등록 중...';

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-content`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`
      },
      body: JSON.stringify([payload])
    });

    if (!res.ok) {
      const msg = res.status === 403 ? '관리자 권한이 필요합니다.' : '등록에 실패했습니다.';
      throw new Error(msg);
    }

    showRegisterResult('✅ 문제가 등록되었습니다!', true);
    resetForm();
  } catch (e) {
    showRegisterResult('❌ ' + e.message, false);
  } finally {
    btn.disabled = false;
    btn.textContent = '문제 등록하기';
  }
}

function showRegisterResult(message, success) {
  const el = document.getElementById('registerResult');
  el.textContent = message;
  el.className = `text-center text-sm py-3 rounded-2xl ${success ? 'success' : 'error'}`;
  el.classList.remove('hidden');
  setTimeout(() => el.classList.add('hidden'), 3000);
}

// =============================================
// 대량 등록
// =============================================
async function submitBulkImport() {
  const rawText = document.getElementById('bulkImportText').value;
  if (!rawText.trim()) {
    showBulkResult([{ rowNumber: 0, message: '데이터를 입력해주세요.' }], false);
    return;
  }

  const btn = document.getElementById('bulkSubmitBtn');
  btn.disabled = true;
  btn.textContent = '등록 중...';

  try {
    const category = document.getElementById('bulkCategory').value;
  const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-content/bulk-import?category=${category}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain;charset=UTF-8',
        Authorization: `Bearer ${accessToken}`
      },
      body: rawText
    });

    const data = await res.json();

    if (!res.ok || !data.success) {
      showBulkResult(data.errors, false);
    } else {
      showBulkResult(null, true, data.savedCount);
      document.getElementById('bulkImportText').value = '';
    }
  } catch (e) {
    showBulkResult([{ rowNumber: 0, message: '서버 오류: ' + e.message }], false);
  } finally {
    btn.disabled = false;
    btn.textContent = '대량 등록하기';
  }
}

function showBulkResult(errors, success, savedCount) {
  const el = document.getElementById('bulkResult');
  el.innerHTML = '';
  el.classList.remove('hidden');

  if (success) {
    const msg = document.createElement('div');
    msg.className = 'text-center text-sm py-3 rounded-2xl success';
    msg.textContent = `✅ ${savedCount}개의 문제가 성공적으로 등록되었습니다!`;
    el.appendChild(msg);
    return;
  }

  const header = document.createElement('div');
  header.className = 'text-center text-sm py-3 rounded-2xl error';
  header.textContent = `❌ 등록 실패 — ${errors.length}개의 오류가 발생했습니다. 수정 후 다시 시도하세요.`;
  el.appendChild(header);

  errors.forEach(err => {
    const item = document.createElement('div');
    item.className = 'bg-white/5 border border-red-500/30 rounded-xl px-4 py-2 text-xs text-red-300';
    item.textContent = err.rowNumber > 0 ? `${err.rowNumber}번째 줄: ${err.message}` : err.message;
    el.appendChild(item);
  });
}

function resetForm() {
  document.getElementById('statement').value = '';
  document.getElementById('answer').value = '';
  document.getElementById('timeLimit').value = '10';
  document.getElementById('tags').value = '';
  document.querySelectorAll('.option-input').forEach((el, i) => {
    el.value = '';
    el.placeholder = `보기 ${i + 1}`;
  });
}

// =============================================
// 제안 검토 — 목록 조회
// =============================================
async function loadSuggestions() {
  const listEl = document.getElementById('suggestionList');
  const emptyEl = document.getElementById('suggestionEmpty');
  listEl.innerHTML = '<p class="text-white/30 text-sm text-center py-8">불러오는 중...</p>';
  emptyEl.classList.add('hidden');

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-suggestions`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('권한이 없습니다.');
    const data = await res.json();

    listEl.innerHTML = '';
    if (data.length === 0) {
      emptyEl.classList.remove('hidden');
      return;
    }

    data.forEach(suggestion => {
      listEl.appendChild(createSuggestionCard(suggestion));
    });
  } catch (e) {
    listEl.innerHTML = `<p class="text-red-400 text-sm text-center py-8">조회 실패: ${e.message}</p>`;
  }
}

function createSuggestionCard(s) {
  const categoryLabel = s.category === 'ENG_VOCA' ? '📖 단어 퀴즈' : '✝️ 성경 퀴즈';
  const choicesList = (s.choices || []).map((c, i) =>
    `<span class="inline-block bg-white/10 rounded-lg px-2 py-0.5 text-xs text-white/70 mr-1 mb-1">${i + 1}. ${c}</span>`
  ).join('');

  const card = document.createElement('div');
  card.id = `suggestion-card-${s.id}`;
  card.className = 'card-glass rounded-3xl p-5 space-y-3';
  card.innerHTML = `
    <div class="flex items-center justify-between">
      <span class="text-xs text-white/40">${categoryLabel}</span>
      <span class="text-xs text-white/20">#${s.id} · ${formatDate(s.createdAt)}</span>
    </div>
    <p class="text-white text-sm font-bold leading-relaxed">${escapeHtml(s.statement)}</p>
    <div>
      <p class="text-white/40 text-xs mb-1">보기</p>
      <div>${choicesList}</div>
    </div>
    <div class="flex items-center gap-2">
      <span class="text-white/40 text-xs">정답</span>
      <span class="text-neon-blue text-sm font-bold">${escapeHtml(s.answer)}</span>
    </div>
    <div class="flex gap-3 pt-1">
      <button
        onclick="approveSuggestion(${s.id})"
        class="flex-1 bg-gradient-to-br from-neon-blue to-neon-blue-dark text-surface-base font-bold rounded-2xl py-3 text-sm tracking-wide hover:opacity-90 transition-opacity">
        ✅ 승인
      </button>
      <button
        onclick="rejectSuggestion(${s.id})"
        class="flex-1 bg-white/10 border border-white/20 text-white/70 font-bold rounded-2xl py-3 text-sm tracking-wide hover:bg-white/20 transition-colors">
        ❌ 반려
      </button>
    </div>
  `;
  return card;
}

// =============================================
// 제안 검토 — 승인
// =============================================
async function approveSuggestion(id) {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-suggestions/${id}/approve`, {
      method: 'PUT',
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('승인 처리에 실패했습니다.');
    removeSuggestionCard(id);
    showSuggestionToast('✅ 제안이 승인되어 퀴즈에 등록되었습니다.');
  } catch (e) {
    showSuggestionToast('❌ ' + e.message);
  }
}

// =============================================
// 제안 검토 — 반려
// =============================================
async function rejectSuggestion(id) {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-suggestions/${id}/reject`, {
      method: 'PUT',
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('반려 처리에 실패했습니다.');
    removeSuggestionCard(id);
    showSuggestionToast('제안이 반려되었습니다.');
  } catch (e) {
    showSuggestionToast('❌ ' + e.message);
  }
}

function removeSuggestionCard(id) {
  const card = document.getElementById(`suggestion-card-${id}`);
  if (card) card.remove();
  const listEl = document.getElementById('suggestionList');
  if (listEl.children.length === 0) {
    document.getElementById('suggestionEmpty').classList.remove('hidden');
  }
}

let suggestionToastTimer;
function showSuggestionToast(message) {
  const toast = document.getElementById('suggestionToast');
  clearTimeout(suggestionToastTimer);
  toast.textContent = message;
  toast.classList.remove('hidden', 'fade-out');
  suggestionToastTimer = setTimeout(() => {
    toast.classList.add('fade-out');
    setTimeout(() => toast.classList.add('hidden'), 300);
  }, 2500);
}

// =============================================
// 유틸 함수
// =============================================
function escapeHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function formatDate(dateArr) {
  if (!dateArr) return '';
  // LocalDateTime이 배열로 올 경우: [2025,5,6,12,30,0]
  if (Array.isArray(dateArr)) {
    const [y, mo, d, h, mi] = dateArr;
    return `${y}.${String(mo).padStart(2,'0')}.${String(d).padStart(2,'0')} ${String(h).padStart(2,'0')}:${String(mi).padStart(2,'0')}`;
  }
  // 문자열로 올 경우
  return new Date(dateArr).toLocaleString('ko-KR', { year:'numeric', month:'2-digit', day:'2-digit', hour:'2-digit', minute:'2-digit' });
}
