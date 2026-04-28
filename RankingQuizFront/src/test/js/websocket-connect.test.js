/**
 * 통합 흐름 테스트 — websocket-connect.js
 * 실제 WebSocket 연결 흐름을 Mock으로 시뮬레이션
 */
const fs   = require('fs');
const path = require('path');

const QUIZ_JS = path.resolve(__dirname, '../../main/resources/static/js/quiz');

eval(fs.readFileSync(path.join(QUIZ_JS, 'guide-message-func.js'), 'utf8'));
eval(fs.readFileSync(path.join(QUIZ_JS, 'quiz-result-func.js'),   'utf8'));
eval(fs.readFileSync(path.join(QUIZ_JS, 'item-control-func.js'),  'utf8'));
eval(fs.readFileSync(path.join(QUIZ_JS, 'websocket-connect.js'),  'utf8'));

// ─── DOM 픽스처 ─────────────────────────────────────────────────────────────

function setupFullDOM() {
  document.body.innerHTML = `
    <button id="stop-button"></button>
    <div id="guideMessageContainer">
      <p id="guideMessageText">연결 중...</p>
    </div>
    <div id="quizBox" class="hidden">
      <span id="quizId"></span>
      <span id="countdown"></span>
      <p id="quizStatement"></p>
      <div id="optionsContainer" style="display:none"></div>
    </div>
    <div id="quizResultContainer" class="hidden">
      <p id="isCorrect"></p>
      <p id="statement"></p>
      <p id="quizAnswer"></p>
      <p id="myAnswer"></p>
      <p id="quizWinnerName"></p>
    </div>
  `;
}

// ─── Mock WebSocket ──────────────────────────────────────────────────────────

let mockSocketInstance;

global.WebSocket = jest.fn().mockImplementation(() => {
  mockSocketInstance = {
    send:      jest.fn(),
    close:     jest.fn(),
    onopen:    null,
    onclose:   null,
    onerror:   null,
    onmessage: null,
  };
  return mockSocketInstance;
});

global.window = global.window || {};
global.window.open = jest.fn();

// ─── 헬퍼 ───────────────────────────────────────────────────────────────────

function simulateMessage(dataType, object) {
  mockSocketInstance.onmessage({
    data: JSON.stringify({ dataType, object })
  });
}

function makeQuizObject(secondsFromNow = 30) {
  const d = new Date(Date.now() + secondsFromNow * 1000);
  return {
    quizId: 1,
    finishedAt: [d.getFullYear(), d.getMonth() + 1, d.getDate(),
                 d.getHours(), d.getMinutes(), d.getSeconds()],
    quizContentDto: {
      statement: '문제입니다',
      options: ['A', 'B', 'C', 'D']
    }
  };
}

// ─── 공통 셋업 ───────────────────────────────────────────────────────────────

beforeEach(() => {
  setupFullDOM();
  sessionStorage.setItem('accessToken', 'mock-access-token');
  sessionStorage.setItem('username', '테스트유저');
  jest.useFakeTimers();
  initQuizWebSocket('voca');
});

afterEach(() => {
  jest.useRealTimers();
  sessionStorage.clear();
});

// ─── 테스트 1: 연결 성공 → 안내메시지 ──────────────────────────────────────────

describe('테스트 1 — 연결 성공 후 GuideMessage 수신', () => {
  test('onopen 시 accessToken 이 서버로 전송된다', () => {
    mockSocketInstance.onopen();
    const sent = JSON.parse(mockSocketInstance.send.mock.calls[0][0]);
    expect(sent.dataType).toBe('AccessToken');
    expect(sent.object).toBe('mock-access-token');
  });

  test('GuideMessage 수신 시 안내 텍스트가 표시된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('GuideMessage', { display: true, message: '퀴즈를 기다리는 중...' });

    expect(document.getElementById('guideMessageText').textContent)
      .toBe('퀴즈를 기다리는 중...');
  });
});

// ─── 테스트 2: QuizDto → 문제 화면 ─────────────────────────────────────────────

