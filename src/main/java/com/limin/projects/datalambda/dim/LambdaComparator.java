package com.limin.projects.datalambda.dim;

import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.function.Function;

@AllArgsConstructor
public enum LambdaComparator {
    STRINGCOMPARATOR(Comparator.comparing(String::toString),String.class,Caster.stringCaster()),
    LONGCOMPARATOR(Comparator.comparing(Long::longValue),Long.class,Caster.longCaster());

    private Comparator comparator;

    private Class clazz;

    private Caster caster;

    public int compare(String o1,String o2)
    {
        return comparator.compare(clazz.cast(caster.apply(o1)),clazz.cast(caster.apply(o2)));
    }

    static interface Caster extends Function<String, Object> {
        static Caster longCaster(){
            return  v  ->  Long.valueOf(v);
        }
        static Caster stringCaster(){
            return v -> v;
        }
    }

}
