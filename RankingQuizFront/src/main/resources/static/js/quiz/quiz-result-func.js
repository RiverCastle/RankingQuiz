function quizResultUpdate(quizResultObject, category) {
    const isCorrectEl   = document.getElementById('isCorrect');
    const statementEl   = document.getElementById('statement');
    const quizAnswerEl  = document.getElementById('quizAnswer');
    const myAnswerEl    = document.getElementById('myAnswer');
    const winnerNameEl  = document.getElementById('quizWinnerName');

    if (quizResultObject.correct) {
        isCorrectEl.textContent = '✅ 정답입니다!';
        isCorrectEl.className   = 'result-correct';
    } else {
        isCorrectEl.textContent = '❌ 오답입니다.';
        isCorrectEl.className   = 'result-incorrect';
    }

    statementEl.textContent  = quizResultObject.statement;
    quizAnswerEl.textContent = '정답: ' + quizResultObject.answer;
    myAnswerEl.textContent   = '내 답변: ' + (quizResultObject.myAnswer ?? '미제출 😭');

    if (winnerNameEl) {
        if (quizResultObject.winnerName) {
            winnerNameEl.textContent = '🏆 ' + quizResultObject.winnerName;
        } else {
            winnerNameEl.textContent = '정답자가 없습니다';
        }
    }

    const quizId = Number(document.getElementById('quizId').textContent) || null;
    setReportData(
        quizId,
        category || null,
        quizResultObject.statement,
        quizResultObject.answer,
        quizResultObject.myAnswer ?? null
    );
}

function quizResultOn() {
    document.getElementById('resultSection').classList.remove('quiz-section--hidden');
}

function quizResultOff() {
    document.getElementById('resultSection').classList.add('quiz-section--hidden');
}
