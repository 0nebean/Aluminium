package net.onebean.aluminium.service.impl;
import net.onebean.core.Condition;
import net.onebean.core.Pagination;
import net.onebean.aluminium.service.SysRoleUserService;
import net.onebean.util.CollectionUtil;
import org.springframework.stereotype.Service;
import net.onebean.core.BaseBiz;
import net.onebean.aluminium.model.SysRoleUser;
import net.onebean.aluminium.dao.SysRoleUserDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SysRoleUserServiceImpl extends BaseBiz<SysRoleUser, SysRoleUserDao> implements SysRoleUserService {

    @Override
    public void deleteByUserId(Long userId) {
        baseDao.deleteByUserId(userId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        baseDao.deleteByRoleId(roleId);
    }

    @Override
    public void save(SysRoleUser entity) {
        List<Condition> params = new ArrayList<>();
        Condition c1 = Condition.parseModelCondition("sysRoleId@int@eq$");
        c1.setValue(entity.getSysRoleId());
        Condition c2 = Condition.parseModelCondition("sysUserId@int@eq$");
        c2.setValue(entity.getSysUserId());
        params.add(c1);
        params.add(c2);
        if(CollectionUtil.isEmpty(this.find(null,params))){
            super.save(entity);
        }
    }

    @Override
    public List<SysRoleUser> findbyRoleId(Object roleId) {
        Condition param = Condition.parseModelCondition("sysRoleId@int@eq");
        param.setValue(roleId);
        return find(null,param);
    }
}