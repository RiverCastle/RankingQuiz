// 버튼 클릭 이벤트 리스너 추가
const submitButton = document.getElementById('submit-btn');

submitButton.addEventListener('click', function() {
    const userFeedbackDtos = [];

    // 새로운 기능 건의 항목 수집
    const newFeatureTexts = document.querySelectorAll('#new-feature-container .feedback');
    newFeatureTexts.forEach(textarea => {
        const content = textarea.value.trim();
        if (content) {
            userFeedbackDtos.push({
                content: content,
                category: 'NewFeatureProposal'
            });
        }
    });

    // 불편한 점 항목 수집
    const inconvenienceItems = document.querySelectorAll('#inconvenience-container .feedback-item');
    inconvenienceItems.forEach(item => {
        const textarea = item.querySelector('.feedback');
        const select = item.querySelector('.feedback-category');
        const content = textarea.value.trim();
        const category = select.value;

        if (content) {
            userFeedbackDtos.push({
                content: content,
                category: category
            });
        }
    });

    // 서버에 데이터 전송
    if (userFeedbackDtos.length > 0) {
        fetch('https://' + BACKEND_BASE_URL + '/user-feedback', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userFeedbackDtos)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.'); // 응답이 실패한 경우 에러 발생
            }
            alert('피드백이 성공적으로 제출되었습니다.');
            location.reload(); // 페이지 새로 고침
        })
    }
});