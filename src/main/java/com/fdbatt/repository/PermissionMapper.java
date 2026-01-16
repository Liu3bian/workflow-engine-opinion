package com.fdbatt.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("""
      SELECT p.perm_code
      FROM sys_permission p
      JOIN sys_role_permission rp ON p.id = rp.permission_id
      JOIN sys_user_role ur ON rp.role_id = ur.role_id
      WHERE ur.user_id = #{userId}
    """)
    List<String> findPermsByUserId(Long userId);
}
