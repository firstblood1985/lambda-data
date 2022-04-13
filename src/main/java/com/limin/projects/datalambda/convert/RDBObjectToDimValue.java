package com.limin.projects.datalambda.convert;

import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.dim.RDBDimCode;
import com.limin.projects.datalambda.dim.RDBDimEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * usage of this class: RDBObjectToDimEntity
 * created by limin @ 2022/4/7
 */
//@Component("RDBObjectDimConverter")
public class RDBObjectToDimValue extends ObjectToDimValue<RDBDimEntity, RDBDimCode> {

}
