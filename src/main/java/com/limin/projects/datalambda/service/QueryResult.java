package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.dim.AbstractDimCode;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.indicator.AbstractIndicator;
import com.limin.projects.datalambda.indicator.AbstractIndicatorEntity;
import com.limin.projects.datalambda.indicator.IndicatorValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * usage of this class: QueryResult
 * created by limin @ 2022/4/12
 */
@Getter
@Setter
@AllArgsConstructor
public class QueryResult<T extends AbstractDimEntity,P extends AbstractDimCode,M extends AbstractIndicatorEntity,N extends AbstractIndicator> {

   private List<DimValue<T,P>> dimValues;

   private List<IndicatorValue<M,N>> indicatorValues;
}
