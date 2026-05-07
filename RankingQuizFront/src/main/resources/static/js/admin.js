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
      // radio.checked = true 는 programmatic 변경이라 change 이벤트가 자동 발생하지 않음
      // 명시적으로 dispatch 해야 initQuizTypeToggle 리스너가 동작
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

  // 카테고리 라디오 선택
  document.querySelectorAll('input[name="editCategory"]').forEach(r => {
    r.closest('.radio-card').classList.remove('selected');
    if (r.value === data.category) {
      r.checked = true;
      r.closest('.radio-card').classList.add('selected');
    }
  });

  // 현재 이미지
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

  // 새 이미지 미리보기 초기화
  document.getElementById('editImageFile').value = '';
  document.getElementById('editNewImagePreviewContainer').classList.add('hidden');

  // 보기 초기화 및 채우기
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
    showEditResult('문제 내용과 정답을 모두 입력해주세요.', false);
    return;
  }
  if (choices.length < 2) {
    showEditResult('보기를 최소 2개 이상 입력해주세요.', false);
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
    showEditResult('✅ 수정이 완료되었습니다!', true);
    // 현재 이미지 업데이트
    if (updated.imageUrl) {
      const currentImg = document.getElementById('editCurrentImage');
      currentImg.src = `${protocol}${BACKEND_BASE_URL.replace('/api', '')}${updated.imageUrl}`;
      currentImg.classList.remove('hidden');
      document.getElementById('editNoImage').classList.add('hidden');
    }
    document.getElementById('editImageFile').value = '';
    document.getElementById('editNewImagePreviewContainer').classList.add('hidden');
  } catch (e) {
    showEditResult('❌ ' + e.message, false);
  } finally {
    btn.disabled    = false;
    btn.textContent = '수정하기';
  }
}

function showEditResult(message, success) {
  const el = document.getElementById('editResult');
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
