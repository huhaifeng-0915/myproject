package com.hhf.quartz.dynamicquery;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  *创建者 胡海丰
 * 创建时间	2019年3月8日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeQueryResultEntity {
	
}
