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
  applyQuizTypeUI('MULTIPLE_CHOICE');
  loadQuizStatus();
  loadSessionCounts();
  setInterval(loadSessionCounts, 15000);
  document.getElementById('tab-bulk').addEventListener('click', () => switchTab('bulk'));
});

// =============================================
// 탭 전환
// =============================================
function initTabs() {
  ['home', 'manage'].forEach(name => {
    document.getElementById(`tab-${name}`).addEventListener('click', () => switchTab(name));
  });
}

function switchTab(name) {
  ['home', 'manage', 'bulk'].forEach(t => {
    document.getElementById(`tab-${t}`).classList.toggle('active', t === name);
    document.getElementById(`panel-${t}`).classList.toggle('hidden', t !== name);
  });
  if (name === 'manage') loadQuizList();
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
      radio.dispatchEvent(new Event('change'));
    });
  });

  document.querySelectorAll('input[type="radio"]:checked').forEach(r => {
    r.closest('.radio-card').classList.add('selected');
  });
}

// =============================================
// 문제 유형 변경 시 섹션 토글
// =============================================
function initQuizTypeToggle() {
  document.querySelectorAll('input[name="quizType"]').forEach(radio => {
    radio.addEventListener('change', () => applyQuizTypeUI(radio.value));
  });
}

function applyQuizTypeUI(quizType) {
  const isMultiple    = quizType === 'MULTIPLE_CHOICE';
  const isImage       = quizType === 'IMAGE';
  const showOptions   = isMultiple || isImage;

  document.getElementById('multipleOptionsSection').classList.toggle('hidden', !showOptions);
  document.getElementById('imageUploadSection').classList.toggle('hidden', !isImage);
  document.getElementById('imageEditSection').classList.toggle('hidden', !isImage);
  document.getElementById('tagsSection').classList.toggle('hidden', isImage);
  document.getElementById('timeLimitSection').classList.toggle('hidden', isImage);
}

// =============================================
// 이미지 미리보기 (등록 폼)
// =============================================
function previewRegisterImage(input) {
  const container = document.getElementById('imagePreviewContainer');
  const img       = document.getElementById('imagePreview');
  if (input.files && input.files[0]) {
    img.src = URL.createObjectURL(input.files[0]);
    container.classList.remove('hidden');
  } else {
    container.classList.add('hidden');
  }
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
// 보기 추가/제거 (등록 폼)
// =============================================
function addOption() {
  const list  = document.getElementById('optionsList');
  const count = list.querySelectorAll('.option-input').length + 1;
  const input = document.createElement('input');
  input.type        = 'text';
  input.placeholder = `보기 ${count}`;
  input.className   = 'option-input neon-border w-full bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-white placeholder-white/25 text-sm transition-all duration-200';
  list.appendChild(input);
}

function removeOption() {
  const list   = document.getElementById('optionsList');
  const inputs = list.querySelectorAll('.option-input');
  if (inputs.length > 2) {
    inputs[inputs.length - 1].remove();
  }
}

// =============================================
// 문제 등록 제출
// =============================================
async function submitQuiz() {
  const quizType = document.querySelector('input[name="quizType"]:checked')?.value;

  if (quizType === 'IMAGE') {
    await submitImageQuiz();
    return;
  }

  const statement = document.getElementById('statement').value.trim();
  const answer    = document.getElementById('answer').value.trim();
  const timeLimit = parseInt(document.getElementById('timeLimit').value) || 10;
  const category  = document.querySelector('input[name="category"]:checked')?.value;
  const tagsRaw   = document.getElementById('tags').value.trim();
  const tags      = tagsRaw ? tagsRaw.split(',').map(t => t.trim()).filter(Boolean) : [];

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
  btn.disabled    = true;
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
    btn.disabled    = false;
    btn.textContent = '문제 등록하기';
  }
}

