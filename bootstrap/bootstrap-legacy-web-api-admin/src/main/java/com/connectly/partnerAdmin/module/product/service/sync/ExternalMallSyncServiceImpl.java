package com.connectly.partnerAdmin.module.product.service.sync;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.coreServer.CoreServerProductService;

/**
 * 외부몰 상품 동기화 서비스 구현체.
 *
 * 비동기 + 새 트랜잭션으로 외부몰 API를 호출합니다.
 * 기존 트랜잭션과 완전히 분리되어 외부몰 실패가 상품 저장에 영향을 주지 않습니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalMallSyncServiceImpl implements ExternalMallSyncService {

    private final CoreServerProductService coreServerProductService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void syncProductCreated(Long productGroupId) {
        log.info("[ExternalMallSync] 상품 등록 동기화 시작 - productGroupId: {}", productGroupId);

        try {
            coreServerProductService.register(productGroupId);

            log.info("[ExternalMallSync] 상품 등록 동기화 완료 - productGroupId: {}", productGroupId);
        } catch (Exception e) {
            log.error("[ExternalMallSync] 상품 등록 동기화 실패 - productGroupId: {}, error: {}",
                productGroupId, e.getMessage(), e);
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void syncProductUpdated(Long productGroupId) {
        log.info("[ExternalMallSync] 상품 수정 동기화 시작 - productGroupId: {}", productGroupId);

        try {
            coreServerProductService.update(productGroupId);
            log.info("[ExternalMallSync] 상품 수정 동기화 완료 - productGroupId: {}", productGroupId);
        } catch (Exception e) {
            log.error("[ExternalMallSync] 상품 수정 동기화 실패 - productGroupId: {}, error: {}",
                productGroupId, e.getMessage(), e);
        }
    }

}
