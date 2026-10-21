/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        bgDark: '#0A0A0F',
        cardSurface: '#14141F',
        cardBorder: '#23233A',
        inputBorder: '#2E2E4A',
        textPrimary: '#F5F5FA',
        textMuted: '#9498AE',
        acidLime: '#C8FF3D',
        secondaryViolet: '#7B5CFF',
        deepPurpleStart: '#3A24A8',
        deepPurpleEnd: '#17113D',
        errorDanger: '#FF7A8A',
      },
      fontFamily: {
        sora: ['Sora', 'sans-serif'],
      },
      borderRadius: {
        'card': '22px',
        'btn': '18px',
      },
    },
  },
  plugins: [],
};
