package net.onebean.aluminium.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * spring net.onebean.aluminium.security
 * web安全配置器,boot版本基本配置
 * @author 0neBean
 */
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyFilterSecurityInterceptor filterSecurityInterceptor;
    @Autowired
    UserDetailsService customUserService;
    @Autowired
    MyPermissionEvaluator permissionEvaluator;
    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //user Details Service验证
        //指定spring 提供密码加密类 采用SHA-256(采用随机盐+秘钥+密码)加密方式  加密后密码长度80位 随机秘钥随每次启动程序生成
        auth.userDetailsService(customUserService).passwordEncoder(myPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        web.expressionHandler(handler);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        myAuthenticationFailureHandler.setDefaultFailureUrl("/error/401");

        http.authorizeRequests()
                .antMatchers(AccessWhiteList.unSecuredUrls).permitAll()
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .permitAll() //登录页面用户任意访问
                .and()
                .headers().frameOptions().sameOrigin()//允许iframe嵌套本应用页面
                .and().rememberMe().and()
                .logout().permitAll(); //注销行为任意访问
        http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class).csrf().disable();
    }

}

