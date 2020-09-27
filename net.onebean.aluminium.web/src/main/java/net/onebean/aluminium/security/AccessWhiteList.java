package net.onebean.aluminium.security;

/**
 *  访问白名单
 * 0neBean
 */
public class AccessWhiteList {

    public static final String[] unSecuredUrls = { "/system_assets/**", "/assets/**","/druid/**","/error/**","/*.ico","/health","/info"};

}
