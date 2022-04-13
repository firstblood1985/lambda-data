package com.limin.projects.datalambda.dim;

import com.limin.projects.datalambda.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * usage of this class: RDBDimCode
 * created by limin @ 2022/4/6
 */
@Getter
@Setter
public class RDBDimCode extends AbstractDimCode {
    private String columnName;


    public RDBDimCode(String mapping) {
        super(mapping);
        columnName = mapping.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RDBDimCode dimCode = (RDBDimCode) o;

        return new EqualsBuilder().append(columnName, dimCode.columnName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(columnName).toHashCode();
    }
}
