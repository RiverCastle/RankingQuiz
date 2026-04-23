// 새로운 기능 건의 입력란 추가
const addNewFeatureButton = document.getElementById('add-new-feature');
const removeNewFeatureButton = document.getElementById('remove-new-feature');
const newFeatureContainer = document.getElementById('new-feature-container');

addNewFeatureButton.addEventListener('click', function() {
    const feedbackItemDiv = document.createElement('div');
    feedbackItemDiv.className = 'text-input-container';

    const newTextarea = document.createElement('textarea');
    newTextarea.className = 'feedback';
    newTextarea.placeholder = '여기에 내용을 입력해주세요...';

    feedbackItemDiv.appendChild(newTextarea);
    newFeatureContainer.appendChild(feedbackItemDiv);
});

// 새로운 기능 건의 입력란 제거
removeNewFeatureButton.addEventListener('click', function() {
    const feedbackItems = newFeatureContainer.getElementsByClassName('text-input-container');
    if (feedbackItems.length > 1) {
        newFeatureContainer.removeChild(feedbackItems[feedbackItems.length - 1]);
    } else {
        alert('제거할 항목이 없습니다.');
    }
});