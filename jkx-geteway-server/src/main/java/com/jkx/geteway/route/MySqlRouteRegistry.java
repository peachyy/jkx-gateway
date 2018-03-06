package com.jkx.geteway.route;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;

import javax.sql.DataSource;

/**
 * Created by Taoxs on 2017/12/26.
 */
public class MySqlRouteRegistry implements RouteRegistry {

    public static final String NAME="mysql";
    private DataSource dataSource;
    @Override
    public void init() {

    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public List<ZuulProperties.ZuulRoute> getRoutes() {
        throw new RuntimeException("暂未实现");
    }
}