// =============================================
// 이미지 퀴즈 등록
// =============================================
async function submitImageQuiz() {
  const statement = document.getElementById('statement').value.trim();
  const answer    = document.getElementById('answer').value.trim();
  const category  = document.querySelector('input[name="category"]:checked')?.value;
  const imageFile = document.getElementById('imageFile').files[0];
  const options   = [...document.querySelectorAll('.option-input')]
    .map(i => i.value.trim())
    .filter(Boolean);

  if (!statement || !answer || !category) {
    showRegisterResult('문제 내용과 정답을 모두 입력해주세요.', false);
    return;
  }
  if (!imageFile) {
    showRegisterResult('이미지 파일을 선택해주세요.', false);
    return;
  }
  if (options.length < 2) {
    showRegisterResult('보기를 최소 2개 이상 입력해주세요.', false);
    return;
  }

  const formData = new FormData();
  formData.append('category', category);
  formData.append('question', statement);
  formData.append('imageFile', imageFile);
  formData.append('answer', answer);
  formData.append('choices', JSON.stringify(options));

  const btn = document.getElementById('submitBtn');
  btn.disabled    = true;
  btn.textContent = '등록 중...';

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/image`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${accessToken}` },
      body: formData
    });

    if (!res.ok) {
      const msg = res.status === 403 ? '관리자 권한이 필요합니다.' : '등록에 실패했습니다.';
      throw new Error(msg);
    }

    showRegisterResult('✅ 이미지 퀴즈가 등록되었습니다!', true);
    resetForm();
  } catch (e) {
    showRegisterResult('❌ ' + e.message, false);
  } finally {
    btn.disabled    = false;
    btn.textContent = '문제 등록하기';
  }
}

