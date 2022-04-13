package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.service.RawQueryResult;

import java.util.List;

/**
 * usage of this class: RDBIndicatorValue
 * created by limin @ 2022/4/12
 */
public class RDBIndicatorValue extends IndicatorValue<RDBIndicatorEntity,RDBIndicator>{
    public RDBIndicatorValue(RDBIndicatorEntity indicatorEntity, RawQueryResult rawQueryResult) {
        super(indicatorEntity, rawQueryResult);
    }

    @Override
    protected void retrieveResult() {
        List<RDBIndicator> indicators = indicatorEntity.getIndicators();
        for (RDBIndicator indicator : indicators) {
            if(null != rawQueryResult.getRawResults().getOrDefault(indicator.getColumnName(),null))
            {
                indicatorValue.put(indicator,rawQueryResult.getRawResults().get(indicator.getColumnName()));
            }
        }
    }
}
