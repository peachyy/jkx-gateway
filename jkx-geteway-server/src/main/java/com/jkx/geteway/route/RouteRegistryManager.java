package com.jkx.geteway.route;

import com.jkx.geteway.common.ConfigProvider;
import com.jkx.geteway.common.GeteWayConstants;
import com.jkx.geteway.ext.ExtensionLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Taoxs on 2017/12/21.
 */
public class RouteRegistryManager {

    public static final     Logger               LOGGER                    = LoggerFactory.getLogger(RouteRegistryManager
            .class);
    public static final     String               ROUTE_REGISTRY_CONFIG_KEY = GeteWayConstants.CONFIG_ROUTE_RESITRY_KEY;
    private static final    RouteRegistryManager registryManager           = new RouteRegistryManager();
    private static volatile boolean              init                      = false;
    private static          RouteRegistry        registryCache             = null;

    public static RouteRegistryManager getInstance() {
        if (!init) {
            synchronized (RouteRegistryManager.class) {
                if (!init) {
                    registryManager.init();
                    init = true;
                }
            }
        }
        return registryManager;
    }

    public RouteRegistry getRouteRegistry() {
        return registryCache;
    }


    private void init() {
        String str = ExtensionLoader.getExtension(ConfigProvider.class)
                .getString(ROUTE_REGISTRY_CONFIG_KEY, "localFile");
        RouteRegistry registry = ExtensionLoader.getExtension(RouteRegistry.class, GeteWayConstants
                .EXTTENSION_POINT_NAMESPANCE, str);
        if (registry != null) {
            registry.init();
            LOGGER.info("Route Registry {}/{} started.", registry.name(), registry.getClass().getName());
            registryCache = registry;
        }
//        List<RouteRegistry> lst = ExtensionLoader.getExtensionList(RouteRegistry.class);
//        if (lst == null || lst.size() == 0) {
//            LOGGER.error("SPI 没有正确获取到 {} 扩展厂商", RouteRegistry.class.getName());
//            return;
//        }
//        if (lst != null && lst.size() > 0) {
//            String str = ExtensionLoader.getExtension(ConfigProvider.class)
//                    .getString(ROUTE_REGISTRY_CONFIG_KEY, "localFile");
//            for (RouteRegistry registry : lst) {
//                if (registry.name().equalsIgnoreCase(str)) {
//                    registry.init();
//                    registryCache = registry;
//
//                    break;
//                }
//            }
//        }

    }
}
