package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.GetAdminProductGroupUseCase;
import org.springframework.stereotype.Service;

/**
 * GetAdminProductGroupService - Admin 상품그룹 상세 조회 Service.
 *
 * <p>ReadFacade에서 번들을 조회하고, Assembler를 통해 최종 Composite 결과를 조립합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Service
public class GetAdminProductGroupService implements GetAdminProductGroupUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupAssembler assembler;

    public GetAdminProductGroupService(
            ProductGroupReadFacade readFacade, ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.assembler = assembler;
    }

    @Override
    public ProductGroupDetailCompositeResult execute(Long productGroupId) {
        ProductGroupDetailBundle bundle = readFacade.getProductGroupDetailBundle(productGroupId);
        return assembler.toDetailResult(bundle);
    }
}
