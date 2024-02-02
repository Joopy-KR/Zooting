import { createApp } from "vue"
import { createPinia } from "pinia"
import piniaPersist from "pinia-plugin-persistedstate"
import App from './App.vue'
import router from './router'
import axios from "axios"
import './index.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPersist)

// fontawesome import
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
library.add(fas)
app.component('font-awesome-icon', FontAwesomeIcon)

// click outside
import clickOutside from 'v3-click-outside'
app.use(clickOutside)

app.config.globalProperties.$axios = axios
app.use(router)
app.use(pinia)

app.mount("#app")
