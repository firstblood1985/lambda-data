package com.limin.projects.datalambda.config;

import com.limin.projects.datalambda.convert.ObjectToDimValue;
import com.limin.projects.datalambda.convert.ObjectToIndicatorEntity;
import com.limin.projects.datalambda.dim.DimService;
import com.limin.projects.datalambda.indicator.IndicatorService;
import com.limin.projects.datalambda.service.QueryService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * usage of this class: LambdaConfigFactory
 * created by limin @ 2022/4/5
 */

@Configuration
public class LambdaInit {

    @Autowired
    private LambdaRawConfig lambdaRawConfig;

    @Bean
    SupportedDBType supportedDBType(){
        return  SupportedDBType.of(lambdaRawConfig.getDbType(), lambdaRawConfig.getDbInstanceType());
    }


    @Autowired
    @Bean
    LambdaConfig lambdaConfig(@NonNull SupportedDBType dbType) {
        return LambdaConfig.createLamdbdaConfig(dbType, lambdaRawConfig);
    }

    @Autowired
    @Bean
    LambdaConfigService lambdaConfigService(@NonNull SupportedDBType dbType,@NonNull LambdaConfig lambdaConfig, @NonNull DataSource dataSource) {
        return LambdaConfigService.getBuilder(dbType).apply(dbType, dataSource, lambdaConfig);
    }

    @Autowired
    @Bean
    DimService dimService(@NonNull SupportedDBType dbType){
        return DimService.getDimServiceBuilder(dbType).apply(lambdaRawConfig);
    }

    @Autowired
    @Bean
    ObjectToDimValue objectToDimValue(@NonNull SupportedDBType dbType){
       return ObjectToDimValue.getBuilder(dbType).get();
    }

    @Autowired
    @Bean
    ObjectToIndicatorEntity objectToIndicatorEntity(@NonNull SupportedDBType dbType){
        return ObjectToIndicatorEntity.getBuilder(dbType).get();
    }

    @Autowired
    @Bean
    IndicatorService indicatorService(@NonNull SupportedDBType dbType){
        return IndicatorService.getBuilder(dbType).apply(lambdaRawConfig);
    }

    @Autowired
    @Bean
    QueryService queryService(@NonNull SupportedDBType dbType,@NonNull DataSource dataSource,@NonNull LambdaConfigService lambdaConfigService,@NonNull ObjectToDimValue objectToDimValue,@NonNull ObjectToIndicatorEntity objectToIndicatorEntity) {
        return QueryService.getQueryServiceBuiler(dbType).apply(lambdaRawConfig,dataSource,lambdaConfigService,objectToDimValue,objectToIndicatorEntity);
    }
}
