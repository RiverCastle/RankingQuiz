document.getElementById('bible-quiz-button').addEventListener('click', function() {
    const nameDiv = document.querySelector('.username');

    sessionStorage.setItem('username', nameDiv.textContent);
    window.location.href = protocol + FRONTEND_BASE_URL + '/quiz/bible';
});