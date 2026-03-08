package com.ryuqq.setof.storage.legacy.qna.repository;

import com.ryuqq.setof.storage.legacy.qna.entity.LegacyQnaImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * LegacyQnaImageJpaRepository - 레거시 Q&A 이미지 JPA 레포지토리.
 *
 * <p>PER-REP-001: JpaRepository는 CUD 작업만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface LegacyQnaImageJpaRepository extends JpaRepository<LegacyQnaImageEntity, Long> {

    /**
     * Q&A ID로 이미지 전체 소프트 삭제.
     *
     * @param qnaId 레거시 Q&A ID
     */
    @Modifying
    @Query(
            "UPDATE LegacyQnaImageEntity e SET e.deleteYn = 'Y' WHERE e.qnaId = :qnaId AND"
                    + " e.deleteYn = 'N'")
    void softDeleteAllByQnaId(long qnaId);
}
