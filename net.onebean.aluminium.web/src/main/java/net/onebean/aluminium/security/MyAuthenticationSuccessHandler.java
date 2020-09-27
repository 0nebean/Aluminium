package net.onebean.aluminium.security;

import net.onebean.aluminium.enumModel.LoginStatusEnum;
import net.onebean.aluminium.service.SysAdminAccessIpRecordService;
import net.onebean.util.StringUtils;
import net.onebean.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SysAdminAccessIpRecordService accessIpRecordService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //记录登录成功记录
        String ipAddress = WebUtils.getIpAddress();
        String username = Optional.ofNullable(httpServletRequest).map(ServletRequest::getParameterMap).map(m -> Arrays.toString(m.get("username"))).orElse("");
        if(StringUtils.isNotEmpty(username)){
            username = StringUtils.cleanArrayStr(username);
            accessIpRecordService.addAdminAccessIpRecord(LoginStatusEnum.SUCCESS.getValue(),ipAddress,username);
        }
        // 会帮我们跳转到上一次请求的页面上
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
