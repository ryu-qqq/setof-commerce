package com.setof.connectly.module.product.service.price;

import com.setof.connectly.module.order.dto.order.OrderSheet;
import java.util.List;

public interface PriceService {
    void checkPrices(List<OrderSheet> orderSheets);
}
