package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;

/**
 * Seller Persistence Port (Command)
 *
 * <p>Seller Aggregate를 영속화하는 쓰기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface SellerPersistencePort {

    /**
     * Seller 저장 (신규 생성 또는 수정)
     *
     * @param seller 저장할 Seller (Domain Aggregate)
     * @return 저장된 Seller의 ID
     */
    SellerId persist(Seller seller);
}
