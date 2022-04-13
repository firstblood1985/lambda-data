package com.limin.projects.datalambda.config;

import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * usage of this class: LambdaConfigService
 * created by limin @ 2022/4/5
 */
@Getter
//@Setter
public abstract class LambdaConfigService<T extends LambdaConfig> {
    protected  SupportedDBType dbType;
    protected  DataSource dataSource;
    protected  T lambdaConfig;

    private static Map<SupportedDBType,LambdaConfigServiceBuilder> builders;

    static {
        builders = new HashMap<>();
        builders.put(SupportedDBType.SQL_MYSQL,LambdaConfigServiceBuilder.rdbLambdaConfigServiceBuilder());
    }

    protected LambdaConfigService(SupportedDBType dbType, DataSource dataSource, T lambdaConfig) {
        this.dbType = dbType;
        this.dataSource = dataSource;
        this.lambdaConfig = lambdaConfig;
    }

    protected abstract void setDataSourceMetaData();

    public static LambdaConfigServiceBuilder getBuilder(SupportedDBType dbType)
    {
        return builders.get(dbType);
    }
    @FunctionalInterface
    public static interface LambdaConfigServiceBuilder{
        LambdaConfigService apply(SupportedDBType dbType,DataSource dataSource, LambdaConfig lambdaConfig);

        static LambdaConfigServiceBuilder rdbLambdaConfigServiceBuilder(){
            return (dbType1, dataSource1, lambdaConfig1) -> {
                return new RDBLambdaConfigService(dbType1,dataSource1,(RDBLamdbdaConfig) lambdaConfig1);
            };
        };
    }
}
