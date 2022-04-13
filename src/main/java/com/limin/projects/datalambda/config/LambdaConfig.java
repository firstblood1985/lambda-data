package com.limin.projects.datalambda.config;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * usage of this class: LambdaConfig
 * created by limin @ 2022/4/5
 */
@Getter
@Setter
public abstract class LambdaConfig {

    protected final SupportedDBType dbType;

    protected final LambdaRawConfig lambdaRawConfig;

    private static Map<SupportedDBType,LambdaConfigBuilder> builders;
    static {
        builders= new HashMap<>();
        builders.put(SupportedDBType.SQL_MYSQL,LambdaConfigBuilder.rdbConfigBuilder());
    }

    protected LambdaConfig(SupportedDBType dbType, LambdaRawConfig lambdaRawConfig) {
        this.dbType = dbType;
        this.lambdaRawConfig = lambdaRawConfig;
    }

    public static LambdaConfig createLamdbdaConfig(SupportedDBType dbType, LambdaRawConfig lambdaRawConfig)
    {
        return builders.get(dbType).apply(dbType,lambdaRawConfig);
    }


    private static interface LambdaConfigBuilder extends BiFunction<SupportedDBType,LambdaRawConfig,LambdaConfig> {

        static LambdaConfigBuilder rdbConfigBuilder() {
            return (dbType, lambdaRawConfig)  -> {
                 return new RDBLamdbdaConfig(dbType,lambdaRawConfig);
            };
        }
    }

}
