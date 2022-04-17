package com.limin.projects.datalambda.facade;


import com.google.gson.Gson;
import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.indicator.IndicatorValue;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

/**
 * usage of this class: QueryBuilder
 * created by limin @ 2022/4/7
 */
@Getter
@ToString
public class LambdaQueryParams {
    private List<Object> dimInstances;
    private List<Object> indicatorInstances;
    private boolean rangeQuery = false;

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

    public LambdaQueryParams setRangeQuery()
    {
        rangeQuery = true;
        return this;
    }

    public List<Object> matchDimInstances(List<DimValue> dimValues)
    {
        List<Object> matchedResults = new ArrayList<>();
        Set<String> isMatched = new HashSet<>();
        for(DimValue dv:dimValues){
                for(Object dimInstance: dimInstances) {
                    if (dv.matchDimInstance(dimInstance)) {
                        if(!isMatched.contains(dv.getDimEntity().getCode())) {
                            matchedResults.add(dimInstance);
                            isMatched.add(dv.getDimEntity().getCode());
                        }
                        continue;
                    }

                    if(dv.matchDimEntity(dimInstance)){
                        if(!isMatched.contains(dv.getDimEntity().getCode())) {
                            Gson gson = new Gson();
                            Object copiedDimInstance = gson.fromJson(gson.toJson(dimInstance), dimInstance.getClass());
                            dv.writeBack(copiedDimInstance);
                            isMatched.add(dv.getDimEntity().getCode());
                            matchedResults.add(copiedDimInstance);
                        }
                    }
                }

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
