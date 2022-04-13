package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import org.springframework.stereotype.Component;

/**
 * usage of this class: RDBIndicatorService
 * created by limin @ 2022/4/7
 */
//@Component("RDBIndicatorService")
public class RDBIndicatorService extends IndicatorService{
    protected RDBIndicatorService(LambdaRawConfig lambdaRawConfig) {
        super(lambdaRawConfig);
        scanPackageAndBuildIndicatorEntities();
    }
}
