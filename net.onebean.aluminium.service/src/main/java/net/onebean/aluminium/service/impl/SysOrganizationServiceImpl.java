package net.onebean.aluminium.service.impl;

import net.onebean.aluminium.common.dataPerm.DataPermUtils;
import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.dao.SysOrganizationDao;
import net.onebean.aluminium.model.SysOrganization;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysOrganizationService;
import net.onebean.aluminium.service.SysUserService;
import net.onebean.aluminium.vo.OrgTree;
import net.onebean.core.base.BaseBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.core.form.Parse;
import net.onebean.util.CollectionUtil;
import net.onebean.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysOrganizationServiceImpl extends BaseBiz<SysOrganization, SysOrganizationDao> implements SysOrganizationService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private DataPermUtils dataPermUtils;

    /**
     * 查找所有子节点
     * @author Heisenberg
     * @return list
     */
    public List<SysOrganization> findChildSync(SysUser currentUser) {
        String join = " LEFT JOIN sys_user u on u.org_id = t.id";
        List<SysOrganization> queryList = this.findAll(dataPermUtils.dataPermFilter(currentUser,"t","u",join));
        if (CollectionUtil.isNotEmpty(queryList)){
            queryList.get(0).setIsRoot("1");
        }
        return tree(queryList);
    }


    /**
     * 包装数据列表成树结构
     * @param original 数据列表
     * @return List<SysOrganization>
     */
    private List<SysOrganization> tree(List<SysOrganization> original){
        List<SysOrganization> _final = new ArrayList<>();
        for (SysOrganization p0:original){
            //如果是根节点,放入最终的结果中剩余数量减一
            if (p0.getIsRoot().equals("1")){
                _final.add(p0);
            }
        }
        treeChild(_final,original);
        return _final;
    }

    /**
     * 包装方法递归子方法
     * @param list 数据列表
     * @param original 元数据
     */
    private void treeChild(List<SysOrganization> list,List<SysOrganization> original){
        List<SysOrganization> _final;
        for (SysOrganization s : list) {
            if (CollectionUtil.isEmpty(s.getChildList())){
                _final = new ArrayList<>();
                for (SysOrganization s1 : original) {
                    if (null != s1.getParentId() && null != s.getId() && s1.getParentId().equals(s.getId())){
                        _final.add(s1);
                    }
                }
                //递归算法
                treeChild(_final,original);
                s.setChildList(_final);
            }
        }
    }

    /**
     * 异步查找子节点,每次查找一级
     * @author Heisenberg
     * @param parentId
     * @return
     */
    @Override
    public List<OrgTree> findChildAsync(Long parentId,Long selfId,SysUser currentUser) {
        List<OrgTree> res = new ArrayList<>();
        String join = "LEFT JOIN sys_user u ON o.`id` = u.`org_id`";
        List<OrgTree> list = baseDao.findChildAsync(parentId,dataPermUtils.dataPermFilter(currentUser,"o","t",join));
        for (OrgTree o : list) {//某些业务场景 节点不能选择自己作为父级节点,故过滤掉所有自己及以下节点
            if (null == selfId || (Parse.toInt(o.getId()) != selfId) || selfId == 1) {
                res.add(o);
            }
        }
        return res;
    }


    @Override
    public List<OrgTree> organizationToOrgTree(List<SysOrganization> before,Long selfId){
        List<OrgTree> treeList = new ArrayList<>();
        OrgTree temp;
        if(CollectionUtil.isNotEmpty(before)){
            for (SysOrganization sysOrganization: before) {
                if (null == selfId || (Parse.toInt(sysOrganization.getId()) != selfId) || selfId == 1) {
                    temp = new OrgTree();
                    temp.setTitle(sysOrganization.getOrgName());
                    temp.setId(sysOrganization.getId());
                    temp.setSort(sysOrganization.getSort());
                    if (CollectionUtil.isNotEmpty(sysOrganization.getChildList())) {
                        temp.setType(OrgTree.TYPE_FOLDER);
                        temp.setChildList(organizationToOrgTree(sysOrganization.getChildList(),selfId));
                    } else {
                        temp.setType(OrgTree.TYPE_ITEM);
                    }
                    treeList.add(temp);
                }
            }
        }
        return  treeList;
    }

    @Override
    public void save(SysOrganization entity) {
        super.save(entity);
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        super.save(entity);
    }

    @Override
    public void saveBatch(List<SysOrganization> entities) {
        super.saveBatch(entities);
        for (SysOrganization entity : entities) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        }
        super.saveBatch(entities);
    }

    @Override
    public Integer update(SysOrganization entity) {
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        return super.update(entity);
    }

    @Override
    public Integer updateBatch(SysOrganization entity, List<Long> ids) {
        entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        return super.updateBatch(entity, ids);
    }

    @Override
    public void updateBatch(List<SysOrganization> entities) {
        for (SysOrganization entity : entities) {
            entity.setParentIds(getParentOrgIdsNotEmpty(entity.getId()));
        }
        super.updateBatch(entities);
    }

    protected String getParentOrgIdsNotEmpty(Long id){
        String res = baseDao.getParentOrgIds(id);
        if (StringUtils.isEmpty(res)){
            return  null;
        }
        return  res;
    }

    @Override
    public List<SysOrganization> findByUserId(Long userId) {
        return baseDao.findByUserId(userId);
    }

    @Override
    public Boolean deleteSelfAndChildById(Long id) {
        List<String> ids = baseDao.findDeleteId(id);
        if (CollectionUtil.isEmpty(ids)){
            throw new BusinessException(ErrorCodesEnum.NONE_QUERY_DATA.code(),ErrorCodesEnum.NONE_QUERY_DATA.msg());
        }
        Integer count = sysUserService.countUserByIds(ids);
        if (null == count){
            throw new BusinessException(ErrorCodesEnum.NONE_QUERY_DATA.code(),ErrorCodesEnum.NONE_QUERY_DATA.msg());
        }
        if (count > 0){
            return false;
        }
        baseDao.deleteSelfAndChildById(id);
        return true;
    }

    @Override
    public Integer findChildOrderNextNum(Long parentId) {
        Integer res = baseDao.findChildOrderNextNum(parentId);
        return (null == res)?0:res;
    }

    @Override
    public Boolean deleteOrg(Object id) {
        if (CollectionUtil.isNotEmpty(sysUserService.findUserByOrgID(id))) {
            throw new BusinessException(ErrorCodesEnum.ASSOCIATED_DATA_CANNOT_BE_DELETED.code(),"该机构关联了用户不能删除!");
        }
        if (!this.deleteSelfAndChildById(Parse.toLong(id))) {
            throw new BusinessException(ErrorCodesEnum.ASSOCIATED_DATA_CANNOT_BE_DELETED.code(),"该机构下级机构关联了用户不能删除!");
        }
        return true;
    }
}