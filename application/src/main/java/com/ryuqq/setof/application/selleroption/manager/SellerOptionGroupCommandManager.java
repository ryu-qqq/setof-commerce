package com.ryuqq.setof.application.selleroption.manager;

import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionGroupCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerOptionGroupCommandManager {

    private final SellerOptionGroupCommandPort commandPort;

    public SellerOptionGroupCommandManager(SellerOptionGroupCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Transactional
    public Long persist(SellerOptionGroup sellerOptionGroup) {
        return commandPort.persist(sellerOptionGroup);
    }

    @Transactional
    public void persistAll(List<SellerOptionGroup> sellerOptionGroups) {
        commandPort.persistAll(sellerOptionGroups);
    }
}
