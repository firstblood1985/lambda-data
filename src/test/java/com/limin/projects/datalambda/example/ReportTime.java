package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.DimCode;
import com.limin.projects.datalambda.annotations.DimEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * usage of this class: ReportTime
 * created by limin @ 2022/4/10
 */
@Getter
@Setter
@AllArgsConstructor
@DimEntity(code = "ReportTime")
@ToString
public class ReportTime {

    @DimCode(mapping = "REPORT_TIME")
    private String reportTime;
}
