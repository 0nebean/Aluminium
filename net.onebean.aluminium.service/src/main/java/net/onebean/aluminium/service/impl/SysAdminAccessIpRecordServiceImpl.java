package net.onebean.aluminium.service.impl;

import net.onebean.aluminium.dao.SysAdminAccessIpRecordDao;
import net.onebean.aluminium.model.SysAdminAccessIpRecord;
import net.onebean.aluminium.service.SysAdminAccessIpRecordService;
import net.onebean.core.base.BaseBiz;
import org.springframework.stereotype.Service;

/**
* @author 0neBean
* @description 后台访问记录流水 serviceImpl
* @date 2020-07-12 01:38:43
*/
@Service
public class SysAdminAccessIpRecordServiceImpl extends BaseBiz<SysAdminAccessIpRecord, SysAdminAccessIpRecordDao> implements SysAdminAccessIpRecordService{
    @Override
    public void addAdminAccessIpRecord(String loginStatus, String loginIp, String userName) {
        SysAdminAccessIpRecord r = new SysAdminAccessIpRecord(loginStatus, loginIp, userName);
        this.save(r);
    }
}