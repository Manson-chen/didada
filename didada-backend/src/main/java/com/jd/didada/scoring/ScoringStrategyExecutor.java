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
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;

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
        // 根据注解类型获取策略
        for (ScoringStrategy scoringStrategy : scoringStrategyList) {
            // 通过反射获取注解
            ScoringStrategyConfig scoringStrategyConfig = scoringStrategy.getClass().getAnnotation(ScoringStrategyConfig.class);
            if (scoringStrategyConfig.appType() == appTypeEnum.getValue() && scoringStrategyConfig.scoringStrategy() == appScoreStrategyEnum.getValue()) {
                return scoringStrategy.doScore(choices, app);
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用配置有误，未找到对应的评分策略");
    }
}
