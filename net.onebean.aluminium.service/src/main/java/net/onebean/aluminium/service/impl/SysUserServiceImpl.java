package net.onebean.aluminium.service.impl;

import net.onebean.core.BaseBiz;
import net.onebean.core.Condition;
import net.onebean.aluminium.dao.SysUserDao;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseBiz<SysUser, SysUserDao> implements SysUserService{

	@Override
	public SysUser findByUsername(String username) {
		return baseDao.findByUsername(username);
	}

	@Override
	public List<SysUser> findUserByOrgID(Object ordId) {
		Condition condition =  Condition.parseModelCondition("orgId@int@eq$");
		condition.setValue(ordId);
		return this.find(null,condition);
	}

	@Override
	public Integer countUserByIds(List<String> orgIds) {
		return baseDao.countUserByIds(orgIds);
	}
}