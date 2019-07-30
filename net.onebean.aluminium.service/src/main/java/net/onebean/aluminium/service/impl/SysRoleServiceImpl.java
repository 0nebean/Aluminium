package net.onebean.aluminium.service.impl;

import net.onebean.aluminium.common.error.ErrorCodesEnum;
import net.onebean.aluminium.dao.SysRoleDao;
import net.onebean.aluminium.model.SysRole;
import net.onebean.aluminium.model.SysRoleUser;
import net.onebean.aluminium.service.SysPermissionRoleService;
import net.onebean.aluminium.service.SysRoleService;
import net.onebean.aluminium.service.SysRoleUserService;
import net.onebean.core.base.BaseBiz;
import net.onebean.core.error.BusinessException;
import net.onebean.core.extend.Sort;
import net.onebean.core.form.Parse;
import net.onebean.core.query.ConditionMap;
import net.onebean.core.query.ListPageQuery;
import net.onebean.core.query.Pagination;
import net.onebean.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseBiz<SysRole, SysRoleDao> implements SysRoleService {


    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysPermissionRoleService sysPermissionRoleService;


    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return baseDao.findRolesByUserId(userId);
    }

    @Override
    public Boolean deleteRole(Object id) {
        if (CollectionUtil.isNotEmpty(sysRoleUserService.findbyRoleId(id))) {
            throw new BusinessException(ErrorCodesEnum.ASSOCIATED_DATA_CANNOT_BE_DELETED.code(),"该角色关联了用户，不能删除!");
        } else {
            this.deleteById(id);
            long roleId = Parse.toLong(id);
            sysRoleUserService.deleteByRoleId(roleId);
            sysPermissionRoleService.deteleByRoleId(roleId);
        }
        return true;
    }

    @Override
    public List<SysRole> findByName(String name) {
        ConditionMap map = new ConditionMap();
        map.parseModelCondition(MessageFormat.format("chName@string@like${0}^isLock@string@eq${1}", name, 0));
        return this.find(null,map);
    }

    @Override
    public Boolean addRoleUser(Long userId, String roleIds) {

        String[] roleIdsArry = roleIds.split(",");
        for (String s : roleIdsArry) {
            SysRoleUser temp = new SysRoleUser(userId, Parse.toLong(s));
            sysRoleUserService.save(temp);
        }
        return true;
    }

    @Override
    public Boolean removeRoleUser(String urIds) {
        String[] urIdsArry = urIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String s : urIdsArry) {
            ids.add(Parse.toLong(s));
        }
        sysRoleUserService.deleteByIds(ids);
        return true;
    }
}