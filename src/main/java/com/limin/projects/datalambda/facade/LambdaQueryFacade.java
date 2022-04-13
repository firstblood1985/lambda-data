package com.limin.projects.datalambda.facade;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.service.QueryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * usage of this class: QueryService
 * created by limin @ 2022/4/7
 */
@Component
public class LambdaQueryFacade {
   private final SupportedDBType dbType;

    @Autowired
    public LambdaQueryFacade(LambdaRawConfig lambdaRawConfig) {
        this.dbType = SupportedDBType.of(lambdaRawConfig.getDbType(),lambdaRawConfig.getDbInstanceType());
    }

    public void query(LambdaQueryParams params) {
        if(CollectionUtils.isEmpty(params.getDimInstances()))
            throw new IllegalArgumentException("Dim Objects are empty");

        if(CollectionUtils.isEmpty(params.getIndicatorInstances()))
            throw new IllegalArgumentException("Indicator Objects are empty");

        //call query service to do actual query
        QueryService.getQueryService(dbType).query(params);
    }



}
