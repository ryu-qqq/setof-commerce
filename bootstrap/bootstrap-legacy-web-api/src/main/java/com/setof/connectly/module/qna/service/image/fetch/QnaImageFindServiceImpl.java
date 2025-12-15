package com.setof.connectly.module.qna.service.image.fetch;


import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;
import com.setof.connectly.module.qna.repository.image.fetch.QnaImageFindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaImageFindServiceImpl implements QnaImageFindService{

    private final QnaImageFindRepository qnaImageFindRepository;

    @Override
    public List<QnaImage> fetchQnaImageEntities(QnaIssueType qnaIssueType, long qnaAnswerId) {
        return qnaImageFindRepository.fetchQnaImageEntities(qnaIssueType, qnaAnswerId);
    }

    @Override
    public List<QnaImage> fetchQnaImageEntitiesByQnaId(long qnaId) {
        return qnaImageFindRepository.fetchQnaImageEntitiesByQnaId(qnaId);
    }

    @Override
    public List<QnaImage> fetchQnaImageEntitiesByQnaId(List<Long> qnaIds) {
        return qnaImageFindRepository.fetchQnaImageEntitiesByQnaId(qnaIds);
    }

}
