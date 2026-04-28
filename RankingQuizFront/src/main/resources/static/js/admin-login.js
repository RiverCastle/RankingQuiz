document.addEventListener('DOMContentLoaded', () => {
  // 이미 로그인된 경우 admin 페이지로 이동
  if (sessionStorage.getItem('adminToken')) {
    window.location.href = '/admin';
    return;
  }

  document.getElementById('loginBtn').addEventListener('click', handleLogin);
  document.getElementById('adminPw').addEventListener('keydown', (e) => {
    if (e.key === 'Enter') handleLogin();
  });
});

async function handleLogin() {
  const id = document.getElementById('adminId').value.trim();
  const pw = document.getElementById('adminPw').value;
  const errorEl = document.getElementById('loginError');
  const loginBtn = document.getElementById('loginBtn');

  errorEl.classList.add('hidden');
  loginBtn.disabled = true;
  loginBtn.textContent = '로그인 중...';

  try {
    const res = await fetch(`${protocol}${BACKEND_BASE_URL}/auth/admin`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id, password: pw })
    });

    if (!res.ok) {
      errorEl.classList.remove('hidden');
      return;
    }

    const data = await res.json();
    sessionStorage.setItem('adminToken', data.accessToken);
    window.location.href = '/admin';
  } catch (e) {
    errorEl.classList.remove('hidden');
  } finally {
    loginBtn.disabled = false;
    loginBtn.textContent = '로그인';
  }
}
