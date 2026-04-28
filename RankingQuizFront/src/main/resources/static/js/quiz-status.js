(async function () {
  const CATEGORIES = {
    ENG_VOCA: 'voca-quizButton',
    BIBLE: 'bible-quizButton'
  };

  let statusMap = {};
  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/quiz-status/public`);
    statusMap = res.ok ? await res.json() : {};
  } catch (_) {
    return;
  }

  let enabledCount = 0;
  for (const [category, btnId] of Object.entries(CATEGORIES)) {
    const enabled = statusMap[category] ?? true;
    const btn = document.getElementById(btnId);
    if (!btn) continue;
    if (enabled) {
      enabledCount++;
    } else {
      btn.disabled = true;
      btn.classList.add('opacity-30', 'cursor-not-allowed');
      btn.title = '현재 준비 중인 퀴즈입니다.';
    }
  }

  if (enabledCount === 0) {
    document.getElementById('quizButtonsSection').classList.add('hidden');
    document.getElementById('comingSoonSection').classList.remove('hidden');
  }
})();
