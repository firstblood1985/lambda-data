package com.limin.projects.datalambda.convert;

import com.limin.projects.datalambda.annotations.IndicatorEntity;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.indicator.AbstractIndicator;
import com.limin.projects.datalambda.indicator.AbstractIndicatorEntity;
import com.limin.projects.datalambda.indicator.IndicatorService;
import com.limin.projects.datalambda.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * usage of this class: ObjectToIndicatorEntity
 * created by limin @ 2022/4/8
 */
public abstract class ObjectToIndicatorEntity<T extends AbstractIndicatorEntity, P extends AbstractIndicator> {
    @Autowired
    private IndicatorService<T, P> indicatorService;

    private static Map<SupportedDBType,ObjectToIndicatorEntityBuilder> builders;

    static {
        builders = new HashMap<>();
        builders.put(SupportedDBType.SQL_MYSQL,ObjectToIndicatorEntityBuilder.rdbObjectToIndicatorEntityBuilder());
    }

    public static ObjectToIndicatorEntityBuilder getBuilder (SupportedDBType dbType) {
        return builders.getOrDefault(dbType, null);
    }



    public List<T> objectToIndicatorEntity(List<Object> objects) {
        return objects.stream().map(o -> objectToIndicatorEntity(o)).collect(Collectors.toList());
    }

    public T objectToIndicatorEntity(Object o) {
        if (!CommonUtil.isSomeEntity(o, IndicatorEntity.class)) {
            throw new ObjectToIndicatorEntityException(String.format("%s is not a indicator entity class", o.getClass()));
        }

        IndicatorEntity indicatorEntityAnno = o.getClass().getAnnotation(IndicatorEntity.class);

        T indicatorEntity =  indicatorService.getIndicatorEntities().get(indicatorEntityAnno.code());
        indicatorEntity.setIndicatorInstance(o);
        return indicatorEntity;
    }

    protected static class ObjectToIndicatorEntityException extends RuntimeException {
        public ObjectToIndicatorEntityException(String message) {
            super(message);
        }
    }

    public static interface ObjectToIndicatorEntityBuilder extends Supplier<ObjectToIndicatorEntity> {
        static ObjectToIndicatorEntityBuilder rdbObjectToIndicatorEntityBuilder(){
            return ()-> new RDBObjectToIndicatorEntity();
        }
    }
}
