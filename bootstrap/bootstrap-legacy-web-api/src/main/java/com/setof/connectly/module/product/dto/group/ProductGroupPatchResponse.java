package com.setof.connectly.module.product.dto.group;

import com.setof.connectly.module.product.entity.group.embedded.ProductGroupDetails;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ProductGroupPatchResponse {
    private long productGroupId;
    private ProductGroupDetails productGroupDetails;

    @Builder.Default private Set<ProductDto> products = new HashSet<>();
}
