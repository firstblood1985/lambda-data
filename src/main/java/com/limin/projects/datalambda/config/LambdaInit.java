package com.limin.projects.datalambda.config;

import com.limin.projects.datalambda.convert.ObjectToDimValue;
import com.limin.projects.datalambda.convert.ObjectToIndicatorEntity;
import com.limin.projects.datalambda.dim.DimService;
import com.limin.projects.datalambda.indicator.IndicatorService;
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
    LambdaConfig lambdaConfig(SupportedDBType dbType) {
        return LambdaConfig.createLamdbdaConfig(dbType, lambdaRawConfig);
    }

    @Autowired
    @Bean
    LambdaConfigService lambdaConfigService(SupportedDBType dbType,LambdaConfig lambdaConfig, DataSource dataSource) {
        return LambdaConfigService.getBuilder(dbType).apply(dbType, dataSource, lambdaConfig);
    }

    @Autowired
    @Bean
    DimService dimService(SupportedDBType dbType){
        return DimService.getDimServiceBuilder(dbType).apply(lambdaRawConfig);
    }

    @Autowired
    @Bean
    ObjectToDimValue objectToDimValue(SupportedDBType dbType){
       return ObjectToDimValue.getBuilder(dbType).get();
    }

    @Autowired
    @Bean
    ObjectToIndicatorEntity objectToIndicatorEntity(SupportedDBType dbType){
        return ObjectToIndicatorEntity.getBuilder(dbType).get();
    }

    @Autowired
    @Bean
    IndicatorService indicatorService(SupportedDBType dbType){
        return IndicatorService.getBuilder(dbType).apply(lambdaRawConfig);
    }

    //TODO: create query service

}
