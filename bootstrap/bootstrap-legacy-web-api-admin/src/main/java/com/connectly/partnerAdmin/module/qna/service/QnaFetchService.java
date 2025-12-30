package com.connectly.partnerAdmin.module.qna.service;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.qna.dto.fetch.DetailQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import org.springframework.data.domain.Pageable;

public interface QnaFetchService {

    Qna fetchQnaEntity(long qnaId);
    DetailQnaResponse fetchQna(long qnaId);
    CustomPageable<FetchQnaResponse> fetchQnas(QnaFilter filterDto, Pageable pageable);

}
