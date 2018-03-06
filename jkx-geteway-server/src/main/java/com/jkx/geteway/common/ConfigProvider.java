package com.jkx.geteway.common;

/**
 * Created by Taoxs on 2017/12/22.
 */
public interface ConfigProvider {

    String name();


    String getString(String key);

    String getString(String key, String defaultValue);

    Boolean getBoolean(String key, boolean defaultValue);

    Boolean getBoolean(String key);


}
