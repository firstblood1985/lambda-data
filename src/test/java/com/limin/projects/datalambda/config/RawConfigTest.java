package com.limin.projects.datalambda.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * usage of this class: RawConfigTest
 * created by limin @ 2022/4/5
 */
@RunWith(SpringRunner.class)
@EnableConfigurationProperties(value = LambdaRawConfig.class)
public class RawConfigTest {

    @Autowired
    private LambdaRawConfig lambdaRawConfig;

    @Test
    public void testRawConfig()
    {
        Assert.assertNotNull(lambdaRawConfig);
        Assert.assertEquals("mysql",lambdaRawConfig.getDbInstanceType());
    }


}
