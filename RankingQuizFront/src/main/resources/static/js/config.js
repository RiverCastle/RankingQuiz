const _isLocal = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';

const FRONTEND_BASE_URL  = _isLocal ? 'localhost:8081'                         : 'rankingquiz.rivercastleworks.site';
const BACKEND_BASE_URL   = _isLocal ? 'localhost:8090/api'                     : 'rankingquiz.rivercastleworks.site/api';
const protocol           = _isLocal ? 'http://'                                : 'https://';
const websocket_protocol = _isLocal ? 'ws://'                                  : 'wss://';
