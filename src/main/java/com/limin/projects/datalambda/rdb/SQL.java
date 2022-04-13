package com.limin.projects.datalambda.rdb;

import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.dim.RDBDimCode;
import com.limin.projects.datalambda.dim.RDBDimEntity;
import com.limin.projects.datalambda.indicator.RDBIndicatorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * usage of this class: SQL
 * created by limin @ 2022/4/12
 */
@Getter
@Setter
@AllArgsConstructor
public class SQL {
    private String sql;
    private List<DimValue<RDBDimEntity, RDBDimCode>> dimValues;
    private List<RDBIndicatorEntity> indicatorEntities;

}
