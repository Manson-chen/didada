<template>
  <div id="doAnswerPage">
    <a-card>
      <h1>{{ app.appName }}</h1>
      <p>{{ app.appDesc }}</p>
      <h2 style="margin-bottom: 16px">
        {{ current }}、{{ currentQuestion?.title }}
      </h2>
      <div>
        <a-radio-group
          direction="vertical"
          v-model="currentAnswer"
          :options="questionOptions"
          @change="doRadioChange"
        ></a-radio-group>
      </div>
      <div style="margin-top: 24px">
        <a-space size="large">
          <a-button
            type="primary"
            circle
            v-if="current < questionContent.length"
            :disabled="!currentAnswer"
            @click="current += 1"
          >
            下一题
          </a-button>
          <a-button
            type="primary"
            circle
            v-if="current === questionContent.length"
            :disabled="!currentAnswer"
            :loading="submitting"
            @click="doSubmit"
          >
            {{ submitting ? "评分中" : "查看结果" }}
          </a-button>
          <a-button v-if="current > 1" circle @click="current -= 1">
            上一题
          </a-button>
        </a-space>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {
  computed,
  defineProps,
  reactive,
  ref,
  watchEffect,
  withDefaults,
} from "vue";
import API from "@/api";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { listQuestionVoByPageUsingPost } from "@/api/questionController";
import { getAppVoByIdUsingGet } from "@/api/appController";
import {
  addUserAnswerUsingPost,
  generateUserAnswerIdUsingGet,
} from "@/api/userAnswerController";

// 从路由获取id的值
interface Props {
  appId: string;
}

const props = withDefaults(defineProps<Props>(), {
  appId: () => {
    return "";
  },
});

const submitting = ref(false);

const router = useRouter();

const app = ref<API.AppVO>({});

// 唯一 id
const id = ref<number>();

// 生成唯一 id
const generateId = async () => {
  const res = await generateUserAnswerIdUsingGet();
  if (res.data.code === 0) {
    id.value = res.data.data as any;
  } else {
    message.error("获取唯一 id 失败，" + res.data.message);
  }
};

// 进入页面时生成唯一 id
watchEffect(() => {
  generateId();
});

// 题目内容结构：理解为题目列表
const questionContent = ref<API.QuestionContentDTO[]>([]);

// 当前题目的序号（从 1 开始）
const current = ref(1);
// 当前题目
const currentQuestion = ref<API.QuestionContentDTO>({});
// 当前题目的选项
const questionOptions = computed(() => {
  // 如果 options 存在 则返回对应的结果，否则返回空数组
  return currentQuestion.value?.options
    ? currentQuestion.value.options.map((option) => {
        return {
          label: `${option.key}. ${option.value}`,
          value: option.key, // 单选框的 value 值绑定的是选项的 key
        };
      })
    : [];
});
// 当前答案
const currentAnswer = ref<string>();
// 回答列表
const answerList = reactive<string[]>([]);

/**
 * 加载数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  // 获取应用 app
  let res: any = await getAppVoByIdUsingGet({
    id: props.appId as any,
  });
  // 请求成功 且 响应的 data 存在
  if (res.data.code === 0 && res.data.data) {
    app.value = res.data.data as any;
  } else {
    message.error("获取应用失败，" + res.data.message);
  }
  // 获取题目信息
  res = await listQuestionVoByPageUsingPost({
    appId: props.appId as any,
    current: 1,
    pageSize: 1,
    sortField: "createTime",
    sortOrder: "descend",
  });
  // 请求成功 且 响应的 data 存在
  if (res.data.code === 0 && res.data.data?.records) {
    // 因为一个 app 只有一个 question，所以直接取第一个
    questionContent.value = res.data.data?.records[0].questionContent;
  } else {
    message.error("获取题目失败，" + res.data.message);
  }
};

/**
 * 获取旧数据
 */
watchEffect(() => {
  loadData();
});

/**
 * 改变当前题号后，会自动更新当前题目和当前答案
 * currentAnswer：显示回答列表中当前题目的答案，如果之前选过，则显示，否则不显示
 */
watchEffect(() => {
  currentQuestion.value = questionContent.value[current.value - 1];
  currentAnswer.value = answerList[current.value - 1];
});

/**
 * 选中选项后，保存选项记录
 * @param value
 */
const doRadioChange = (value: string) => {
  answerList[current.value - 1] = value;
};

/**
 * 提交
 */
const doSubmit = async () => {
  if (!props.appId || !answerList) {
    return;
  }
  let res: any;
  submitting.value = true;
  res = await addUserAnswerUsingPost({
    appId: props.appId as any,
    choices: answerList,
    id: id.value,
  });

  if (res.data.code === 0 && res.data.data) {
    router.push(`/answer/result/${res.data.data}`);
  } else {
    message.error("提交答案失败，" + res.data.message);
  }
  submitting.value = false;
};
</script>
<style scoped>
#doAnswerPage {
  /*text-align: center;*/
}
</style>
