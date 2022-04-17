package com.limin.projects.datalambda.indicator;

import com.limin.projects.datalambda.annotations.Indicator;
import com.limin.projects.datalambda.annotations.IndicatorEntity;
import com.limin.projects.datalambda.config.LambdaRawConfig;
import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * usage of this class: IndicatorService
 * created by limin @ 2022/4/7
 */
@Getter
@Setter
@Slf4j
public abstract class IndicatorService<T extends AbstractIndicatorEntity, P extends AbstractIndicator> {

    private final String indicatorPackage;
    private final SupportedDBType dbType;

    private static Map<SupportedDBType, AbstractIndicatorEntity.IndicatorEntityBuilder> indicatorEntityBuilders;
    private static Map<SupportedDBType, AbstractIndicator.IndicatorBuilder> indicatorBuilders;
    private static Map<SupportedDBType, IndicatorServiceBuilder> builders;
    protected Map<String, T> indicatorEntities = new HashMap<>();


    protected IndicatorService(LambdaRawConfig lambdaRawConfig) {
        indicatorPackage = lambdaRawConfig.getIndicatorPackage();
        dbType = SupportedDBType.of(lambdaRawConfig.getDbType(), lambdaRawConfig.getDbInstanceType());
    }

    static {
        indicatorEntityBuilders = new HashMap<>();
        indicatorBuilders = new HashMap<>();
        builders = new HashMap<>();
        indicatorEntityBuilders.put(SupportedDBType.SQL_MYSQL, AbstractIndicatorEntity.IndicatorEntityBuilder.rdbIndicatorBuilder());
        indicatorBuilders.put(SupportedDBType.SQL_MYSQL, AbstractIndicator.IndicatorBuilder.rdbIndicatorBuilder());
        builders.put(SupportedDBType.SQL_MYSQL, IndicatorServiceBuilder.rdbIndicatorServiceBuilder());
    }

    public static IndicatorServiceBuilder getBuilder(SupportedDBType dbType) {
        return builders.getOrDefault(dbType,null);
    }

    public void scanPackageAndBuildIndicatorEntities() {
        List<Class> indicatorEntityClasses = ReflectionUtils.scanPackageWithAnnotationAndFilterAbstract(indicatorPackage, IndicatorEntity.class);
        log.info("{} indicator entities are scanned, indicator entities: {}", indicatorEntityClasses.size(),indicatorEntityClasses);

        if(0 == indicatorEntityClasses.size()) {
            log.warn("no indicator entities are found under {}",indicatorPackage);
        }

        for (Class indicatorEntityClass : indicatorEntityClasses) {
            IndicatorEntity indicatorAnno = (IndicatorEntity) indicatorEntityClass.getDeclaredAnnotation(IndicatorEntity.class);
            if (StringUtils.isEmpty(indicatorAnno.code())) {
                throw new IndicatorEntityException(String.format("Indicator Entity: %s code is empty", indicatorEntityClass.getCanonicalName()));
            }

            String indicatorEntityCode = indicatorAnno.code();
            T indicatorEntity = (T) indicatorEntityBuilders.get(dbType).apply(indicatorEntityCode);
            indicatorEntities.put(indicatorEntityCode, indicatorEntity);

            List<Field> fields = ReflectionUtils.scanClassWithAnnotation(indicatorEntityClass, Indicator.class);
            for (Field f : fields) {
                String mapping = f.getAnnotation(Indicator.class).mapping();
                if (StringUtils.isEmpty(mapping)) {
                    throw new IndicatorCodeException(String.format("Indicator %s mapping is empty", f));
                }
                P indicator = (P) indicatorBuilders.get(dbType).apply(mapping);
                indicatorEntity.addIndicator(indicator);
            }

        }


    }

    protected static class IndicatorEntityException extends RuntimeException {
        public IndicatorEntityException(String m) {
            super(m);
        }
    }

    protected static class IndicatorCodeException extends RuntimeException {
        public IndicatorCodeException(String m) {
            super(m);
        }
    }

    public static interface IndicatorServiceBuilder extends Function<LambdaRawConfig, IndicatorService> {
        static IndicatorServiceBuilder rdbIndicatorServiceBuilder() {
            return (lambdaRawConfig) -> new RDBIndicatorService(lambdaRawConfig);
        }
    }

}
