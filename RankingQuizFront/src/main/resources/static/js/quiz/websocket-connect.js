// bible / voca 공통 WebSocket 연결 함수
// wsPath: 'bible' | 'voca'
function initQuizWebSocket(wsPath) {
    const wsUrl  = websocket_protocol + BACKEND_BASE_URL + '/ws/quiz/' + wsPath;
    const socket = new WebSocket(wsUrl);
    let intentionalClose = false;

    initServiceFeedbackModal(wsPath);

    document.getElementById('stop-button').addEventListener('click', function () {
        const quizInProgress = !document.getElementById('quizSection').classList.contains('quiz-section--hidden');

        if (quizInProgress) {
            const confirmed = window.confirm('진행 상황이 저장되지 않을 수 있습니다. 나가시겠습니까?');
            if (!confirmed) return;
        }

        intentionalClose = true;
        quizBoxOff();
        quizResultOff();
        guideMessageOff();
        socket.close();
        showServiceFeedbackModal();
        window.location.href = '/';
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
        if (!intentionalClose) {
            window.open('/feedback', '_blank');
        }
    };

    socket.onmessage = function (event) {
        const data = JSON.parse(event.data);

        switch (data.dataType) {
            case 'QuizDto':
                quizResultOff();
                disableReportBtn();
                guideMessageOff();
                quizItemUpdate(data.object, socket);
                break;

            case 'QuizResultDto':
                guideMessageOff();
                quizBoxOff();
                quizResultUpdate(data.object);
                quizResultOn();
                enableReportBtn(wsPath, data.object);
                break;

            case 'GuideMessage':
                guideMessageOn(data.object);
                break;
        }
    };
}
