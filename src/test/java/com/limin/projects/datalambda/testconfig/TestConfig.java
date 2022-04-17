package com.limin.projects.datalambda.testconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * usage of this class: TestConfig
 * created by limin @ 2022/4/17
 */
@Configuration
public class TestConfig {

    @Bean
    DataSource dataSource(){
        return    new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema_lambda_example.sql")
                .addScript("classpath:lambda_data_test_data.sql")
                .build();
    }
}
