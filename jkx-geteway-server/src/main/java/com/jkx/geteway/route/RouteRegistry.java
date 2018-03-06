package com.jkx.geteway.route;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;

/**
 * Created by Taoxs on 2017/12/21.
 */
public interface RouteRegistry {

    void init();

    String name();


    /**
     * 直接强依赖zuul算啦
     */
    List<ZuulProperties.ZuulRoute> getRoutes();
}
