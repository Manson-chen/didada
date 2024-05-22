import { View } from "@tarojs/components";
import { AtButton, AtRadio } from "taro-ui";
import { useEffect, useState } from "react";
import Taro from "@tarojs/taro";
import GlobalFooter from "../../components/GlobalFooter";
import "./index.scss";
import questions from "../../data/questions.json";

export default () => {
  // 当前题目序号（从 1 开始）：状态变量
  const [current, setCurrent] = useState<number>(1);
  // 当前题目
  const [currentQuestion, setCurrentQuestion] = useState<Question>(
    questions[0]
  );
  const radioOptions = currentQuestion.options.map((option) => {
    return {
      label: `${option.key}. ${option.value}`,
      value: option.key,
    };
  });

  // 当前答案，其实就是 "A" 或 "B"
  const [currentAnswer, setCurrentAnswer] = useState<string>();

  // 回答列表
  const [answerList] = useState<string[]>([]);

  // current序号变化时，要驱动当前题目的变化
  useEffect(() => {
    setCurrentQuestion(questions[current - 1]); // 这里是数组下标，current是题目序号
    // 上下翻的时候有做题记录
    setCurrentAnswer(answerList[current - 1]); // 这里的设置和下面的设置不一样，这里是页面的变化取改变当前答案，如果点击下一题，还未做的话是不显示已经选的答案的
  }, [current]);

  return (
    <View className="doQuestionPage">
      {/*{ JSON.stringify(answerList) }*/}
      <View className="at-article__h2 title">
        {current}、{currentQuestion.title}
      </View>
      <View className="options-wrapper">
        <AtRadio
          options={radioOptions}
          value={currentAnswer} // 选择的答案
          onClick={(value) => {
            // 选择答案，将currentAnswer设置为 "A" 或 "B"
            setCurrentAnswer(value);
            // 记录回答
            answerList[current - 1] = value; // 答案要加进数组里
          }}
        />
      </View>
      {current < questions.length && (
        <AtButton
          type="primary"
          className="controlBtn"
          circle
          disabled={!currentAnswer}
          onClick={() => {
            setCurrent(current + 1);
            // setCurrentQuestion(questions[current])
          }}
        >
          {" "}
          下一题{" "}
        </AtButton>
      )}
      {current === questions.length && (
        <AtButton
          type="primary"
          className="controlBtn"
          circle
          // 跳转到目的页面，打开新页面
          onClick={() => {
            // 本地存储答案列表
            Taro.setStorageSync("answerList", answerList);
            Taro.navigateTo({
              url: "/pages/result/index",
            });
          }}
        >
          {" "}
          查看结果{" "}
        </AtButton>
      )}
      {current > 1 && (
        <AtButton
          className="controlBtn"
          circle
          // 跳转到目的页面，打开新页面
          onClick={() => {
            setCurrent(current - 1);
            // setCurrentQuestion(questions[current])
          }}
        >
          {" "}
          上一题{" "}
        </AtButton>
      )}
      <GlobalFooter />
    </View>
  );
};
