package com.jd.didada.scoring;

import com.jd.didada.model.entity.App;
import com.jd.didada.model.entity.UserAnswer;

import java.util.List;

public interface ScoringStrategy {

    /**
     * 执行评分
     * @param choices 用户答案列表
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}
