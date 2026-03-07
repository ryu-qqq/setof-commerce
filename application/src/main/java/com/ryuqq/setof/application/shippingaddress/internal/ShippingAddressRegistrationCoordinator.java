package com.ryuqq.setof.application.shippingaddress.internal;

import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.factory.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressCommandManager;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddressRegistrationCoordinator - 배송지 등록 Coordinator.
 *
 * <p>Factory로 도메인 객체 생성 → ReadManager로 조회 → 도메인 VO로 검증 → CommandManager로 persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressRegistrationCoordinator {

    private final ShippingAddressReadManager readManager;
    private final ShippingAddressCommandFactory factory;
    private final ShippingAddressCommandManager commandManager;

    public ShippingAddressRegistrationCoordinator(
            ShippingAddressReadManager readManager,
            ShippingAddressCommandFactory factory,
            ShippingAddressCommandManager commandManager) {
        this.readManager = readManager;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Transactional
    public Long register(RegisterShippingAddressCommand command) {
        ShippingAddress newAddress = factory.createNewAddress(command);
        ShippingAddressBook book = readManager.getBookByUserId(command.userId());
        book.validateCanAdd();

        if (newAddress.isDefault()) {
            List<ShippingAddress> unmarked = book.unmarkDefaults(newAddress.createdAt());
            commandManager.persistAll(unmarked);
        }

        return commandManager.persist(newAddress);
    }
}
