const currentUrl = window.location.href; // 현재 url 가져오기

const urlObj = new URL(currentUrl); // URL 객체 생성

const params = new URLSearchParams(urlObj.search); // URLSearchParams를 사용하여 쿼리 파라미터 추출

const authorizationCode = params.get('code'); // Authorization Code 'code' 값 가져오기

if (authorizationCode) {
    const params = {
        authorizationCode: authorizationCode // 실제 인증 코드를 여기에 넣어야 합니다.
    };

    // fetch API를 사용하여 POST 요청
    fetch(protocol + BACKEND_BASE_URL + '/auth/code/kakao', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    })
    .then(response => {
        if (!response.ok) {
            document.getElementById('step1').innerText =
                '2단계로 진행됩니다. 1단계에 실패했어요.\n' +
                '피드백 페이지에 상황을 알려주시면,\n' +
                '빠르게 수정하겠습니다.\n' +
                '다시 시도하거나, 비회원으로 진행해주세요.';

            throw new Error('네트워크 응답이 좋지 않습니다.');
        } else {
            document.getElementById('step1').innerText='2단계로 진행됩니다. 1단계에 성공했어요.';
            return response.json();
        }
    })
    .then(data => {
        // AuthTokens
        const { accessToken, refreshToken, grantType, expiresIn } = data;
        console.log(1);
        document.getElementById('step2').innerText='2단계에 성공했어요. 이제 퀴즈를 시작할 수 있어요.';
        // 세션 스토리지에 토큰 저장
        sessionStorage.setItem('accessToken', accessToken);
        sessionStorage.setItem('refreshToken', refreshToken);
        sessionStorage.setItem('grantType', grantType);
        sessionStorage.setItem('expiresIn', expiresIn);
        console.log(2);
        document.getElementById('step3').innerText='Ranking Quiz에 오신 것을 환영합니다.';
        // 로그인 성공 후 처리
        console.log(3);
        window.location.href = protocol + FRONTEND_BASE_URL + '/user';
    })
    .catch(error => {
        document.getElementById('step2').innerText =
            '2단계에 실패했어요.\n' +
            '피드백 페이지에 상황을 알려주시면,\n' +
            '빠르게 수정하겠습니다.\n' +
            '다시 시도하거나, 비회원으로 진행해주세요.';
    });
} else {
    console.log('카카오로부터 승인코드를 받아오지 못했습니다. 다시 카카오 로그인을 진행해주시거나, 계속 실패하면 비회원으로 진행해주세요.');
}