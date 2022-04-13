package com.limin.projects.datalambda.deepcopy;

import com.google.gson.Gson;
import com.limin.projects.datalambda.example.City;
import com.limin.projects.datalambda.example.Population;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * usage of this class: DeepCopyTest
 * created by limin @ 2022/4/13
 */
public class DeepCopyTest {

    @Test
    public void deepCopyTest()
    {
        City sh = new City("SH");
        Gson gson = new Gson();
        Object copied = gson.fromJson(gson.toJson(sh),City.class);

        Assert.assertTrue(copied instanceof City);
    }

    @Test
    public void writeBackTest() throws NoSuchFieldException, IllegalAccessException {
        String longValue = "1000";

        Population p = new Population();
        Field f = p.getClass().getDeclaredField("numberOfPopulationYesterday");
        f.setAccessible(true);
        f.set(p,Long.valueOf(longValue));
        Assert.assertTrue(f.getType() == Long.class);
        Assert.assertEquals(1000,p.getNumberOfPopulationYesterday().longValue());

    }

}
