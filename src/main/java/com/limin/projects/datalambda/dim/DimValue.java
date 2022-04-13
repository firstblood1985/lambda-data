package com.limin.projects.datalambda.dim;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.service.RawQueryResult;
import com.limin.projects.datalambda.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * usage of this class: AbstractDimValue
 * created by limin @ 2022/4/8
 */
@Getter
@Setter
public class DimValue<T extends AbstractDimEntity, P extends AbstractDimCode> {

    private final T dimEntity;

    private final Object dimInstance;

    private Map<P, String> dimCodeToValue = new HashMap<>();



    public DimValue(T dimEntity, Object dimInstance) {
        this.dimInstance = dimInstance;
        this.dimEntity = dimEntity;
        List<P> dimCodes = dimEntity.getDimCodes();
        List<Field> fields = ReflectionUtils.scanClassWithAnnotation(dimInstance.getClass(), DimCode.class);

        for (P dimCode : dimCodes) {
            Field f = fields.stream()
                    .filter(field -> StringUtils.equals(field.getAnnotation(DimCode.class).mapping(),dimCode.getMapping()))
                    .findAny().get();
                    String value = "";
                    try {
                        f.setAccessible(true);
                        value = f.get(dimInstance).toString();
                    } catch (IllegalAccessException e) {
                    }
                    dimCodeToValue.put(dimCode, value);
        }
    }

    public boolean match(RawQueryResult rawQueryResult){
        for (Map.Entry<P,String> e:dimCodeToValue.entrySet()){
           if(rawQueryResult.getRawResults().containsKey(e.getKey().getMapping()))
           {
              if(rawQueryResult.getRawResults().get(e.getKey().getMapping()).equals(e.getValue()))
                  return true;
              else
                  return false;
           }else
               return false;
        }
        return true;
    }

    public boolean matchDimInstance(Object dimInstance){
        return this.dimInstance.equals(dimInstance);
    }

    @Getter
    @AllArgsConstructor
    public static class DimValueGroup<T extends AbstractDimEntity,P extends AbstractDimCode>{

        private T dimEntity;
        private List<DimValue<T,P>> dimValues;

        public static <T extends AbstractDimEntity,P extends AbstractDimCode> List<DimValueGroup<T,P>> of(List<DimValue<T,P>> dimValues)
        {
            Map<T,List<DimValue<T,P>>> dimEntityMap = new HashMap<>();
            for(DimValue<T,P> dimValue:dimValues)
            {
                if(dimEntityMap.getOrDefault(dimValue.getDimEntity(),null) == null)
                {
                    dimEntityMap.put(dimValue.getDimEntity(),new ArrayList<DimValue<T, P>>(Collections.singletonList(dimValue)));
                }else{
                    dimEntityMap.get(dimValue.getDimEntity()).add(dimValue);
                }
            }

            return dimEntityMap.entrySet().stream().map(e-> new DimValueGroup<T,P>(e.getKey(),e.getValue())).collect(Collectors.toList());
        }
    }
}
