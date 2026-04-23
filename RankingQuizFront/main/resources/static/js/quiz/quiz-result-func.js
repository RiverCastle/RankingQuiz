function quizResultUpdate(quizResultObject) {
    let quizResultContainer = document.getElementById('quizResultContainer');
    let statementContainer = document.getElementById('statement');
    let isCorrectContainer = document.getElementById('isCorrect');
    let quizAnswerContainer = document.getElementById('quizAnswer');
    let myAnswerContainer = document.getElementById('myAnswer');
    let quizWinnerNameContainer = document.getElementById('quizWinnerName');

    // 정답 여부에 따라 스타일 변경 및 이모지 추가
    if (quizResultObject.correct) {
        isCorrectContainer.innerText = "✅ 정답입니다!"; // 체크 마크 이모지
        isCorrectContainer.style.color = "green"; // 정답일 경우 초록색
    } else {
        isCorrectContainer.innerText = "❌ 오답입니다."; // 엑스 마크 이모지
        isCorrectContainer.style.color = "red"; // 오답일 경우 빨간색
    }

    // 문제, 정답과 사용자의 답변 표시
    statementContainer.innerText = "문제: " + quizResultObject.statement;
    quizAnswerContainer.innerText = "정답: " + quizResultObject.answer + " 🎉"; // 축하 이모지 추가
    if (quizResultObject.myAnswer != null) {
        myAnswerContainer.innerText = "내 답변: " + quizResultObject.myAnswer;
    } else {
        myAnswerContainer.innerText = "내 답변: 없음 😭"
    }
    quizResultOn();
}


function quizResultOff() {
    document.getElementById('quizResultContainer').classList.add('hidden');;
}

function quizResultOn() {
    document.getElementById('quizResultContainer').classList.remove('hidden');
}