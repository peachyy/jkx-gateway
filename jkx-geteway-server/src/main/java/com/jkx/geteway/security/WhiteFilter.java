package com.jkx.geteway.security;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.jkx.geteway.common.ConfigManager;
import com.jkx.geteway.common.GeteWayConstants;
import com.jkx.geteway.common.util.HttpUtils;
import com.jkx.geteway.common.util.JSONUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Taoxs on 2017/12/22.
 */
public class WhiteFilter extends ZuulFilter {

    public static final Logger       LOGGER        = LoggerFactory.getLogger(WhiteFilter
            .class);
    private static      List<String> IP_WHITE_LIST = null;
    /**
     * 排除掉可以不验证IP的路径
     * 待实现
     */
    private             List<String> EXCLUDEPATHS  = Lists.newArrayList();

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -10;
    }

    @Override
    public boolean shouldFilter() {
        boolean enable = ConfigManager.getInstance().getBoolean(GeteWayConstants.CONFIG_ENABLE_WHITELIST_KEY, true);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" Geteway filter {} enable: {}", this.getClass().getName(), enable);
        }
        return enable;
    }

    @Override
    public Object run() {
        if (IP_WHITE_LIST == null) {
            synchronized (this) {
                if (IP_WHITE_LIST == null) {
                    initIPList();
                }
            }
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        String url = request.getRequestURI();

        //获取来源IP
        String sourceIP = null;
        try {
            sourceIP = HttpUtils.getIpAddress(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ua = HttpUtils.getUserAgent(request);

        //判断IP是否白名单内IP
        boolean bIp = IP_WHITE_LIST.contains(sourceIP);
        if (!bIp) {
            Map<String, String[]> paramMap = request.getParameterMap();
            if (paramMap == null) {
                paramMap = Maps.newHashMap();
            }
            LOGGER.error("请求ip地址非法，URL:{},请求参数：{} 请求IP {}  ua: {} method:{} ", url, JSONUtil.obj2Json(paramMap), sourceIP,
                    ua, request.getMethod());
            ctx.setSendZuulResponse(false);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/html;charset=utf-8");
            ctx.setResponseBody("CLIENT ADDR UNDEFINED");
            return null;
        }
        return null;
    }

    private static void initIPList() {
        IP_WHITE_LIST = Lists.newArrayList();
        BufferedReader br = null;
        try {
            String filePath = ConfigManager.getInstance().getString(GeteWayConstants.CONFIG_WHITELIST_FILE_KEY,
                    "classpath:config/ip.txt");
            LOGGER.debug("GeteWay IP whiteList file in {}", filePath);
            Resource resource = new DefaultResourceLoader(Thread.currentThread().getContextClassLoader()).getResource
                    (filePath);
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            List<String> lines = Lists.newArrayList();
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            if (null != lines && lines.size() > 0) {
                for (String ip : lines) {
                    if (StringUtils.isEmpty(ip)) {
                        continue;
                    }
                    if (StringUtils.startsWithIgnoreCase(ip, "#")) {
                        continue;
                    }
                    if (ip.indexOf("#") != -1) {
                        ip = ip.substring(0, ip.indexOf("#"));
                    }
                    ip = ip.replaceAll("\\s", "");

                    if (ip.indexOf(",") != -1) {
                        String[] ips = ip.split(",");
                        IP_WHITE_LIST.addAll(Lists.newArrayList(ips));
                        continue;
                    }
                    IP_WHITE_LIST.add(ip);
                }
            }
        } catch (IOException e) {
            LOGGER.error("读取IP白名单文件失败");
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                //SKIP
            }
        }

    }
}
