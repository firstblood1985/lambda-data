package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * usage of this class: ReportDate
 * created by limin @ 2022/4/10
 */
@Getter
@Setter
@AllArgsConstructor
@DimEntity(code = "ReportDate")
public class ReportDate {

    @DimCode(mapping = "REPORT_DATE")
    private String reportDate;
}
