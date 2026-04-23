function backgroundColorChange() {
    // 배경 설정
    document.body.style.background = "linear-gradient(135deg, #ff4e50, #fc913a)";
}

function quizItemUpdate(quizObject) {
    // 대기 상태 종료
    document.body.style.background = "linear-gradient(135deg, #00b09b, #96c93d)";

    // 퀴즈 데이터 초기화
    const quizId = quizObject.quizId;
    const writtenAt = new Date().toISOString();
    let userAnswer = null;
    const userName = sessionStorage.getItem('username');

    const answerData = {
        userName: userName,
        quizId: quizId,
        writtenAt: writtenAt,
        userAnswer: userAnswer
    };

    const messageWrapper = {
        dataType: 'AnswerDto',
        object: answerData
    };

    // 퀴즈 컨테이너
    let quizContainer = document.getElementById('quizForm');

    // QuizID 요소 재할당
    let quizIdElement = document.getElementById('quizId');
    quizIdElement.textContent = quizId; // 내용 재할당

    // 카운트다운 요소 재할당
    let countdownElement = document.getElementById('countdown');

    // 퀴즈 마감 시간 카운트다운 생성
    const finishedAt = new Date(quizObject.finishedAt[0], quizObject.finishedAt[1] - 1, quizObject.finishedAt[2],
                                 quizObject.finishedAt[3], quizObject.finishedAt[4], quizObject.finishedAt[5]);

    const countdownInterval = setInterval(() => {
        const now = new Date();
        const timeLeft = finishedAt - now;

        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            countdownElement.textContent = '0.0';
            quizBoxOff(); // 퀴즈 종료 시 요소 숨김
        } else {
            const secondsLeft = (timeLeft / 1000).toFixed(1); // 시간을 초 단위로 환산하고 소수점 첫째 자리까지 표현
            countdownElement.textContent = secondsLeft;
        }
    }, 100);

    // QuizStatement 요소 생성 또는 재할당
    let statementElement = document.getElementById('quizStatement');
    statementElement.textContent = quizObject.quizContentDto.statement; // 내용 재할당
    const textLength = quizObject.quizContentDto.statement.length;
    if (textLength < 10) {
        quizStatement.style.fontSize = '300%'; // 큰 글씨
    }

    // 옵션 컨테이너 재할당
    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    const options = quizObject.quizContentDto.options;
    options.sort(() => Math.random() - 0.5); // 임의의 순서로 섞기

    options.forEach(option => {
        const button = document.createElement('button');
        button.textContent = option;
        button.className = 'optionButton';

        button.onclick = function() {
            answerData.userAnswer = option;
            answerData.userName = userName;
            socket.send(JSON.stringify(messageWrapper));
            // 클릭된 버튼에 클래스 추가하여 가시적 변화
            this.classList.add('clicked');

            // 다른 버튼 비활성화
            const allButtons = document.querySelectorAll('.optionButton'); // 모든 버튼 선택
            allButtons.forEach(btn => {
                if (btn !== this) {
                    btn.disabled = true; // 비활성화
                }
            });
        };
        optionsContainer.appendChild(button);
    });
    quizBoxOn();
}

function quizBoxOff() {
    // 퀴즈 관련 요소 숨김 처리
    document.getElementById('quizBox').classList.add('hidden');
    // optionsContainer 요소를 선택
    const optionsContainer = document.getElementById('optionsContainer');
    // display 속성을 none으로 변경
    optionsContainer.style.display = 'none';

}

function quizBoxOn() {
    document.getElementById('quizBox').classList.remove('hidden');
    // optionsContainer 요소를 선택
    const optionsContainer = document.getElementById('optionsContainer');
    // display 속성을 none으로 변경
    optionsContainer.style.display = 'grid';
}