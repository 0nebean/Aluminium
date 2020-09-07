package net.onebean.aluminium.service;

import net.onebean.core.base.IBaseBiz;
import net.onebean.aluminium.model.SysUser;

import java.util.List;

public interface SysUserService extends IBaseBiz<SysUser> {
    /**
     * 根据用户名查询出用户及其拥有的所有的角色
     * @param username 用户名
     * @return List<SysUser>
     */
    SysUser findByUsername(String username);
    /**
     * 根据机构ID查找用户
     * @param ordId 机构ID
     * @return List<SysUser>
     */
    List<SysUser> findUserByOrgID(Object ordId);
    /**
     * 查询ids影响多少条数据
     * @param orgIds ids
     * @return int
     */
    Integer countUserByIds(List<String> orgIds);
    /**
     * 删除用户
     * @param id 主键
     * @return bool
     */
    Boolean deleteUser(Object id);
    /**
     * 重设用户密码
     * @param currentUser 登录用户
     * @param password 密码
     * @return bool
     */
    Boolean resetPassword(SysUser currentUser, String password);
}