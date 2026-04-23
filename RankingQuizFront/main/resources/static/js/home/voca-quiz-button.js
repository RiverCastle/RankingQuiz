document.getElementById('voca-quizButton').addEventListener('click', function() {
    const username = document.getElementById('nameInput').value.trim();
    if (username === "") {
        alert("이름/별명을 입력해주세요.");
        return;
    }
    sessionStorage.setItem('username', username);
    window.location.href = protocol + FRONTEND_BASE_URL + '/quiz/voca';
});
