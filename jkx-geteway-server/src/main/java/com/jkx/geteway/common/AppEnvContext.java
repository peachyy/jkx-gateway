package com.jkx.geteway.common;

import org.springframework.beans.BeansException;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE - 50)
public class AppEnvContext implements EnvironmentAware, ConfigProvider, ApplicationContextAware {

    private static Environment        env;
    private static ApplicationContext ctx;

    public static final String NAME = "spring-config";

    public String getProperty(String key) {

        return env.getProperty(key);
    }

    @Override
    public String name() {
        return NAME;
    }

    public String getString(String key) {

        return env.getProperty(key);
    }

    public String getString(String key, String defaultValue) {

        String v = getProperty(key);
        return StringUtils.isEmpty(v) ? defaultValue : v;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (!StringUtils.isEmpty(val)) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    /**
     * <code>
     * getRelaxedPropertyResolver( "spring.datasource.").getProperty("url");
     * <p>
     * </code>
     */
    public static RelaxedPropertyResolver getRelaxedPropertyResolver(String prefix) {

        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, prefix);
        return propertyResolver;
    }

    public static Environment getEnv() {
        return env;
    }

    public static ApplicationContext getCtx() {
        return ctx;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        env = null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}