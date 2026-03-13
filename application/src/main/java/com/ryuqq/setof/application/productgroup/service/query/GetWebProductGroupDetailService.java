package com.ryuqq.setof.application.productgroup.service.query;

import com.ryuqq.setof.application.productgroup.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupDetailBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.WebProductGroupDetailCompositeResult;
import com.ryuqq.setof.application.productgroup.internal.ProductGroupReadFacade;
import com.ryuqq.setof.application.productgroup.port.in.query.GetWebProductGroupDetailUseCase;
import org.springframework.stereotype.Service;

/**
 * GetWebProductGroupDetailService - 웹(사용자) 상품그룹 상세 조회 Service.
 *
 * <p>Admin과 동일한 번들 데이터를 조회하되, Assembler에서 사용자 관점으로 조립합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GetWebProductGroupDetailService implements GetWebProductGroupDetailUseCase {

    private final ProductGroupReadFacade readFacade;
    private final ProductGroupAssembler assembler;

    public GetWebProductGroupDetailService(
            ProductGroupReadFacade readFacade, ProductGroupAssembler assembler) {
        this.readFacade = readFacade;
        this.assembler = assembler;
    }

    @Override
    public WebProductGroupDetailCompositeResult execute(Long productGroupId) {
        ProductGroupDetailBundle bundle = readFacade.getProductGroupDetailBundle(productGroupId);
        return assembler.toWebDetailResult(bundle);
    }
}
