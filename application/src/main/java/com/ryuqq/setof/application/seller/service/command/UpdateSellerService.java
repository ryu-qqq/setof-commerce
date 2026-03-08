package com.ryuqq.setof.application.seller.service.command;

import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.internal.SellerUpdateCoordinator;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import org.springframework.stereotype.Service;

@Service
public class UpdateSellerService implements UpdateSellerUseCase {

    private final SellerUpdateCoordinator sellerUpdateCoordinator;

    public UpdateSellerService(SellerUpdateCoordinator sellerUpdateCoordinator) {
        this.sellerUpdateCoordinator = sellerUpdateCoordinator;
    }

    @Override
    public void execute(UpdateSellerCommand command) {
        sellerUpdateCoordinator.update(command);
    }
}
