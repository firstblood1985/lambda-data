package com.limin.projects.datalambda.dim;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * usage of this class: RDBDimService
 * created by limin @ 2022/4/6
 */
//@Component("RDBDimService")
public class RDBDimService extends DimService<RDBDimEntity,RDBDimCode>{

    @Autowired
    protected RDBDimService(LambdaRawConfig lambdaRawConfig) {
        super(lambdaRawConfig);
        scanPackageAndBuildDimEntities();
    }
}
