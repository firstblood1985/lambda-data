package com.limin.projects.datalambda.service;

import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.RDBLambdaConfigService;
import com.limin.projects.datalambda.convert.RDBObjectToDimValue;
import com.limin.projects.datalambda.convert.RDBObjectToIndicatorEntity;
import com.limin.projects.datalambda.dim.*;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.facade.LambdaQueryResults;
import com.limin.projects.datalambda.indicator.RDBIndicator;
import com.limin.projects.datalambda.indicator.RDBIndicatorEntity;
import com.limin.projects.datalambda.config.RDBLamdbdaConfig.Table;
import com.limin.projects.datalambda.rdb.RDBQueryExecutor;
import com.limin.projects.datalambda.rdb.SQL;
import com.limin.projects.datalambda.rdb.SQLGenerater;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * usage of this class: RDBQueryService
 * created by limin @ 2022/4/8
 */
public class RDBQueryService extends QueryService<RDBDimEntity, RDBIndicatorEntity, RDBLambdaConfigService,RDBObjectToDimValue,RDBObjectToIndicatorEntity> {


    private JdbcTemplate jdbcTemplate;

    private final String nonExistedDim;


    public RDBQueryService(LambdaRawConfig lambdaRawConfig, DataSource dataSource,
                           RDBLambdaConfigService rdbLambdaConfigService,RDBObjectToDimValue rdbObjectToDimValue,RDBObjectToIndicatorEntity rdbObjectToIndicatorEntity) {
        super(lambdaRawConfig, dataSource, rdbLambdaConfigService,rdbObjectToDimValue,rdbObjectToIndicatorEntity);

        nonExistedDim = lambdaRawConfig.getNonExistedDim();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<LambdaQueryResults> query(LambdaQueryParams params) {
        List<SQL> sqls = generateSqls(params);
        RDBQueryExecutor executor = new RDBQueryExecutor(jdbcTemplate,sqls);
        List<QueryResult<RDBDimEntity,RDBDimCode,RDBIndicatorEntity,RDBIndicator>> queryResults = executor.executeQuery();
        return queryResults.stream().map(r-> LambdaQueryResults.of(r,params)).collect(Collectors.toList());

    }

    private List<SQL> generateSqls(LambdaQueryParams params) {
        List<DimValue<RDBDimEntity, RDBDimCode>> dimValues = objectToDimValue.ObjectToDimValue(params.getDimInstances());

        List<RDBIndicatorEntity> indicatorEntities = objectToIndicatorEntity.objectToIndicatorEntity(params.getIndicatorInstances());

        //group indicators with same table
        Set<String> tableAliases = getUniqueTableAliases(indicatorEntities, dimValues);

        return tableAliases.stream()
                .map(alias -> lambdaConfigService.getTableByAlias(alias))
                .map(getTableSQLFunction(dimValues, indicatorEntities,params))
                .collect(Collectors.toList());
    }

    private Function<Table, SQL> getTableSQLFunction(List<DimValue<RDBDimEntity, RDBDimCode>> dimValues, List<RDBIndicatorEntity> indicatorEntities,LambdaQueryParams params) {
        return t -> new SQLGenerater(dimValues, indicatorEntities.stream().filter(
                //filter out indicators that are coming from same table
                rdbIndicatorEntity -> {
                    return filterIndicatorWithinSameTable(t, rdbIndicatorEntity);
                }
        ).collect(Collectors.toList()), t,nonExistedDim,params).generateSQL();
    }

    private boolean filterIndicatorWithinSameTable(Table t, RDBIndicatorEntity rdbIndicatorEntity) {
        Optional<RDBIndicator> id = rdbIndicatorEntity.getIndicators().stream()
                .filter(rdbIndicator -> StringUtils.equals(t.getAlias(), rdbIndicator.getTableAlias()))
                .findAny();
        if (id.isPresent())
            return true;
        else
            return false;
    }

    private Set<String> getUniqueTableAliases(List<RDBIndicatorEntity> indicatorEntities, List<DimValue<RDBDimEntity, RDBDimCode>> dimValues) {
        Set<String> uniqueTableAliases = new HashSet<>();
        indicatorEntities.stream().forEach(ie ->
                ie.getIndicators().stream().forEach(i -> uniqueTableAliases.add(i.getTableAlias()))
        );

        Set<String> uniqueTableAliases2 = new HashSet<>(uniqueTableAliases);
        for (DimValue<RDBDimEntity, RDBDimCode> dv : dimValues) {
            for (RDBDimCode dc : dv.getDimCodeToValue().keySet()) {
                Set<String> tableAlias = lambdaConfigService.getTableAliasByDimCode(dc);
                uniqueTableAliases2 = SetUtils.intersection(uniqueTableAliases2, tableAlias);
            }
        }
        return SetUtils.intersection(uniqueTableAliases,uniqueTableAliases2);
    }




}
