package com.jkx.geteway.route;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Taoxs on 2017/12/21.
 */
public class RouteYamlFactoryBean extends YamlMapFactoryBean {

    private static <T> T getGeneric(final Map map, final Object key, T defaultValue) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return (T) answer;
            }
        }
        return defaultValue;
    }

    private static <T> T getGeneric(final Map map, final Object key) {

        return getGeneric(map, key, null);
    }

    public List<ZuulProperties.ZuulRoute> getRoutes() {
        Map<String, Object> lst = getObject();
        if (lst != null && !lst.isEmpty()) {
            Map<String, ZuulProperties.ZuulRoute> maps = Maps.transformValues(lst, new Function<Object, ZuulProperties
                    .ZuulRoute>() {
                @Override
                public ZuulProperties.ZuulRoute apply(Object input) {
                    if (Map.class.isAssignableFrom(input.getClass())) {
                        Map<String, Object> map = (Map<String, Object>) input;
                        ZuulProperties.ZuulRoute route = new ZuulProperties.ZuulRoute();
                        if (null != MapUtils.getString(map, "path")) {
                            route.setPath(MapUtils.getString(map, "path"));
                        }
                        if (null != MapUtils.getString(map, "url")) {
                            route.setUrl(MapUtils.getString(map, "url"));
                        }
                        if (null != MapUtils.getString(map, "serviceId")) {
                            route.setServiceId(MapUtils.getString(map, "serviceId"));
                        }
                        if (null != MapUtils.getString(map, "stripPrefix")) {
                            route.setStripPrefix(MapUtils.getBooleanValue(map, "stripPrefix"));
                        }
                        if (null != MapUtils.getString(map, "retryable")) {
                            route.setRetryable(MapUtils.getBoolean(map, "retryable"));
                        }
                        Object obj = null;
                        if ((obj = MapUtils.getObject(map, "sensitiveHeaders")) != null) {
                            if (Iterable.class.isAssignableFrom(obj.getClass())) {
                                Collection<String> head = getGeneric(map, "sensitiveHeaders");
                                if (head != null && !head.isEmpty()) {
                                    route.setSensitiveHeaders(Sets.newHashSet(head));
                                }
                            } else if (String.class.isAssignableFrom(obj.getClass())) {
                                String[] strings = Objects.toString(obj).split(",");
                                route.setSensitiveHeaders(Sets.newHashSet(strings));
                            }
                        }
                        return route;
                    }
                    return null;
                }
            });
            if (maps != null) {
                List<ZuulProperties.ZuulRoute> _hold = Lists.newArrayList();
                for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : maps.entrySet()) {
                    ZuulProperties.ZuulRoute route = entry.getValue();
                    if (route == null) {
                        continue;
                    }
                    route.setId(entry.getKey());
                    _hold.add(route);
                }
                //加速GC
                maps = null;
                return _hold;
            }
        }
        return null;
    }
}
