package com.mia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mia.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Author GuoDingWei
 * @Date 2022/5/12 20:20
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("select * from ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{adminId})")
    List<Permission> findPermissionsByAdminId(Long adminId);
}
