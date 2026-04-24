# RankingQuizFront — 개발 참고 정보

## 도메인 및 프로토콜

| 항목 | 값 |
|------|-----|
| 프론트엔드 도메인 | `rankingquiz.rivercastleworks.site` |
| 백엔드 API base | `rankingquiz.rivercastleworks.site/api` |
| HTTP 프로토콜 | `https://` |
| WebSocket 프로토콜 | `wss://` |

`config.js`에서 전역 변수로 관리:
```js
const FRONTEND_BASE_URL = 'rankingquiz.rivercastleworks.site';
const BACKEND_BASE_URL  = 'rankingquiz.rivercastleworks.site/api';
const protocol          = 'https://';
const websocket_protocol = 'wss://';
```

---

## 페이지 라우팅 (PageController → Thymeleaf 템플릿)

| URL | 템플릿 경로 |
|-----|------------|
| `/` | `templates/home/home.html` |
| `/login-wait` | `templates/login-wait/login-wait.html` |
| `/quiz/voca` | `templates/quiz/voca/voca.html` |
| `/quiz/bible` | `templates/quiz/bible/bible.html` |
| `/quiz-result` | `templates/quiz-result/quiz-result.html` |
| `/user` | `templates/user/user.html` |
| `/feedback` | `templates/feedback/feedback.html` |

---

## HTTP API 엔드포인트

### 인증 불필요

| 메서드 | 경로 | 설명 |
|--------|------|------|
| POST | `/auth/code/kakao` | 카카오 Authorization Code → JWT 발급 |
| POST | `/user-feedback` | 피드백 제출 |

### Bearer 토큰 필요 (`Authorization: Bearer {accessToken}`)

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/member/myInfo` | 내 정보 조회 (name, point) |
| PUT | `/member/myInfo/name?newName={newName}` | 이름 변경 |
| GET | `/quiz-results/my-results` | 오답 목록 조회 |

---

## WebSocket 엔드포인트

| 퀴즈 종류 | URL |
|----------|-----|
| 성경 퀴즈 | `wss://rankingquiz.rivercastleworks.site/api/ws/quiz/bible` |
| 단어 퀴즈 | `wss://rankingquiz.rivercastleworks.site/api/ws/quiz/voca` |

### 메시지 래퍼 구조 (공통)
```json
{ "dataType": "...", "object": { ... } }
```

### 클라이언트 → 서버 메시지

**AccessToken** (연결 직후 전송):
```json
{ "dataType": "AccessToken", "object": "eyJhbGci..." }
```

**AnswerDto** (답안 제출):
```json
{
  "dataType": "AnswerDto",
  "object": {
    "userName": "세션에서 가져온 이름",
    "quizId": 1,
    "writtenAt": "2024-01-01T00:00:00.000Z",
    "userAnswer": "선택한 보기"
  }
}
```

### 서버 → 클라이언트 메시지

| dataType | 처리 함수 | 설명 |
|----------|----------|------|
| `QuizDto` | `quizItemUpdate(object)` | 새 문제 수신 |
| `QuizResultDto` | `quizResultUpdate(object)` | 채점 결과 수신 |
| `GuideMessage` | `guideMessageOn(object)` | 안내 메시지 수신 |

**QuizDto 구조:**
```json
{
  "quizId": 1,
  "finishedAt": [2024, 1, 1, 12, 0, 30],
  "quizContentDto": {
    "statement": "문제 내용",
    "options": ["보기1", "보기2", "보기3", "보기4"]
  }
}
```

**QuizResultDto 구조:**
```json
{
  "correct": true,
  "statement": "문제 내용",
  "answer": "정답",
  "myAnswer": "내 답변",
  "userName": "우승자 이름"
}
```

**GuideMessage 구조:**
```json
{ "message": "안내 텍스트", "display": true }
```

---

## 카카오 OAuth

| 항목 | 값 |
|------|-----|
| Client ID | `41ad8e26ac1e92bfcac84f788d229cef` |
| Redirect URI | `https://rankingquiz.rivercastleworks.site/login-wait` |
| 인가 URL | `https://kauth.kakao.com/oauth/authorize` |

로그인 성공 후 응답 필드: `accessToken`, `refreshToken`, `grantType`, `expiresIn`

---

## sessionStorage 키

