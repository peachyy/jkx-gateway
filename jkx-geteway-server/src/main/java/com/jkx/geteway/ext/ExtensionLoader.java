package com.jkx.geteway.ext;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Taoxs on 2017/12/20.
 */
public final class ExtensionLoader {

    private static Map<Class<?>, Object> extensionMap = new ConcurrentHashMap<Class<?>, Object>();

    private static Map<Class<?>, List<?>> extensionListMap = new ConcurrentHashMap<Class<?>, List<?>>();

    private ExtensionLoader() {
    }

    public static <T> T getExtension(Class<T> clazz) {
        T extension = (T) extensionMap.get(clazz);
        if (extension == null) {
            extension = newExtension(clazz);
            if (extension != null) {
                extensionMap.put(clazz, extension);
            }
        }
        return extension;
    }

    public static <T> List<T> getExtensionList(Class<T> clazz) {
        List<T> extensions = (List<T>) extensionListMap.get(clazz);
        if (extensions == null) {
            extensions = newExtensionList(clazz);
            if (!extensions.isEmpty()) {
                extensionListMap.put(clazz, extensions);
            }
        }
        return extensions;
    }

    public static <T> T newExtension(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        for (T service : serviceLoader) {
            return service;
        }
        return null;
    }

    public static <T> List<T> newExtensionList(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        List<T> extensions = new ArrayList<T>();
        for (T service : serviceLoader) {
            extensions.add(service);
        }
        return extensions;
    }

    /**
     * @param namespaceFun   命名空间函数
     * @param namespaceValue 指定命名空间的值
     */
    public static <T> T getExtension(Class<T> clazz, String namespaceFun, String namespaceValue) {
        List<T> lst = ExtensionLoader.getExtensionList(clazz);
        if (lst == null || lst.size() == 0) {
            throw new RuntimeException(String.format("请配置 %s 厂商实现", clazz.getName()));
        }
        if (lst != null && lst.size() > 0) {
            //String str = ExtensionLoader.getExtension(ConfigProvider.class)
            //        .getString(ROUTE_REGISTRY_CONFIG_KEY, "localFile");
            for (T ext : lst) {
                Method method = ReflectionUtils.findMethod(clazz, namespaceFun);
                Assert.notNull(method, String.format("厂商接口 %s 没有找到命名空间函数 %s", clazz.getName(), namespaceFun));
                String namespaceValue_ = Objects.toString(ReflectionUtils.invokeMethod(method, ext));
                if (namespaceValue_.equalsIgnoreCase(namespaceValue)) {

                    return ext;
                }
            }
        }
        return null;
    }
}