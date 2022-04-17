package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import lombok.*;

/**
 * usage of this class: Province
 * created by limin @ 2022/4/3
 */
@Getter
@Setter
@DimEntity(code = "Province")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Province extends Location{

    @DimCode(mapping = "PROVINCE_CODE")
    private String code;

}
