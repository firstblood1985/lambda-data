package com.limin.projects.datalambda.config;

import com.limin.projects.datalambda.config.RDBLamdbdaConfig.Table;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * usage of this class: RDBLambdaConfigTest
 * created by limin @ 2022/4/5
 */
@RunWith(SpringRunner.class)
@EnableConfigurationProperties(value = LambdaRawConfig.class)
@TestPropertySource("/test.properties")
public class RDBLambdaConfigTest {

    @Autowired
    LambdaRawConfig lambdaRawConfig;

    @Test
    public void testGetTables()
    {
        RDBLamdbdaConfig rdbLamdbdaConfig = new RDBLamdbdaConfig(SupportedDBType.SQL_MYSQL,lambdaRawConfig);

        List<Table> tables = rdbLamdbdaConfig.getTables();

        Assert.assertEquals(2,tables.size());

        Assert.assertEquals("POPULATION_STATS",tables.get(0).getTableName());
    }
}
