package com.limin.projects.datalambda.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum SupportedDBType {
    SQL_MYSQL("sql","mysql","RDBObjectDimConverter","RDBObjectIndicatorConverter","RDBQueryService"),
    SQL_OB("sql","oceanbase","RDBObjectDimConverter","RDBObjectIndicatorConverter","RDBQueryService"),
    NOSQL_HBASE("nosql","hbase","RDBObjectDimConverter","RDBObjectIndicatorConverter","NOSQLQueryService");

    private String dbType;

    private String dbInstanceType;

    private String objectToDimConverter;

    private String objectToIndicatorConverter;

    private String queryService;
    
    public static SupportedDBType of(String dbType,String dbInstanceType)
    {
        Optional<SupportedDBType> supportedDBType = Arrays.stream(SupportedDBType.values())
                        .filter(x->x.dbType.equals(dbType) && x.dbInstanceType.equals(dbInstanceType)).findFirst();

        if(supportedDBType.isPresent())
            return supportedDBType.get();
        else
            throw new UnsupportedOperationException(String.format("unsupported dbType: %s, dbInstanceType: %s",dbType,dbInstanceType));

    }

    public static class UnSupportedDBTypeException extends RuntimeException{

        public UnSupportedDBTypeException(String errorMessage) {
            super(errorMessage);
        }
    }

}
