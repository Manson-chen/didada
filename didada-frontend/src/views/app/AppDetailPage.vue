<template>
  <div id="appDetailPage">
    <a-card>
      <a-row class="grid-demo" style="margin-bottom: 16px">
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ data.appName }}</h2>
          <p>{{ data.appDesc }}</p>
          <p>应用类型：{{ APP_TYPE_MAP[data.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[data.scoringStrategy] }}</p>
          <div
            :style="{
              display: 'flex',
              alignItems: 'center',
            }"
          >
            <span>作者：</span>
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
            创建时间：{{ dayjs(data.createTime).format("YYYY-MM-DD HH:mm:ss") }}
          </p>
          <a-space size="medium">
            <a-button type="primary" :href="`/answer/do/${props.id}`"
              >开始答题
            </a-button>
            <a-button @click="doShare">分享应用</a-button>
            <a-button v-if="isMy" :href="`/add/question/${id}`"
              >设置题目
            </a-button>
            <a-button v-if="isMy" :href="`/add/scoring_result/${id}`"
              >设置评分
            </a-button>
            <a-button v-if="isMy" :href="`/add/app/${id}`">修改应用</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="data.appIcon" />
        </a-col>
      </a-row>
    </a-card>
    <ShareModal :link="shareLink" title="应用分享" ref="shareModalRef" />
  </div>
</template>

<script setup lang="ts">
import API from "@/api";
import { withDefaults, defineProps, ref, watchEffect, computed } from "vue";
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { dayjs } from "@arco-design/web-vue/es/_utils/date";
import { APP_TYPE_MAP } from "@/constant/app";
import { APP_SCORING_STRATEGY_MAP } from "@/constant/app";
import { useLoginUserStore } from "@/store/userStore";
import ShareModal from "@/components/ShareModal.vue";

// 设置为空对象
const data = ref<API.AppVO>({});

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

// 获取登录用户
const loginUserStore = useLoginUserStore();
let loginUserId = loginUserStore.loginUser.id;
// 判断该app是否是当前登录用户所有
// computed 是为了等待 loginUser 获取到值之后重新计算
const isMy = computed(() => {
  return loginUserId && loginUserId === data.value.userId;
});

/**
 * 根据 id 获取 app 详情
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({
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

//分享弹窗的引用
const shareModalRef = ref();

// 分享链接
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.id}`;

const doShare = (e: Event) => {
  if (shareModalRef.value) {
    shareModalRef.value.openModal();
  }
  // 停止冒泡，防止跳转到详情页
  e.stopPropagation();
};
</script>

<style scoped>
#appDetailPage {
}

/* 给 content-wrapper 下的第一级标签添加样式 */
#appDetailPage .content-wrapper > * {
  margin-bottom: 24px;
}
</style>
