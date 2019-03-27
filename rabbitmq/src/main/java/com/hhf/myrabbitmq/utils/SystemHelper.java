package com.hhf.myrabbitmq.utils;

import com.hhf.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 获取ip地址
 * @author huhaifeng
 */
public class SystemHelper {

    private static final Logger logger = LoggerFactory.getLogger(SystemHelper.class);

    private SystemHelper() {
    }

    public static String getSystemLocalIpStr() {
        String hostip = System.getProperty("container.hostip");
        if (StringUtils.isEmpty(hostip)) {
            InetAddress inetAddress = getSystemLocalIp();
            if (inetAddress != null) {
                return inetAddress.getHostAddress();
            } else {
                throw new BusinessException("获取不到当前服务器ip地址.");
            }
        } else {
            return hostip;
        }
    }

    public static InetAddress getSystemLocalIp() {
        InetAddress inet = null;

        try {
            if (isWindows()) {
                inet = getWinLocalIp();
            } else {
                inet = getUnixLocalIp();
            }

            return null == inet ? null : inet;
        } catch (SocketException var2) {
            logger.error("获取本机ip错误" + var2.getMessage(), var2);
            return null;
        }
    }

    public static boolean isWindows() {
        String os = getSystemOSName().toLowerCase();
        return os.indexOf("win") >= 0;
    }

    public static boolean isMac() {
        String os = getSystemOSName().toLowerCase();
        return os.indexOf("mac") >= 0;
    }

    public static boolean isUnix() {
        String os = getSystemOSName().toLowerCase();
        return os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
    }

    public static String getSystemOSName() {
        Properties props = System.getProperties();
        String osname = props.getProperty("os.name");
        if (logger.isDebugEnabled()) {
            logger.debug("the ftp client system os Name " + osname);
        }

        return osname;
    }

    private static InetAddress getWinLocalIp() {
        InetAddress inet = null;

        try {
            inet = InetAddress.getLocalHost();
            logger.debug("本机的ip=" + inet.getHostAddress());
        } catch (UnknownHostException var2) {
            logger.error("获取windows系统的本机ip错误" + var2.getMessage(), var2);
        }

        return inet;
    }

    private static InetAddress getUnixLocalIp() throws SocketException {
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;

        while(netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
            Enumeration addresses = ni.getInetAddresses();

            while(addresses.hasMoreElements()) {
                ip = (InetAddress)addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address) {
                    logger.debug("本机的IP = " + ip.getHostAddress());
                    return ip;
                }
            }
        }

        return null;
    }

    public static List<String> getLocalIpList() {
        ArrayList ipList = new ArrayList();

        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)networkInterfaces.nextElement();
                Enumeration inetAddresses = networkInterface.getInetAddresses();

                while(inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException var6) {
            logger.error("获取本机ip列表失败：" + var6.getMessage(), var6);
        }

        return ipList;
    }
}
