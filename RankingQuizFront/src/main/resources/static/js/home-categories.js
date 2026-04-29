(async function () {
    const nameInput = document.getElementById('nameInput');
    const section = document.getElementById('quizButtonsSection');
    const comingSoon = document.getElementById('comingSoonSection');

    let categories = [];
    try {
        const res = await fetch(protocol + BACKEND_BASE_URL + '/categories');
        if (res.ok) categories = await res.json();
    } catch (_) {}

    const enabled = categories.filter(function (c) { return c.enabled; });

    if (enabled.length === 0) {
        section.classList.add('hidden');
        comingSoon.classList.remove('hidden');
        return;
    }

    const styles = [
        'btn-glow-blue bg-gradient-to-br from-neon-blue to-neon-blue-dark text-surface-base',
        'btn-glow-purple bg-gradient-to-br from-neon-purple to-neon-purple-dark text-white'
    ];

    enabled.forEach(function (cat, i) {
        const btn = document.createElement('button');
        btn.id = 'quiz-btn-' + cat.code;
        btn.className = styles[i % styles.length] + ' font-bold rounded-2xl py-4 text-sm tracking-wide';
        btn.textContent = cat.displayName;
        btn.addEventListener('click', function () {
            const name = nameInput ? nameInput.value.trim() : '';
            if (!name) {
                alert('닉네임을 입력해주세요.');
                if (nameInput) nameInput.focus();
                return;
            }
            sessionStorage.setItem('username', name);
            window.location.href = '/quiz/' + cat.code;
        });
        section.appendChild(btn);
    });
})();
