package com.limin.projects.datalambda.rdb;

import com.limin.projects.datalambda.config.RDBLamdbdaConfig;
import com.limin.projects.datalambda.dim.*;
import com.limin.projects.datalambda.facade.LambdaQueryParams;
import com.limin.projects.datalambda.indicator.RDBIndicatorEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * usage of this class: SQLGenerater
 * created by limin @ 2022/4/12
 */
public class SQLGenerater {
    private final String sqlTemplate = "select \n" +
            "%s" +
            "%s" +
            "\nfrom %s \n where\n" +
            "%s";

    private List<DimValue<RDBDimEntity, RDBDimCode>> dimValues;
    private List<RDBIndicatorEntity> indicatorEntities;
    private RDBLamdbdaConfig.Table table;
    private String nonExistedDim;
    private LambdaQueryParams params;

    public SQLGenerater(List<DimValue<RDBDimEntity, RDBDimCode>> dimValues, List<RDBIndicatorEntity> indicatorEntities, RDBLamdbdaConfig.Table table, String nonExistedDim,LambdaQueryParams params) {
        this.dimValues = dimValues;
        this.indicatorEntities = indicatorEntities;
        this.table = table;
        this.nonExistedDim = nonExistedDim;
        this.params = params;
    }

    public SQL generateSQL() {
        String indicatorSql = buildIndicatorQueryString();
        String dimSelectSql = buildDimSelectString();
        String dimSql = buildDimQueryString();

        String sql = String.format(sqlTemplate, dimSelectSql,indicatorSql, table.getTableName(), dimSql);

        return new SQL(sql, dimValues, indicatorEntities);
    }

    private String buildIndicatorQueryString() {
        StringBuilder sb = new StringBuilder();
        for (RDBIndicatorEntity indicatorEntity : indicatorEntities)
            sb.append(
                    StringUtils.joinWith(",\n", indicatorEntity
                            .getIndicators().stream()
                            .filter(i -> StringUtils.equals(i.getTableAlias(), table.getAlias()))
                            .map(i -> i.getColumnName()).toArray())
            );

        return sb.toString();
    }

    private String buildDimSelectString(){
        StringBuilder sb = new StringBuilder();
        for(DimValue<RDBDimEntity,RDBDimCode> dv:dimValues)
        {
            sb.append(
                   StringUtils.joinWith(",\n",dv.getDimCodeToValue().keySet().stream().map(RDBDimCode::getColumnName).toArray()
                   )
            );
            sb.append(",\n");
        }
        return sb.toString();
    }

    private String buildDimQueryString() {
        List<String> dimEntitySqls = new ArrayList<>();
        //build for existed dim
        List<DimValue.DimValueGroup<RDBDimEntity, RDBDimCode>> dimValueGroups = DimValue.DimValueGroup.of(dimValues);

        for (DimValue.DimValueGroup<RDBDimEntity, RDBDimCode> dimValueGroup : dimValueGroups) {
            //for same dimentity
            String dimValueSql = "";
            RDBDimEntity dimEntity = dimValueGroup.getDimEntity();
            if (dimValueGroup.getDimValues().size() == 1) {
                DimValue<RDBDimEntity, RDBDimCode> dimValue = dimValueGroup.getDimValues().get(0);
                List<String> ands = dimValue.getDimCodeToValue().entrySet().stream().map(getSingleDimSql(table)).collect(Collectors.toList());
                dimValueSql = StringUtils.joinWith("\nAND ", ands.toArray());

            } else {
                // group by dim codes
                Map<RDBDimCode, List<String>> dimCodeValues = new HashMap<>();
                for (DimValue<RDBDimEntity, RDBDimCode> dv : dimValueGroup.getDimValues()) {
                    for (RDBDimCode dimCode : dv.getDimCodeToValue().keySet()) {
                        if (dimCodeValues.containsKey(dimCode)) {
                            dimCodeValues.get(dimCode).add(dv.getDimCodeToValue().get(dimCode));
                        } else {
                            List<String> values = new ArrayList<>(Collections.singleton(dv.getDimCodeToValue().get(dimCode)));
                            dimCodeValues.put(dimCode, values);
                        }
                    }
                }
                List<String> dimCodeSql = dimCodeValues.entrySet().stream().map(getDimCodeSqlString(table)).collect(Collectors.toList());

                dimValueSql = StringUtils.joinWith("\nAND", dimCodeSql.toArray());
            }

            dimEntitySqls.add(dimValueSql);
        }

        //build for nonexisted dim
        // filter out non existed dims
        List<RDBLamdbdaConfig.Column> nonExistedDimColumns = table.getColumns().stream()
                .filter(RDBLamdbdaConfig.Column::isDimColumn)
                .filter(getColumnPredicate(dimValues)).collect(Collectors.toList());

        List<String> nonExistedDimSql = nonExistedDimColumns.stream()
                .map(c -> String.format(" %s = '%s'", c.getColumnName(), nonExistedDim))
                .collect(Collectors.toList());

        dimEntitySqls.addAll(nonExistedDimSql);
        return StringUtils.joinWith("\nAND ", dimEntitySqls.toArray());
    }

