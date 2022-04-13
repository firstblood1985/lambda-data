package com.limin.projects.datalambda.utils;

import java.lang.annotation.Annotation;

/**
 * usage of this class: CommonUtil
 * created by limin @ 2022/4/7
 */
public class CommonUtil {
    public static final String DOT_DELIMITER = "\\.";

    public static boolean isSomeEntity(Object o, Class<? extends Annotation> clazz) {
        Annotation anno = o.getClass().getAnnotation(clazz);

        if (null == anno)
            return false;
        else
            return true;
    }

}
