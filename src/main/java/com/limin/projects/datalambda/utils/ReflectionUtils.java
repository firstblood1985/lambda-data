package com.limin.projects.datalambda.utils;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * usage of this class: ReflectionUtils
 * created by limin @ 2022/4/6
 */
@Slf4j
public class ReflectionUtils {



    public static List<Class> scanPackageWithAnnotationAndFilterAbstract(String packagePath, Class<? extends Annotation> annotation)
    {
        log.debug("scanning {} for {}",packagePath,annotation.toString());
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotation);

        return classes.stream().
                filter(aClass -> !Modifier.isAbstract(aClass.getModifiers())).
                collect(Collectors.toList());
    }

    public static List<Field> scanClassWithAnnotation(Class clazz,Class<? extends Annotation> annotation)
    {
        log.debug("scanning {} for {}",clazz,annotation);
        return Arrays.stream(clazz.getDeclaredFields()).
                filter(field -> null!=field.getAnnotation(annotation)).collect(Collectors.toList());
    }

}
