package net.onebean.aluminium.dao;

import net.onebean.aluminium.vo.OrgTree;
import net.onebean.core.base.BaseDao;
import net.onebean.aluminium.vo.MenuTree;
import net.onebean.aluminium.model.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysPermissionDao extends BaseDao<SysPermission> {

    /**
     * 根据用户查找菜单
     * @param userId
     * @return
     */
    List<SysPermission> findByUserId(@Param("userId")String userId);

    /**
     * 异步查找子节点,每次查找一级
     * @param parentId 父级id
     * @param dp 数据权限sql
     * @return
     */
    List<MenuTree> findChildAsync(@Param("parentId") Long parentId, @Param("dp") Map<String,Object> dp);

    /**
     * 根据id删除自身以及自项
     * @param id
     */
    void deleteSelfAndChildById(@Param("id")Long id);

    /**
     * 根据父ID查找下一个排序值
     * @param parentId
     * @return
     */
    Integer findChildOrderNextNum(@Param("parentId") Long parentId);

    /**
     * 获取所有父级ID
     * @param childId
     * @return pids
     */
    String getParentMenuIds(@Param("childId")Long childId);

}