| 키 | 저장 시점 | 사용처 |
|----|----------|-------|
| `accessToken` | 카카오 로그인 성공 후 | 모든 인증 API 요청 |
| `refreshToken` | 카카오 로그인 성공 후 | (현재 미사용) |
| `grantType` | 카카오 로그인 성공 후 | (현재 미사용) |
| `expiresIn` | 카카오 로그인 성공 후 | (현재 미사용) |
| `username` | 퀴즈 시작 버튼 클릭 시 | WebSocket 답안 전송 시 userName 필드 |

---

## HTML 요소 ID — JS 의존 목록

HTML 재작성 시 아래 ID는 반드시 유지해야 JS가 정상 동작함.

### home.html
| ID | 용도 |
|----|------|
| `kakaoLoginBtn` | 카카오 로그인 버튼 |
| `nameInput` | 비회원 이름 입력 |
| `voca-quizButton` | Voca 퀴즈 시작 버튼 |
| `bible-quizButton` | Bible 퀴즈 시작 버튼 |

### login-wait.html
| ID | 용도 |
|----|------|
| `step1` | 1단계 진행 상태 메시지 |
| `step2` | 2단계 진행 상태 메시지 |
| `step3` | 3단계 진행 상태 메시지 |

### bible.html / voca.html
| ID | 용도 |
|----|------|
| `stop-button` | 퀴즈 그만하기 버튼 |
| `quizBox` | 퀴즈 문제 전체 컨테이너 |
| `quizStatement` | 문제 텍스트 |
| `quizForm` | 퀴즈 폼 컨테이너 |
| `quizId` | 퀴즈 ID 표시 |
| `countdown` | 카운트다운 타이머 |
| `optionsContainer` | 선택지 버튼 컨테이너 |
| `quizResultContainer` | 채점 결과 컨테이너 |
| `statement` | 결과 화면 문제 텍스트 |
| `quizAnswer` | 정답 표시 |
| `myAnswer` | 내 답변 표시 |
| `isCorrect` | 정답 여부 표시 |
| `guideMessageContainer` | 안내 메시지 컨테이너 |

### user.html
| ID / class | 용도 |
|-----------|------|
| `name-change-btn` | 이름 변경 버튼 |
| `.username` | 사용자 이름 표시 |
| `.point` | 점수 표시 |
| `rankImage` | 랭크 이미지 (`<img>`) |
| `voca-quiz-button` | Voca 퀴즈 이동 버튼 |
| `bible-quiz-button` | Bible 퀴즈 이동 버튼 |
| `quizResultButton` | 오답 보기 이동 버튼 |

### quiz-result.html
| ID / class | 용도 |
|-----------|------|
| `quiz-results-container` | 오답 목록 렌더링 컨테이너 |
| `userButton` | 돌아가기 버튼 |

### feedback.html
| ID | 용도 |
|----|------|
| `submit-btn` | 피드백 제출 버튼 |
| `new-feature-container` | 기능 건의 입력 영역 |
| `inconvenience-container` | 불편 사항 입력 영역 |
| `add-new-feature` | 기능 건의 항목 추가 버튼 |
| `remove-new-feature` | 기능 건의 항목 제거 버튼 |
| `add-inconvenience` | 불편 사항 항목 추가 버튼 |
| `remove-inconvenience` | 불편 사항 항목 제거 버튼 |

---

## 랭크 이미지 경로 및 기준 (static/image/rank/)

| 점수 범위 | 파일 | 랭크명 |
|---------|------|-------|
| 0 ~ 199 | `/image/rank/1Iron.webp` | Iron |
| 200 ~ 399 | `/image/rank/2Bronze.webp` | Bronze |
| 400 ~ 599 | `/image/rank/3Silver.webp` | Silver |
| 600 ~ 799 | `/image/rank/4Gold.webp` | Gold |
| 800 ~ 999 | `/image/rank/5Platinum.webp` | Platinum |
| 1000 ~ | `/image/rank/6Diamond.webp` | Diamond |

---

## JS 파일 구조 — quiz / quiz-result 페이지

### 파일 구성 (bible.html / voca.html 공통)

| 파일 | 역할 |
|------|------|
| `bible-websocket-connect.js` / `voca-websocket-connect.js` | WebSocket 연결 및 메시지 라우팅 |
| `item-control-func.js` | 문제 수신 처리, 타이머, 보기 버튼 생성 |
| `quiz-result-func.js` | 채점 결과 수신 처리, 결과 UI 표시/숨김 |
| `guideMessageFunc.js` | 안내 메시지 표시/숨김 |

