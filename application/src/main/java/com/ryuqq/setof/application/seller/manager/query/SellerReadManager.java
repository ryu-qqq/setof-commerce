package com.ryuqq.setof.application.seller.manager.query;

import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Seller Read Manager
 *
 * <p>Seller 조회를 담당하는 Manager
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerReadManager {

    private final SellerQueryPort sellerQueryPort;

    public SellerReadManager(SellerQueryPort sellerQueryPort) {
        this.sellerQueryPort = sellerQueryPort;
    }

    /**
     * ID로 Seller 조회 (없으면 예외)
     *
     * @param sellerId Seller ID
     * @return Seller
     * @throws SellerNotFoundException 셀러가 존재하지 않으면
     */
    public Seller findById(Long sellerId) {
        SellerId id = SellerId.of(sellerId);
        return sellerQueryPort
                .findById(id)
                .orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    /**
     * 조건으로 Seller 목록 조회
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Seller 목록
     */
    public List<Seller> findByConditions(
            String sellerName, String approvalStatus, int page, int size) {
        int offset = page * size;
        return sellerQueryPort.findByConditions(sellerName, approvalStatus, offset, size);
    }

    /**
     * 조건에 맞는 Seller 총 개수 조회
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태
     * @return 셀러 총 개수
     */
    public long countByConditions(String sellerName, String approvalStatus) {
        return sellerQueryPort.countByConditions(sellerName, approvalStatus);
    }

    /**
     * Seller 존재 여부 확인
     *
     * @param sellerId Seller ID
     * @return 존재 여부
     */
    public boolean existsById(Long sellerId) {
        SellerId id = SellerId.of(sellerId);
        return sellerQueryPort.existsById(id);
    }
}
