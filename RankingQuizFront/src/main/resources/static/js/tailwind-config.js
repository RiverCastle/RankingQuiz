tailwind.config = {
  theme: {
    extend: {
      colors: {
        neon: {
          blue: '#00f0ff',
          'blue-dark': '#0099cc',
          purple: '#bf00ff',
          'purple-dark': '#7700aa',
          pink: '#ff2d78',
        },
        surface: {
          base: '#080c14',
        },
        kakao: {
          yellow: '#FEE500',
          hover: '#f5da00',
          dark: '#191919',
        },
      },
      fontFamily: {
        display: ['Pretendard', 'system-ui', 'sans-serif'],
      },
      animation: {
        'pulse-slow': 'pulse 3s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'float': 'float 6s ease-in-out infinite',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-12px)' },
        },
      },
    },
  },
}
