const FRONTEND_BASE_URL = 'rankingquiz.rivercastleworks.site';
const BACKEND_BASE_URL = 'rankingquiz.rivercastleworks.site/api';
const protocol = 'https://';
const websocket_protocol = 'wss://';

// 토큰 가져오기
const accessToken = sessionStorage.getItem('accessToken');

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}