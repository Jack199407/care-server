package com.care.infrastructure.repository.mapper.care;

import com.care.infrastructure.repository.model.care.RolePermission;

public interface RolePermissionBizMapper {
    RolePermission selectByPrimaryKey(Integer id);
}