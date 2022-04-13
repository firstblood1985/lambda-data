package com.limin.projects.datalambda.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * usage of this class: RawConfig
 * created by limin @ 2022/4/5
 */
@Configuration
@ConfigurationProperties(prefix = "lambdadata.config")
@Getter
@Setter
public class LambdaRawConfig {
    private String welcome;

    private String dbType;

    private String dbInstanceType;

    private String nonExistedDim;

    private String mappings;

    private String partitionedBy;

    private String partitionFormat;

    private String dimPackage;

    private String indicatorPackage;

    private String dimColumns;

}
