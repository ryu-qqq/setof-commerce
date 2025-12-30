package com.ryuqq.setof.application.member.service.query;

import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.member.assembler.MemberAssembler;
import com.ryuqq.setof.application.member.dto.query.GetMembersQuery;
import com.ryuqq.setof.application.member.dto.response.MemberSummaryResponse;
import com.ryuqq.setof.application.member.manager.query.MemberReadManager;
import com.ryuqq.setof.application.member.port.in.query.GetMembersUseCase;
import com.ryuqq.setof.domain.member.aggregate.Member;
import com.ryuqq.setof.domain.member.query.criteria.MemberSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 회원 목록 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>Query → Criteria 변환
 *   <li>MemberReadManager로 회원 목록 및 카운트 조회
 *   <li>MemberAssembler로 Response DTO 변환
 *   <li>PageResponse 생성 및 반환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GetMembersService implements GetMembersUseCase {

    private final MemberReadManager memberReadManager;
    private final MemberAssembler memberAssembler;

    public GetMembersService(MemberReadManager memberReadManager, MemberAssembler memberAssembler) {
        this.memberReadManager = memberReadManager;
        this.memberAssembler = memberAssembler;
    }

    @Override
    public PageResponse<MemberSummaryResponse> execute(GetMembersQuery query) {
        MemberSearchCriteria criteria = createCriteria(query);

        List<Member> members = memberReadManager.findByCriteria(criteria);
        long totalCount = memberReadManager.countByCriteria(criteria);

        List<MemberSummaryResponse> content = toSummaryResponses(members);

        int totalPages = calculateTotalPages(totalCount, query.size());
        boolean isFirst = query.page() == 0;
        boolean isLast = query.page() >= totalPages - 1;

        return PageResponse.of(
                content, query.page(), query.size(), totalCount, totalPages, isFirst, isLast);
    }

    private MemberSearchCriteria createCriteria(GetMembersQuery query) {
        return MemberSearchCriteria.of(
                query.name(), query.phoneNumber(), query.status(), query.page(), query.size());
    }

    private List<MemberSummaryResponse> toSummaryResponses(List<Member> members) {
        return members.stream().map(memberAssembler::toSummaryResponse).toList();
    }

    private int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
