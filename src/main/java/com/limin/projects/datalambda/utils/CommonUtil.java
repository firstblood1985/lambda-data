package com.limin.projects.datalambda.utils;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

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

    public static <T> T colesce(T object1, T object2)
    {
        Gson gson = new Gson();
        T r = (T) gson.fromJson(gson.toJson(object1),object1.getClass());
        Field[] fields = r.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                Object f1 = f.get(object1);
                Object f2 = f.get(object2);
                f.set(r,f1 != null ? f1:f2);
            }
        }catch (IllegalAccessException e)
        {
            //TODO
        }
        return r;
    }

}
