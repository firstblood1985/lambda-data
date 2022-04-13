package com.limin.projects.datalambda.dim;

/**
 * usage of this class: DimServiceTest
 * created by limin @ 2022/4/6
 */

import com.limin.projects.datalambda.config.LambdaRawConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/test.properties")
public class DimServiceTest {

   @Autowired
   private LambdaRawConfig lambdaRawConfig;

   private RDBDimService rdbDimService;

   @Before
   public void setUp()
   {
        rdbDimService = new RDBDimService(lambdaRawConfig);
   }

   @Test
    public void testScanPackageAndBuildDimEntities(){

       Assert.assertNotNull(rdbDimService.getDimEntities());

       Assert.assertEquals(4,rdbDimService.getDimEntities().size());

       Assert.assertNotNull(rdbDimService.getDimEntities().get("Province"));
   }


}
