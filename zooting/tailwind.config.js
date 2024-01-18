/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './index.html', 
    './src/**/*.{vue,js,ts,jsx,tsx}',
    './node_modules/vue-tailwind-datepicker/**/*.js'
  ],
   darkMode: 'media',
   theme: {
     extend: {
      colors: {
        // "vtd-primary": colors.sky,
        // "vtd-secondary": colors.gray,
      },
     },
   },
   variants: {
     extend: {},
   },
   plugins: [require("@tailwindcss/forms")],
 }