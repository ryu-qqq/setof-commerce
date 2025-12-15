package com.setof.connectly.module.discount.service.history;

import com.setof.connectly.module.discount.dto.DiscountUseDto;
import java.util.List;

public interface DiscountUseHistoryQueryService {

    void saveDiscountUseHistories(long paymentId, List<DiscountUseDto> discountUses);
}
