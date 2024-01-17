import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPersist from "pinia-plugin-persistedstate";

import App from "./App.vue";
import router from "./router";
import axios from "axios";
import "./index.css";

const app = createApp(App);

const pinia = createPinia();
pinia.use(piniaPersist);

app.use(pinia);
app.use(router);
app.config.globalProperties.$axios = axios;

app.mount("#app");
