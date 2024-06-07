package com.jd.didada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.didada.model.dto.statistic.AppAnswerCountDTO;
import com.jd.didada.model.dto.statistic.AppAnswerResultCountDTO;
import com.jd.didada.model.entity.UserAnswer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author jiandongchen
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2024-05-23 14:38:48
* @Entity generator.domain.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    /**
     * 统计 app 答题的数量
     * @return
     */
    @Select("select appId, count(userId) as answerCount from user_answer\n" +
            "    group by appId  order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> doAppAnswerCount();


    /**
     * 统计某个 app 的答题结果
     * @param appId
     * @return
     */
    @Select("select resultName, count(resultName) as resultCount from user_answer\n" +
            "    where appId = #{appId}\n" +
            "    group by resultName order by resultCount desc;")
    List<AppAnswerResultCountDTO> doAppAnswerResultCount(Long appId);
}




