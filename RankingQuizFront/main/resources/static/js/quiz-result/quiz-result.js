fetch(protocol + BACKEND_BASE_URL + '/quiz-results/my-results', {
    method: 'GET',
    headers: {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    }
})
.then(response => response.json())
.then(data => {
    console.log(data);
    const container = document.getElementById('quiz-results-container');
    container.innerHTML = '';

    data.forEach(result => {
        console.log(result);
        const resultElement = document.createElement('div');
        resultElement.className = 'quiz-result';

        const statementElement = document.createElement('div');
        statementElement.className = 'statement';
        statementElement.textContent = result.statement;

        const answerContainer = document.createElement('div');
        answerContainer.className = 'answer-container';

        const answerElement = document.createElement('div');
        answerElement.className = 'answer';
        answerElement.textContent = `정답: ${result.answer}`;

        const myAnswerElement = document.createElement('div');
        myAnswerElement.className = 'my-answer';
        myAnswerElement.textContent = `제출: ${result.myAnswer}`;

        answerContainer.appendChild(answerElement);
        answerContainer.appendChild(myAnswerElement);
        resultElement.appendChild(statementElement);
        resultElement.appendChild(answerContainer);
        container.appendChild(resultElement);
    });
})
.catch(error => console.error('Error fetching quiz results:', error));