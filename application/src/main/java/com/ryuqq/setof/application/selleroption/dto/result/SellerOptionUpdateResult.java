package com.ryuqq.setof.application.selleroption.dto.result;

import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import java.time.Instant;
import java.util.List;

public record SellerOptionUpdateResult(
        List<SellerOptionValueId> resolvedActiveValueIds, Instant occurredAt, boolean hasChanges) {}
