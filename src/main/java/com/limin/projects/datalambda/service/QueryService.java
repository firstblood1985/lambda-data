package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.config.LambdaConfigService;
import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.convert.ObjectToDimValue;
import com.limin.projects.datalambda.convert.ObjectToIndicatorEntity;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.facade.LambdaQueryResults;
import com.limin.projects.datalambda.indicator.AbstractIndicatorEntity;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * usage of this class: QueryService
 * created by limin @ 2022/4/7
 */
@Getter
public abstract class QueryService<T extends AbstractDimEntity
                                    ,P extends AbstractIndicatorEntity
                                    ,M extends LambdaConfigService
                                    ,N extends ObjectToDimValue
                                    ,O extends ObjectToIndicatorEntity>{

    protected final M lambdaConfigService;

    protected final SupportedDBType dbType;

    protected final DataSource dataSource;

    protected final N objectToDimValue;

    protected final O objectToIndicatorEntity;
    @Autowired
    private static Map<String,QueryService> queryServiceMap;

    public abstract List<LambdaQueryResults> query(LambdaQueryParams params);

    protected QueryService(LambdaRawConfig lambdaRawConfig,DataSource dataSource,
                           M lambdaConfigService,N objectToDimValue,O objectToIndicatorEntity) {
        dbType = SupportedDBType.of(lambdaRawConfig.getDbType(),lambdaRawConfig.getDbInstanceType());
        this.dataSource = dataSource;
        this.lambdaConfigService = lambdaConfigService;
        this.objectToDimValue = objectToDimValue;
        this.objectToIndicatorEntity = objectToIndicatorEntity;
    }

    public static QueryService getQueryService(SupportedDBType dbType) {
        return queryServiceMap.getOrDefault(dbType.getQueryService(),null);
    }

}
