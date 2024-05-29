import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 登录用户信息全局状态
 */
export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.LoginUserVO>({
    userName: "未登录",
  });

  // 相当于 getter 方法：远程获取当前登录用户
  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      loginUser.value = res.data.data;
    } else {
      // 如果登录失败了，也会有个 userRole 是未登录，而不是每次都重新获取登录信息
      // 如果有 userRole 就是获取过登录信息了
      loginUser.value = {
        userRole: ACCESS_ENUM.NOT_LOGIN,
      };
    }
  }

  // 相当于 setter 方法：设置当前登录用户
  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser;
  }

  return { loginUser, setLoginUser, fetchLoginUser };
});
