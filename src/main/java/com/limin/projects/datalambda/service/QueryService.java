package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.config.LambdaConfigService;
import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.RDBLambdaConfigService;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.convert.ObjectToDimValue;
import com.limin.projects.datalambda.convert.ObjectToIndicatorEntity;
import com.limin.projects.datalambda.convert.RDBObjectToDimValue;
import com.limin.projects.datalambda.convert.RDBObjectToIndicatorEntity;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.facade.LambdaQueryResults;
import com.limin.projects.datalambda.indicator.AbstractIndicatorEntity;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
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

    private static Map<SupportedDBType,QueryServiceBuiler> queryServiceMap;

    public abstract List<LambdaQueryResults> query(LambdaQueryParams params);

    protected QueryService(LambdaRawConfig lambdaRawConfig,DataSource dataSource,
                           M lambdaConfigService,N objectToDimValue,O objectToIndicatorEntity) {
        dbType = SupportedDBType.of(lambdaRawConfig.getDbType(),lambdaRawConfig.getDbInstanceType());
        this.dataSource = dataSource;
        this.lambdaConfigService = lambdaConfigService;
        this.objectToDimValue = objectToDimValue;
        this.objectToIndicatorEntity = objectToIndicatorEntity;
    }
    static {
        queryServiceMap = new HashMap<>();
        queryServiceMap.put(SupportedDBType.SQL_MYSQL,QueryServiceBuiler.rdbQueryService());
    }

    public static QueryServiceBuiler getQueryServiceBuiler(SupportedDBType dbType) {
        return queryServiceMap.getOrDefault(dbType,null);
    }
    @FunctionalInterface
    public static interface QueryServiceBuiler {
        QueryService apply(LambdaRawConfig lambdaRawConfig, DataSource dataSource, LambdaConfigService lambdaConfigService,ObjectToDimValue objectToDimValue, ObjectToIndicatorEntity objectToIndicatorEntity);

        static QueryServiceBuiler rdbQueryService(){
            return (lambdaRawConfig1,dataSource1, lambdaConfigService1, objectToDimValue1, objectToIndicatorEntity1) -> new RDBQueryService(lambdaRawConfig1,
                    dataSource1,
                    (RDBLambdaConfigService) lambdaConfigService1,
                    (RDBObjectToDimValue) objectToDimValue1,
                    (RDBObjectToIndicatorEntity) objectToIndicatorEntity1);
        }
    }


}
