// 사용자 정보를 가져오는 함수
async function fetchMemberInfo() {
    try {
        const response = await fetch(protocol + BACKEND_BASE_URL + '/member/myInfo', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        });

        // 응답이 성공적이지 않으면 오류 처리
        if (!response.ok) {
            throw new Error('네트워크 응답이 실패했습니다.');
        }

        // JSON 형식으로 응답 데이터 파싱
        const data = await response.json();
        // 데이터 표시
        displayMemberInfo(data);
    } catch (error) {
        console.error('데이터 요청 중 오류 발생:', error);
    }
}

function displayMemberInfo(memberInfo) {
    // 유저명 표시
    const nameDiv = document.querySelector('.username');
    nameDiv.textContent = memberInfo.name;
    const point = memberInfo.point;
    // 현재 점수와 현재 랭크 표시
    const scoreDiv = document.querySelector('.point');
    scoreDiv.textContent = `${memberInfo.point} 점`;
    setRankImage(point);
}

// 함수 호출
fetchMemberInfo();