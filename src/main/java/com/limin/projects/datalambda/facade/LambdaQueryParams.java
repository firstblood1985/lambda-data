package com.limin.projects.datalambda.facade;


import com.google.gson.Gson;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.indicator.IndicatorValue;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * usage of this class: QueryBuilder
 * created by limin @ 2022/4/7
 */
@Getter
public class LambdaQueryParams {
    private List<Object> dimInstances;

    private List<Object> indicatorInstances;

    public LambdaQueryParams()
    {
        dimInstances = new ArrayList<>();
        indicatorInstances = new ArrayList<>();
    }
    public LambdaQueryParams dims(Object... objects)
    {
        dimInstances.addAll(Arrays.asList(objects));
        return this;
    }

    public LambdaQueryParams indicators(Object... objects)
    {
        indicatorInstances.addAll(Arrays.asList(objects));
        return this;
    }

    public List<Object> matchDimInstances(List<DimValue> dimValues)
    {
        List<Object> matchedResults = new ArrayList<>();
        for(DimValue dv:dimValues){
                for(Object dimInstance: dimInstances)
                    if(dv.matchDimInstance(dimInstance))
                        matchedResults.add(dimInstance);
        }
        return matchedResults;
    }

    public List<Object> fetchResults(List<IndicatorValue> indicatorValues)
    {
        List<Object> indicatorResults = new ArrayList<>();
        Gson gson = new Gson();
        for(IndicatorValue iv: indicatorValues)
        {
            for(Object indicatorInstance: indicatorInstances)
            {
                if(iv.getIndicatorEntity().getIndicatorInstance().equals(indicatorInstance))
                {
                    // object copy and set value
                     Object indicatorResult = gson.fromJson(gson.toJson(indicatorInstance),indicatorInstance.getClass());
                     iv.writeBack(indicatorResult);
                     indicatorResults.add(indicatorResult);
                }
            }
        }
        return indicatorResults;
    }

}
