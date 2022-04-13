package com.limin.projects.datalambda.indicator;

import java.util.function.Function;

/**
 * usage of this class: AbstractIndicatorCode
 * created by limin @ 2022/4/7
 */
public abstract class AbstractIndicator {
    protected final String mapping;

    protected AbstractIndicator(String mapping) {
        this.mapping = mapping;
    }

    public static interface IndicatorBuilder extends Function<String,AbstractIndicator> {

        static IndicatorBuilder rdbIndicatorBuilder(){
            return mapping -> {
                return new RDBIndicator(mapping);
            };
        };
    }
}
