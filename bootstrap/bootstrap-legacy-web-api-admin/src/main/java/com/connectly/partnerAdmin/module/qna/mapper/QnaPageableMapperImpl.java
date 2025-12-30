package com.connectly.partnerAdmin.module.qna.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QnaPageableMapperImpl implements QnaPageableMapper{

    @Override
    public CustomPageable<FetchQnaResponse> toProductGroupDetailResponse(List<FetchQnaResponse> qnaResponses, Pageable pageable, long total) {
        Long lastDomainId = qnaResponses.isEmpty() ? null : qnaResponses.getLast().getQnaId();
        return new CustomPageable<>(qnaResponses, pageable, total, lastDomainId);
    }
}
