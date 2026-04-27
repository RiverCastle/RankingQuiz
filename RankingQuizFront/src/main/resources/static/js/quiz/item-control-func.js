let _countdownInterval = null; // [버그2] 인터벌 누적 방지용 모듈 레벨 관리

function quizItemUpdate(quizObject, socket) {
    const quizId   = quizObject.quizId;
    const userName = sessionStorage.getItem('username');

    const answerData = {
        userName:   userName,
        quizId:     quizId,
        writtenAt:  null, // [버그1] 클릭 시점에 기록 (아래 onclick에서 설정)
        userAnswer: null
    };

    const messageWrapper = {
        dataType: 'AnswerDto',
        object:   answerData
    };

    // 퀴즈 ID / 문제 텍스트 업데이트
    document.getElementById('quizId').textContent        = quizId;
    document.getElementById('quizStatement').textContent = quizObject.quizContentDto.statement;

    // [버그2] 이전 문제의 타이머가 남아있으면 제거
    if (_countdownInterval !== null) {
        clearInterval(_countdownInterval);
        _countdownInterval = null;
    }

    // 카운트다운 타이머
    const countdownEl = document.getElementById('countdown');
    const f = quizObject.finishedAt;
    const finishedAt  = new Date(f[0], f[1] - 1, f[2], f[3], f[4], f[5]);

    _countdownInterval = setInterval(() => {
        const timeLeft = finishedAt - new Date();
        if (timeLeft <= 0) {
            clearInterval(_countdownInterval);
            _countdownInterval = null;
            countdownEl.textContent = '0.0';
            countdownEl.classList.add('countdown-urgent');
            quizBoxOff();
        } else {
            const sec = (timeLeft / 1000).toFixed(1);
            countdownEl.textContent = sec;
            countdownEl.classList.toggle('countdown-urgent', timeLeft < 3000);
        }
    }, 100);

    // 선택지 버튼 생성
    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    const options = [...quizObject.quizContentDto.options].sort(() => Math.random() - 0.5);

    options.forEach(option => {
        const button = document.createElement('button');
        button.textContent = option;
        button.className   = 'option-btn';

        button.onclick = function () {
            // [버그3] 이미 제출한 경우 중복 전송 차단
            if (answerData.userAnswer !== null) return;

            // [버그1] 클릭 시점을 writtenAt으로 기록
            answerData.writtenAt  = new Date().toISOString();
            answerData.userAnswer = option;

            socket.send(JSON.stringify(messageWrapper));

            document.querySelectorAll('.option-btn').forEach(btn => {
                btn.disabled = true;
                btn.classList.remove('option-selected');
            });
            this.classList.add('option-selected');
        };

        optionsContainer.appendChild(button);
    });

    quizBoxOn();
}

function quizBoxOn() {
    document.getElementById('quizBox').classList.remove('hidden');
    document.getElementById('optionsContainer').style.display = 'grid';
}

function quizBoxOff() {
    document.getElementById('quizBox').classList.add('hidden');
    document.getElementById('optionsContainer').style.display = 'none';
}
