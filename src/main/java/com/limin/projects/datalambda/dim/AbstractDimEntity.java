package com.limin.projects.datalambda.dim;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

/**
 * usage of this class: DimEntity
 * created by limin @ 2022/4/6
 */
@Getter
@Setter
public abstract class AbstractDimEntity<T extends AbstractDimCode> {

    protected final String code;
    protected AbstractDimEntity(String code) {
        this.code = code;
    }

    public abstract void setDimCodes(List<T> dimCodes);
    public abstract void addDimCode(T dimCode);
    public abstract List<T> getDimCodes();
    public static interface DimEntityBuilder extends Function<String, AbstractDimEntity> {

        static DimEntityBuilder rdbDimEntityBuiler(){
            return code -> {
                return new RDBDimEntity(code);
            };
        }

    }
}
