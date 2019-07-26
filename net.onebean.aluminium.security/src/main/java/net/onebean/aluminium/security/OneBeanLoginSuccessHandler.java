package net.onebean.aluminium.security;

import net.onebean.aluminium.model.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录成功信息初始化
 * @author 0neBean
 */
@Service
public class OneBeanLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(OneBeanLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LOGGER.info("login success,trying put current login user info into session");
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("current_sys_user", SpringSecurityUtil.getCurrentLoginUser());
        LOGGER.info("initialize login user info done");
        if (httpServletRequest.getRequestURI().equals("/login")){
            httpServletResponse.sendRedirect("/");
        }
    }
}
