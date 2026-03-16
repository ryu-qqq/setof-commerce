package com.ryuqq.setof.application.contentpage.port.out;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;

/**
 * ContentPageCommandPort - 콘텐츠 페이지 저장 Port-Out.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface ContentPageCommandPort {

    /**
     * 콘텐츠 페이지를 저장하고 생성된 ID를 반환합니다.
     *
     * @param contentPage 저장할 콘텐츠 페이지
     * @return 생성된 콘텐츠 페이지 ID
     */
    Long persist(ContentPage contentPage);
}
