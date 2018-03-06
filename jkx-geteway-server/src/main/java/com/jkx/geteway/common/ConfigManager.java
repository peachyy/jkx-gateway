package com.jkx.geteway.common;

import com.jkx.geteway.ext.ExtensionLoader;

/**
 * Created by Taoxs on 2017/12/22.
 */
public class ConfigManager implements ConfigProvider {

    private static ConfigProvider configProvider;

    private static ConfigManager configManager = new ConfigManager();

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (configProvider == null) {
            synchronized (ConfigManager.class) {
                String configProviderName = ExtensionLoader.getExtension(ConfigProvider.class)
                        .getString(GeteWayConstants.CONFIG_CONFIG_PROVIDER_KEY, "spring-config");
                configProvider = ExtensionLoader.getExtension(ConfigProvider.class, GeteWayConstants
                        .EXTTENSION_POINT_NAMESPANCE, configProviderName);
            }
        }
        return configManager;
    }


    @Override
    public String name() {
        throw new RuntimeException("不是具体实现 只是调用者门面 不能调用");
    }

    @Override
    public String getString(String key) {
        return configProvider.getString(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return configProvider.getString(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key, boolean defaultValue) {
        return configProvider.getBoolean(key, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return configManager.getBoolean(key);
    }
}
