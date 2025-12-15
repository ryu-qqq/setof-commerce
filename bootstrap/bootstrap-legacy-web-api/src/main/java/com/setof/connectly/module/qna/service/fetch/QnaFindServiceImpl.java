package com.setof.connectly.module.qna.service.fetch;


import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.exception.qna.QnaNotFoundException;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.qna.dto.FetchQnaResponse;
import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.filter.QnaFilter;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaType;
import com.setof.connectly.module.qna.mapper.QnaMapper;
import com.setof.connectly.module.qna.mapper.QnaSliceMapper;
import com.setof.connectly.module.qna.repository.QnaFindRepository;
import com.setof.connectly.module.qna.service.image.fetch.QnaImageFindService;
import com.setof.connectly.module.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class QnaFindServiceImpl implements QnaFindService {
    private final QnaMapper qnaMapper;

    private final QnaSliceMapper qnaSliceMapper;
    private final QnaFindRepository qnaFindRepository;
    private final QnaImageFindService qnaImageFindService;

    @Override
    public Page<QnaResponse> fetchProductQuestions(long productGroupId, Pageable pageable){
        List<Long> qnaIds = qnaFindRepository.fetchQnaProductIds(productGroupId, pageable);
        List<QnaResponse> qnaResponses = qnaMapper.toQnaList(fetchQnas(QnaType.PRODUCT, qnaIds));
        return PageableExecutionUtils.getPage(qnaResponses, pageable, () -> qnaFindRepository.fetchQnaCountQuery(productGroupId).fetchCount());
    }

    @Override
    public List<QnaResponse> fetchQnas(QnaType qnaType, List<Long> qnaIds) {
        List<QnaResponse> qnaResponses = qnaFindRepository.fetchQnas(qnaIds);

        List<QnaImage> qnaImages = new ArrayList<>();
        if(qnaType.isOrderQna()){
            qnaImages = qnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaIds);
        }

        return qnaMapper.toQnaResponse(qnaResponses, qnaImages);
    }


    @Override
    public Qna fetchQnaEntity(long qnaId) {
        return qnaFindRepository.fetchQnaEntity(qnaId, SecurityUtils.currentUserId())
                .orElseThrow(() -> new QnaNotFoundException(qnaId));
    }

    @Override
    public QnaStatus fetchQnaStatus(long qnaId) {
        return qnaFindRepository.fetchQnaStatus(qnaId)
                .orElseThrow(() -> new QnaNotFoundException(qnaId));
    }

    @Override
    public CustomSlice<QnaResponse> fetchMyQnas(QnaFilter qnaFilter, Pageable pageable){
        long userId = SecurityUtils.currentUserId();
        List<QnaResponse> qnaResponses = qnaFindRepository.fetchMyQnas(userId, qnaFilter, pageable);

        List<QnaImage> qnaImages;

        List<Long> qnaIds = qnaResponses.stream()
                .map(QnaResponse::getQna)
                .map(FetchQnaResponse::getQnaId)
                .collect(Collectors.toList());

        if(qnaFilter.getQnaType().isOrderQna()){
            qnaImages = qnaImageFindService.fetchQnaImageEntitiesByQnaId(qnaIds);
            qnaResponses = qnaMapper.toQnaResponse(qnaResponses, qnaImages);
        }

        List<QnaResponse> results = qnaMapper.toQnaList(qnaResponses);

        return qnaSliceMapper.toSlice(results, pageable);
    }

    @Override
    public QnaSheet fetchQnaSheet(long qnaId, QnaType qnaType) {
        if(qnaType.isOrderQna()) return qnaFindRepository.fetchOrderQnaSheet(qnaId).orElseThrow(() -> new QnaNotFoundException(qnaId));
        return qnaFindRepository.fetchProductQnaSheet(qnaId).orElseThrow(() -> new QnaNotFoundException(qnaId));

    }

}
