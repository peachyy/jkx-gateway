package com.jkx.geteway.route;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Taoxs on 2017/12/20.
 */
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    private DiscoveryClient discovery;


    public DynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties) {
        super(servletPath, discovery, properties);
    }


    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routeMap = super.locateRoutes();
        List<ZuulProperties.ZuulRoute> lst = RouteRegistryManager.getInstance()
                .getRouteRegistry().getRoutes();
        if (lst != null) {
            for (ZuulProperties.ZuulRoute zuulRoute : lst) {
                routeMap.put(zuulRoute.getPath(), zuulRoute);
            }
        }
        return routeMap;
    }
}
