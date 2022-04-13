package com.limin.projects.datalambda.example;

import com.limin.projects.datalambda.annotations.Indicator;
import com.limin.projects.datalambda.annotations.IndicatorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * usage of this class: Population
 * created by limin @ 2022/4/3
 */
@Getter
@Setter
@IndicatorEntity(code = "Population")
@NoArgsConstructor
@AllArgsConstructor
public class Population {

    @Indicator(mapping = "t1.NUMBER_OF_POPULATION")
    private Long numberOfPopulationYesterday;

    @Indicator(mapping = "t2.NUMBER_OF_PEOPLE_BORNED")
    private Long numberOfPeopleBornedDelta;

    @Indicator(mapping = "t1.NUMBER_OF_POPULATION_DECEASED")
    private Long numberOfPopulationDeceased;

    @Indicator(mapping = "t2.NUMBER_OF_PEOPLE_DIED")
    private Long numberOfPeopleDiedDelta;

    private Long numberOfPopulation;

}
