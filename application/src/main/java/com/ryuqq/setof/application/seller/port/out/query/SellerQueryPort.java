package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import java.util.List;
import java.util.Optional;

/**
 * Seller Query Port (Query)
 *
 * <p>Seller Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SellerQueryPort {

    /**
     * ID로 Seller 단건 조회
     *
     * @param id Seller ID (Value Object)
     * @return Seller Domain (Optional)
     */
    Optional<Seller> findById(SellerId id);

    /**
     * 셀러명으로 검색
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태 (nullable)
     * @param offset 오프셋
     * @param limit 개수 제한
     * @return Seller 목록
     */
    List<Seller> findByConditions(String sellerName, String approvalStatus, int offset, int limit);

    /**
     * 검색 조건에 맞는 셀러 총 개수 조회
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태 (nullable)
     * @return 셀러 총 개수
     */
    long countByConditions(String sellerName, String approvalStatus);

    /**
     * Seller ID 존재 여부 확인
     *
     * @param id Seller ID
     * @return 존재 여부
     */
    boolean existsById(SellerId id);

    /**
     * 활성 셀러 존재 여부 확인
     *
     * @param sellerId Seller ID (Long)
     * @return 존재 여부
     */
    boolean existsActiveById(Long sellerId);
}
