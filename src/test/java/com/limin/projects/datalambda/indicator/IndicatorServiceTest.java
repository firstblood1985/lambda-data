package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * usage of this class: IndicatorServiceTest
 * created by limin @ 2022/4/7
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/test.properties")
public class IndicatorServiceTest {
    @Autowired
    private LambdaRawConfig lambdaRawConfig;

    private RDBIndicatorService rdbIndicatorService;

    @Before
    public void setUp(){
        rdbIndicatorService = new RDBIndicatorService(lambdaRawConfig);
    }

    @Test
    public void testScanPackageAndBuildIndicatorEntities(){
        rdbIndicatorService.scanPackageAndBuildIndicatorEntities();

        assertNotNull(rdbIndicatorService.getIndicatorEntities());
        assertEquals(1,rdbIndicatorService.getIndicatorEntities().size());

        RDBIndicatorEntity entity = (RDBIndicatorEntity) rdbIndicatorService.getIndicatorEntities().get("Population");
        assertEquals(4,entity.getIndicators().size());

    }

}
