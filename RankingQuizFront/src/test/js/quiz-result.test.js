/**
 * 테스트 5 — 퀴즈 결과 수신 시 결과 화면 표시
 */
const fs   = require('fs');
const path = require('path');

eval(fs.readFileSync(
  path.resolve(__dirname, '../../main/resources/static/js/quiz/quiz-result-func.js'),
  'utf8'
));

function setupDOM() {
  document.body.innerHTML = `
    <div id="quizResultContainer" class="hidden">
      <p id="isCorrect"></p>
      <p id="statement"></p>
      <p id="quizAnswer"></p>
      <p id="myAnswer"></p>
      <p id="quizWinnerName"></p>
    </div>
  `;
}

const correctResult = {
  correct: true,
  statement: '문제 내용',
  answer: '정답보기',
  myAnswer: '정답보기',
  userName: '우승자'
};

const incorrectResult = {
  correct: false,
  statement: '문제 내용',
  answer: '정답보기',
  myAnswer: '오답보기',
  userName: null
};

const noAnswerResult = {
  correct: false,
  statement: '문제 내용',
  answer: '정답보기',
  myAnswer: null,
  userName: null
};

describe('테스트 5 — 퀴즈 결과 수신 시 화면 표시', () => {
  beforeEach(setupDOM);

  test('정답일 때 result-correct 클래스가 적용된다', () => {
    quizResultUpdate(correctResult);
    expect(document.getElementById('isCorrect').className).toBe('result-correct');
  });

  test('오답일 때 result-incorrect 클래스가 적용된다', () => {
    quizResultUpdate(incorrectResult);
    expect(document.getElementById('isCorrect').className).toBe('result-incorrect');
  });

  test('문제, 정답, 내 답변이 올바르게 표시된다', () => {
    quizResultUpdate(correctResult);
    expect(document.getElementById('statement').textContent).toBe('문제 내용');
    expect(document.getElementById('quizAnswer').textContent).toBe('정답: 정답보기');
    expect(document.getElementById('myAnswer').textContent).toBe('내 답변: 정답보기');
  });

  test('미제출(myAnswer === null) 이면 "미제출" 텍스트가 표시된다', () => {
    quizResultUpdate(noAnswerResult);
    expect(document.getElementById('myAnswer').textContent).toContain('미제출');
  });

  test('우승자 이름이 표시된다', () => {
    quizResultUpdate(correctResult);
    expect(document.getElementById('quizWinnerName').textContent).toContain('우승자');
  });

  test('userName 이 null 이면 우승자 이름이 비어있다', () => {
    quizResultUpdate(incorrectResult);
    expect(document.getElementById('quizWinnerName').textContent).toBe('');
  });
});

describe('quizResultOn / quizResultOff', () => {
  beforeEach(setupDOM);

  test('quizResultOn 은 hidden 을 제거한다', () => {
    quizResultOn();
    expect(document.getElementById('quizResultContainer').classList.contains('hidden')).toBe(false);
  });

  test('quizResultOff 는 hidden 을 추가한다', () => {
    document.getElementById('quizResultContainer').classList.remove('hidden');
    quizResultOff();
    expect(document.getElementById('quizResultContainer').classList.contains('hidden')).toBe(true);
  });
});
