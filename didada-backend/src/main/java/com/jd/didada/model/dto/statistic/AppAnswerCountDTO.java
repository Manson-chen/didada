package com.jd.didada.model.dto.statistic;

import lombok.Data;

import java.io.Serializable;

/**
 * App 用户提交答案数统计
 *
 */
@Data
public class AppAnswerCountDTO {

    private Long appId;

    /**
     * 用户提交答案数
     */
    private Long answerCount;

}