// =============================================
// 이미지 퀴즈 수정 모달
// =============================================
async function openImageEditModal() {
  const idInput = document.getElementById('editTargetId');
  const id      = idInput.value.trim();
  if (!id) {
    showRegisterResult('수정할 퀴즈 ID를 입력해주세요.', false);
    return;
  }

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/image/${id}`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error(res.status === 404 ? '해당 ID의 퀴즈를 찾을 수 없습니다.' : '조회에 실패했습니다.');
    const data = await res.json();
    fillEditModal(data);
    document.getElementById('imageEditModal').classList.remove('hidden');
  } catch (e) {
    showRegisterResult('❌ ' + e.message, false);
  }
}

function fillEditModal(data) {
  document.getElementById('editQuizId').value    = data.id;
  document.getElementById('editQuestion').value  = data.question ?? '';
  document.getElementById('editAnswer').value    = data.answer ?? '';

  document.querySelectorAll('input[name="editCategory"]').forEach(r => {
    r.closest('.radio-card').classList.remove('selected');
    if (r.value === data.category) {
      r.checked = true;
      r.closest('.radio-card').classList.add('selected');
    }
  });

  const currentImg = document.getElementById('editCurrentImage');
  const noImg      = document.getElementById('editNoImage');
  if (data.imageUrl) {
    currentImg.src = `${protocol}${BACKEND_BASE_URL.replace('/api', '')}${data.imageUrl}`;
    currentImg.classList.remove('hidden');
    noImg.classList.add('hidden');
  } else {
    currentImg.classList.add('hidden');
    noImg.classList.remove('hidden');
  }

  document.getElementById('editImageFile').value = '';
  document.getElementById('editNewImagePreviewContainer').classList.add('hidden');

  const optionsList = document.getElementById('editOptionsList');
  optionsList.innerHTML = '';
  const choices = data.choices ?? [];
  choices.forEach((choice, i) => {
    const input = createEditOptionInput(choice, i + 1);
    optionsList.appendChild(input);
  });
  if (choices.length === 0) {
    optionsList.appendChild(createEditOptionInput('', 1));
    optionsList.appendChild(createEditOptionInput('', 2));
  }

  document.getElementById('editResult').classList.add('hidden');
}

function createEditOptionInput(value, index) {
  const input       = document.createElement('input');
  input.type        = 'text';
  input.value       = value;
  input.placeholder = `보기 ${index}`;
  input.className   = 'edit-option-input neon-border w-full bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-white placeholder-white/25 text-sm transition-all duration-200';
  return input;
}

function addEditOption() {
  const list  = document.getElementById('editOptionsList');
  const count = list.querySelectorAll('.edit-option-input').length + 1;
  list.appendChild(createEditOptionInput('', count));
}

function removeEditOption() {
  const list   = document.getElementById('editOptionsList');
  const inputs = list.querySelectorAll('.edit-option-input');
  if (inputs.length > 2) {
    inputs[inputs.length - 1].remove();
  }
}

function closeImageEditModal() {
  document.getElementById('imageEditModal').classList.add('hidden');
}

function previewRegisterImage(input) {
  const container = document.getElementById('imagePreviewContainer');
  const img       = document.getElementById('imagePreview');
  if (input.files && input.files[0]) {
    img.src = URL.createObjectURL(input.files[0]);
    container.classList.remove('hidden');
  } else {
    container.classList.add('hidden');
  }
}

function previewEditImage(input) {
  const container = document.getElementById('editNewImagePreviewContainer');
  const img       = document.getElementById('editNewImagePreview');
  if (input.files && input.files[0]) {
    img.src = URL.createObjectURL(input.files[0]);
    container.classList.remove('hidden');
  } else {
    container.classList.add('hidden');
  }
}

async function submitImageEdit() {
  const id       = document.getElementById('editQuizId').value;
  const question = document.getElementById('editQuestion').value.trim();
  const answer   = document.getElementById('editAnswer').value.trim();
  const category = document.querySelector('input[name="editCategory"]:checked')?.value;
  const imageFile = document.getElementById('editImageFile').files[0];
  const choices  = [...document.querySelectorAll('.edit-option-input')]
    .map(i => i.value.trim())
    .filter(Boolean);

  if (!question || !answer || !category) {
    showImageEditResult('문제 내용과 정답을 모두 입력해주세요.', false);
    return;
  }
  if (choices.length < 2) {
    showImageEditResult('보기를 최소 2개 이상 입력해주세요.', false);
    return;
  }

  const formData = new FormData();
  formData.append('category', category);
  formData.append('question', question);
  if (imageFile) formData.append('imageFile', imageFile);
  formData.append('answer', answer);
  formData.append('choices', JSON.stringify(choices));

  const btn = document.getElementById('editSubmitBtn');
  btn.disabled    = true;
  btn.textContent = '수정 중...';

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/admin/quiz/image/${id}`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${accessToken}` },
      body: formData
    });

    if (!res.ok) {
      const msg = res.status === 403 ? '관리자 권한이 필요합니다.' : '수정에 실패했습니다.';
      throw new Error(msg);
    }

    const updated = await res.json();
    showImageEditResult('✅ 수정이 완료되었습니다!', true);
    if (updated.imageUrl) {
      const currentImg = document.getElementById('editCurrentImage');
      currentImg.src = `${protocol}${BACKEND_BASE_URL.replace('/api', '')}${updated.imageUrl}`;
      currentImg.classList.remove('hidden');
      document.getElementById('editNoImage').classList.add('hidden');
    }
    document.getElementById('editImageFile').value = '';
    document.getElementById('editNewImagePreviewContainer').classList.add('hidden');
  } catch (e) {
    showImageEditResult('❌ ' + e.message, false);
  } finally {
    btn.disabled    = false;
    btn.textContent = '수정하기';
  }
}

function showImageEditResult(message, success) {
  const el = document.getElementById('imageEditResult');
  el.textContent = message;
  el.className = `text-center text-sm py-3 rounded-2xl ${success ? 'success' : 'error'}`;
  el.classList.remove('hidden');
  if (success) {
    setTimeout(() => closeImageEditModal(), 1500);
  }
}

// =============================================
// 등록 결과 메시지
// =============================================
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
  btn.disabled    = true;
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
    btn.disabled    = false;
    btn.textContent = '대량 등록하기';
  }
}

function showBulkResult(errors, success, savedCount) {
  const el = document.getElementById('bulkResult');
  el.innerHTML = '';
  el.classList.remove('hidden');

  if (success) {
    const msg = document.createElement('div');
    msg.className   = 'text-center text-sm py-3 rounded-2xl success';
    msg.textContent = `✅ ${savedCount}개의 문제가 성공적으로 등록되었습니다!`;
    el.appendChild(msg);
    return;
  }

  const header = document.createElement('div');
  header.className   = 'text-center text-sm py-3 rounded-2xl error';
  header.textContent = `❌ 등록 실패 — ${errors.length}개의 오류가 발생했습니다. 수정 후 다시 시도하세요.`;
  el.appendChild(header);

  errors.forEach(err => {
    const item = document.createElement('div');
    item.className   = 'bg-white/5 border border-red-500/30 rounded-xl px-4 py-2 text-xs text-red-300';
    item.textContent = err.rowNumber > 0 ? `${err.rowNumber}번째 줄: ${err.message}` : err.message;
    el.appendChild(item);
  });
}

// =============================================
// 등록 폼 초기화
// =============================================
function resetForm() {
  document.getElementById('statement').value    = '';
  document.getElementById('answer').value       = '';
  document.getElementById('timeLimit').value    = '10';
  document.getElementById('tags').value         = '';
  document.getElementById('imageFile').value    = '';
  document.getElementById('imagePreviewContainer').classList.add('hidden');
  document.querySelectorAll('.option-input').forEach((el, i) => {
    el.value       = '';
    el.placeholder = `보기 ${i + 1}`;
  });
}

// =============================================
// 개별 퀴즈 관리 — 목록 조회
// =============================================
const manageState = {
  page: 0,
  size: 10,
  totalPages: 0,
  category: '',
  answer: '',
};

let quizDataMap = {};

function onManageCategoryChange() {
  manageState.category = document.getElementById('manageCategory').value;
  manageState.page = 0;
  loadQuizList();
}

function searchQuizList() {
  manageState.answer = document.getElementById('manageKeyword').value.trim();
  manageState.page = 0;
  loadQuizList();
}

async function loadQuizList() {
  const container = document.getElementById('quizListContainer');
  container.innerHTML = '<div class="text-white/30 text-sm text-center py-8">목록을 불러오는 중...</div>';
  document.getElementById('quizPagination').innerHTML = '';

  const params = new URLSearchParams({ page: manageState.page, size: manageState.size });
  if (manageState.category) params.set('category', manageState.category);
  if (manageState.answer) params.set('answer', manageState.answer);

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-content?${params}`, {
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error(res.status === 403 ? '관리자 권한이 필요합니다.' : '목록 조회에 실패했습니다.');
    const data = await res.json();
    manageState.totalPages = data.totalPages ?? 0;
    quizDataMap = {};
    renderQuizList(data.content ?? []);
    renderPagination();
  } catch (e) {
    container.innerHTML = `<div class="text-red-400 text-sm text-center py-8">오류: ${escapeHtml(e.message)}</div>`;
  }
}

