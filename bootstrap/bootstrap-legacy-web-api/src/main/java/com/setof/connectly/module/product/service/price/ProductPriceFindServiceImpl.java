package com.setof.connectly.module.product.service.price;

import com.setof.connectly.module.exception.product.ProductNotFoundException;
import com.setof.connectly.module.product.dto.price.ProductGroupPriceDto;
import com.setof.connectly.module.product.repository.group.ProductGroupFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductPriceFindServiceImpl implements ProductPriceFindService {

    private final ProductGroupFindRepository productGroupFindRepository;

    @Override
    public ProductGroupPriceDto fetchProductGroupPrice(long productId) {
        return productGroupFindRepository
                .fetchProductGroupPrice(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public List<ProductGroupPriceDto> fetchProductGroupPrices(List<Long> productIds) {
        List<ProductGroupPriceDto> productGroupPrices =
                productGroupFindRepository.fetchProductGroupPrices(productIds);
        if (productGroupPrices.isEmpty()) throw new ProductNotFoundException();
        return productGroupPrices;
    }
}
