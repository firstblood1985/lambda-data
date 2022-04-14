package com.limin.projects.datalambda.annotations;

import com.limin.projects.datalambda.dim.DimCodeType;
import com.limin.projects.datalambda.dim.LambdaComparator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DimCode {
    String mapping() default "";

    DimCodeType dimType() default DimCodeType.SINGLE;

    LambdaComparator comparator() default LambdaComparator.STRINGCOMPARATOR;
}
