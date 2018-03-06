package com.jkx.geteway.common;

/**
 * Created by Taoxs on 2017/12/22.
 */
public class GeteWayConstants {

    public static final String BASE_PREFIX                 = "geteway";
    public static final String BASE_APPEND_REGIX           = ".";
    /**
     * 扩展点默认的命名空间函数名称
     */
    public static final String EXTTENSION_POINT_NAMESPANCE = "name";
    /**
     * 配置读取器 默认实现了 spring-config {@link com.jkx.geteway.common.AppEnvContext}
     */
    public static final String CONFIG_CONFIG_PROVIDER_KEY  = BASE_PREFIX.concat(BASE_APPEND_REGIX).concat("configProvider");

    /**
     * 路由配置注册中心
     * 默认为 localFile
     */
    public static final String CONFIG_ROUTE_RESITRY_KEY    = BASE_PREFIX.concat(BASE_APPEND_REGIX).concat("registry");
    /**
     * 本地文件路由配置文件
     */
    public static final String CONFIG_ROUTE_LOCALFILE_KEY  = BASE_PREFIX.concat(BASE_APPEND_REGIX).concat("routeFile");
    /**
     * 是否启动IP白名单过滤配置
     */
    public static final String CONFIG_ENABLE_WHITELIST_KEY = BASE_PREFIX.concat(BASE_APPEND_REGIX).concat("enableWhiteList");
    /**
     * 白名单文件路径 默认为classpath:ip.txt
     */
    public static final String CONFIG_WHITELIST_FILE_KEY   = BASE_PREFIX.concat(BASE_APPEND_REGIX).concat("whiteListFile");


}
