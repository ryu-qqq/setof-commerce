package com.setof.connectly.module.product.service.stock;

import com.setof.connectly.module.crawl.CrawlProduct;
import com.setof.connectly.module.product.dto.group.ProductGroupPatchResponse;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.product.dto.stock.UpdateProductStock;
import com.setof.connectly.module.product.entity.group.Product;
import com.setof.connectly.module.product.entity.group.ProductGroup;
import com.setof.connectly.module.product.mapper.product.ProductMapper;
import com.setof.connectly.module.product.service.individual.fetch.ProductFindService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductStockQueryServiceImpl implements ProductStockQueryService {

    private final ProductFindService productFindService;
    private final ProductStockRedisQueryService productStockRedisQueryService;
    private final ProductMapper productMapper;

    @Override
    public ProductGroupPatchResponse updateProductStock(UpdateProductStock stock) {
        Product findProduct = productFindService.fetchProductEntity(stock.getProductId());
        updateStockAndCache(findProduct, stock.getProductStockQuantity());

        if (findProduct.isSoldOut()) {
            // updateGroup(Collections.singletonList(findProduct.getId()));
        }

        return productMapper.toProductPatchResponse(findProduct);
    }

    @Override
    public List<ProductGroupPatchResponse> updateProductStocks(
            List<UpdateProductStock> updateProductStocks) {

        List<Long> toUpdateProductIds = new ArrayList<>();

        List<Product> products = fetchProductsForUpdate(updateProductStocks);
        Map<Long, Integer> productIdMap = toProductIdMap(updateProductStocks);

        products.forEach(
                product -> {
                    updateStockAndCache(
                            product, findStockQtyForProduct(product.getId(), productIdMap));
                    if (product.isSoldOut()) toUpdateProductIds.add(product.getId());
                });

        if (!toUpdateProductIds.isEmpty()) {
            // updateGroup(toUpdateProductIds);
        }

        return products.stream()
                .map(productMapper::toProductPatchResponse)
                .collect(Collectors.toList());
    }

    private void updateStockAndCache(Product product, int quantity) {
        product.updateStock(quantity);

        ProductGroup productGroup = product.getProductGroup();
        String productGroupName = productGroup.getProductGroupDetails().getProductGroupName();
        CrawlProduct crawlProduct = productGroup.getCrawlProduct();
        long crawlProductSku = 0L;
        if (crawlProduct != null) {
            crawlProductSku = productGroup.getCrawlProduct().getCrawlProductSku();
        }

        updateCache(product.getId(), product.getStockQuantity(), productGroupName, crawlProductSku);
    }

    private void updateCache(
            long productId, int qty, String productGroupName, long crawlProductSku) {
        StockDto stockDto = new StockDto(productId, qty, productGroupName, crawlProductSku);
        productStockRedisQueryService.saveStockInCache(stockDto);
    }

    private void updateGroup(List<Long> productIds) {

        Map<ProductGroup, List<Product>> productGroupListMap =
                productFindService.fetchProductEntities(productIds).stream()
                        .collect(Collectors.groupingBy(Product::getProductGroup));

        productGroupListMap.forEach(
                (key, values) -> {
                    if (isGroupSoldOut(values)) {
                        key.soldOut();
                    }
                });
    }

    private List<Product> fetchProductsForUpdate(List<UpdateProductStock> updateProductStocks) {
        List<Long> productIds =
                updateProductStocks.stream()
                        .map(UpdateProductStock::getProductId)
                        .collect(Collectors.toList());

        return productFindService.fetchProductEntities(productIds);
    }

    private Map<Long, Integer> toProductIdMap(List<UpdateProductStock> updateProductStocks) {
        return updateProductStocks.stream()
                .collect(
                        Collectors.toMap(
                                UpdateProductStock::getProductId,
                                UpdateProductStock::getProductStockQuantity));
    }

    private boolean isGroupSoldOut(List<Product> products) {
        return products.stream().allMatch(product -> product.getStockQuantity() == 0);
    }

    private int findStockQtyForProduct(Long productId, Map<Long, Integer> productIdMap) {
        Integer integer = productIdMap.get(productId);
        return integer == null ? 0 : integer;
    }
}
