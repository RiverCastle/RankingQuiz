/**
 * 테스트 1 — 연결 성공 후 안내메시지 표시
 * guide-message-func.js 의 guideMessageOn / guideMessageOff 검증
 */
const fs   = require('fs');
const path = require('path');

eval(fs.readFileSync(
  path.resolve(__dirname, '../../main/resources/static/js/quiz/guide-message-func.js'),
  'utf8'
));

function setupDOM() {
  document.body.innerHTML = `
    <div id="guideMessageContainer" class="hidden">
      <p class="emoji">⏳</p>
      <p id="guideMessageText"></p>
    </div>
  `;
}

describe('guideMessageOn', () => {
  beforeEach(setupDOM);

  test('display:true 이면 컨테이너가 표시되고 텍스트가 설정된다', () => {
    guideMessageOn({ display: true, message: '퀴즈를 기다리는 중...' });

    const container = document.getElementById('guideMessageContainer');
    const textEl    = document.getElementById('guideMessageText');

    expect(container.classList.contains('hidden')).toBe(false);
    expect(textEl.textContent).toBe('퀴즈를 기다리는 중...');
  });

  test('display:false 이면 아무것도 변경되지 않는다', () => {
    guideMessageOn({ display: false, message: '무시됨' });

    const container = document.getElementById('guideMessageContainer');
    expect(container.classList.contains('hidden')).toBe(true);
  });

  test('내부 HTML 구조(⏳ 이모지)가 유지된다', () => {
    guideMessageOn({ display: true, message: '테스트 메시지' });

    const emojiEl = document.querySelector('.emoji');
    expect(emojiEl).not.toBeNull();
    expect(emojiEl.textContent).toBe('⏳');
  });
});

describe('guideMessageOff', () => {
  beforeEach(() => {
    setupDOM();
    document.getElementById('guideMessageContainer').classList.remove('hidden');
    document.getElementById('guideMessageText').textContent = '이전 메시지';
  });

  test('컨테이너가 hidden 처리되고 텍스트가 초기화된다', () => {
    guideMessageOff();

    const container = document.getElementById('guideMessageContainer');
    const textEl    = document.getElementById('guideMessageText');

    expect(container.classList.contains('hidden')).toBe(true);
    expect(textEl.textContent).toBe('');
  });
});
