package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Comments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentsBizMapper {
    List<Comments> selectByCondition(@Param("offset") int offset,
                                     @Param("limit") int limit,
                                     @Param("displayList") List<Boolean> displayList);

    int countByCondition(@Param("displayList") List<Boolean> displayList);

    int insertComment(Comments comment);

    int updateDisplayById(@Param("id") Integer id, @Param("display") Boolean display);

    int deleteById(@Param("id") Integer id);

}