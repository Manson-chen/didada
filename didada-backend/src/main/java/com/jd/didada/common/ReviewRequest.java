package com.jd.didada.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 审核请求
 *
 * @author <a href="https://github.com/Manson-chen">程序员Jiandong</a>
 *
 */
@Data
public class ReviewRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    private static final long serialVersionUID = 1L;
}