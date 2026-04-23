// 불편한 점 입력란 추가
const addInconvenienceButton = document.getElementById('add-inconvenience');
const removeInconvenienceButton = document.getElementById('remove-inconvenience');
const inconvenienceContainer = document.getElementById('inconvenience-container');

addInconvenienceButton.addEventListener('click', function() {
    const feedbackItemDiv = document.createElement('div');
    feedbackItemDiv.className = 'feedback-item';

    const select = document.createElement('select');
    select.className = 'feedback-category';
    select.innerHTML = `
        <option value="BackendIssue">기능</option>
        <option value="FrontendIssue">화면</option>
        <option value="NotSelected">미분류</option>
    `;

    const newTextarea = document.createElement('textarea');
    newTextarea.className = 'feedback';
    newTextarea.placeholder = '여기에 내용을 입력해주세요...';

    feedbackItemDiv.appendChild(select);
    feedbackItemDiv.appendChild(newTextarea);
    inconvenienceContainer.appendChild(feedbackItemDiv);
});

// 불편한 점 입력란 제거
removeInconvenienceButton.addEventListener('click', function() {
    const feedbackItems = inconvenienceContainer.getElementsByClassName('feedback-item');
    if (feedbackItems.length > 1) {
        inconvenienceContainer.removeChild(feedbackItems[feedbackItems.length - 1]);
    } else {
        alert('제거할 항목이 없습니다.');
    }
});