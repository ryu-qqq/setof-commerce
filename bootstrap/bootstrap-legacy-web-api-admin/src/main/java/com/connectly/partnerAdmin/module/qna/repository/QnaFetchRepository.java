package com.connectly.partnerAdmin.module.qna.repository;

import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.qna.dto.fetch.DetailQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.QnaCountResponse;
import com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QnaFetchRepository {

    Optional<Qna> fetchQnaEntity(long qnaId, Optional<Long> sellerIdOpt);
    Optional<QnaType> fetchQnaType(long qnaId, Optional<Long> sellerIdOpt);
    Optional<DetailQnaResponse> fetchProductQna(long qnaId, Optional<Long> sellerIdOpt);
    Optional<DetailQnaResponse> fetchOrderQna(long qnaId, Optional<Long> sellerIdOpt);
    JPAQuery<Long> fetchQnaCountQuery(QnaFilter filter);
    List<FetchQnaResponse> fetchQnas(QnaFilter filter, Pageable pageable);
    Optional<QnaCountResponse> fetchTodayQnaCountQuery(BaseRoleFilter filter);

}
