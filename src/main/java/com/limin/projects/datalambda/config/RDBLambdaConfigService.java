package com.limin.projects.datalambda.config;


//import org.springframework.jdbc.core.JdbcTemplate;

import com.limin.projects.datalambda.dim.RDBDimCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * usage of this class: RDBLambdaConfigService
 * created by limin @ 2022/4/5
 */
public class RDBLambdaConfigService extends LambdaConfigService<RDBLamdbdaConfig> {

    private JdbcTemplate jdbcTemplate;

    protected RDBLambdaConfigService(SupportedDBType dbType, DataSource dataSource, RDBLamdbdaConfig lambdaConfig) {
        super(dbType, dataSource, lambdaConfig);

        jdbcTemplate = new JdbcTemplate(dataSource);
        setDataSourceMetaData();
    }

    @Override
    public void setDataSourceMetaData() {
        List<RDBLamdbdaConfig.Table> tables = lambdaConfig.getTables();

        String getColumnsTemplate = "show columns from %s";
        //query table meta
        for (RDBLamdbdaConfig.Table table : tables) {
            String queryColumnMetaSql = String.format(getColumnsTemplate, table.getTableName());
            List<RDBLamdbdaConfig.Column> columns = jdbcTemplate.query(queryColumnMetaSql, new RDBLamdbdaConfig.ColumnMetaRowMapper());
            table.setColumns(columns);
            table.markDimColumns(lambdaConfig.getDimColumns().split(","));
        }
    }

    public List<RDBLamdbdaConfig.Table> getTables() {
        return lambdaConfig.getTables();
    }

    public RDBLamdbdaConfig.Table getTableByAlias(String alias) {
        Optional<RDBLamdbdaConfig.Table> result = lambdaConfig.getTables().stream().filter(t -> StringUtils.equals(t.getAlias(), alias)).findFirst();

        if (result.isPresent())
            return result.get();
        else
            return null;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Set<String> getTableAliasByDimCode(RDBDimCode dimCode) {
        return lambdaConfig.getTables().stream()
                .filter( table -> {
                    Optional<RDBLamdbdaConfig.Column> c =
                            table.getColumns().stream()
                                    .filter(column -> StringUtils.equals(column.getColumnName(), dimCode.getColumnName()))
                                    .findAny();
                    if(c.isPresent())
                        return true;
                    else
                        return false;
                }).map(RDBLamdbdaConfig.Table::getAlias).collect(Collectors.toSet());
    }
}