describe('테스트 2 — QuizDto 수신 시 문제 화면 표시', () => {
  test('quizBox 가 표시되고 guideMessage 가 숨겨진다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());

    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('guideMessageContainer').classList.contains('hidden')).toBe(true);
  });

  test('문제 텍스트와 선택지 버튼이 렌더링된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());

    expect(document.getElementById('quizStatement').textContent).toBe('문제입니다');
    expect(document.querySelectorAll('.option-btn').length).toBe(4);
  });
});

// ─── 테스트 3: 답안 선택 → 서버 전송 ──────────────────────────────────────────

describe('테스트 3 — 답안 선택 시 AnswerDto 전송', () => {
  test('버튼 클릭 시 AnswerDto 가 전송된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());

    const btn = document.querySelector('.option-btn');
    btn.click();

    // send 호출 횟수: 1(AccessToken) + 1(AnswerDto)
    expect(mockSocketInstance.send).toHaveBeenCalledTimes(2);
    const lastSent = JSON.parse(
      mockSocketInstance.send.mock.calls[1][0]
    );
    expect(lastSent.dataType).toBe('AnswerDto');
    expect(lastSent.object.userAnswer).toBe(btn.textContent);
  });

  test('답안 선택 후 모든 버튼이 비활성화된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());
    document.querySelector('.option-btn').click();

    document.querySelectorAll('.option-btn').forEach(btn => {
      expect(btn.disabled).toBe(true);
    });
  });
});

// ─── 테스트 4: 타이머 만료 → 대기 화면 ────────────────────────────────────────

describe('테스트 4 — 타이머 만료 시 문제 종료', () => {
  test('타이머 만료 시 quizBox 가 hidden 된다', () => {
    mockSocketInstance.onopen();
    // 과거 시간으로 즉시 만료
    const past = new Date(Date.now() - 1000);
    simulateMessage('QuizDto', {
      quizId: 1,
      finishedAt: [past.getFullYear(), past.getMonth() + 1, past.getDate(),
                   past.getHours(), past.getMinutes(), past.getSeconds()],
      quizContentDto: { statement: '문제', options: ['A', 'B', 'C', 'D'] }
    });

    jest.advanceTimersByTime(200);

    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(true);
    expect(document.getElementById('countdown').textContent).toBe('0.0');
  });
});

// ─── 테스트 5: QuizResultDto → 결과 화면 ──────────────────────────────────────

describe('테스트 5 — QuizResultDto 수신 시 결과 화면 표시', () => {
  test('quizResultContainer 가 표시되고 quizBox 가 숨겨진다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());
    simulateMessage('QuizResultDto', {
      correct: true,
      statement: '문제입니다',
      answer: 'A',
      myAnswer: 'A',
      userName: '우승자'
    });

    expect(document.getElementById('quizResultContainer').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(true);
  });

  test('정답 여부, 정답, 내 답변, 우승자 이름이 표시된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());
    simulateMessage('QuizResultDto', {
      correct: true,
      statement: '문제입니다',
      answer: 'A',
      myAnswer: 'A',
      userName: '우승자'
    });

    expect(document.getElementById('isCorrect').className).toBe('result-correct');
    expect(document.getElementById('quizAnswer').textContent).toBe('정답: A');
    expect(document.getElementById('myAnswer').textContent).toBe('내 답변: A');
    expect(document.getElementById('quizWinnerName').textContent).toContain('우승자');
  });

  test('결과 화면 후 QuizDto 수신 시 문제 화면으로 복귀된다', () => {
    mockSocketInstance.onopen();
    simulateMessage('QuizDto', makeQuizObject());
    simulateMessage('QuizResultDto', {
      correct: false, statement: '문제', answer: 'A', myAnswer: 'B', userName: null
    });

    // 다음 문제 수신
    simulateMessage('QuizDto', makeQuizObject());

    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(false);
    expect(document.getElementById('quizResultContainer').classList.contains('hidden')).toBe(true);
  });
});
