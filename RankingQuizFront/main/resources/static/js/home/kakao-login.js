
document.getElementById('kakaoLoginBtn').addEventListener('click', function() {

    // TODO Nginx를 사용해 값을 전달하라고 했으나 실패
    const clientId = '41ad8e26ac1e92bfcac84f788d229cef';
    const redirect_uri = protocol + FRONTEND_BASE_URL + '/login-wait';
    const authUrl = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirect_uri}&response_type=code`;
    window.location.href = authUrl;
});