package com.xjj.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils {
	//private final static Logger logger = LoggerFactory.getLogger(WebUtils.class);

	/**
	 * 获得请求的IP地址
	 * 
	 * @param request
	 * @return IP地址
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (ip!=null && ip.length()!=0) {
			//如果有多次代理（如"1.1.1.1, 2.2.2.2, 3.3.3.3"），只取第一个：
			if(ip!=null && ip.contains(",")){
				ip=ip.substring(0, ip.indexOf(","));
			}
		}
		if (ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
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
		return ip; 
	}
	
	/**
	 * 获取HttpRequest中的所有Headers
	 * @author XuJijun
	 * @param HttpServletRequest
	 * @return
	 */
	public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}
	
	/**
	 * 设置cookie到response中
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期， 以分钟为单位
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
	    Cookie cookie = new Cookie(name, value);
	    cookie.setPath("/");
	    if(maxAge>0){
	    	cookie.setMaxAge(maxAge*60);
	    }
	    response.addCookie(cookie);
	}
	
	/**
	 * 根据名字从request中获取cookie的值
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static String getCookieByName(HttpServletRequest request, String name){
		Cookie[] cookies = request.getCookies();//获取cookie数组
		if(cookies != null){
			for(Cookie cookie : cookies){
			    if(cookie.getName().equals(name)){
			    	return cookie.getValue();
			    }
			}
		}
		return null;
	}
}
