package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Users;

public interface UsersBizMapper {
    Users selectByPrimaryKey(Integer id);
}