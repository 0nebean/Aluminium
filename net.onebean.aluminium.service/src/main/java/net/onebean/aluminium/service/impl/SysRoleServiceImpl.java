package net.onebean.aluminium.service.impl;
import net.onebean.aluminium.service.SysRoleService;
import org.springframework.stereotype.Service;
import net.onebean.core.BaseBiz;
import net.onebean.aluminium.model.SysRole;
import net.onebean.aluminium.dao.SysRoleDao;

import java.util.List;

@Service
public class SysRoleServiceImpl extends BaseBiz<SysRole, SysRoleDao> implements SysRoleService {
    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return baseDao.findRolesByUserId(userId);
    }
}