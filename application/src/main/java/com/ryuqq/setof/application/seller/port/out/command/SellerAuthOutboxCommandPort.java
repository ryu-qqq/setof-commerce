package com.ryuqq.setof.application.seller.port.out.command;

import com.ryuqq.setof.domain.seller.aggregate.SellerAuthOutbox;

/** SellerAuthOutbox Command Port. */
public interface SellerAuthOutboxCommandPort {

    Long persist(SellerAuthOutbox outbox);
}
