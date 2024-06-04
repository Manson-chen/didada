<template>
  <div id="homePage">
    <div class="searchBar">
      <a-input-search
        :style="{ width: '320px' }"
        placeholder="快速发现答题应用"
        button-text="搜索"
        size="large"
        search-button
      />
    </div>
    <a-list
      class="list-demo-action-layout"
      :grid-props="{ gutter: [20, 20], sm: 24, md: 12, lg: 8, xl: 6 }"
      :bordered="false"
      :data="dataList"
      :pagination-props="{
        current: searchParams.current,
        pageSize: searchParams.pageSize,
        total,
      }"
      @pageChange="onPageChange"
    >
      <template #item="{ item }">
        <AppCard :app="item" />
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
import AppCard from "@/components/AppCard.vue";
import API from "@/api";
import { listAppVoByPageUsingPost } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";

// 定义初始搜索条件（不被修改）
const initSearchParmas = {
  current: 1,
  pageSize: 12, // 是 3 和 4 的倍数
};

// 定义表单搜索的初始参数（搜索时使用，点击搜素才会请求数据）
const formSearchParams = ref<API.AppQueryRequest>({});

// 与 formSearchParams 不一样是因为，修改搜索框的值，不会立即触发
const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParmas,
});

const dataList = ref<API.AppVO[]>();
const total = ref<number>(0);

/**
 * 加载应用列表数据
 */
const loadData = async () => {
  const res = await listAppVoByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 执行搜索，不用直接发请求，而是修改 searchParams 的值，因为该值给监听，修改就会间接重新请求
 */
const doSearch = () => {
  searchParams.value = {
    ...initSearchParmas,
    ...formSearchParams.value,
  };
};

/**
 * 监听 loadData() 里的变量searchParams，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 当分页变化时，改变搜索条件，触发数据加载，和 doSearch 类似
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
</script>

<style scoped>
#homePage {
}

.searchBar {
  margin: 0 auto 28px;
  /* 搜索框居中 */
  text-align: center;
}

.list-demo-action-layout .image-area {
  width: 183px;
  height: 119px;
  overflow: hidden;
  border-radius: 2px;
}

.list-demo-action-layout .list-demo-item {
  padding: 20px 0;
  border-bottom: 1px solid var(--color-fill-3);
}

.list-demo-action-layout .image-area img {
  width: 100%;
}

.list-demo-action-layout .arco-list-item-action .arco-icon {
  margin: 0 4px;
}
</style>
