package com.limin.projects.datalambda.reflection;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import com.limin.projects.datalambda.dim.AbstractDimCode;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * usage of this class: ReflectionTest
 * created by limin @ 2022/4/3
 */
public class ReflectionTest {

    @Test
    public void testScanAnnotation()
    {
        String packagePath = "com.limin.projects.datalambda.example";

        Reflections reflections = new Reflections(packagePath);

        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(DimEntity.class);

        List<Class> dimEntityClasses = classes.stream().filter(aClass -> !Modifier.isAbstract(aClass.getModifiers())).collect(Collectors.toList());


        for(Class dimEntity: dimEntityClasses)
        {
            List<Field> fields = Arrays.stream(dimEntity.getDeclaredFields()).
                    filter(field -> null!=field.getAnnotation(DimCode.class)).collect(Collectors.toList());
            Assert.assertNotNull(fields);
        }

    }

}
