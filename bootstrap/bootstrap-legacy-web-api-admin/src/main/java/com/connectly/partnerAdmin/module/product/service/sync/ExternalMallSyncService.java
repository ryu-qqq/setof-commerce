package com.connectly.partnerAdmin.module.product.service.sync;

/**
 * 외부몰 상품 동기화 서비스.
 *
 * 상품 등록/수정 트랜잭션 완료 후 비동기로 외부몰에 동기화합니다.
 */
public interface ExternalMallSyncService {

    /**
     * 상품 등록 후 외부몰 동기화.
     *
     * @param productGroupId 등록된 상품 그룹 ID
     */
    void syncProductCreated(Long productGroupId);

    /**
     * 상품 수정 후 외부몰 동기화.
     *
     * @param productGroupId 수정된 상품 그룹 ID
     */
    void syncProductUpdated(Long productGroupId);

}
