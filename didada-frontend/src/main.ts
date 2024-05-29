import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ArcoVue from "@arco-design/web-vue";
import { createPinia } from "pinia";
import "@arco-design/web-vue/dist/arco.css";
// 引入权限判断
import "@/access";

const pinia = createPinia();

createApp(App).use(router).use(ArcoVue).use(pinia).mount("#app");
