/** @type {import('tailwindcss').Config} */

const colors = require('tailwindcss/colors');

module.exports = {
  content: [
    './index.html', 
    './src/**/*.{vue,js,ts,jsx,tsx}',
    './node_modules/vue-tailwind-datepicker/**/*.js',
    './node_modules/flowbite/**/*.js',
  ],
   darkMode: 'media',
   theme: {
     extend: {
      colors: {
        'vtd-primary': colors.sky,
      }
     },
   },
   variants: {
     extend: {},
   },
   plugins: [
    require("@tailwindcss/forms"), 
    require('flowbite/plugin'),
    require("daisyui")
  ],
 }