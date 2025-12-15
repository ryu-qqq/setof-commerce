package com.setof.connectly.module.product.service.individual.fetch;

import com.setof.connectly.module.exception.product.ProductNotFoundException;
import com.setof.connectly.module.product.dto.option.OptionDto;
import com.setof.connectly.module.product.entity.group.Product;
import com.setof.connectly.module.product.repository.individual.ProductFindRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFindServiceImpl implements ProductFindService {

    private final ProductFindRepository productFindRepository;

    @Override
    public Optional<String> fetchOptionName(long productId) {
        List<OptionDto> options = productFindRepository.fetchOptions(productId);
        String optionName =
                options.stream().map(OptionDto::getOptionValue).collect(Collectors.joining(""));

        return Optional.ofNullable(optionName);
    }

    @Override
    public Product fetchProductEntity(long productId) {
        return productFindRepository
                .fetchProductEntity(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public List<Product> fetchProductEntities(List<Long> productIds) {
        return productFindRepository.fetchProductEntities(productIds);
    }
}
