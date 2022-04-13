package com.limin.projects.datalambda.convert;

import com.limin.projects.datalambda.config.SupportedDBType;
import com.limin.projects.datalambda.indicator.RDBIndicator;
import com.limin.projects.datalambda.indicator.RDBIndicatorEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * usage of this class: RDBObjectToIndicatorEntity
 * created by limin @ 2022/4/8
 */
public class RDBObjectToIndicatorEntity extends ObjectToIndicatorEntity<RDBIndicatorEntity, RDBIndicator>{

}
