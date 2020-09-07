package net.onebean.aluminium.dao;
import net.onebean.aluminium.model.SysUser;
import org.apache.ibatis.annotations.Param;

import net.onebean.core.base.BaseDao;

import java.util.List;

public interface SysUserDao extends BaseDao<SysUser> {
    /**
     *根据用户名查询出用户及其拥有的所有的角色
     * @param username 用户名
     * @return SysUser
     */
    SysUser findByUsername(@Param("username")String username);
    /**
     * 查询ids影响多少条数据
     * @param orgIds ids
     * @return int
     */
    Integer countUserByIds(@Param("orgIds") List<String> orgIds);
}