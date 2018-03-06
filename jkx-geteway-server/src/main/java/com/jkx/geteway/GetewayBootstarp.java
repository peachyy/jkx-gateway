package com.jkx.geteway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by Taoxs on 2017/12/21.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaServer
@EnableDiscoveryClient
public class GetewayBootstarp {

    public static void main(String[] args) {
        SpringApplication.run(GetewayBootstarp.class, args);
    }
}
