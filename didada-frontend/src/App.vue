<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRoute } from "vue-router";
import { onMounted } from "vue";

const route = useRoute();

/**
 * 全局初始化函数，在全局单词调用的代码，都可以写到这里
 */
const doInt = () => {
  console.log("hello 欢迎来到我的项目");
};
// 只在页面加载完成时执行一次
onMounted(() => {
  doInt();
});
</script>

<style scoped></style>
