package com.connectly.partnerAdmin.module.qna.mapper;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaPageableMapper {

    CustomPageable<FetchQnaResponse> toProductGroupDetailResponse(List<FetchQnaResponse> qnaResponses, Pageable pageable, long total);

}
