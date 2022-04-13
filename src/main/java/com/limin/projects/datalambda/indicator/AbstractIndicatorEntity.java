package com.limin.projects.datalambda.indicator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

/**
 * usage of this class: IndicatorEntity
 * created by limin @ 2022/4/6
 */
@Getter
@Setter
public abstract class AbstractIndicatorEntity<T extends AbstractIndicator> {
    protected final String code;

    protected Object indicatorInstance;

    protected AbstractIndicatorEntity(String code) {
        this.code = code;
    }
    public abstract void setIndicators(List<T> indicators);
    public abstract void addIndicator(T indicator);
    public abstract List<T> getIndicators();

    public static interface IndicatorEntityBuilder extends Function<String, AbstractIndicatorEntity> {

        static IndicatorEntityBuilder rdbIndicatorBuilder(){
            return code -> {
                return new RDBIndicatorEntity(code);
            };
        };
    }
}
