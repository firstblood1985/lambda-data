package com.limin.projects.datalambda.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * usage of this class: RDBRawQueryResult
 * created by limin @ 2022/4/12
 */
@Getter
@Setter
public class RawQueryResult {
    private Map<String,String> rawResults;

    public RawQueryResult(Map<String,Object> rs)
    {
       rawResults = new HashMap<>();
       rs.entrySet().forEach( e -> rawResults.put(e.getKey(),e.getValue().toString()) );
    }
}
