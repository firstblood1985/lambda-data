package com.limin.projects.datalambda.indicator;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * usage of this class: RDBIndicatorEntity
 * created by limin @ 2022/4/6
 */
public class RDBIndicatorEntity extends AbstractIndicatorEntity<RDBIndicator> {
    private List<RDBIndicator> rdbIndicators;

    public RDBIndicatorEntity(String code) {
        super(code);
    }

    @Override
    public void setIndicators(List<RDBIndicator> indicators) {
       rdbIndicators = indicators;
    }

    @Override
    public void addIndicator(RDBIndicator indicator) {
        if(CollectionUtils.isEmpty(rdbIndicators))
            rdbIndicators = new ArrayList<>();

        rdbIndicators.add(indicator);
    }

    @Override
    public List<RDBIndicator> getIndicators() {
        return rdbIndicators;
    }
}
