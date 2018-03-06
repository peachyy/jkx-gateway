package com.jkx.geteway.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Taoxs on 2017/12/22.
 */
@SuppressWarnings("ALL")
public class HttpUtils {

    private static final Logger LOG           = LoggerFactory.getLogger(HttpUtils.class);
    public static final  String JSONP_FUN_KEY = "callback";

    public static boolean isXMLHttpRequest(HttpServletRequest request) {
        if (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").contains("XMLHttpRequest")) {
            return true;
        }
        return false;
    }

    public static boolean isJsonpRequest(HttpServletRequest request) {
        String callback = request.getParameter(JSONP_FUN_KEY);
        if (callback != null && !callback.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static String getIpAddress(HttpServletRequest request) throws IOException {
        String ip = request.getHeader("X-Forwarded-For");
        try {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = (String) ips[index];
                    if (!("unknown".equalsIgnoreCase(strIp))) {
                        ip = strIp;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("获取客户端IP失败");
        }
        return ip;
    }

    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent").toLowerCase();
        return ua;
    }

    public static boolean isMobile(HttpServletRequest request) {
        String ua = getUserAgent(request);
        try {
            if (ua.matches("^.*(android|iphone|symbianos|windows phone|ipad|ipod|bb10|mobile).*$")) {
                return true;
            } else {
                String wxFlag = request.getHeader("MicroMessenger");
                return wxFlag == null ? true : false;
            }
        } catch (Exception e) {
            LOG.info("获取访问来源出错，用户UA为：" + ua, e);
            return false;
        }
    }
}
