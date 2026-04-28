function quizResultUpdate(quizResultObject) {
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
        winnerNameEl.textContent = quizResultObject.userName
            ? '🏆 ' + quizResultObject.userName
            : '';
    }
}

function quizResultOn() {
    document.getElementById('resultSection').classList.remove('quiz-section--hidden');
}

function quizResultOff() {
    document.getElementById('resultSection').classList.add('quiz-section--hidden');
}
