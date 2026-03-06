package com.ryuqq.setof.application.category.port.in;

import com.ryuqq.setof.application.category.dto.response.CategoryDisplayResult;
import java.util.List;

/**
 * 노출용 카테고리 트리 조회 UseCase.
 *
 * <p>Public API용 간단한 카테고리 목록 조회입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface GetCategoriesForDisplayUseCase {

    /**
     * 노출용 카테고리 트리를 조회합니다.
     *
     * @return 카테고리 트리 목록
     */
    List<CategoryDisplayResult> execute();
}
