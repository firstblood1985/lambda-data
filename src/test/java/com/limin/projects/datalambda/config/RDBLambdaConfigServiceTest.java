package com.limin.projects.datalambda.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

/**
 * usage of this class: RDBLambdaConfigServiceTest
 * created by limin @ 2022/4/5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/test2.properties")
public class RDBLambdaConfigServiceTest {

    @Autowired
    LambdaRawConfig lambdaRawConfig;

    private DataSource dataSource;
    private RDBLambdaConfigService rdbLambdaConfigService;
    private RDBLamdbdaConfig rdbLamdbdaConfig;

    @Before
    public void setup() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema2.sql")
                .addScript("classpath:test-data2.sql")
                .build();

        rdbLamdbdaConfig = new RDBLamdbdaConfig(SupportedDBType.SQL_MYSQL,lambdaRawConfig);

        rdbLambdaConfigService = new RDBLambdaConfigService(SupportedDBType.SQL_MYSQL,dataSource,rdbLamdbdaConfig);
    }

    @Test
    public void testDatasource(){
        Assert.assertEquals("mysql",lambdaRawConfig.getDbInstanceType());
        Assert.assertNotNull(dataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Assert.assertNotNull(jdbcTemplate);
    }

    @Test
    public void testSetMetaData(){

        Assert.assertEquals(1,rdbLamdbdaConfig.getTables().size());
        Assert.assertEquals("EMPLOYEE",rdbLamdbdaConfig.getTables().get(0).getTableName());
        Assert.assertEquals(4,rdbLamdbdaConfig.getTables().get(0).getColumns().size());
    }

    @After
    public void finish(){
    }

}
