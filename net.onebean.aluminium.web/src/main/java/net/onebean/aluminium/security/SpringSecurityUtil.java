package net.onebean.aluminium.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.onebean.component.SpringUtil;
import net.onebean.core.query.Condition;
import net.onebean.core.query.Pagination;
import net.onebean.aluminium.model.SysUser;
import net.onebean.aluminium.service.SysUserService;
import net.onebean.aluminium.service.impl.SysUserServiceImpl;
import net.onebean.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Random;

/**
 * @author 0neBean
 * spring security工具类
 */
public class SpringSecurityUtil {


    /**
     * 获取当前登录用户
     * 此方法不抛出任何异常,获取不到对应用户时返回null
     * @return
     */
    public static SysUser getCurrentLoginUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
            String username =  Optional.ofNullable(securityContext).map(SecurityContextImpl::getAuthentication).map(Authentication::getPrincipal).map(s -> (UserDetails)s).map(UserDetails::getUsername).orElse("");
            if (StringUtils.isEmpty(username)){
                return null;
            }
            Condition condition = Condition.parseModelCondition("username@string@eq");
            condition.setValue(username);
            SysUserService sysUserService = SpringUtil.getBean(SysUserServiceImpl.class);
            return sysUserService.find(new Pagination(),condition).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    public static JSONArray getCurrentPermissions(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        JSONArray array = JSONArray.parseArray(JSON.toJSONString(securityContext.getAuthentication().getAuthorities()));
        return  array;
    }


    /**
     * 生成随机数
     * @param max
     * @param min
     * @return
     */
    protected static int getRandom(int max,int min) {
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }


}
