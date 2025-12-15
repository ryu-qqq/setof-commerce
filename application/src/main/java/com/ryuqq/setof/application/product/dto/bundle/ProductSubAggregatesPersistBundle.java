package com.ryuqq.setof.application.product.dto.bundle;

import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import java.util.Objects;

/**
 * Product 하위 Aggregate 영속화 Bundle
 *
 * <p>ProductGroup 저장 후 productGroupId를 받아 생성된 하위 Aggregate들을 묶습니다.
 *
 * <p>Products(필수), Images(선택), Description(필수), Notice(필수)를 하나의 단위로 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SuppressFBWarnings({"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record ProductSubAggregatesPersistBundle(
        List<Product> products,
        List<ProductImage> images,
        ProductDescription description,
        ProductNotice notice) {

    public ProductSubAggregatesPersistBundle {
        Objects.requireNonNull(products, "products must not be null");
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(notice, "notice must not be null");
        if (products.isEmpty()) {
            throw new IllegalArgumentException("products must not be empty");
        }
        if (images == null) {
            images = List.of();
        }
    }

    public boolean hasImages() {
        return !images.isEmpty();
    }

    public int productCount() {
        return products.size();
    }
}
