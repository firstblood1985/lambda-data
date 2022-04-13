package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.dim.AbstractDimCode;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.indicator.AbstractIndicator;
import com.limin.projects.datalambda.indicator.AbstractIndicatorEntity;

import java.util.List;

/**
 * usage of this class: QueryExecutor
 * created by limin @ 2022/4/12
 */
public abstract class QueryExecutor<T extends AbstractDimEntity,P extends AbstractDimCode,M extends AbstractIndicatorEntity,N extends AbstractIndicator> {

    public abstract List<QueryResult<T,P,M,N>> executeQuery();
}
