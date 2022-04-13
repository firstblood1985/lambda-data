package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.annotations.Indicator;
import com.limin.projects.datalambda.service.RawQueryResult;
import com.limin.projects.datalambda.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * usage of this class: IndicatorValue
 * created by limin @ 2022/4/12
 */
@Getter
@Setter
public abstract class IndicatorValue<T extends AbstractIndicatorEntity, P extends AbstractIndicator> {

    protected final T indicatorEntity;
    protected final RawQueryResult rawQueryResult;
    protected Map<P, String> indicatorValue = new HashMap<>();

    public IndicatorValue(T indicatorEntity, RawQueryResult rawQueryResult) {
        this.indicatorEntity = indicatorEntity;
        this.rawQueryResult = rawQueryResult;
        retrieveResult();
    }

    protected abstract void retrieveResult();

    private void writeBack(Object o,Field f, String value)
    {
        f.setAccessible(true);
        try {
            if (f.getType() == Long.class)
                f.set(o, Long.valueOf(value));

            if(f.getType() == Integer.class)
                f.set(o,Integer.valueOf(value));

            if(f.getType() == String.class)
                f.set(o,value);

        }catch (IllegalAccessException e)
        {
            //TODO: for value type not matched, should throw
        }
    }

    public void writeBack(Object indicatorResult) {
        List<Field> indicatorFields = ReflectionUtils.scanClassWithAnnotation(indicatorResult.getClass(), Indicator.class);
        for (Map.Entry<P, String> e : indicatorValue.entrySet()) {
            if (!StringUtils.isEmpty(e.getValue())) {
                Optional<Field> optionalField = indicatorFields.stream().filter(f -> StringUtils.equals(f.getAnnotation(Indicator.class).mapping(), e.getKey().mapping)).findAny();
                if (optionalField.isPresent()) {
                    writeBack(indicatorResult,optionalField.get(),e.getValue());
                }
            }
        }
    }
}
