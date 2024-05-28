package com.jd.didada.scoring;

import com.jd.didada.common.ErrorCode;
import com.jd.didada.exception.BusinessException;
import com.jd.didada.model.entity.App;
import com.jd.didada.model.entity.UserAnswer;
import com.jd.didada.model.enums.AppTypeEnum;
import com.jd.didada.model.enums.ScoreStrategyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Deprecated
public class ScoringStrategyContext {

    /**
     * 引入两种评分策略
     */
    @Resource
    private CustomTestScoringStrategy customTestScoringStrategy;

    @Resource
    private CustomScoreScoringStrategy customScoreScoringStrategy;

    /**
     * 评分
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    public UserAnswer doScore(List<String> choices, App app) throws Exception {
        // 获取应用类型
        AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(app.getAppType());
        ScoreStrategyEnum appScoreStrategyEnum = ScoreStrategyEnum.getEnumByValue(app.getScoringStrategy());
        if (appTypeEnum == null || appScoreStrategyEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应用类型或评分策略不正确");
        }
        // 根据不同的应用类型和评分策略，选择对应的策略执行
        switch (appTypeEnum) {
            case SCORE:
                switch (appScoreStrategyEnum) {
                    case CUSTOM:
                        return customScoreScoringStrategy.doScore(choices, app);
                    case AI:
                        break;
                }
                break;
            case TEST:
                switch (appScoreStrategyEnum) {
                    case CUSTOM:
                        return customTestScoringStrategy.doScore(choices, app);
                    case AI:
                        break;
                }
                break;
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到对应的评分策略");
    }
}
