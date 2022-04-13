package com.limin.projects.datalambda.config;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * usage of this class: RDBLamdbdaConfig
 * created by limin @ 2022/4/5
 */
@Getter
@Setter
public class RDBLamdbdaConfig extends LambdaConfig {
    private String dbInstanceType;

    private String nonExistedDim;

    private String mappings;

    private String partitionedBy;

    private String partitionFormat;

    private List<Table> tables;

    private String dimColumns;

    @SneakyThrows
    protected RDBLamdbdaConfig(SupportedDBType dbType, LambdaRawConfig lambdaRawConfig) {
        super(dbType, lambdaRawConfig);
        BeanUtils.copyProperties(this, lambdaRawConfig);
    }

    public List<Table> getTables() {
        if (null == mappings) return null;
        if (null == tables) {
            //t1:table1,t2:table2
            tables = Arrays.stream(mappings.split(",")).map(
                    tableMapping -> {
                        return new Table(tableMapping.split(":")[0], tableMapping.split(":")[1].toUpperCase());
                    }
            ).collect(Collectors.toList());
        }
        return tables;
    }

    @Setter
    @Getter
    public static class Table {
        private String alias;
        private String tableName;

        private List<Column> columns;

        public Table(String alias, String tableName) {
            this.alias = alias;
            this.tableName = tableName;
        }

        public void markDimColumns(String[] dimColumns) {
            if (!CollectionUtils.isEmpty(columns)) {
                for (String columnName : dimColumns)
                    for (Column c : columns) {
                        if (StringUtils.equals(columnName, c.columnName))
                            c.setDimColumn(true);
                        else
                            c.setDimColumn(false);
                    }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Table table = (Table) o;

            return new EqualsBuilder().append(alias, table.alias).append(tableName, table.tableName).append(columns, table.columns).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(alias).append(tableName).append(columns).toHashCode();
        }

        @Override
        public String toString() {
            return "Table{" +
                    "alias='" + alias + '\'' +
                    ", tableName='" + tableName + '\'' +
                    ", columns=" + columns +
                    '}';
        }
    }

    @Getter
    @Setter
    public static class Column {
        private String columnName;
        private ColumnType columnType;
        private boolean isDimColumn;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Column column = (Column) o;

            return new EqualsBuilder().append(columnName, column.columnName).append(columnType, column.columnType).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(columnName).append(columnType).toHashCode();
        }

        @Override
        public String toString() {
            return "Column{" +
                    "columnName='" + columnName + '\'' +
                    ", columnType=" + columnType +
                    '}';
        }
    }

    public static enum ColumnType {
        INT,
        VARCHAR;

        protected static ColumnType of(String columnType) {
            if (columnType.startsWith("int")) return ColumnType.INT;

            if (columnType.startsWith("varchar")) return ColumnType.VARCHAR;

            return null;
        }
    }

    protected static class ColumnMetaRowMapper implements RowMapper<Column> {

        @Override
        public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Column column = new Column();
            column.setColumnName(rs.getString("Field"));
            column.setColumnType(ColumnType.of(rs.getString("Type").toLowerCase()));
            return column;
        }
    }

}
