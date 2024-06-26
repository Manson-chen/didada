import axios from "axios";
import { Message } from "@arco-design/web-vue";

// 是否是开发环境
export const isDev = process.env.NODE_ENV === "development";

const myAxios = axios.create({
  baseURL: isDev
    ? "http://localhost:8101"
    : "https://didada-backend-110829-4-1327185844.sh.run.tcloudbase.com/",
  timeout: 60000,
  withCredentials: true,
});

// 全局请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// 全局响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    console.log(response);
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    const { data } = response;
    if (data.code === 40100) {
      // 不是 获取用户信息的请求，或者用户目前不是已经在用户登录页面，则跳转到登录页面
      if (
        !response.request.responseURL.includes("user/get/login") &&
        !window.location.pathname.includes("/user/login")
      ) {
        Message.warning("请先登录");
        window.location.href = `/user/login?redirect=${window.location.href}`;
      }
    }
    return response;
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);

// 全局导出，外部使用不需要引入
export default myAxios;
