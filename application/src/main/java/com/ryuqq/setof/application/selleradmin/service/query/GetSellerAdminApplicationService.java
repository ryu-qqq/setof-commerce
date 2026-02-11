package com.ryuqq.setof.application.selleradmin.service.query;

import com.ryuqq.setof.application.selleradmin.assembler.SellerAdminAssembler;
import com.ryuqq.setof.application.selleradmin.dto.query.GetSellerAdminApplicationQuery;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminApplicationResult;
import com.ryuqq.setof.application.selleradmin.manager.SellerAdminReadManager;
import com.ryuqq.setof.application.selleradmin.port.in.query.GetSellerAdminApplicationUseCase;
import com.ryuqq.setof.domain.selleradmin.aggregate.SellerAdmin;
import com.ryuqq.setof.domain.selleradmin.id.SellerAdminId;
import org.springframework.stereotype.Service;

/**
 * GetSellerAdminApplicationService - 셀러 관리자 가입 신청 상세 조회 Service.
 *
 * <p>가입 신청 상태의 상세 정보를 조회합니다 (PENDING_APPROVAL, REJECTED).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetSellerAdminApplicationService implements GetSellerAdminApplicationUseCase {

    private final SellerAdminReadManager readManager;
    private final SellerAdminAssembler assembler;

    public GetSellerAdminApplicationService(
            SellerAdminReadManager readManager, SellerAdminAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public SellerAdminApplicationResult execute(GetSellerAdminApplicationQuery query) {
        SellerAdminId sellerAdminId = SellerAdminId.of(query.sellerAdminId());

        SellerAdmin sellerAdmin = readManager.getByIdAndStatuses(sellerAdminId, query.statuses());

        return assembler.toResult(sellerAdmin);
    }
}
