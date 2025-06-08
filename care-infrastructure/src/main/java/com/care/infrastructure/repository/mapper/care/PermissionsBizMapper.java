package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.Permissions;

public interface PermissionsBizMapper {
    Permissions selectByPrimaryKey(Integer id);
}