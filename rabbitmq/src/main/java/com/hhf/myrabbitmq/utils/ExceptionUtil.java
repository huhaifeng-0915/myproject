package com.hhf.myrabbitmq.utils;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class ExceptionUtil {

    public ExceptionUtil() {
    }

    public static String getErrorMsg(Throwable e) {
        if (e == null) {
            return "";
        } else {
            String errMsg = e.getMessage() == null ? "" : e.getMessage();
            if (!(e instanceof NullPointerException) && !(e instanceof ArithmeticException)) {
                if (StringUtils.isEmpty(errMsg)) {
                    errMsg = e.toString();
                }

                return errMsg;
            } else {
                String clsName = ClassUtils.getShortName(e.getClass());
                StackTraceElement[] trace = e.getStackTrace();
                return trace != null && trace.length != 0 ? clsName + ":" + errMsg + " " + trace[0].toString() : clsName + ":" + errMsg;
            }
        }
    }

    public static String getErrorMsg(String operateInfo, String businessNo, Throwable e) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(operateInfo)) {
            sb.append("操作场景[" + operateInfo + "],");
        }

        if (StringUtils.hasText(operateInfo)) {
            sb.append("业务流水号：" + businessNo + ",");
        }

        sb.append("错误信息:" + getErrorMsg(e));
        return sb.toString();
    }
}
