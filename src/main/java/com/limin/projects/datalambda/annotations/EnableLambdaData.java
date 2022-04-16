package com.limin.projects.datalambda.annotations;

import com.limin.projects.datalambda.config.LambdaInit;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * usage of this interface: EnableLambdaData
 * created by limin @ 2022/4/16
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(LambdaInit.class)
public @interface EnableLambdaData {
}
