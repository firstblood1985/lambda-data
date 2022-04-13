package com.limin.projects.datalambda.convert;

import com.limin.projects.datalambda.annotations.DimEntity;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.dim.AbstractDimCode;
import com.limin.projects.datalambda.dim.AbstractDimEntity;
import com.limin.projects.datalambda.dim.DimService;
import com.limin.projects.datalambda.dim.DimValue;
import com.limin.projects.datalambda.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * usage of this class: ObjectToDimEntity
 * created by limin @ 2022/4/7
 */
@Getter
@Setter
public abstract class ObjectToDimValue<T extends AbstractDimEntity, P extends AbstractDimCode> {
    @Autowired
    protected DimService<T, P> dimService;

    private static Map<SupportedDBType,ObjectToDimValueBuilder> builders;


    static {
        builders = new HashMap<>();
        builders.put(SupportedDBType.SQL_MYSQL,ObjectToDimValueBuilder.rdbObjectToDimValueBuilder());
    }

    public static ObjectToDimValueBuilder getBuilder(SupportedDBType dbType) {
        return builders.getOrDefault(dbType,null);
    }

    public List<DimValue<T, P>> ObjectToDimValue(List<Object> objects) {
        return objects.stream().map(o -> ObjectToDimValue(o)).collect(Collectors.toList());
    }

    public DimValue<T, P> ObjectToDimValue(Object o) {
        if (!CommonUtil.isSomeEntity(o, DimEntity.class))
            throw new ObjectToDimEntityException(String.format("%s is not a dim entity class", o.getClass()));

        DimEntity dimEntityAnno = o.getClass().getAnnotation(DimEntity.class);

        T dimEntity = dimService.getDimEntities().get(dimEntityAnno.code());
        return new DimValue<T, P>(dimEntity, o);
    }

    protected static class ObjectToDimEntityException extends RuntimeException {
        public ObjectToDimEntityException(String message) {
            super(message);
        }
    }

    public static interface ObjectToDimValueBuilder extends Supplier<ObjectToDimValue>{
        static ObjectToDimValueBuilder rdbObjectToDimValueBuilder(){
            return () -> new RDBObjectToDimValue();
        };
    }


}
