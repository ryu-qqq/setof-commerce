package com.ryuqq.setof.config.mysql;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;

/**
 * MySQL MATCH AGAINST 함수를 Hibernate 6 HQL 함수로 등록.
 *
 * <p>MySQL ngram FULLTEXT 인덱스를 사용한 검색을 QueryDSL에서 사용할 수 있도록 합니다.
 *
 * <p>사용: {@code function('match_against', column, '+searchWord*')}
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class MatchAgainstFunctionContributor implements FunctionContributor {

    @Override
    public void contributeFunctions(FunctionContributions functionContributions) {
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();

        functionRegistry.registerPattern(
                "match_against",
                "match(?1) against (?2 in boolean mode)",
                functionContributions
                        .getTypeConfiguration()
                        .getBasicTypeRegistry()
                        .resolve(StandardBasicTypes.DOUBLE));
    }
}
