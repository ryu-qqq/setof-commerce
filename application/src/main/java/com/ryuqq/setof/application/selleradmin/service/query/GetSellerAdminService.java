package com.ryuqq.setof.application.selleradmin.service.query;

import com.ryuqq.setof.application.selleradmin.assembler.SellerAdminAssembler;
import com.ryuqq.setof.application.selleradmin.dto.query.GetSellerAdminQuery;
import com.ryuqq.setof.application.selleradmin.dto.response.SellerAdminResult;
import com.ryuqq.setof.application.selleradmin.manager.SellerAdminReadManager;
import com.ryuqq.setof.application.selleradmin.port.in.query.GetSellerAdminUseCase;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.selleradmin.aggregate.SellerAdmin;
import com.ryuqq.setof.domain.selleradmin.id.SellerAdminId;
import org.springframework.stereotype.Service;

/**
 * GetSellerAdminService - 셀러 관리자 상세 조회 Service.
 *
 * <p>승인된 관리자 정보를 조회합니다 (ACTIVE, INACTIVE, SUSPENDED).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetSellerAdminService implements GetSellerAdminUseCase {

    private final SellerAdminReadManager readManager;
    private final SellerAdminAssembler assembler;

    public GetSellerAdminService(
            SellerAdminReadManager readManager, SellerAdminAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public SellerAdminResult execute(GetSellerAdminQuery query) {
        SellerId sellerId = SellerId.of(query.sellerId());
        SellerAdminId sellerAdminId = SellerAdminId.of(query.sellerAdminId());

        SellerAdmin sellerAdmin =
                readManager.getBySellerIdAndIdAndStatuses(
                        sellerId, sellerAdminId, query.statuses());

        return assembler.toAdminResult(sellerAdmin);
    }
}
