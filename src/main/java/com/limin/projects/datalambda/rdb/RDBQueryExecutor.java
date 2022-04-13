package com.limin.projects.datalambda.rdb;

import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.dim.RDBDimCode;
import com.limin.projects.datalambda.dim.RDBDimEntity;
import com.limin.projects.datalambda.indicator.IndicatorValue;
import com.limin.projects.datalambda.indicator.RDBIndicator;
import com.limin.projects.datalambda.indicator.RDBIndicatorEntity;
import com.limin.projects.datalambda.indicator.RDBIndicatorValue;
import com.limin.projects.datalambda.service.QueryExecutor;
import com.limin.projects.datalambda.service.QueryResult;
import com.limin.projects.datalambda.service.RawQueryResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * usage of this class: RDBQueryExecutor
 * created by limin @ 2022/4/12
 */
@Getter
@Setter
@AllArgsConstructor
public class RDBQueryExecutor extends QueryExecutor {
    private final JdbcTemplate jdbcTemplate;
    private final List<SQL> sqls;


    @Override
    public List<QueryResult<RDBDimEntity, RDBDimCode,RDBIndicatorEntity, RDBIndicator>> executeQuery() {

        List<QueryResult<RDBDimEntity, RDBDimCode,RDBIndicatorEntity, RDBIndicator>> results = new ArrayList<>();
        for (SQL sql : sqls) {
            List<RawQueryResult> rawQueryResults = executeQuery(sql);
            for (RawQueryResult r : rawQueryResults) {
                List<DimValue<RDBDimEntity, RDBDimCode>> uniqueDimValues = sql.getDimValues().stream()
                        .filter(dv -> dv.match(r))
                        .collect(Collectors.toList());
                List<IndicatorValue<RDBIndicatorEntity, RDBIndicator>> values = sql.getIndicatorEntities()
                        .stream().map(i -> new RDBIndicatorValue(i, r))
                        .collect(Collectors.toList());

                results.add(
                        new QueryResult<RDBDimEntity, RDBDimCode,RDBIndicatorEntity, RDBIndicator>(uniqueDimValues, values)
                );
            }
        }
        return results;
    }

    private List<RawQueryResult> executeQuery(SQL sql) {

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql.getSql());
        return results.stream().map(RawQueryResult::new).collect(Collectors.toList());
    }
}
