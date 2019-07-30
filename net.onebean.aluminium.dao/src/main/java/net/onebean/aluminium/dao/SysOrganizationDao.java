package net.onebean.aluminium.dao;
import net.onebean.core.base.BaseDao;
import net.onebean.aluminium.vo.OrgTree;
import net.onebean.aluminium.model.SysOrganization;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOrganizationDao extends BaseDao<SysOrganization> {

    /**
     * 异步查找子节点,每次查找一级
     * @param parentId 父级id
     * @return
     */
    List<OrgTree> findChildAsync(@Param("parentId") Long parentId);
    /**
     * 异步查找子节点,每次查找一级
     * @param parentId 父级id
     * @param dp 数据权限sql
     * @return
     */
    List<OrgTree> findChildAsync(@Param("parentId") Long parentId,@Param("dp") Map<String,Object> dp);

    /**
     * 查找所有子节点
     * @param parentId 父级id
     * @return List<SysOrganization>
     */
    List<SysOrganization> findChildSync(@Param("parentId") Long parentId);

    /**
     * 获取所有父ID
     * @param childId
     * @return
     */
    String getParentOrgIds(@Param("childId")Long childId);

    /**
     * 根据userid 查找所有机构
     * @param userId
     * @return
     */
    List<SysOrganization> findByUserId(@Param("userId")Long userId);

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
    Integer findChildOrderNextNum(Long parentId);
    /**
     * 查询待删除行数ID
     * @param id
     * @return
     */
    List<String> findDeleteId(@Param("id")Long id);
}