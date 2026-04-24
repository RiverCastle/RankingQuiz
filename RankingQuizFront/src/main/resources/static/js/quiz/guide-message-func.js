function guideMessageOn(guideMessage) {
    if (!guideMessage.display) return;
    const container = document.getElementById('guideMessageContainer');
    const textEl    = document.getElementById('guideMessageText');
    if (container && textEl) {
        textEl.textContent = guideMessage.message;
        container.classList.remove('hidden');
    }
}

function guideMessageOff() {
    const container = document.getElementById('guideMessageContainer');
    const textEl    = document.getElementById('guideMessageText');
    if (container && textEl) {
        textEl.textContent = '';
        container.classList.add('hidden');
    }
}
