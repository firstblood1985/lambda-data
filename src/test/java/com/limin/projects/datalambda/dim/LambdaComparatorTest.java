package com.limin.projects.datalambda.dim;

import org.junit.Assert;
import org.junit.Test;

/**
 * usage of this class: LambdaComparatorTest
 * created by limin @ 2022/4/14
 */
public class LambdaComparatorTest {

    @Test
    public void testLambdaComparator()
    {
        LambdaComparator c1 = LambdaComparator.LONGCOMPARATOR;

        String o1 = "100";
        String o2 = "1000";

        Assert.assertEquals(-1,c1.compare(o1,o2));

        LambdaComparator c2 = LambdaComparator.STRINGCOMPARATOR;

        o1 = "20220408";
        o2 = "20220409";
        Assert.assertEquals(-1,c2.compare(o1,o2));
    }

}
