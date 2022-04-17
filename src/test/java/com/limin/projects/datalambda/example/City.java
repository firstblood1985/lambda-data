package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import lombok.*;

/**
 * usage of this class: City
 * created by limin @ 2022/4/3
 */
@Getter
@Setter
@DimEntity(code="City")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City extends Location{

    @DimCode(mapping = "CITY_CODE")
    private String code;

}
