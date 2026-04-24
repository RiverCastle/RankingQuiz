function quizResultUpdate(quizResultObject) {
    const isCorrectEl   = document.getElementById('isCorrect');
    const statementEl   = document.getElementById('statement');
    const quizAnswerEl  = document.getElementById('quizAnswer');
    const myAnswerEl    = document.getElementById('myAnswer');
    const winnerNameEl  = document.getElementById('quizWinnerName');

    // 정답 여부
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

    // 우승자 이름 (QuizResultDto.userName)
    if (winnerNameEl) {
        winnerNameEl.textContent = quizResultObject.userName
            ? '🏆 ' + quizResultObject.userName
            : '';
    }
}

function quizResultOn() {
    document.getElementById('quizResultContainer').classList.remove('hidden');
}

function quizResultOff() {
    document.getElementById('quizResultContainer').classList.add('hidden');
}
