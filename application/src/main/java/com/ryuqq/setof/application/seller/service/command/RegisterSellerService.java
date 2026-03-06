package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.bundle.SellerRegistrationBundle;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.factory.SellerCommandFactory;
import com.ryuqq.setof.application.seller.internal.SellerRegistrationCoordinator;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import org.springframework.stereotype.Service;

@Service
public class RegisterSellerService implements RegisterSellerUseCase {

    private final SellerCommandFactory sellerCommandFactory;
    private final SellerRegistrationCoordinator sellerRegistrationCoordinator;

    public RegisterSellerService(
            SellerCommandFactory sellerCommandFactory,
            SellerRegistrationCoordinator sellerRegistrationCoordinator) {
        this.sellerCommandFactory = sellerCommandFactory;
        this.sellerRegistrationCoordinator = sellerRegistrationCoordinator;
    }

    @Override
    public Long execute(RegisterSellerCommand command) {
        SellerRegistrationBundle bundle = sellerCommandFactory.createRegistrationBundle(command);
        return sellerRegistrationCoordinator.register(bundle);
    }
}