function renderQuizList(items) {
  const container = document.getElementById('quizListContainer');

  if (!items.length) {
    container.innerHTML = '<div class="text-white/30 text-sm text-center py-8">등록된 퀴즈가 없습니다.</div>';
    return;
  }

  container.innerHTML = '';
  items.forEach(quiz => {
    quizDataMap[quiz.id] = quiz;

    const badge = quiz.category === 'ENG_VOCA'
      ? '<span class="text-xs bg-neon-blue/10 text-neon-blue border border-neon-blue/20 rounded-lg px-2 py-0.5">단어</span>'
      : '<span class="text-xs bg-neon-purple/10 text-neon-purple border border-neon-purple/20 rounded-lg px-2 py-0.5">성경</span>';

    const optionsHtml = (quiz.options && quiz.options.length > 0)
      ? `<div class="mb-2.5">
          <p class="text-white/40 text-xs mb-1.5">보기</p>
          <div class="flex flex-wrap gap-1.5">
            ${quiz.options.map((opt, i) =>
              `<span class="text-xs bg-white/5 border border-white/10 rounded-lg px-2.5 py-1 text-white/70">${i + 1}. ${escapeHtml(opt)}</span>`
            ).join('')}
          </div>
        </div>`
      : '';

    const card = document.createElement('div');
    card.className = 'card-glass rounded-2xl p-4 border border-white/5';
    card.innerHTML = `
      <div class="flex items-center justify-between mb-3">
        <div class="flex items-center gap-2">
          ${badge}
          <span class="text-white/30 text-xs">#${quiz.id}</span>
        </div>
        <div class="flex gap-2">
          <button onclick="openEditModal(${quiz.id})"
            class="text-xs text-neon-blue/70 hover:text-neon-blue transition-colors px-3 py-1.5 rounded-xl border border-neon-blue/20 hover:border-neon-blue/50">
            수정
          </button>
          <button onclick="openDeleteModal(${quiz.id})"
            class="text-xs text-red-400/70 hover:text-red-400 transition-colors px-3 py-1.5 rounded-xl border border-red-500/20 hover:border-red-500/50">
            삭제
          </button>
        </div>
      </div>
      <div class="mb-2.5">
        <p class="text-white/40 text-xs mb-1">문제</p>
        <p class="text-white text-sm leading-relaxed break-words">${escapeHtml(quiz.statement)}</p>
      </div>
      ${optionsHtml}
      <div class="pt-2.5 border-t border-white/5">
        <p class="text-white/40 text-xs mb-0.5">정답</p>
        <p class="text-neon-blue font-bold text-sm">${escapeHtml(quiz.answer)}</p>
      </div>
    `;
    container.appendChild(card);

function renderPagination() {
  const container = document.getElementById('quizPagination');
  container.innerHTML = '';
  if (manageState.totalPages <= 1) return;

  const btnBase = 'w-9 h-9 rounded-xl text-sm font-bold transition-colors border';

  const prev = document.createElement('button');
  prev.className = `${btnBase} border-white/10 text-white/40 hover:text-white/70 disabled:opacity-30 disabled:cursor-not-allowed`;
  prev.textContent = '‹';
  prev.disabled = manageState.page === 0;
  prev.onclick = () => { manageState.page--; loadQuizList(); };
  container.appendChild(prev);

  for (let i = 0; i < manageState.totalPages; i++) {
    const btn = document.createElement('button');
    btn.className = `${btnBase} ${i === manageState.page
      ? 'bg-neon-blue/20 border-neon-blue/40 text-neon-blue'
      : 'border-white/10 text-white/40 hover:text-white/70'}`;
    btn.textContent = i + 1;
    btn.onclick = () => { manageState.page = i; loadQuizList(); };
    container.appendChild(btn);
  }

  const next = document.createElement('button');
  next.className = `${btnBase} border-white/10 text-white/40 hover:text-white/70 disabled:opacity-30 disabled:cursor-not-allowed`;
  next.textContent = '›';
  next.disabled = manageState.page >= manageState.totalPages - 1;
  next.onclick = () => { manageState.page++; loadQuizList(); };
  container.appendChild(next);
}

// =============================================
// 수정 모달
// =============================================
function openEditModal(quizId) {
  const quiz = quizDataMap[quizId];
  if (!quiz) return;

  document.getElementById('editQuizId').value = quiz.id;
  document.getElementById('editCategory').value =
    quiz.category === 'ENG_VOCA' ? '📖 단어 퀴즈 (ENG_VOCA)' : '✝️ 성경 퀴즈 (BIBLE)';
  document.getElementById('editStatement').value = quiz.statement;
  document.getElementById('editAnswer').value = quiz.answer;

  const optionsSection = document.getElementById('editOptionsSection');
  const optionsList = document.getElementById('editOptionsList');
  optionsList.innerHTML = '';

  if (quiz.options && quiz.options.length > 0) {
    optionsSection.classList.remove('hidden');
    quiz.options.forEach((opt, i) => {
      const input = document.createElement('input');
      input.type = 'text';
      input.value = opt;
      input.placeholder = `보기 ${i + 1}`;
      input.className = 'neon-border w-full bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-white placeholder-white/25 text-sm transition-all duration-200';
      optionsList.appendChild(input);
    });
  } else {
    optionsSection.classList.add('hidden');
  }

  document.getElementById('editResult').classList.add('hidden');
  document.getElementById('editModal').classList.remove('hidden');
  document.body.style.overflow = 'hidden';
}

function closeEditModal() {
  document.getElementById('editModal').classList.add('hidden');
  document.body.style.overflow = '';
}

async function submitEditQuiz() {
  const id = document.getElementById('editQuizId').value;
  const statement = document.getElementById('editStatement').value.trim();
  const answer = document.getElementById('editAnswer').value.trim();

  if (!statement || !answer) {
    showEditResult('문제 내용과 정답을 입력해주세요.', false);
    return;
  }

  const payload = { statement, answer };

  const optionInputs = document.querySelectorAll('#editOptionsList input');
  if (optionInputs.length > 0) {
    const options = [...optionInputs].map(i => i.value.trim()).filter(Boolean);
    if (options.length < 2) {
      showEditResult('보기는 최소 2개 이상 입력해주세요.', false);
      return;
    }
    payload.multipleOptions = options;
  }

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-content/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`
      },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error(res.status === 403 ? '관리자 권한이 필요합니다.' : '수정에 실패했습니다.');
    showEditResult('✅ 수정되었습니다.', true);
    setTimeout(() => { closeEditModal(); loadQuizList(); }, 1200);
  } catch (e) {
    showEditResult('❌ ' + e.message, false);
  }
}

function showEditResult(message, success) {
  const el = document.getElementById('editResult');
  el.textContent = message;
  el.className = `text-center text-sm py-3 rounded-2xl ${success ? 'success' : 'error'}`;
  el.classList.remove('hidden');
}

// =============================================
// 삭제 모달
// =============================================
function openDeleteModal(quizId) {
  const quiz = quizDataMap[quizId];
  if (!quiz) return;

  document.getElementById('deleteQuizId').value = quizId;
  const preview = quiz.statement.length > 30
    ? quiz.statement.substring(0, 30) + '...'
    : quiz.statement;
  document.getElementById('deleteConfirmText').textContent =
    `"${preview}" 문제를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.`;

  document.getElementById('deleteModal').classList.remove('hidden');
  document.body.style.overflow = 'hidden';
}

function closeDeleteModal() {
  document.getElementById('deleteModal').classList.add('hidden');
  document.body.style.overflow = '';
}

async function confirmDeleteQuiz() {
  const id = document.getElementById('deleteQuizId').value;
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-content/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${accessToken}` }
    });
    if (!res.ok) throw new Error(res.status === 403 ? '관리자 권한이 필요합니다.' : '삭제에 실패했습니다.');
    closeDeleteModal();
    showToast('퀴즈가 삭제되었습니다.');
    loadQuizList();
  } catch (e) {
    closeDeleteModal();
    showToast('삭제 실패: ' + e.message);
  }
}

// =============================================
// 유틸
// =============================================
function escapeHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}
