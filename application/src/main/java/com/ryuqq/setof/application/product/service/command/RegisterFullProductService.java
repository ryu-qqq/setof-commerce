package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.product.facade.ProductRegistrationFacade;
import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 전체 상품 등록 Service
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 등록
 *
 * <p>트랜잭션 경계를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterFullProductService implements RegisterFullProductUseCase {

    private final ProductRegistrationFacade registrationFacade;

    public RegisterFullProductService(ProductRegistrationFacade registrationFacade) {
        this.registrationFacade = registrationFacade;
    }

    /**
     * 상품 전체 등록
     *
     * @param command 전체 등록 Command
     * @return 생성된 ProductGroup ID
     */
    @Override
    @Transactional
    public Long registerFullProduct(RegisterFullProductCommand command) {
        return registrationFacade.registerAll(command);
    }
}
