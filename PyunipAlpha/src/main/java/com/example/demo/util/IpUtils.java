package com.example.demo.util;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;


@Component
@Service
public class IpUtils {
	  public static final String ip_255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	  public static final Pattern pattern = Pattern.compile("^(?:" + ip_255 + "\\.){3}" + ip_255 + "$");

	  public static String longToIpV4(long longIp) {
	    int octet0 = (int) ((longIp >> 24) % 256);
	    int octet1 = (int) ((longIp >> 16) % 256);
	    int octet2 = (int) ((longIp >> 8) % 256);
	    int octet3 = (int) ((longIp) % 256);

	    return octet0 + "." + octet1 + "." + octet2 + "." + octet3;
	  }

	  public static long ipV4ToLong(String ip) {
	    String[] octets = ip.split("\\.");
	    return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) +
	        (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
	  }

	  public static boolean isIPv4Private(String ip) {
	    long longIp = ipV4ToLong(ip);
	    return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255")) ||
	        (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255")) ||
	        longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
	  }

	  public static boolean isIPv4Valid(String ip) {
	    return pattern.matcher(ip).matches();
	  }

	  public static String getIpFromRequest(HttpServletRequest request) {
	    String ip;
	    boolean found = false;
	    if ((ip = request.getHeader("X-FORWARDED-FOR")) != null || (ip = request.getHeader("x-forwarded-for")) != null) {
	      StringTokenizer tokenizer = new StringTokenizer(ip, ",");
	      while (tokenizer.hasMoreTokens()) {
	        ip = tokenizer.nextToken().trim();
	        if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
	          found = true;
	          break;
	        }
	      }
	    }

	    if (!found) {
	      ip = request.getRemoteAddr();
	    }

	    return ip;
	  }	
	  
	 public static String getRemoteIp(HttpServletRequest req) {
		String remoteIp = IpUtils.getIpFromRequest(req);
		return remoteIp;
	}	  
}
