package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * usage of this class: RDBAbstractIndicatorCode
 * created by limin @ 2022/4/7
 */
@Getter
@Setter
public class RDBIndicator extends AbstractIndicator {
    private String tableAlias;
    private String columnName;

    protected RDBIndicator(String mapping) {
        super(mapping);
        tableAlias = mapping.split(CommonUtil.DOT_DELIMITER)[0];
        columnName = mapping.split(CommonUtil.DOT_DELIMITER)[1].toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RDBIndicator indicator = (RDBIndicator) o;

        return new EqualsBuilder().append(tableAlias, indicator.tableAlias).append(columnName, indicator.columnName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(tableAlias).append(columnName).toHashCode();
    }
}
