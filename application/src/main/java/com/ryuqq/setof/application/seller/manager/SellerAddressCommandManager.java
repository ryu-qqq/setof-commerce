package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.command.SellerAddressCommandPort;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** SellerAddress Command Manager. */
@Component
public class SellerAddressCommandManager {

    private final SellerAddressCommandPort commandPort;

    public SellerAddressCommandManager(SellerAddressCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @Transactional
    public Long persist(SellerAddress address) {
        return commandPort.persist(address);
    }

    @Transactional
    public void persistAll(List<SellerAddress> addresses) {
        commandPort.persistAll(addresses);
    }
}
