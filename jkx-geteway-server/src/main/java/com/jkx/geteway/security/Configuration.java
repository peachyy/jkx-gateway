package com.jkx.geteway.security;

import org.springframework.context.annotation.Bean;

/**
 * Created by Taoxs on 2017/12/22.
 */
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public WhiteFilter whiteFilter() {

        return new WhiteFilter();
    }
}
