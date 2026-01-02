package com.connectly.partnerAdmin.module.product.service.stock;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.product.core.Sku;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.mapper.stock.OptionMapper;

@Transactional
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService{

    private final ProductFetchService productFetchService;
    private final OptionMapper optionMapper;

    @Override
    public Set<ProductFetchResponse> decrease(List<? extends Sku> orders) {
        return updateStock(orders, this::decreaseStock);
    }

    @Override
    public Set<ProductFetchResponse> increase(List<? extends Sku> orders) {
        return updateStock(orders, this::increaseStock);
    }

    @Override
    public Set<ProductFetchResponse> update(List<? extends Sku> orders) {
        return updateStock(orders, this::setStock);
    }

    @Override
    public Set<ProductFetchResponse> soldOut(long productGroupId) {
        List<Product> products = productFetchService.fetchProductEntities(productGroupId, Collections.emptyList());
        products.forEach(product -> setStock(product, new UpdateProductStock(product.getId(), 0)));
        products.getFirst().getProductGroup().soldOut();
        return optionMapper.toProductFetchResponse(productGroupId, new HashSet<>(products));
    }

    private Set<ProductFetchResponse> updateStock(List<? extends Sku> skus, BiConsumer<Product, Sku> updateStrategy) {
        Map<Long, Sku> productIdMap = skus.stream()
                .collect(Collectors.toMap(Sku::getProductId, Function.identity(), (v1, v2) -> v1));

        List<Product> products = productFetchService.fetchProductEntities(null, new ArrayList<>(productIdMap.keySet()));

        products.forEach(product -> {
            Sku sku = productIdMap.get(product.getId());
            updateStrategy.accept(product, sku);
        });

        Map<ProductGroup, List<Product>> productGroupListMap = handleGroupSoldOut(products);

        return productGroupListMap.entrySet()
                .parallelStream()
                .map(entry -> optionMapper.toProductFetchResponse(entry.getKey().getId(), new HashSet<>(entry.getValue())))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private void decreaseStock(Product product, Sku sku) {
        product.updateStock(sku);
    }

    private void increaseStock(Product product, Sku sku) {
        product.updateStock(new UpdateProductStock(sku.getProductId(), sku.getQuantity() * -1));
    }

    private void setStock(Product product, Sku sku) {
        product.setStock(sku);
    }

    private Map<ProductGroup, List<Product>> handleGroupSoldOut(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getProductGroup));
    }
}
