package com.ryuqq.setof.application.seller.service.query;

import com.ryuqq.setof.application.seller.manager.SellerCompositionReadManager;
import com.ryuqq.setof.application.seller.port.in.query.ValidateBusinessRegistrationUseCase;
import org.springframework.stereotype.Service;

/**
 * ValidateBusinessRegistrationService - 사업자등록번호 유효성 검증 Service.
 *
 * <p>SellerCompositionReadManager를 통해 사업자등록번호 중복 여부를 확인합니다.
 *
 * <p>APP-SVC-001: Service는 @Service로 등록.
 *
 * <p>APP-SVC-002: UseCase 1:1 구현.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class ValidateBusinessRegistrationService implements ValidateBusinessRegistrationUseCase {

    private final SellerCompositionReadManager compositionReadManager;

    public ValidateBusinessRegistrationService(
            SellerCompositionReadManager compositionReadManager) {
        this.compositionReadManager = compositionReadManager;
    }

    /**
     * 사업자등록번호 중복 검증.
     *
     * <p>이미 등록된 번호가 있으면 false (사용 불가), 없으면 true (사용 가능).
     *
     * @param registrationNumber 사업자등록번호
     * @return 사용 가능하면 true, 이미 등록된 번호면 false
     */
    @Override
    public boolean execute(String registrationNumber) {
        return !compositionReadManager.existsByRegistrationNumber(registrationNumber);
    }
}
