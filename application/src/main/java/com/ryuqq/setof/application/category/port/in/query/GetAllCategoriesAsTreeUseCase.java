package com.ryuqq.setof.application.category.port.in.query;

import com.ryuqq.setof.application.category.dto.response.TreeCategoryResult;
import java.util.List;

/**
 * 전체 카테고리 트리 조회 UseCase.
 *
 * <p>모든 카테고리를 계층 구조(트리)로 조회합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetAllCategoriesAsTreeUseCase {

    /**
     * 전체 카테고리를 트리 구조로 조회합니다.
     *
     * @return 트리 구조 카테고리 목록
     */
    List<TreeCategoryResult> execute();
}
