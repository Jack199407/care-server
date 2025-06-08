package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Comments;

public interface CommentsBizMapper {
    Comments selectByPrimaryKey(Integer id);
}