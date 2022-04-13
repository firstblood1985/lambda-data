package com.limin.projects.datalambda.facade;

import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.service.QueryResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * usage of this class: LambdaQueryResults
 * created by limin @ 2022/4/13
 */
@Getter
@Setter
public class LambdaQueryResults {
    private List<Object> dimInstances;

    private List<Object> indicatorResults;

    public LambdaQueryResults() {
        dimInstances = new ArrayList<>();
        indicatorResults = new ArrayList<>();
    }

    public LambdaQueryResults dims(Object... o) {
        dimInstances.add(o);
        return this;
    }

    public LambdaQueryResults indicatorResult(Object... o) {
        indicatorResults.add(o);
        return this;
    }

    public <T> T retrieveResults(T indicator)
    {
        Optional<Object> r = indicatorResults.stream().filter(x->x.getClass() == indicator.getClass()).findAny();

        if(r.isPresent())
            return (T) r.get();
        else
            return null;
    }

    public static LambdaQueryResults of(QueryResult queryResult, LambdaQueryParams params) {
      LambdaQueryResults lambdaQueryResults = new LambdaQueryResults();
      List<DimValue> dimValues = queryResult.getDimValues();

      List<Object> matchedDimInstances = params.matchDimInstances(dimValues);
      lambdaQueryResults.setDimInstances(matchedDimInstances);

      List<Object> indicatorResults = params.fetchResults(queryResult.getIndicatorValues());
      lambdaQueryResults.setIndicatorResults(indicatorResults);

      return lambdaQueryResults;
    }


}
