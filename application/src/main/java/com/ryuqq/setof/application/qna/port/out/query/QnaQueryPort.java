package com.ryuqq.setof.application.qna.port.out.query;

import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.query.criteria.QnaSearchCriteria;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import java.util.List;
import java.util.Optional;

/**
 * QnA Query Port (Query)
 *
 * <p>QnA Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface QnaQueryPort {

    /**
     * ID로 QnA 단건 조회
     *
     * @param id QnA ID (Value Object)
     * @return QnA Domain (Optional)
     */
    Optional<Qna> findById(QnaId id);

    /**
     * 검색 조건으로 QnA 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 목록
     */
    List<Qna> findByCriteria(QnaSearchCriteria criteria);

    /**
     * 검색 조건에 맞는 QnA 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return QnA 총 개수
     */
    long countByCriteria(QnaSearchCriteria criteria);

    /**
     * QnA ID 존재 여부 확인
     *
     * @param id QnA ID
     * @return 존재 여부
     */
    boolean existsById(QnaId id);
}
