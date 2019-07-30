package net.onebean.aluminium.service;
import net.onebean.core.base.IBaseBiz;
import net.onebean.aluminium.model.SysRole;
import net.onebean.core.extend.Sort;
import net.onebean.core.query.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleService extends IBaseBiz<SysRole> {

    /**
     * 根据用户登录名查询用户所有角色
     * @param userId 用户ID
     * @return List<SysRole>
     */
    List<SysRole> findRolesByUserId(Long userId);
    /**
     * 删除角色
     * @param id 主键
     * @return bool
     */
    Boolean deleteRole(Object id);

    /**
     * 根据角色名查找角色
     * @param name 角色名
     * @return list
     */
    List<SysRole> findByName(String name);

    /**
     * 添加用户角色关联
     * @param userId 用户id
     * @param roleIds 角色IDs
     * @return bool
     */
    Boolean addRoleUser(Long userId, String roleIds);

    /**
     * 移除户角色关联
     * @param urIds ids
     * @return bool
     */
    Boolean removeRoleUser(String urIds);
}