(function () {
    let _category = '';
    let _selectedScore = null;
    let _selectedTags = new Set();
    let _githubCategory = null;

    function initServiceFeedbackModal(category) {
        _category = category;
    }

    function showServiceFeedbackModal() {
        const modal = document.getElementById('serviceFeedbackModal');
        if (!modal) return;

        // 서비스 만족도 초기화
        _selectedScore = null;
        _selectedTags = new Set();
        document.querySelectorAll('.score-btn').forEach(function (btn) {
            btn.classList.remove('score-selected');
        });
        document.querySelectorAll('.tag-btn').forEach(function (btn) {
            btn.classList.remove('tag-selected');
        });
        const commentEl = document.getElementById('feedbackComment');
        if (commentEl) commentEl.value = '';
        const submitBtn = document.getElementById('submitFeedbackBtn');
        if (submitBtn) submitBtn.disabled = true;

        // GitHub 섹션 초기화
        _githubCategory = null;
        document.querySelectorAll('.github-cat-btn').forEach(function (btn) {
            btn.classList.remove('github-cat-selected');
        });
        const githubContent = document.getElementById('githubFeedbackContent');
        if (githubContent) githubContent.value = '';
        const githubSection = document.getElementById('githubFeedbackSection');
        if (githubSection) githubSection.classList.add('hidden');
        const toggleLabel = document.getElementById('githubToggleLabel');
        if (toggleLabel) toggleLabel.innerHTML = '▶&nbsp; 불편한 점이나 제안이 있으신가요?';
        const githubSubmitBtn = document.getElementById('submitGithubFeedbackBtn');
        if (githubSubmitBtn) {
            githubSubmitBtn.disabled = true;
            githubSubmitBtn.classList.remove('hidden');
        }
        const githubSuccess = document.getElementById('githubSubmitSuccess');
        if (githubSuccess) githubSuccess.classList.add('hidden');

        modal.classList.remove('hidden');
    }

    function updateSubmitButton() {
        const canSubmit = _selectedScore !== null || _selectedTags.size > 0;
        const submitBtn = document.getElementById('submitFeedbackBtn');
        if (submitBtn) submitBtn.disabled = !canSubmit;
    }

    function updateGithubSubmitButton() {
        const content = document.getElementById('githubFeedbackContent');
        const hasContent = content && content.value.trim() !== '';
        const githubSubmitBtn = document.getElementById('submitGithubFeedbackBtn');
        if (githubSubmitBtn) githubSubmitBtn.disabled = !(_githubCategory !== null && hasContent);
    }

    function navigateHome() {
        window.location.href = '/';
    }

    async function submitFeedback() {
        const commentEl = document.getElementById('feedbackComment');
        const comment = commentEl ? commentEl.value.trim() : '';

        const payload = {
            satisfactionScore: _selectedScore,
            serviceTags: Array.from(_selectedTags),
            generalComment: comment || null,
            category: _category,
            deviceInfo: window.innerWidth < 768 ? 'Mobile' : 'Web'
        };

        try {
            await fetch(protocol + BACKEND_BASE_URL + '/service-feedback', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        } catch (e) {
            console.error('서비스 피드백 제출 실패:', e);
        }

        navigateHome();
    }

    async function submitGithubFeedback() {
        const contentEl = document.getElementById('githubFeedbackContent');
        const content = contentEl ? contentEl.value.trim() : '';
        if (!content || !_githubCategory) return;

        const githubSubmitBtn = document.getElementById('submitGithubFeedbackBtn');
        if (githubSubmitBtn) githubSubmitBtn.disabled = true;

        try {
            await fetch(protocol + BACKEND_BASE_URL + '/user-feedback', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify([{ content: content, category: _githubCategory }])
            });
            if (githubSubmitBtn) githubSubmitBtn.classList.add('hidden');
            const successEl = document.getElementById('githubSubmitSuccess');
            if (successEl) successEl.classList.remove('hidden');
        } catch (e) {
            console.error('GitHub 피드백 제출 실패:', e);
            if (githubSubmitBtn) githubSubmitBtn.disabled = false;
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        // 만족도 점수 버튼
        document.querySelectorAll('.score-btn').forEach(function (btn) {
            btn.addEventListener('click', function () {
                document.querySelectorAll('.score-btn').forEach(function (b) {
                    b.classList.remove('score-selected');
                });
                btn.classList.add('score-selected');
                _selectedScore = parseInt(btn.dataset.score, 10);
                updateSubmitButton();
            });
        });

        // 서비스 태그 버튼
        document.querySelectorAll('.tag-btn').forEach(function (btn) {
            btn.addEventListener('click', function () {
                const tag = btn.dataset.tag;
                if (_selectedTags.has(tag)) {
                    _selectedTags.delete(tag);
                    btn.classList.remove('tag-selected');
                } else {
                    _selectedTags.add(tag);
                    btn.classList.add('tag-selected');
                }
                updateSubmitButton();
            });
        });

        // GitHub 섹션 토글
        const toggleBtn = document.getElementById('githubSectionToggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', function () {
                const section = document.getElementById('githubFeedbackSection');
                const label = document.getElementById('githubToggleLabel');
                if (!section) return;
                const isHidden = section.classList.toggle('hidden');
                if (label) {
                    label.innerHTML = isHidden
                        ? '▶&nbsp; 불편한 점이나 제안이 있으신가요?'
                        : '▼&nbsp; 불편한 점이나 제안이 있으신가요?';
                }
            });
        }

        // GitHub 카테고리 버튼 (라디오 스타일)
        document.querySelectorAll('.github-cat-btn').forEach(function (btn) {
            btn.addEventListener('click', function () {
                document.querySelectorAll('.github-cat-btn').forEach(function (b) {
                    b.classList.remove('github-cat-selected');
                });
                btn.classList.add('github-cat-selected');
                _githubCategory = btn.dataset.category;
                updateGithubSubmitButton();
            });
        });

        // GitHub 내용 입력
        const githubContent = document.getElementById('githubFeedbackContent');
        if (githubContent) {
            githubContent.addEventListener('input', updateGithubSubmitButton);
        }

        // GitHub 제출 버튼
        const githubSubmitBtn = document.getElementById('submitGithubFeedbackBtn');
        if (githubSubmitBtn) githubSubmitBtn.addEventListener('click', submitGithubFeedback);

        // 서비스 피드백 제출 버튼
        const submitBtn = document.getElementById('submitFeedbackBtn');
        if (submitBtn) submitBtn.addEventListener('click', submitFeedback);

        // 그냥 나가기 버튼
        const skipBtn = document.getElementById('skipFeedbackBtn');
        if (skipBtn) skipBtn.addEventListener('click', navigateHome);
    });

    window.initServiceFeedbackModal = initServiceFeedbackModal;
    window.showServiceFeedbackModal = showServiceFeedbackModal;
})();
