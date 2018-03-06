package com.jkx.geteway.route;

import com.netflix.loadbalancer.DynamicServerListLoadBalancer;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Taoxs on 2017/12/21.
 */
@Configuration
public class Configuation {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public DynamicRouteLocator dynamicRouteLocator(ServerProperties serverProperties,
                                                   ZuulProperties zuulProperties, DiscoveryClient discovery) {
        return new DynamicRouteLocator(serverProperties.getServletPath(), discovery, zuulProperties);
    }

    class cc extends DynamicServerListLoadBalancer {

    }
}
