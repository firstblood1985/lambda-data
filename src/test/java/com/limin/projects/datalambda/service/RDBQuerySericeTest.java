package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.config.LambdaInit;
import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.RDBLambdaConfigService;
import com.limin.projects.datalambda.convert.RDBObjectToDimValue;
import com.limin.projects.datalambda.convert.RDBObjectToIndicatorEntity;
import com.limin.projects.datalambda.example.*;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.facade.LambdaQueryResults;
import com.limin.projects.datalambda.testconfig.TestConfig;
import com.limin.projects.datalambda.utils.CommonUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
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
@RunWith(SpringRunner.class)
@EnableConfigurationProperties(value = LambdaRawConfig.class)
@Import({TestConfig.class,LambdaInit.class})
public class RDBQuerySericeTest {

    @Autowired
    private LambdaRawConfig lambdaRawConfig;

    @Autowired
    private RDBLambdaConfigService rdbLambdaConfigService;

    @Autowired
    private RDBObjectToDimValue rdbObjectToDimValue;

    @Autowired
    private RDBObjectToIndicatorEntity rdbObjectToIndicatorEntity;

    @Autowired
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
        bj = new Province("BJ");
        sh = new Province("SH");
        js = new Province("JS");

        shCity = new City("SH");
        bjCity = new City("BJ");
        wxCity = new City("WX");



        rdbQueryService = new RDBQueryService(lambdaRawConfig,dataSource,rdbLambdaConfigService,rdbObjectToDimValue,rdbObjectToIndicatorEntity);
        population = new Population();
        reportDate = new ReportDate("20220409");
        reportTime = new ReportTime("2022-04-10 10:30:00");
        queryParams = new LambdaQueryParams().dims(sh,shCity,reportDate).indicators(population);
    }

    @Test
    public void testSetUp(){
        Assert.assertNotNull(rdbLambdaConfigService);
        Assert.assertNotNull(rdbQueryService);
    }

    @Test
    public void testSingleQuery(){
        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);

        population = results.get(0).retrieveResults(population);
        Assert.assertEquals(1000,population.getNumberOfPopulationYesterday().longValue());
    }

    @Test
    public void testQueryWithMultiDimInstancesOfSameType(){
        ReportDate anotherReportDate = new ReportDate("20220408");
        queryParams.getDimInstances().add(anotherReportDate);

        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);
        Assert.assertEquals(2,results.size());

        population = results.get(1).retrieveResults(population);
        Assert.assertEquals(993,population.getNumberOfPopulationYesterday().longValue());

    }

    @Test
    public void testQueryFromDeltaTables(){
        queryParams.getDimInstances().remove(2);
        queryParams.getDimInstances().add(reportTime);
        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);
        population = results.get(0).retrieveResults(population);
        Assert.assertEquals(5,population.getNumberOfPeopleBornedDelta().longValue());
    }

    @Test
    public void testThenCoalesce(){
        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);

        Population population1 = results.get(0).retrieveResults(population);

        queryParams.getDimInstances().remove(2);
        queryParams.getDimInstances().add(reportTime);
        results = rdbQueryService.query(queryParams);
        Population population2  = results.get(0).retrieveResults(population);

        population = CommonUtil.colesce(population1,population2);
        Assert.assertEquals(1000,population.getNumberOfPopulationYesterday().longValue());
        Assert.assertEquals(5,population.getNumberOfPeopleBornedDelta().longValue());

    }

    @Test
    public void testRangeQuery(){
        queryParams.getDimInstances().add(new ReportDate("20220407"));
        queryParams.setRangeQuery();

        List<LambdaQueryResults> results = rdbQueryService.query(queryParams);
        Assert.assertEquals(3,results.size());
        ReportDate r = (ReportDate)results.get(1).getDimInstances().get(2);
        Assert.assertEquals("20220408",r.getReportDate());
    }
}
