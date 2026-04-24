function quizItemUpdate(quizObject, socket) {
    const quizId   = quizObject.quizId;
    const userName = sessionStorage.getItem('username');

    const answerData = {
        userName:   userName,
        quizId:     quizId,
        writtenAt:  new Date().toISOString(),
        userAnswer: null
    };

    const messageWrapper = {
        dataType: 'AnswerDto',
        object:   answerData
    };

    // 퀴즈 ID / 문제 텍스트 업데이트
    document.getElementById('quizId').textContent        = quizId;
    document.getElementById('quizStatement').textContent = quizObject.quizContentDto.statement;

    // 카운트다운 타이머
    const countdownEl = document.getElementById('countdown');
    const f = quizObject.finishedAt;
    const finishedAt  = new Date(f[0], f[1] - 1, f[2], f[3], f[4], f[5]);

    const countdownInterval = setInterval(() => {
        const timeLeft = finishedAt - new Date();
        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
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
            answerData.userAnswer = option;
            socket.send(JSON.stringify(messageWrapper));

            // 모든 버튼 비활성화 (클릭된 버튼 포함)
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
