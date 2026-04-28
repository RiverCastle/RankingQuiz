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
  ['home', 'register'].forEach(t => {
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
