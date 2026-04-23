document.getElementById('name-change-btn').addEventListener('click', function() {
// 사용자에게 이름 입력받기
    const newName = prompt("새로운 이름을 입력하세요:");

    if (newName) {
        // API 호출 (예: POST 요청)
        fetch(protocol + BACKEND_BASE_URL + '/member/myInfo/name?newName=' + newName, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 오류 발생');
            } else {
                alert('이름이 변경되었습니다!');
                const nameDiv = document.querySelector('.username');
                nameDiv.textContent = newName;
            }

        })
    }
});