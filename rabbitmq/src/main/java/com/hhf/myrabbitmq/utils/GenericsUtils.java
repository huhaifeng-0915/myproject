package com.hhf.myrabbitmq.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Generics的util类.
 *
 * @author _g5niusx
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class GenericsUtils {


    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @return the index generic declaration, or <status>Object.class</status> if cannot be determined
     */
    public static Class getSuperClassGenericsType(Class clazz) {
        int  index   = 0;
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
}
