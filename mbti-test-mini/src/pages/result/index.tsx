import { Image, View } from "@tarojs/components";
import { AtButton } from "taro-ui";
import Taro from "@tarojs/taro";
import GlobalFooter from "../../components/GlobalFooter";
import "./index.scss";
import headerBg from "../../assets/background.jpeg";
import questionResults from "../../data/question_result.json";
import { getBestQuestionResult } from "../../utils/bizUtils";
import questions from "../../data/questions.json";

/**
 * 查看结果页面
 */
export default () => {
  // 结果页面取答案
  const answerList = Taro.getStorageSync("answerList");
  // 如果答案为空 或者 答案列表长度小于 1
  if (!answerList || answerList.length < 1) {
    Taro.showToast({
      title: "答案为空",
      icon: "error",
      duration: 3000,
    });
  }

  // 通过 utils 中的算法获取结果
  const result = getBestQuestionResult(answerList, questions, questionResults);

  return (
    <View className="resultPage">
      <View className="at-article__h1 title">{result.resultName}</View>
      <View className="at-article__h2 subTitle">{result.resultDesc}</View>
      <AtButton
        type="primary"
        className="enterBtn"
        circle
        // 跳转到目的页面，打开新页面
        onClick={() => {
          Taro.reLaunch({
            url: "/pages/index/index",
          });
        }}
      >
        返回主页{" "}
      </AtButton>
      <Image src={headerBg} className={headerBg} />
      <GlobalFooter />
    </View>
  );
};
