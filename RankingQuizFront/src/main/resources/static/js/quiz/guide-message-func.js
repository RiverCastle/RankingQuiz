function guideMessageOn(guideMessage) {
    if (!guideMessage.display) return;
    const textEl = document.getElementById('guideMessageText');
    if (textEl) textEl.textContent = guideMessage.message;
    document.getElementById('guideSection')?.classList.remove('quiz-section--hidden');
}

function guideMessageOff() {
    const textEl = document.getElementById('guideMessageText');
    if (textEl) textEl.textContent = '';
    document.getElementById('guideSection')?.classList.add('quiz-section--hidden');
}
