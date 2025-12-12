package com.ryuqq.setof.application.seller.manager.command;

import com.ryuqq.setof.application.seller.port.out.command.SellerPersistencePort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seller Persistence Manager
 *
 * <p>Seller 영속화를 담당하는 Manager
 *
 * <p>트랜잭션 경계를 관리
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerPersistenceManager {

    private final SellerPersistencePort sellerPersistencePort;

    public SellerPersistenceManager(SellerPersistencePort sellerPersistencePort) {
        this.sellerPersistencePort = sellerPersistencePort;
    }

    /**
     * Seller 저장
     *
     * @param seller 저장할 Seller
     * @return 저장된 Seller의 ID
     */
    @Transactional
    public SellerId persist(Seller seller) {
        return sellerPersistencePort.persist(seller);
    }
}
