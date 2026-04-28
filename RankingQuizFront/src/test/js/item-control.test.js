/**
 * 테스트 2 — QuizDto 수신 시 문제 화면 표시
 * 테스트 3 — 답안 선택 시 서버 전송 및 화면 변화
 * 테스트 4 — 타이머 만료 시 문제 화면 종료
 */
const fs   = require('fs');
const path = require('path');

eval(fs.readFileSync(
  path.resolve(__dirname, '../../main/resources/static/js/quiz/item-control-func.js'),
  'utf8'
));

// 30초 후를 finishedAt 배열로 반환
function futureFinishedAt(secondsFromNow = 30) {
  const d = new Date(Date.now() + secondsFromNow * 1000);
  return [d.getFullYear(), d.getMonth() + 1, d.getDate(),
          d.getHours(), d.getMinutes(), d.getSeconds()];
}

function makeQuizObject(secondsFromNow = 30) {
  return {
    quizId: 42,
    finishedAt: futureFinishedAt(secondsFromNow),
    quizContentDto: {
      statement: '다음 중 올바른 것은?',
      options: ['보기1', '보기2', '보기3', '보기4']
    }
  };
}

function setupDOM() {
  document.body.innerHTML = `
    <div id="quizBox" class="hidden">
      <div id="quizForm">
        <span id="quizId"></span>
        <span id="countdown"></span>
        <p id="quizStatement"></p>
        <div id="optionsContainer" style="display:none"></div>
      </div>
    </div>
  `;
}

let mockSocket;

beforeEach(() => {
  setupDOM();
  mockSocket = { send: jest.fn() };
  sessionStorage.setItem('username', '테스트유저');
  jest.useFakeTimers();
});

afterEach(() => {
  jest.useRealTimers();
  sessionStorage.clear();
});

// ─── 테스트 2: 문제 화면 표시 ───────────────────────────────────────────────

describe('테스트 2 — QuizDto 수신 시 문제 화면 표시', () => {
  test('quizBox 가 visible 상태가 된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(false);
  });

  test('quizId 와 문제 텍스트가 올바르게 표시된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    expect(document.getElementById('quizId').textContent).toBe('42');
    expect(document.getElementById('quizStatement').textContent).toBe('다음 중 올바른 것은?');
  });

  test('선택지 버튼 4개가 생성된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    expect(document.querySelectorAll('.option-btn').length).toBe(4);
  });

  test('optionsContainer 가 grid 로 표시된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    expect(document.getElementById('optionsContainer').style.display).toBe('grid');
  });
});

// ─── 테스트 3: 답안 선택 → 서버 전송 및 화면 변화 ─────────────────────────────

describe('테스트 3 — 답안 선택 시 서버 전송 및 화면 변화', () => {
  test('버튼 클릭 시 socket.send 가 1회 호출된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    document.querySelector('.option-btn').click();
    expect(mockSocket.send).toHaveBeenCalledTimes(1);
  });

  test('전송 메시지가 AnswerDto 형식이다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    const clickedBtn = document.querySelector('.option-btn');
    clickedBtn.click();

    const sent = JSON.parse(mockSocket.send.mock.calls[0][0]);
    expect(sent.dataType).toBe('AnswerDto');
    expect(sent.object.quizId).toBe(42);
    expect(sent.object.userName).toBe('테스트유저');
    expect(sent.object.userAnswer).toBe(clickedBtn.textContent);
  });

  test('클릭 후 모든 버튼이 disabled 된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    document.querySelector('.option-btn').click();

    document.querySelectorAll('.option-btn').forEach(btn => {
      expect(btn.disabled).toBe(true);
    });
  });

  test('클릭된 버튼에 option-selected 클래스가 추가된다', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    const clickedBtn = document.querySelector('.option-btn');
    clickedBtn.click();
    expect(clickedBtn.classList.contains('option-selected')).toBe(true);
  });

  test('중복 클릭해도 send 는 1회만 호출된다 (disabled 처리)', () => {
    quizItemUpdate(makeQuizObject(), mockSocket);
    const btn = document.querySelector('.option-btn');
    btn.click();
    btn.click(); // disabled 이므로 onclick 미발생
    expect(mockSocket.send).toHaveBeenCalledTimes(1);
  });
});

// ─── 테스트 4: 타이머 만료 ─────────────────────────────────────────────────────

describe('테스트 4 — 타이머 만료 시 퀴즈 종료', () => {
  test('타이머 만료 시 quizBox 가 hidden 된다', () => {
    // finishedAt 을 과거로 설정하여 즉시 만료
    quizItemUpdate(makeQuizObject(-1), mockSocket);
    jest.advanceTimersByTime(200);
    expect(document.getElementById('quizBox').classList.contains('hidden')).toBe(true);
  });

  test('타이머 만료 시 countdown 이 0.0 으로 표시된다', () => {
    quizItemUpdate(makeQuizObject(-1), mockSocket);
    jest.advanceTimersByTime(200);
    expect(document.getElementById('countdown').textContent).toBe('0.0');
  });

  test('3초 미만이면 countdown-urgent 클래스가 추가된다', () => {
    // 2초 남은 상태
    quizItemUpdate(makeQuizObject(2), mockSocket);
    jest.advanceTimersByTime(200);
    expect(document.getElementById('countdown').classList.contains('countdown-urgent')).toBe(true);
  });
});
