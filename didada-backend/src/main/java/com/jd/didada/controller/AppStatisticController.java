package com.jd.didada.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jd.didada.annotation.AuthCheck;
import com.jd.didada.common.*;
import com.jd.didada.constant.UserConstant;
import com.jd.didada.exception.BusinessException;
import com.jd.didada.exception.ThrowUtils;
import com.jd.didada.mapper.UserAnswerMapper;
import com.jd.didada.mapper.UserMapper;
import com.jd.didada.model.dto.app.AppAddRequest;
import com.jd.didada.model.dto.app.AppEditRequest;
import com.jd.didada.model.dto.app.AppQueryRequest;
import com.jd.didada.model.dto.app.AppUpdateRequest;
import com.jd.didada.model.dto.statistic.AppAnswerCountDTO;
import com.jd.didada.model.dto.statistic.AppAnswerResultCountDTO;
import com.jd.didada.model.entity.App;
import com.jd.didada.model.entity.User;
import com.jd.didada.model.enums.ReviewStatusEnum;
import com.jd.didada.model.vo.AppVO;
import com.jd.didada.service.AppService;
import com.jd.didada.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * App 统计分析接口
 *
 * @author <a href="https://github.com/Manson-chen">程序员Jiandong</a>
 *
 */
@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用及回答数统计（top 10）
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }

    /**
     * 某应用回答结果分布统计
     * @param appId
     * @return
     */
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }
}
