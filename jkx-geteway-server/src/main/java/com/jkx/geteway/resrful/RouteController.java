package com.jkx.geteway.resrful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Taoxs on 2017/12/21.
 */
@RestController
@RequestMapping("/route")
@Order()
public class RouteController {

    @Autowired
    private ZuulHandlerMapping zuulDynamicMapping;

    @RequestMapping("/doRefresh")
    public String doRefresh(HttpServletRequest request, HttpServletResponse response) {
        zuulDynamicMapping.setDirty(true);
        return "OK!";
    }
}
