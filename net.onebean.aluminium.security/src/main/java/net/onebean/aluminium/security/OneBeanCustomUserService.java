package net.onebean.aluminium.security;

import net.onebean.core.form.Parse;
import net.onebean.aluminium.model.SysPermission;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysPermissionService;
import net.onebean.aluminium.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * spring security 根据用户名关联相应的权限实现
 * @author 0neBean
 */
@Service
public class OneBeanCustomUserService implements UserDetailsService { //自定义UserDetailsService 接口

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysPermissionService sysPermissionService;

    public UserDetails loadUserByUsername(String username) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String sss = request.getRequestURI();
        SysUser user = sysUserService.findByUsername(username);
        if (user != null) {
            List<SysPermission> permissions = sysPermissionService.springSecurityFindByAdminUserId(Parse.toInt(user.getId()));
            List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
            for (SysPermission permission : permissions) {
                if (permission != null && permission.getName()!=null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                    grantedAuthorities.add(grantedAuthority);
                }
            }
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}
