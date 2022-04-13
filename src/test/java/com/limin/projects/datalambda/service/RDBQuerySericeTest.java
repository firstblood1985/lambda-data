package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.RDBLambdaConfigService;
import com.limin.projects.datalambda.convert.RDBObjectToDimValue;
import com.limin.projects.datalambda.convert.RDBObjectToIndicatorEntity;
import com.limin.projects.datalambda.example.*;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.facade.LambdaQueryResults;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.List;

/**
 * usage of this class: RDBQuerySericeTest
 * created by limin @ 2022/4/9
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/test.properties")
public class RDBQuerySericeTest {

    @Autowired
    private LambdaRawConfig lambdaRawConfig;

    @Autowired
    private RDBLambdaConfigService rdbLambdaConfigService;

    @Autowired
    private RDBObjectToDimValue rdbObjectToDimValue;

    @Autowired
    private RDBObjectToIndicatorEntity rdbObjectToIndicatorEntity;

    private DataSource dataSource;

    private RDBQueryService rdbQueryService;

    Province bj;
    Province sh;
    Province js;
    City shCity;
    City bjCity;
    City wxCity;

    ReportDate reportDate;
    ReportTime reportTime;

    Population population;

    LambdaQueryParams queryParams;

    @Before
    public void setup() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .generateUniqueName(true)
                .addScript("classpath:schema_lambda_example.sql")
                .addScript("classpath:lambda_data_test_data.sql")
                .build();

        bj = new Province("BJ");
        sh = new Province("SH");
        js = new Province("JS");

        shCity = new City("SH");
        bjCity = new City("BJ");
        wxCity = new City("WX");


        rdbLambdaConfigService.setDataSource(dataSource);
        rdbLambdaConfigService.setDataSourceMetaData();

        rdbQueryService = new RDBQueryService(lambdaRawConfig,dataSource,rdbLambdaConfigService,rdbObjectToDimValue,rdbObjectToIndicatorEntity);
        population = new Population();
        reportDate = new ReportDate("20220409");
        reportTime = new ReportTime("2022-04-10 10:30:00");
    }

    @Test
    public void testSetUp(){
        Assert.assertNotNull(rdbLambdaConfigService);
        Assert.assertNotNull(rdbQueryService);
    }

    @Test
    public void testSingleQuery(){
        queryParams = new LambdaQueryParams().dims(sh,shCity,reportDate).indicators(population);
        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);

        population = results.get(0).retrieveResults(population);
        Assert.assertEquals(1000,population.getNumberOfPopulationYesterday().longValue());
    }


}
