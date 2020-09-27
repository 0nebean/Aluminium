package net.onebean.aluminium.service;

import net.onebean.aluminium.model.SysAdminAccessIpRecord;
import net.onebean.core.base.IBaseBiz;


/**
* @author 0neBean
* @description 后台访问记录流水 service
* @date 2020-07-12 01:38:43
*/
public interface SysAdminAccessIpRecordService extends IBaseBiz<SysAdminAccessIpRecord> {
    /**
     * 记录访问后台记录
     * @param loginStatus 登录状态0:成功,1:账号密码错误
     * @param loginIp 登录ip
     * @param userName 用户名
     */
    void addAdminAccessIpRecord(String loginStatus, String loginIp, String userName);
}