    private Function<Map.Entry<RDBDimCode, String>, String> getSingleDimSql(RDBLamdbdaConfig.Table table) {
        return e -> {
            Optional<RDBLamdbdaConfig.Column> c = table.getColumns().stream().
                    filter(col -> StringUtils.equals(col.getColumnName(), e.getKey().getColumnName())).findAny();
            if (c.isPresent()) {
                RDBLamdbdaConfig.ColumnType type = c.get().getColumnType();
                if (RDBLamdbdaConfig.ColumnType.VARCHAR == type) {
                    return String.format("%s = '%s'", e.getKey().getColumnName(), e.getValue());
                } else {
                    return String.format("%s = %s ", e.getKey().getColumnName(), e.getValue());
                }
            } else {
                return "";
            }
        };
    }

    private Function<Map.Entry<RDBDimCode, List<String>>, String> getDimCodeSqlString(RDBLamdbdaConfig.Table table) {
        return e -> {
            Optional<RDBLamdbdaConfig.Column> c = table.getColumns().stream().
                    filter(col -> StringUtils.equals(col.getColumnName(), e.getKey().getColumnName())).findAny();
            if (c.isPresent()) {
                RDBLamdbdaConfig.ColumnType type = c.get().getColumnType();
                String colName = e.getKey().getColumnName();
                String values = "";

                if(e.getKey().getDimCodeType() == DimCodeType.RANGE && params.isRangeQuery()) {
                   if(e.getValue().size() != 2) // for range query, 2 parameters are must to represent start and end
                   {
                       throw new DimService.DimCodeException(String.format("For range query, only 2 parameters are required, but got %d",e.getValue().size()));
                   }


                }

                if (RDBLamdbdaConfig.ColumnType.VARCHAR == type) {
                    values = StringUtils.joinWith(",", e.getValue().stream().map( v -> String.format("'%s'",v)).toArray());
                } else {
                    values = StringUtils.joinWith(",", e.getValue().stream().map( v -> String.format("%s",v)).toArray());
                }
                return String.format("%s in ( %s )", colName, values);
            } else {
                return "";
            }
        };
    }

    private Predicate<RDBLamdbdaConfig.Column> getColumnPredicate(List<DimValue<RDBDimEntity, RDBDimCode>> dimValues) {
        return column -> {
            Optional<DimValue<RDBDimEntity, RDBDimCode>> exsitedDv = dimValues.stream().filter(dv -> {
                Optional<RDBDimCode> dc = dv.getDimCodeToValue().keySet().stream().
                        filter(rdbDimCode -> StringUtils.equals(rdbDimCode.getColumnName(), column.getColumnName())).findAny();
                if (dc.isPresent())
                    return true;
                else
                    return false;
            }).findAny();
            if (exsitedDv.isPresent())
                return false;
            else
                return true;
        };
    }
}

