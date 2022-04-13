package com.limin.projects.datalambda.convert;

import com.limin.projects.datalambda.dim.DimService;
import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.dim.RDBDimCode;
import com.limin.projects.datalambda.dim.RDBDimEntity;
import com.limin.projects.datalambda.example.City;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * usage of this class: ObjectToDimEntityTest
 * created by limin @ 2022/4/7
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectToDimEntityTest {
    private RDBDimEntity cityEntity;

    private Map<String,RDBDimEntity> map;
    @Mock
    private DimService<RDBDimEntity,RDBDimCode> dimService;
    @InjectMocks
    private RDBObjectToDimValue rdbObjectToDimEntity;
    @Before
    public void setUp(){
        RDBObjectToDimValue rdbObjectToDimEntity = new RDBObjectToDimValue();

        cityEntity = new RDBDimEntity("City");
        RDBDimCode code = new RDBDimCode("CITY_CODE");
        cityEntity.addDimCode(code);

        map = new HashMap<>();
        map.put("City",cityEntity);

    }

    @Test
    public void testObjectToDim()
    {
        City city = new City();
        city.setCode("SH");
        city.setName("上海");

        when(dimService.getDimEntities()).thenReturn(map);

        DimValue<RDBDimEntity,RDBDimCode> result = rdbObjectToDimEntity.ObjectToDimValue(city);

        Assert.assertNotNull(result);

        Assert.assertEquals("City",result.getDimEntity().getCode());
        Collection<String> values = result.getDimCodeToValue().values();

        Assert.assertEquals(1,values.size());
        Assert.assertEquals(true,values.contains("SH"));
    }
}
