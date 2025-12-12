package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.factory.command.SellerCommandFactory;
import com.ryuqq.setof.application.seller.manager.command.SellerPersistenceManager;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import org.springframework.stereotype.Service;

/**
 * 셀러 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>SellerCommandFactory로 Seller 도메인 생성 (VO 검증 포함)
 *   <li>SellerPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterSellerService implements RegisterSellerUseCase {

    private final SellerCommandFactory sellerCommandFactory;
    private final SellerPersistenceManager sellerPersistenceManager;

    public RegisterSellerService(
            SellerCommandFactory sellerCommandFactory,
            SellerPersistenceManager sellerPersistenceManager) {
        this.sellerCommandFactory = sellerCommandFactory;
        this.sellerPersistenceManager = sellerPersistenceManager;
    }

    @Override
    public Long execute(RegisterSellerCommand command) {
        Seller seller = sellerCommandFactory.create(command);
        SellerId sellerId = sellerPersistenceManager.persist(seller);
        return sellerId.value();
    }
}
