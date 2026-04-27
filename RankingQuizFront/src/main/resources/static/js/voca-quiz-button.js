document.getElementById('voca-quizButton').addEventListener('click', function () {
  const username = document.getElementById('nameInput').value.trim();
  if (!username) {
    alert('닉네임을 입력해주세요.');
    document.getElementById('nameInput').focus();
    return;
  }
  sessionStorage.setItem('username', username);
  window.location.href = '/quiz/voca';
});