> bible/voca WebSocket 파일은 wsUrl 경로만 다르고 나머지 구조가 완전히 동일.
> 재구현 시 공통 함수로 통합하고 wsUrl만 파라미터로 전달하는 구조 권장.

---

### WebSocket 연결 흐름

```
1. stop-button 클릭 이벤트 등록 (quizBoxOff / quizResultOff / guideMessageOff / socket.close)
2. new WebSocket(wsUrl)
3. onopen  → accessToken 존재 시 즉시 전송 → backgroundColorChange()
4. onerror → console.error
5. onclose → /feedback 새 창으로 오픈
6. onmessage → dataType 분기 처리
```

### onmessage 분기 구조

```
dataType === 'QuizDto'
  → quizResultOff(), guideMessageOff()
  → quizItemUpdate(data.object)

dataType === 'QuizResultDto'
  → guideMessageOff(), quizBoxOff()
  → quizResultUpdate(data.object)
  → quizResultOn()

dataType === 'GuideMessage'
  → guideMessageOn(data.object)
```

---

### quizItemUpdate(quizObject) 처리 흐름

```
1. answerData 객체 생성
   { userName, quizId, writtenAt: new Date().toISOString(), userAnswer: null }

2. messageWrapper = { dataType: 'AnswerDto', object: answerData }

3. quizId, statement UI 업데이트

4. 카운트다운 타이머 생성
   finishedAt 배열([yyyy,MM,dd,HH,mm,ss]) → new Date() 변환
   setInterval(100ms)
     → timeLeft > 0 : countdown 텍스트 갱신 (소수점 1자리)
     → timeLeft <= 0 : clearInterval, quizBoxOff()
       ※ 타이머 만료 시 서버로 별도 전송 없음 (서버가 자체 처리)

5. optionsContainer 초기화 후 options 랜덤 셔플
   각 option → <button> 생성
   button.onclick:
     answerData.userAnswer = option
     socket.send(JSON.stringify(messageWrapper))
     클릭 버튼 → classList.add('clicked')
     나머지 버튼 → disabled = true
     ※ 클릭된 버튼 본인은 disabled 미처리 (재구현 시 개선 필요)

6. quizBoxOn()
```

---

### quizResultUpdate(quizResultObject) 처리 흐름

```
correct === true  → isCorrect: "✅ 정답입니다!" (green)
correct === false → isCorrect: "❌ 오답입니다." (red)

statement  → "문제: " + quizResultObject.statement
quizAnswer → "정답: " + quizResultObject.answer
myAnswer   → "내 답변: " + quizResultObject.myAnswer
             (null이면 "내 답변: 없음 😭")

※ quizResultObject.userName (우승자 이름) 필드 존재하나 미사용
  재구현 시 quizWinnerName 요소에 할당 필요
```

---

### UI 표시/숨김 함수 구조

```
quizBoxOn()           → quizBox.classList.remove('hidden'), optionsContainer.display = 'grid'
quizBoxOff()          → quizBox.classList.add('hidden'),    optionsContainer.display = 'none'
quizResultOn()        → quizResultContainer.classList.remove('hidden')
quizResultOff()       → quizResultContainer.classList.add('hidden')
guideMessageOn(obj)   → obj.display === true 일 때 guideMessageContainer 텍스트 설정 후 hidden 제거
guideMessageOff()     → guideMessageContainer 텍스트 초기화 + hidden 추가
```

---

### quiz-result.js 처리 흐름

```
GET /quiz-results/my-results
  Headers: Authorization: Bearer {accessToken}
           Content-Type: application/json

응답 배열 순회 → quiz-results-container에 동적 렌더링
각 항목 구조:
  .quiz-result
    .statement   → result.statement
    .answer-container
      .answer    → "정답: " + result.answer
      .my-answer → "제출: " + result.myAnswer

※ accessToken을 파일 내 선언 없이 전역 참조
  재구현 시 sessionStorage.getItem('accessToken') 명시 필요
```

---

## Spring Boot 서버 정보

| 항목 | 값 |
|------|-----|
| 포트 | `8081` |
| 빌드 도구 | Gradle 8.10.2 |
| Java | 17 |
| Spring Boot | 3.3.4 |
| 의존성 | `spring-boot-starter-web`, `spring-boot-starter-thymeleaf` |
