package com.ryuqq.setof.application.contentpage.port.in;

import java.util.Set;

/**
 * 전시 중인 콘텐츠 페이지 ID 목록 조회 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GetOnDisplayContentPageIdsUseCase {

    /**
     * 전시 중인 콘텐츠 페이지 ID 목록을 조회합니다.
     *
     * @return 전시 중인 콘텐츠 페이지 ID Set
     */
    Set<Long> execute();
}
