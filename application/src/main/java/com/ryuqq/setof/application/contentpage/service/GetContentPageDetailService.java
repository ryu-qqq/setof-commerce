package com.ryuqq.setof.application.contentpage.service;

import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.facade.ContentPageDetailReadFacade;
import com.ryuqq.setof.application.contentpage.port.in.GetContentPageDetailUseCase;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import org.springframework.stereotype.Service;

/**
 * GetContentPageDetailService - 콘텐츠 페이지 상세 조회 서비스.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetContentPageDetailService implements GetContentPageDetailUseCase {

    private final ContentPageDetailReadFacade readFacade;

    public GetContentPageDetailService(ContentPageDetailReadFacade readFacade) {
        this.readFacade = readFacade;
    }

    @Override
    public ContentPageDetailResult execute(ContentPageSearchCriteria criteria) {
        return readFacade.getContentPageDetail(criteria);
    }
}
