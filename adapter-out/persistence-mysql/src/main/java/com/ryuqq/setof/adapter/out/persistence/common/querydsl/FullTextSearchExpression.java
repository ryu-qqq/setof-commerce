package com.ryuqq.setof.adapter.out.persistence.common.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

/**
 * MySQL Full-Text Search Expression for QueryDSL
 *
 * <p>MySQL의 MATCH AGAINST 구문을 QueryDSL에서 사용할 수 있도록 지원합니다.
 *
 * <p><strong>사전 요구사항:</strong>
 *
 * <ul>
 *   <li>대상 컬럼에 FULLTEXT 인덱스가 생성되어 있어야 합니다
 *   <li>MySQL 5.6+ (InnoDB FULLTEXT 지원)
 * </ul>
 *
 * <p><strong>인덱스 생성 예시:</strong>
 *
 * <pre>{@code
 * ALTER TABLE product_group ADD FULLTEXT INDEX idx_product_group_name_ft (name);
 * }</pre>
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * // Natural Language Mode (기본)
 * builder.and(FullTextSearchExpression.matchAgainst(PRODUCT_GROUP.name, "티셔츠"));
 *
 * // Boolean Mode (와일드카드, +/- 연산자 지원)
 * builder.and(FullTextSearchExpression.matchAgainstBoolean(PRODUCT_GROUP.name, "+티셔츠*"));
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
public final class FullTextSearchExpression {

    private FullTextSearchExpression() {
        // Utility class
    }

    /**
     * MATCH AGAINST (Natural Language Mode)
     *
     * <p>자연어 검색 모드로, MySQL이 자동으로 관련성 점수를 계산합니다. 불용어(stopwords)가 자동으로 제외됩니다.
     *
     * @param column 검색 대상 컬럼
     * @param keyword 검색 키워드
     * @return BooleanExpression
     */
    public static BooleanExpression matchAgainst(StringPath column, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return Expressions.booleanTemplate(
                "MATCH({0}) AGAINST({1} IN NATURAL LANGUAGE MODE)", column, keyword.trim());
    }

    /**
     * MATCH AGAINST (Boolean Mode)
     *
     * <p>불리언 검색 모드로, 와일드카드(*)와 연산자(+, -, >, <, ~, ())를 지원합니다.
     *
     * <p><strong>연산자 예시:</strong>
     *
     * <ul>
     *   <li>+word: 반드시 포함
     *   <li>-word: 반드시 제외
     *   <li>word*: 와일드카드 (word로 시작하는 모든 단어)
     *   <li>"exact phrase": 정확한 구문 매칭
     * </ul>
     *
     * @param column 검색 대상 컬럼
     * @param keyword 검색 키워드 (연산자 포함 가능)
     * @return BooleanExpression
     */
    public static BooleanExpression matchAgainstBoolean(StringPath column, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return Expressions.booleanTemplate(
                "MATCH({0}) AGAINST({1} IN BOOLEAN MODE)", column, keyword.trim());
    }

    /**
     * MATCH AGAINST (Boolean Mode) with Wildcard
     *
     * <p>키워드 끝에 자동으로 와일드카드(*)를 추가합니다. 부분 일치 검색에 유용합니다.
     *
     * @param column 검색 대상 컬럼
     * @param keyword 검색 키워드
     * @return BooleanExpression
     */
    public static BooleanExpression matchAgainstWithWildcard(StringPath column, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        String wildcardKeyword = keyword.trim() + "*";
        return Expressions.booleanTemplate(
                "MATCH({0}) AGAINST({1} IN BOOLEAN MODE)", column, wildcardKeyword);
    }

    /**
     * MATCH AGAINST 관련성 점수 반환
     *
     * <p>검색 결과의 관련성 점수를 반환합니다. ORDER BY 절에서 사용할 수 있습니다.
     *
     * @param column 검색 대상 컬럼
     * @param keyword 검색 키워드
     * @return NumberExpression (관련성 점수)
     */
    public static com.querydsl.core.types.dsl.NumberExpression<Double> matchAgainstScore(
            StringPath column, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Expressions.asNumber(0.0);
        }
        return Expressions.numberTemplate(
                Double.class,
                "MATCH({0}) AGAINST({1} IN NATURAL LANGUAGE MODE)",
                column,
                keyword.trim());
    }
}
