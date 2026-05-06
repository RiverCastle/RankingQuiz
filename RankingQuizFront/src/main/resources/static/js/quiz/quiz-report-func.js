let _reportData = {
    quizId:   null,
    category: null,
    question: null,
    answer:   null,
    myAnswer: null
};

function setReportData(quizId, category, question, answer, myAnswer) {
    _reportData = { quizId, category, question, answer, myAnswer };

    const btn = document.getElementById('reportBtn');
    if (btn) {
        btn.disabled = false;
        btn.classList.remove('opacity-40', 'cursor-not-allowed');
        btn.textContent = '';
        btn.innerHTML =
            '<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
            '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" ' +
            'd="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4' +
            'c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>' +
            '</svg>' +
            '문제 이상 신고';
    }
}

function reportQuizIssue() {
    const btn = document.getElementById('reportBtn');
    if (!btn || btn.disabled) return;

    const reporterId = sessionStorage.getItem('username') || 'anonymous';
    const payload = {
        quizId:     _reportData.quizId,
        category:   _reportData.category,
        question:   _reportData.question,
        answer:     _reportData.answer,
        myAnswer:   _reportData.myAnswer,
        reporterId: reporterId,
        reportedAt: new Date().toISOString().slice(0, 19)
    };

    fetch(protocol + BACKEND_BASE_URL + '/quiz/reports', {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify(payload)
    })
    .then(res => {
        if (res.ok) {
            btn.disabled = true;
            btn.classList.add('opacity-40', 'cursor-not-allowed');
            btn.innerHTML =
                '<svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">' +
                '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" ' +
                'd="M5 13l4 4L19 7"/>' +
                '</svg>' +
                '신고 완료';
        }
    })
    .catch(err => console.error('신고 전송 오류:', err));
}
