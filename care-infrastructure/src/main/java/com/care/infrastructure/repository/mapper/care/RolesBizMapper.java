package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Roles;

public interface RolesBizMapper {
    Roles selectByPrimaryKey(Long id);
}