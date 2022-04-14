package com.limin.projects.datalambda.dim;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * usage of this class: DimCodeService
 * created by limin @ 2022/4/6
 */
@Getter
@Setter
public abstract class DimService<T extends AbstractDimEntity,P extends AbstractDimCode>{
    private final String dimPackage;
    private final SupportedDBType dbType;
    private static Map<SupportedDBType, AbstractDimEntity.DimEntityBuilder> dimEntityBuilders;
    private static Map<SupportedDBType, AbstractDimCode.DimCodeBuilder> dimCodeBuilders;
    private static Map<SupportedDBType,DimServiceBuilder> dimServiceBuilders;

    protected Map<String,T> dimEntities = new HashMap<>();

    protected DimService(LambdaRawConfig lambdaRawConfig) {
        dimPackage = lambdaRawConfig.getDimPackage();
        dbType = SupportedDBType.of(lambdaRawConfig.getDbType(), lambdaRawConfig.getDbInstanceType());
    }
    static {
        dimEntityBuilders = new HashMap<>();
        dimEntityBuilders.put(SupportedDBType.SQL_MYSQL, AbstractDimEntity.DimEntityBuilder.rdbDimEntityBuiler());

        dimCodeBuilders = new HashMap<>();
        dimCodeBuilders.put(SupportedDBType.SQL_MYSQL,AbstractDimCode.DimCodeBuilder.rdbDimCodeBuilder());

        dimServiceBuilders = new HashMap<>();
        dimServiceBuilders.put(SupportedDBType.SQL_MYSQL,DimServiceBuilder.rdbDimServiceBuiler());
    }

    public static DimServiceBuilder getDimServiceBuilder(SupportedDBType dbType){
        return dimServiceBuilders.getOrDefault(dbType,null);
    }

    public void scanPackageAndBuildDimEntities() {
        List<Class> dimEntityClasses = ReflectionUtils.scanPackageWithAnnotationAndFilterAbstract(dimPackage, DimEntity.class);

        for(Class dimEntityClass:dimEntityClasses)
        {
            DimEntity dimEntityAnno = (DimEntity) dimEntityClass.getDeclaredAnnotation(DimEntity.class);
            if(StringUtils.isEmpty(dimEntityAnno.code()))
            {
                throw new DimEntityException(String.format("DimEntity: %s value is empty",dimEntityClass.getCanonicalName()));
            }
            String dimEntityCode = dimEntityAnno.code();

            T dimEntity = (T) dimEntityBuilders.get(dbType).apply(dimEntityCode);
            dimEntities.put(dimEntityCode,dimEntity);
            // build dim code
            List<Field> fields = ReflectionUtils.scanClassWithAnnotation(dimEntityClass,DimCode.class);

            for(Field field:fields)
            {
                DimCode dimCodeAnno = field.getAnnotation(DimCode.class);
                String mapping = dimCodeAnno.mapping();
                DimCodeType dimCodeType = dimCodeAnno.dimType();
                LambdaComparator comparator = dimCodeAnno.comparator();
                if(StringUtils.isEmpty(mapping ))
                {
                    throw new DimCodeException(String.format("DimCode %s mapping value is empty",field));
                }
                P dimCode = (P) dimCodeBuilders.get(dbType).apply(mapping,dimCodeType,comparator );
                dimEntity.addDimCode(dimCode);
            }
        }
    }

    public static class DimEntityException extends RuntimeException{
        public DimEntityException(String message) {
            super(message);
        }
    }

    public static class DimCodeException extends RuntimeException{
        public DimCodeException(String message) {
            super(message);
        }
    }

    public static interface DimServiceBuilder extends Function<LambdaRawConfig,DimService> {

        static DimServiceBuilder rdbDimServiceBuiler(){
            return (lambdaRawConfig -> new RDBDimService(lambdaRawConfig));
        };

    }
}
