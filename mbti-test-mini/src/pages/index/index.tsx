import { View, Image } from "@tarojs/components";
import { AtButton } from "taro-ui";
import Taro from "@tarojs/taro";
import GlobalFooter from "../../components/GlobalFooter";
// 已经全局引入了，不需要按需引入
// import "taro-ui/dist/style/components/button.scss" // 按需引入
import "./index.scss";
import headerBg from "../../assets/background.jpeg";

export default () => {
  return (
    <View className="indexPage">
      <View className="at-article__h1 title">MBTI 性格测试</View>
      <View className="at-article__h2 subTitle">
        只需 2 分钟，就能非常准确地描述出你是谁，以及你的性格特点
      </View>
      <AtButton
        type="primary"
        className="enterBtn"
        circle
        // 跳转到目的页面，打开新页面
        onClick={() => {
          Taro.navigateTo({
            url: "/pages/doQuestion/index",
          });
        }}
      >
        开始测试{" "}
      </AtButton>
      <Image src={headerBg} className={headerBg} />
      <GlobalFooter />
    </View>
  );
};
