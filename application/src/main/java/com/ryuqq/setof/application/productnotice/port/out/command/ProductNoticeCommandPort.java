package com.ryuqq.setof.application.productnotice.port.out.command;

import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;

public interface ProductNoticeCommandPort {
    Long persist(ProductNotice notice);
}
