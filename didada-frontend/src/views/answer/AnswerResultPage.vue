<template>
  <div id="answerResultPage">
    <a-card>
      <a-row class="grid-demo" style="margin-bottom: 16px">
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ data.resultName }}</h2>
          <p>结果描述：{{ data.resultDesc }}</p>
          <p>结果 id：{{ data.resultId }}</p>
          <p>结果得分：{{ data.resultScore }}</p>
          <p>我的答案：{{ data.choices }}</p>
          <p>应用 id：{{ data.appId }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data.scoringStrategy] }}</p>
          <div
            :style="{
              display: 'flex',
              alignItems: 'center',
            }"
          >
            <span> 答题人： </span>
            <a-avatar
              :size="24"
              :image-url="data.user?.userAvatar"
              :style="{ marginRight: '8px' }"
            />
            <a-typography-text
              >{{ data.user?.userName ?? "无名" }}
            </a-typography-text>
          </div>
          <p>
            答题时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-button type="primary" :href="`/answer/do/${data.appId}`"
            >去答题
          </a-button>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.resultPicture" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import API from "@/api";
import { withDefaults, defineProps, ref, watchEffect, computed } from "vue";
import { getUserAnswerVoByIdUsingGet } from "@/api/userAnswerController";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { APP_TYPE_MAP } from "@/constant/app";
import { APP_SCORING_STRATEGY_MAP } from "@/constant/app";
import { useLoginUserStore } from "@/store/userStore";

// 设置为空对象
const data = ref<API.UserAnswerVO>({});

interface Props {
  id: string;
}

/**
 * 设置属性的默认值，从路由中获取 id
 */
const props = withDefaults(defineProps<Props>(), {
  id: () => {
    return "";
  },
});

/**
 * 根据 id 获取 userAnswer 详情
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getUserAnswerVoByIdUsingGet({
    id: props.id as any,
  });
  // 请求成功 且 响应的 data 存在
  if (res.data.code === 0 && res.data.data) {
    data.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
  console.log(data.value);
};

/**
 * 监听变量，改变时触发数据的重新加载
 */
watchEffect(() => {
  loadData();
});
</script>

<style scoped>
#answerResultPage {
}

/* 给 content-wruserAnswerer 下的第一级标签添加样式 */
#answerResultPage .content-wrapper > * {
  margin-bottom: 24px;
}
</style>
