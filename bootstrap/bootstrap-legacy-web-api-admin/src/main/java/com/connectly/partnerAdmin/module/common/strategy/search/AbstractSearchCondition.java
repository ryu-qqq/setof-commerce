package com.connectly.partnerAdmin.module.common.strategy.search;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract class AbstractSearchCondition implements SearchCondition{

    @Override
    public BooleanExpression apply(String searchWord, Path<?> path) {
        return null;
    }


    protected List<Long> splitWords(String searchWord){
        return Arrays.stream(searchWord.split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
