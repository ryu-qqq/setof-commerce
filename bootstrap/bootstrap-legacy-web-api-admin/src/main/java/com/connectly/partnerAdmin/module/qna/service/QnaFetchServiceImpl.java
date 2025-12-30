package com.connectly.partnerAdmin.module.qna.service;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.qna.dto.fetch.DetailQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.connectly.partnerAdmin.module.qna.exception.QnaNotFoundException;
import com.connectly.partnerAdmin.module.qna.mapper.QnaPageableMapper;
import com.connectly.partnerAdmin.module.qna.repository.QnaFetchRepository;
import com.connectly.partnerAdmin.module.utils.SecurityUtils;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class QnaFetchServiceImpl implements QnaFetchService {

    private final QnaFetchRepository qnaFetchRepository;
    private final QnaPageableMapper qnaPageableMapper;

    @Override
    public Qna fetchQnaEntity(long qnaId) {
        return qnaFetchRepository.fetchQnaEntity(qnaId, SecurityUtils.currentSellerIdOpt())
                .orElseThrow(QnaNotFoundException::new);
    }

    @Override
    public DetailQnaResponse fetchQna(long qnaId) {
        QnaType qnaType = qnaFetchRepository.fetchQnaType(qnaId, SecurityUtils.currentSellerIdOpt()).orElseThrow(QnaNotFoundException::new);
        if(qnaType.isProductQna()) return   qnaFetchRepository.fetchProductQna(qnaId, SecurityUtils.currentSellerIdOpt()).orElseThrow(QnaNotFoundException::new);
        else return  qnaFetchRepository.fetchOrderQna(qnaId, SecurityUtils.currentSellerIdOpt()).orElseThrow(QnaNotFoundException::new);
    }

    @Override
    public CustomPageable<FetchQnaResponse> fetchQnas(QnaFilter filter, Pageable pageable) {
        List<FetchQnaResponse> results = qnaFetchRepository.fetchQnas(filter, pageable);
        long totalCount = fetchQnaProductCountQuery(filter);
        return qnaPageableMapper.toProductGroupDetailResponse(results, pageable, totalCount);
    }

    private long fetchQnaProductCountQuery(QnaFilter filter){
        JPAQuery<Long> longJPAQuery = qnaFetchRepository.fetchQnaCountQuery(filter);
        Long totalCount = longJPAQuery.fetchOne();
        if(totalCount == null) return 0L;
        return totalCount;
    }


}
