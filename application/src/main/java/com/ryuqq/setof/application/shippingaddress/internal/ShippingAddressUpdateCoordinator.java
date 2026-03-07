package com.ryuqq.setof.application.shippingaddress.internal;

import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.factory.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressCommandManager;
import com.ryuqq.setof.application.shippingaddress.manager.ShippingAddressReadManager;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressUpdateData;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddressUpdateCoordinator - 배송지 상태 변경 Coordinator.
 *
 * <p>수정/삭제 모두 상태 변경 후 persist. ReadManager → 도메인 로직 → CommandManager persist.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressUpdateCoordinator {

    private final ShippingAddressReadManager readManager;
    private final ShippingAddressCommandFactory factory;
    private final ShippingAddressCommandManager commandManager;

    public ShippingAddressUpdateCoordinator(
            ShippingAddressReadManager readManager,
            ShippingAddressCommandFactory factory,
            ShippingAddressCommandManager commandManager) {
        this.readManager = readManager;
        this.factory = factory;
        this.commandManager = commandManager;
    }

    @Transactional
    public void update(UpdateShippingAddressCommand command) {
        ShippingAddressBook book = readManager.getBookByUserId(command.userId());
        ShippingAddress address = book.findById(command.shippingAddressId());

        ShippingAddressUpdateData updateData = factory.createUpdateData(command);
        address.update(updateData);

        if (command.defaultAddress()) {
            List<ShippingAddress> unmarked = book.unmarkDefaults(updateData.occurredAt());
            commandManager.persistAll(unmarked);
            address.markAsDefault(updateData.occurredAt());
        }

        commandManager.persist(address);
    }

    @Transactional
    public void delete(DeleteShippingAddressCommand command) {
        Instant now = Instant.now();
        ShippingAddressBook book = readManager.getBookByUserId(command.userId());
        ShippingAddress address = book.findById(command.shippingAddressId());

        if (address.isDefault()) {
            book.reassignDefaultAfterRemove(command.shippingAddressId(), now)
                    .ifPresent(commandManager::persist);
        }

        address.delete(now);
        commandManager.persist(address);
    }
}
