package com.limin.projects.datalambda.dim;

import lombok.Getter;

import java.util.function.Function;

/**
 * usage of this class: DimCode
 * created by limin @ 2022/4/6
 */
@Getter
public abstract class AbstractDimCode {
    protected final String mapping;

    protected AbstractDimCode(String mapping) {
        this.mapping = mapping;
    }

    public static interface DimCodeBuilder extends Function<String, AbstractDimCode>{

        static DimCodeBuilder rdbDimCodeBuilder(){
            return mapping -> {
                return new RDBDimCode(mapping) ;
            };
        };

    }
}
