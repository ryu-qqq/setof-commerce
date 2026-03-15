package com.ryuqq.setof.application.mileage.assembler;

import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryItemResult;
import com.ryuqq.setof.application.mileage.dto.response.MileageHistoryPageResult;
import com.ryuqq.setof.domain.common.vo.PageMeta;
import com.ryuqq.setof.domain.mileage.vo.MileageSummary;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * MileageAssembler - 마일리지 조회 결과 조립 컴포넌트.
 *
 * <p>APP-ASM-001: Assembler는 여러 결과 객체를 하나의 응답으로 조립하는 책임만 담당.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class MileageAssembler {

    /**
     * 마일리지 요약 + 이력 목록 + 페이지 메타 → MileageHistoryPageResult 조립.
     *
     * <p>이력 목록의 각 항목에 사유별 제목 자동 적용 (withReasonTitle).
     *
     * @param userId 사용자 ID
     * @param summary 마일리지 요약
     * @param histories 마일리지 이력 목록
     * @param page 현재 페이지 번호
     * @param size 페이지 크기
     * @param totalElements 전체 요소 개수
     * @return MileageHistoryPageResult
     */
    public MileageHistoryPageResult toPageResult(
            long userId,
            MileageSummary summary,
            List<MileageHistoryItemResult> histories,
            int page,
            int size,
            long totalElements) {
        List<MileageHistoryItemResult> titled =
                histories.stream().map(MileageHistoryItemResult::withReasonTitle).toList();
        PageMeta pageMeta = PageMeta.of(page, size, totalElements);
        return MileageHistoryPageResult.of(userId, summary, titled, pageMeta);
    }
}
