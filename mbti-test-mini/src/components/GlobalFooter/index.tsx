import { View } from "@tarojs/components";
// 已经全局引入了，不需要按需引入
// import "taro-ui/dist/style/components/button.scss" // 按需引入
import "./index.scss";

export default () => {
  return (
    <View className="globalFooter">
      <View className="footer">作者：Jiandong</View>
    </View>
  );
};
