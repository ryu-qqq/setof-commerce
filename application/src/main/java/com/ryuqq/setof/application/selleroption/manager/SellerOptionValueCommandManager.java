package com.ryuqq.setof.application.selleroption.manager;

import com.ryuqq.setof.application.selleroption.port.out.command.SellerOptionValueCommandPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerOptionValueCommandManager {

    private final SellerOptionValueCommandPort commandPort;

    public SellerOptionValueCommandManager(SellerOptionValueCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Transactional
    public Long persist(SellerOptionValue sellerOptionValue) {
        return commandPort.persist(sellerOptionValue);
    }

    @Transactional
    public List<Long> persistAll(List<SellerOptionValue> sellerOptionValues) {
        return commandPort.persistAll(sellerOptionValues);
    }

    @Transactional
    public List<Long> persistAllForGroup(
            Long sellerOptionGroupId, List<SellerOptionValue> sellerOptionValues) {
        return commandPort.persistAllForGroup(sellerOptionGroupId, sellerOptionValues);
    }
}
