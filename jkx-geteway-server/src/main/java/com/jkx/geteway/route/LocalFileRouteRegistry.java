package com.jkx.geteway.route;

import com.google.common.collect.Lists;

import com.jkx.geteway.common.ConfigManager;
import com.jkx.geteway.common.GeteWayConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by Taoxs on 2017/12/21.
 */
public class LocalFileRouteRegistry implements RouteRegistry {

    public static final  Logger                         LOGGER                  = LoggerFactory.getLogger
            (LocalFileRouteRegistry.class);
    public static final  String                         FILE_REGISTRY_CACHE_KEY = GeteWayConstants
            .CONFIG_ROUTE_LOCALFILE_KEY;
    public static final  String                         NAME                    = "localFile";
    private static final long                           MI1                     = 1000 * 60;
    private static       List<ZuulProperties.ZuulRoute> HOLD                    = Lists.newArrayList();
    private              long                           lastTime                = 0;

    public void init() {
        fromLocalFile();
    }

    private void fromLocalFile() {
        //至少一分钟同步一次
        // if(System.currentTimeMillis()-lastTime>=MI1){
        String file = ConfigManager.getInstance().getString(FILE_REGISTRY_CACHE_KEY);
        if (StringUtils.isEmpty(file)) {
            LOGGER.warn("当前使用本地文件路由注册中心 而缺失{}配置 请检查", FILE_REGISTRY_CACHE_KEY);
            return;
        }
        LOGGER.info("读取动态路由配置路径 {} ", file);
        RouteYamlFactoryBean routeYamlFactoryBean = new RouteYamlFactoryBean();
        Resource resource = new DefaultResourceLoader(Thread.currentThread().getContextClassLoader()).getResource(file);
        routeYamlFactoryBean.setResources(resource);
        List<ZuulProperties.ZuulRoute> lst = routeYamlFactoryBean.getRoutes();
        LOGGER.debug("读取到路由信息 ：{}", Objects.toString(lst, ""));
        if (lst != null) {
            HOLD = lst;
        }
        lastTime = System.currentTimeMillis();
        // }
    }


    public String name() {
        return NAME;
    }


    public List<ZuulProperties.ZuulRoute> getRoutes() {
        fromLocalFile();
//        List<ZuulProperties.ZuulRoute> lsts= Lists.newArrayList();
//        ZuulProperties.ZuulRoute zuulRoute=new ZuulProperties.ZuulRoute();
//        zuulRoute.setPath("/api/**");
//        zuulRoute.setUrl("http://www.sina.com");
//        zuulRoute.setId("xxxx");
//        lsts.add(zuulRoute);
        return HOLD;
    }

//    public static void main(String[] args) {
//
//        RouteYamlFactoryBean routeYamlFactoryBean=new RouteYamlFactoryBean();
//        Resource resource=new DefaultResourceLoader(Thread.currentThread()
//                .getContextClassLoader()).getResource("file:c:/route.yml");
//        routeYamlFactoryBean.setResources(resource);
//        List<ZuulProperties.ZuulRoute> lst=routeYamlFactoryBean.getRoutes();
//        System.out.println(lst);
//    }
//    @SuppressWarnings("Since15")
//    public static void main(String[] args) throws IOException, InterruptedException {
//        WatchService watchService =FileSystems.getDefault().newWatchService();
//        final Path path = Paths.get( "c:changed.txt");
//        final WatchKey watchKey = path.register( watchService, StandardWatchEventKinds.ENTRY_MODIFY ,
//                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE );
//        boolean fileNotChanged = true;
//        int count = 0;
//        while (fileNotChanged ) {
//            final WatchKey wk = watchService.take();
//            System. out.println("Loop count: " + count );
//            for (WatchEvent<?> event : wk .pollEvents()) {
//                final Path changed = (Path) event.context();
//                System. out.println(changed + ", " + event .kind());
//                if (changed .endsWith("sample1.txt")) {
//                    System. out.println("Sample file has changed" );
//                }
//            }
//            // reset the key
//            boolean valid = wk .reset();
//            if (!valid ) {
//                System. out.println("Key has been unregisterede" );
//            }
//            count++;
//        }
//    }
}
