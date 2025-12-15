package com.setof.connectly.module.qna.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.filter.QnaFilter;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.enums.QnaStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QnaFindRepository {

    List<Long> fetchQnaProductIds(long productGroupId, Pageable pageable);
    JPAQuery<Long> fetchQnaCountQuery(long productGroupId);
    List<QnaResponse> fetchQnas(List<Long> qnaIds);
    Optional<Qna> fetchQnaEntity(long qnaId, long userId);
    Optional<QnaStatus> fetchQnaStatus(long qnaId);
    List<QnaResponse> fetchMyQnas(long userId, QnaFilter filter, Pageable pageable);
    Optional<QnaSheet> fetchOrderQnaSheet(long qnaId);
    Optional<QnaSheet> fetchProductQnaSheet(long qnaId);


}
