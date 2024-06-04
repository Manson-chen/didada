<template>
  <div id="scoringResultTable">
    <a-form
      :model="formSearchParams"
      :style="{ marginBottom: '20px' }"
      layout="inline"
      @submit="doSearch"
    >
      <a-form-item field="resultName" label="结果名称">
        <a-input
          allow-clear
          v-model="formSearchParams.resultName"
          placeholder="请输入结果名称"
        />
      </a-form-item>
      <a-form-item field="resultDesc" label="结果描述">
        <a-input
          allow-clear
          v-model="formSearchParams.resultDesc"
          placeholder="请输入结果描述"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100px"
          >搜索
        </a-button>
      </a-form-item>
    </a-form>
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total: total,
      }"
      @pageChange="onPageChange"
    >
      <template #resultPicture="{ record }">
        <a-image width="64px" :src="record.resultPicture" />
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button status="success" @click="doUpdate?.(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { defineProps, ref, watchEffect, withDefaults, defineExpose } from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import {
  deleteScoringResultUsingPost,
  listScoringResultVoByPageUsingPost,
} from "@/api/scoringResultController";

interface Pros {
  appId: string;
  doUpdate: (scoringResult: API.ScoringResultVO) => void;
}

const props = withDefaults(defineProps<Pros>(), {
  appId: () => {
    return "";
  },
});

// 定义表单搜索的参数为空
const formSearchParams = ref<API.ScoringResultQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
  sortField: "createTime",
  sortOrder: "descend",
};

const searchParams = ref<API.ScoringResultQueryRequest>({
  // 将其初始值设置为initSearchParams对象的展开运算符（...）拷贝。
  ...initSearchParams,
});

const dataList = ref<API.ScoringResultVO[]>();
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  const params = {
    appId: props.appId as any,
    ...searchParams.value,
  };
  const res = await listScoringResultVoByPageUsingPost(params);
  if (res.data.code === 0) {
    // 如果前面的值为空，就赋值为空数组
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

// 暴露函数给父组件
defineExpose({
  loadData,
});

/**
 * 执行搜索
 */
const doSearch = () => {
  // 其实就是修改 searchParams 的值，该值会被监听，只要修改就会执行 loadData
  searchParams.value = {
    // 该值就是一个不变的对象，不需要使用 .value
    ...initSearchParams, // 填充初始不变的值，也就是 current 和 pageSize
    // 注意下面的值是响应式 ref，要使用 .value 获取值
    ...formSearchParams.value, // 填充表单搜索的值，userName 和 userProfile
  };
};

/**
 * 监听 loadData() 里的变量searchParams，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 当分页变化时，改变搜索条件，触发数据加载
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value, // 填充原本的值
    current: page, // 更新值
  };
};

const doDelete = async (record: any) => {
  const res = await deleteScoringResultUsingPost({ id: record.id });
  if (res.data.code === 0) {
    loadData();
    message.success("删除成功");
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

// 表格列配置
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "名称",
    dataIndex: "resultName",
  },
  {
    title: "描述",
    dataIndex: "resultDesc",
  },
  {
    title: "图片",
    dataIndex: "resultPicture",
    slotName: "resultPicture",
  },
  {
    title: "结果属性",
    dataIndex: "resultProp",
  },
  {
    title: "评分范围",
    dataIndex: "resultScoreRange",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>
