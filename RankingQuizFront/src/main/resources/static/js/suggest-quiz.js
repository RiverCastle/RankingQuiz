// =============================================
// 라디오 카드 UX 초기화
// =============================================
document.addEventListener('DOMContentLoaded', () => {
  initSuggestRadioCards();
});

function initSuggestRadioCards() {
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
// 보기 추가 / 제거
// =============================================
function addSuggestOption() {
  const list = document.getElementById('suggest-optionsList');
  const count = list.querySelectorAll('.suggest-option').length + 1;
  const input = document.createElement('input');
  input.type = 'text';
  input.placeholder = `보기 ${count}`;
  input.className = 'suggest-option neon-border w-full bg-white/5 border border-white/10 rounded-2xl px-5 py-3 text-white placeholder-white/25 text-sm transition-all duration-200';
  list.appendChild(input);
}

function removeSuggestOption() {
  const list = document.getElementById('suggest-optionsList');
  const inputs = list.querySelectorAll('.suggest-option');
  if (inputs.length > 2) {
    inputs[inputs.length - 1].remove();
  }
}

// =============================================
// 제안 제출
// =============================================
async function submitSuggestion() {
  const category = document.querySelector('input[name="suggest-category"]:checked')?.value;
  const statement = document.getElementById('suggest-statement').value.trim();
  const answer = document.getElementById('suggest-answer').value.trim();
  const choices = [...document.querySelectorAll('.suggest-option')]
    .map(i => i.value.trim())
    .filter(Boolean);

  if (!statement) {
    showSuggestResult('문제 내용을 입력해주세요.', false);
    return;
  }
  if (!answer) {
    showSuggestResult('정답을 입력해주세요.', false);
    return;
  }
  if (choices.length < 2) {
    showSuggestResult('보기를 최소 2개 이상 입력해주세요.', false);
    return;
  }
  if (!choices.includes(answer)) {
    showSuggestResult('정답이 보기 목록에 포함되어 있지 않습니다.', false);
    return;
  }

  const btn = document.getElementById('suggest-submitBtn');
  btn.disabled = true;
  btn.textContent = '제출 중...';

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-suggestions`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ category, statement, answer, choices })
    });

    if (!res.ok) {
      const msg = await res.text();
      throw new Error(msg || '제출에 실패했습니다.');
    }

    showSuggestResult('✅ 제안이 성공적으로 제출되었습니다! 검토 후 반영됩니다.', true);
    resetSuggestForm();
  } catch (e) {
    showSuggestResult('❌ ' + e.message, false);
  } finally {
    btn.disabled = false;
    btn.textContent = '제안 제출하기';
  }
}

function showSuggestResult(message, success) {
  const el = document.getElementById('suggest-result');
  el.textContent = message;
  el.className = `text-center text-sm py-3 rounded-2xl ${success ? 'success' : 'error'}`;
  el.classList.remove('hidden');
  setTimeout(() => el.classList.add('hidden'), 4000);
}

function resetSuggestForm() {
  document.getElementById('suggest-statement').value = '';
  document.getElementById('suggest-answer').value = '';
  document.querySelectorAll('.suggest-option').forEach((el, i) => {
    el.value = '';
    el.placeholder = `보기 ${i + 1}`;
  });
}
