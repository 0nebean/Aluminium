package net.onebean.aluminium.service;
import net.onebean.core.base.IBaseBiz;
import net.onebean.aluminium.vo.OrgTree;
import net.onebean.aluminium.model.SysOrganization;
import net.onebean.aluminium.model.SysUser;

import java.util.List;

public interface SysOrganizationService extends IBaseBiz<SysOrganization> {
    /**
     * 查找所有子节点
     * @author 0neBean
     * @return
     */
    List<SysOrganization> findChildSync(SysUser currentUser);

    /**a
     * 异步查找子节点,每次查找一级
     * @author 0neBean
     * @param parentId 父级ID
     * @param selfId 自己的ID
     * @param currentUser 当前登录用户
     * @return list
     */
    List<OrgTree> findChildAsync(Long parentId,Long selfId,SysUser currentUser);

    /**
     * 包装方法,将机构包装成treeList
     * @param before
     * @param selfId
     * @return list
     */
    List<OrgTree> organizationToOrgTree(List<SysOrganization> before,Long selfId);

    /**
     * 根据userid 查找所有机构
     * @param userId
     * @return
     */
    List<SysOrganization> findByUserId(Long userId);

    /**
     * 根据id删除自身以及自项
     * @param id
     */
    Boolean deleteSelfAndChildById(Long id);
    /**
     * 根据父ID查找下一个排序值
     * @param parentId
     * @return
     */
    Integer findChildOrderNextNum(Long parentId);
    /**
     * 删除机构
     * @param id 主键
     * @return bool
     */
    Boolean deleteOrg(Object id);
}