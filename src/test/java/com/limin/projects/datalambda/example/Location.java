package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.DimEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * usage of this class: Location
 * created by limin @ 2022/4/3
 */
@Getter
@Setter
@DimEntity
public abstract class Location {

//    @DimCode
    protected String locationCode;

    protected String name;

}
