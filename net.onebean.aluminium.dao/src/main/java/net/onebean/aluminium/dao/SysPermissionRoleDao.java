package net.onebean.aluminium.dao;
import net.onebean.core.base.BaseDao;
import net.onebean.aluminium.model.SysPermissionRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysPermissionRoleDao extends BaseDao<SysPermissionRole> {
    /**
     * 获取角色菜单
     * @param roleId
     * @return
     */
    List<SysPermissionRole> getRolePremissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据RoleId删除数据
     * @param roleId
     */
    void deteleByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据permissionId删除数据
     * @param permissionId
     */
    void deteleByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 批量插入
     * @param list
     */
    void insertBatch(@Param("list") List<Map<String,Object>> list);
}