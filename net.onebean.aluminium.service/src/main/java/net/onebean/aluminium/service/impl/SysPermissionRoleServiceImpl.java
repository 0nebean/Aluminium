package net.onebean.aluminium.service.impl;
import org.springframework.stereotype.Service;
import net.onebean.core.BaseBiz;
import net.onebean.aluminium.model.SysPermissionRole;
import net.onebean.aluminium.service.SysPermissionRoleService;
import net.onebean.aluminium.dao.SysPermissionRoleDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysPermissionRoleServiceImpl extends BaseBiz<SysPermissionRole, SysPermissionRoleDao> implements SysPermissionRoleService{
    @Override
    public List<SysPermissionRole> getRolePremissionByRoleId(Long roleId) {
        return baseDao.getRolePremissionByRoleId(roleId);
    }

    @Override
    public void deteleByRoleId(Long roleId) {
        baseDao.deteleByRoleId(roleId);
    }

    @Override
    public void deteleByPermissionId(Long permissionId) {
        baseDao.deteleByPermissionId(permissionId);
    }

    @Override
    public void insertBatch(String pids,String rid) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map;
        String[] arr = pids.split(",");
        for (String s : arr) {
            map = new HashMap<>();
            map.put("permissionId",s);
            map.put("roleId",rid);
            list.add(map);
        }
        baseDao.insertBatch(list);
    }
}