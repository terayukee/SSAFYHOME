import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

import "@/assets/main.css";

// ==========  font-awesome 시작 ==========
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

// 사용할 아이콘 개별 임포트
import { faHeart as fasHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as farHeart } from "@fortawesome/free-regular-svg-icons";

import "@fortawesome/fontawesome-svg-core/styles.css"; // FontAwesome CSS 가져오기

// 아이콘을 라이브러리에 추가
library.add(fasHeart, farHeart);
// ========== font-awesome 끝 ==========

const pinia = createPinia();
const app = createApp(App);
pinia.use(piniaPluginPersistedstate);

app.use(pinia);
app.use(router);
app.component("font-awesome-icon", FontAwesomeIcon);

app.mount("#app");
