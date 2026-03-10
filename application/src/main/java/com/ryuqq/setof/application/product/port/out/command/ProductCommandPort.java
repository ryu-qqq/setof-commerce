package com.ryuqq.setof.application.product.port.out.command;

import com.ryuqq.setof.domain.product.aggregate.Product;
import java.util.List;

public interface ProductCommandPort {
    Long persist(Product product);

    List<Long> persistAll(List<Product> products);
}
