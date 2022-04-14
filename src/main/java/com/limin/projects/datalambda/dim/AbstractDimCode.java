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
    protected final DimCodeType dimCodeType;
    protected final LambdaComparator lambdaComparator;


    protected AbstractDimCode(String mapping, DimCodeType dimCodeType, LambdaComparator lambdaComparator) {
        this.mapping = mapping;
        this.dimCodeType = dimCodeType;
        this.lambdaComparator = lambdaComparator;
    }

    @FunctionalInterface
    public static interface DimCodeBuilder {
        AbstractDimCode apply(String code, DimCodeType dimCodeType, LambdaComparator lambdaComparator);

        static DimCodeBuilder rdbDimCodeBuilder(){
            return (mapping,dimCodeType,lambdaComparator) -> {
                return new RDBDimCode(mapping,dimCodeType,lambdaComparator) ;
            };
        };
    }

}
