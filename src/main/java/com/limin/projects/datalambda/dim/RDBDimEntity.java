package com.limin.projects.datalambda.dim;

import com.limin.projects.datalambda.annotations.DimCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * usage of this class: RDBDimEntity
 * created by limin @ 2022/4/6
 */
public class RDBDimEntity extends AbstractDimEntity<RDBDimCode> {
    private List<RDBDimCode> rdbDimCodes;

    public RDBDimEntity(String code) {
        super(code);
    }

    @Override
    public void setDimCodes(List<RDBDimCode> dimCodes) {
        rdbDimCodes = dimCodes;
    }


    @Override
    public void addDimCode(RDBDimCode dimCode) {
        if(CollectionUtils.isEmpty(rdbDimCodes))
            rdbDimCodes = new ArrayList<>();

        rdbDimCodes.add(dimCode);
    }

    @Override
    public List<RDBDimCode> getDimCodes() {
        return rdbDimCodes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RDBDimEntity that = (RDBDimEntity) o;

        return new EqualsBuilder().append(rdbDimCodes, that.rdbDimCodes).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(rdbDimCodes).append(code).toHashCode();
    }

}
