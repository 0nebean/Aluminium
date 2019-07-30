package net.onebean.aluminium.service.impl;

import net.onebean.aluminium.service.SysRoleUserService;
import net.onebean.core.base.BaseBiz;
import net.onebean.core.form.Parse;
import net.onebean.core.query.Condition;
import net.onebean.aluminium.dao.SysUserDao;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseBiz<SysUser, SysUserDao> implements SysUserService{

	@Autowired
	private SysRoleUserService sysRoleUserService;

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

	@Override
	public Boolean deleteUser(Object id) {
		this.deleteById(id);
		sysRoleUserService.deleteByUserId(Parse.toLong(id));
		return true;
	}
}