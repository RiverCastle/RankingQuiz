let _countdownInterval = null;

function quizItemUpdate(quizObject, socket) {
    const quizId   = quizObject.quizId;
    const userName = sessionStorage.getItem('username');

    const answerData = {
        userName:   userName,
        quizId:     quizId,
        writtenAt:  null,
        userAnswer: null
    };

    const messageWrapper = {
        dataType: 'AnswerDto',
        object:   answerData
    };

    document.getElementById('quizId').textContent        = quizId;
    document.getElementById('quizStatement').textContent = quizObject.quizContentDto.statement;

    // 이미지 퀴즈: imageUrl이 있으면 이미지 영역 표시, 없으면 숨김
    // imageUrl은 "/uploads/quiz-images/xxx.jpg" 형태로 백엔드 서버 경로이므로
    // config.js의 protocol + BACKEND_BASE_URL(host만)을 앞에 붙여야 함
    const imageContainer = document.getElementById('quizImageContainer');
    const quizImage      = document.getElementById('quizImage');
    if (imageContainer && quizImage) {
        const imageUrl = quizObject.quizContentDto.imageUrl;
        if (imageUrl) {
            const backendHost = protocol + BACKEND_BASE_URL.replace('/api', '');
            quizImage.src = backendHost + imageUrl;
            imageContainer.classList.remove('hidden');
        } else {
            quizImage.src = '';
            imageContainer.classList.add('hidden');
        }
    }

    if (_countdownInterval !== null) {
        clearInterval(_countdownInterval);
        _countdownInterval = null;
    }

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

    const optionsContainer = document.getElementById('optionsContainer');
    optionsContainer.innerHTML = '';

    const options = [...quizObject.quizContentDto.options].sort(() => Math.random() - 0.5);

    options.forEach(option => {
        const button = document.createElement('button');
        button.textContent = option;
        button.className   = 'option-btn';

        button.onclick = function () {
            if (answerData.userAnswer !== null) return;

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
    document.getElementById('quizSection').classList.remove('quiz-section--hidden');
}

function quizBoxOff() {
    document.getElementById('quizSection').classList.add('quiz-section--hidden');
}
