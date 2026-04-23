const wsUrl = websocket_protocol + BACKEND_BASE_URL + '/ws/quiz/bible';
const userName = sessionStorage.getItem('userName');

// 버튼 클릭 이벤트 리스너 추가
document.getElementById('stop-button').addEventListener('click', function() {
    quizBoxOff();
    quizResultOff();
    guideMessageOff();
    socket.close(); // 웹소켓 연결 종료
});

const socket = new WebSocket(wsUrl);

socket.onopen = function() {
    if (sessionStorage.getItem('accessToken')) {
        const tokenWrapper = {
            dataType: 'AccessToken',
            object: sessionStorage.getItem('accessToken')
        };
        socket.send(JSON.stringify(tokenWrapper));
        console.log('엑세스 토큰 전송 완료');
    }
    backgroundColorChange();
};

socket.onerror = function(error) {
    console.error('웹소켓 오류:', error);
};

socket.onclose = function() {
    window.open('/feedback', '_blank'); // 새 창으로 feedback.html 열기
};

socket.onmessage = function(event) {
    const message = event.data;
    const data = JSON.parse(message);
    const dataType = data.dataType;

    if (dataType === 'QuizDto') {
        quizResultOff();
        guideMessageOff();
        quizItemUpdate(data.object);
    } else if (dataType === 'QuizResultDto') {
        guideMessageOff();
        quizBoxOff();
        quizResultUpdate(data.object);
        quizResultOn();
    } else if (dataType === 'GuideMessage') {
        guideMessageOn(data.object);
    }
}
