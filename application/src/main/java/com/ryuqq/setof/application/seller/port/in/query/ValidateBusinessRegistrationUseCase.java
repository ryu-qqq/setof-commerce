package com.ryuqq.setof.application.seller.port.in.query;

/**
 * 사업자등록번호 유효성 검증 UseCase.
 *
 * <p>APP-UC-001: UseCase 인터페이스 Port-In.
 *
 * <p>레거시 SellerController.fetchBusinessValidation 흐름 변환.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface ValidateBusinessRegistrationUseCase {

    /**
     * 사업자등록번호 중복 검증.
     *
     * @param registrationNumber 사업자등록번호
     * @return 사용 가능하면 true, 이미 등록된 번호면 false
     */
    boolean execute(String registrationNumber);
}
