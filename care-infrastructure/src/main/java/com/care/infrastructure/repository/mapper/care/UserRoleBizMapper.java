package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.UserRole;

public interface UserRoleBizMapper {
    UserRole selectByPrimaryKey(Integer id);
}