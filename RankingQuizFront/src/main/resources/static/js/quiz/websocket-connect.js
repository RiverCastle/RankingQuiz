// QUIZ_CATEGORY_CODE는 quiz.html에서 Thymeleaf가 인라인으로 주입
function initQuizWebSocket() {
    const wsUrl  = websocket_protocol + BACKEND_BASE_URL + '/ws/quiz/' + QUIZ_CATEGORY_CODE;
    const socket = new WebSocket(wsUrl);

    document.getElementById('stop-button').addEventListener('click', function () {
        quizBoxOff();
        quizResultOff();
        guideMessageOff();
        socket.close();
    });

    socket.onopen = function () {
        const textEl = document.getElementById('guideMessageText');
        if (textEl) textEl.textContent = '연결되었습니다. 퀴즈를 기다리는 중...';

        const accessToken = sessionStorage.getItem('accessToken');
        if (accessToken) {
            socket.send(JSON.stringify({
                dataType: 'AccessToken',
                object:   accessToken
            }));
        }
    };

    socket.onerror = function (error) {
        console.error('WebSocket 오류:', error);
    };

    socket.onclose = function () {
        window.open('/feedback', '_blank');
    };

    socket.onmessage = function (event) {
        const data = JSON.parse(event.data);

        switch (data.dataType) {
            case 'QuizDto':
                quizResultOff();
                guideMessageOff();
                quizItemUpdate(data.object, socket);
                break;

            case 'QuizResultDto':
                guideMessageOff();
                quizBoxOff();
                quizResultUpdate(data.object);
                quizResultOn();
                break;

            case 'GuideMessage':
                guideMessageOn(data.object);
                break;
        }
    };
}

initQuizWebSocket();
