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
  initQuizTypeToggle();
  loadCategories();
  setInterval(loadSessionCounts, 15000);
  document.getElementById('tab-bulk').addEventListener('click', () => switchTab('bulk'));
});

// =============================================
// 카테고리 목록 로드 (상태카드 + 라디오버튼 동적 렌더링)
// =============================================
async function loadCategories() {
  let categories = [];
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/categories`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (res.ok) categories = await res.json();
  } catch (e) {
    showToast('카테고리 로드 실패: ' + e.message);
  }

  renderStatusCards(categories);
  renderCategoryRadios(categories);
  loadSessionCounts();
}

function renderStatusCards(categories) {
  const list = document.getElementById('categoryStatusList');
  list.innerHTML = '';
  const sliderColors = ['toggle-blue', 'toggle-purple', 'toggle-blue'];
  const countColors = ['text-neon-blue', 'text-neon-purple', 'text-neon-blue'];

  categories.forEach((cat, i) => {
    const colorClass = sliderColors[i % sliderColors.length];
    const countClass = countColors[i % countColors.length];
    const card = document.createElement('div');
    card.className = 'card-glass rounded-3xl p-5 mb-4';
    card.innerHTML = `
      <div class="flex items-center justify-between">
        <div>
          <p class="font-bold text-base">${cat.displayName}</p>
          <p class="text-white/40 text-xs mt-0.5">${cat.code}</p>
        </div>
        <label class="toggle-switch">
          <input type="checkbox" id="toggle-${cat.code}"
            onchange="onToggleQuiz('${cat.code}', '${cat.displayName}', this.checked)"
            ${cat.enabled ? 'checked' : ''} />
          <span class="toggle-slider ${colorClass}"></span>
        </label>
      </div>
      <div class="mt-4 flex items-center gap-2">
        <span class="text-white/40 text-xs">현재 접속자</span>
        <span id="session-count-${cat.code}" class="${countClass} font-bold text-sm">-</span>
        <span class="text-white/40 text-xs">명</span>
      </div>`;
    list.appendChild(card);
  });
}

function renderCategoryRadios(categories) {
  const group = document.getElementById('categoryRadioGroup');
  group.innerHTML = '';
  categories.forEach((cat, i) => {
    const label = document.createElement('label');
    label.className = 'radio-card' + (i === 0 ? ' selected' : '');
    label.id = `radio-${cat.code}`;
    label.innerHTML = `
      <input type="radio" name="categoryCode" value="${cat.code}" class="hidden" ${i === 0 ? 'checked' : ''} />
      <span class="text-sm font-bold">${cat.displayName}</span>`;
    group.appendChild(label);
  });
  initRadioCards();
}

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
  ['home', 'register', 'bulk'].forEach(t => {
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
// 접속자 수 조회 (15초 polling)
// =============================================
async function loadSessionCounts() {
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-sessions/count`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error('조회 실패');
    const data = await res.json();
    Object.entries(data).forEach(([code, count]) => {
      updateSessionCount(code, count);
    });
  } catch (e) {
    console.error('접속자 수 조회 실패:', e.message);
  }
}

function updateSessionCount(code, count) {
  const el = document.getElementById(`session-count-${code}`);
  if (el) el.textContent = count ?? '-';
}

// =============================================
// 퀴즈 상태 토글
// =============================================
async function onToggleQuiz(code, displayName, enabled) {
  try {
    const res = await fetch(
      `${protocol}${BACKEND_BASE_URL}/quiz-status/${code}?enabled=${enabled}`,
      {
        method: 'PUT',
        headers: { Authorization: `Bearer ${accessToken}` }
      }
    );
    if (!res.ok) throw new Error('변경 실패');
    showToast(`${displayName}가 ${enabled ? '활성화' : '비활성화'}되었습니다.`);
  } catch (e) {
    const toggle = document.getElementById(`toggle-${code}`);
    if (toggle) toggle.checked = !enabled;
    showToast('변경 실패: ' + e.message);
  }
}

// =============================================
// 신규 카테고리 등록
// =============================================
async function submitNewCategory() {
  const code = document.getElementById('newCategoryCode').value.trim().toUpperCase();
  const displayName = document.getElementById('newCategoryName').value.trim();
  const allowMultipleWinners = document.getElementById('newCategoryMultiWinner').checked;

  if (!code || !displayName) {
    showToast('코드와 표시명을 모두 입력해주세요.');
    return;
  }

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/categories`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`
      },
      body: JSON.stringify({ code, displayName, allowMultipleWinners })
    });
    if (!res.ok) {
      const msg = res.status === 409 ? '이미 존재하는 카테고리입니다.' : '등록에 실패했습니다.';
      throw new Error(msg);
    }
    document.getElementById('newCategoryCode').value = '';
    document.getElementById('newCategoryName').value = '';
    document.getElementById('newCategoryMultiWinner').checked = false;
    showToast(`'${displayName}' 카테고리가 등록되었습니다.`);
    loadCategories();
  } catch (e) {
    showToast('카테고리 등록 실패: ' + e.message);
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
  const categoryCode = document.querySelector('input[name="categoryCode"]:checked')?.value;
  const quizType = document.querySelector('input[name="quizType"]:checked')?.value;
  const tagsRaw = document.getElementById('tags').value.trim();
  const tags = tagsRaw ? tagsRaw.split(',').map(t => t.trim()).filter(Boolean) : [];

  if (!statement || !answer || !categoryCode || !quizType) {
    showRegisterResult('문제 내용과 정답을 모두 입력해주세요.', false);
    return;
  }

  const payload = { statement, answer, timeLimit, categoryCode, quizType, tags };

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
