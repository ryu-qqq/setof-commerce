package com.connectly.partnerAdmin.module.product.service.stock;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOption;
import com.connectly.partnerAdmin.module.product.dto.query.CreateOptionDetail;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateDisplayYn;
import com.connectly.partnerAdmin.module.product.dto.query.UpdateProductStock;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import com.connectly.partnerAdmin.module.product.entity.option.OptionDetail;
import com.connectly.partnerAdmin.module.product.entity.option.OptionGroup;
import com.connectly.partnerAdmin.module.product.entity.option.ProductOption;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.connectly.partnerAdmin.module.product.exception.InvalidProductException;
import com.connectly.partnerAdmin.module.product.exception.ProductNotFoundException;
import com.connectly.partnerAdmin.module.product.mapper.stock.OptionMapper;
import com.connectly.partnerAdmin.module.product.mapper.stock.ProductMapper;
import com.connectly.partnerAdmin.module.product.repository.option.OptionDetailRepository;
import com.connectly.partnerAdmin.module.product.repository.option.OptionGroupRepository;
import com.connectly.partnerAdmin.module.product.repository.stock.ProductOptionRepository;
import com.connectly.partnerAdmin.module.product.repository.stock.ProductRepository;

import static com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant.PRODUCT_NOT_FOUND_MSG;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductUpdateServiceImpl implements ProductUpdateService{

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final ProductFetchService productFetchService;

    private final ProductMapper productMapper;
    private final OptionMapper optionMapper;

    @Override
    public long updateIndividualDisplayYn(long productId, UpdateDisplayYn updateDisplayYn) {
        Product findProduct = productFetchService.fetchProductEntity(productId);
        findProduct.updateDisplayYn(updateDisplayYn.getDisplayYn());
        return findProduct.getId();
    }


    @Override
    public Set<ProductFetchResponse> updateProductAndStock(long productGroupId, List<CreateOption> options) {
        if(options.getFirst() != null && options.getFirst().getOptions().size() > 2){
            if(!isValidTwoStepOptionCombination(options)) throw new InvalidProductException("2단 옵션은 COLOR, SIZE 조합 또는 Default_One, Default_TWO 여야 합니다.");
        }

        List<Product> findProducts = productFetchService.fetchProductEntities(productGroupId, Collections.emptyList());
        ProductGroup productGroup = findProducts.getFirst().getProductGroup();
        Set<Product> newProducts = productMapper.toProducts(options);

        Map<Long, Product> existingProductsMap = findProducts.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<Product> updatedProducts = new LinkedHashSet<>();
        Set<Product> productsToSave = new LinkedHashSet<>();

        for (Product newProduct : newProducts) {
            if (newProduct.getId() != 0) {
                Product existingProduct = existingProductsMap.get(newProduct.getId());
                if (existingProduct == null) {
                    throw new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG);
                }
                updateExistingProduct(existingProduct, newProduct);
                updatedProducts.add(existingProduct);
                existingProductsMap.remove(newProduct.getId());
                productsToSave.add(existingProduct);
            } else {
                updatedProducts.add(newProduct);
                newProduct.setProductGroup(productGroup);
                productsToSave.add(newProduct);
            }
        }

        Set<Product> deletedProducts = existingProductsMap.values()
                .stream()
                .peek(Product::delete)
                .collect(Collectors.toSet());

        updatedProducts.removeAll(deletedProducts);

        if(!productsToSave.isEmpty()) {
            saveOptions(productsToSave);
        }

        return optionMapper.toProductFetchResponse(productGroupId, updatedProducts);
    }

    private void updateExistingProduct(Product existingProduct, Product newProduct) {
        existingProduct.setStock(new UpdateProductStock(newProduct.getId(), newProduct.getStockQuantity()));

        Map<Long, OptionDetail> newProductOptionMap = newProduct.getProductOptions().stream()
                .collect(Collectors.toMap(
                        po -> po.getOptionGroup().getId(),
                        ProductOption::getOptionDetail,
                        (v1, v2) -> v1));

        for (ProductOption existingProductOption : existingProduct.getProductOptions()) {
            long id = existingProductOption.getOptionGroup().getId();
            OptionDetail newOptionDetail = newProductOptionMap.get(id);
            if (newOptionDetail != null) {
                existingProductOption.setOptionName(newOptionDetail.getOptionGroup().getOptionName());
                existingProductOption.setOptionValue(newOptionDetail.getOptionValue());
            }
        }
    }

    private void saveOptions(Set<Product> products) {
        for (Product product : products) {
            long id = product.getId();
            productRepository.save(product);
            Set<ProductOption> productOptions = product.getProductOptions();
            for (ProductOption productOption : productOptions) {
                OptionGroup optionGroup = productOption.getOptionGroup();

                if(optionGroup.getId() ==0) {
                    optionGroupRepository.save(optionGroup);
                }

                OptionDetail optionDetail = productOption.getOptionDetail();

                if(optionDetail.getId() ==0 || id == 0){
                    optionDetailRepository.save(optionDetail);
                }

                if(productOption.getId() ==0){
                    productOptionRepository.save(productOption);
                }
            }
        }
    }


    private boolean isValidTwoStepOptionCombination(List<CreateOption> options) {
        Set<OptionName> optionNameSet = options
            .stream()
            .flatMap(c -> c.getOptions().stream())
            .map(
                CreateOptionDetail::getOptionName)
            .collect(Collectors.toSet());


        return (optionNameSet.contains(OptionName.COLOR) && optionNameSet.contains(OptionName.SIZE)) ||
            (optionNameSet.contains(OptionName.DEFAULT_ONE) && optionNameSet.contains(OptionName.DEFAULT_TWO));
    }


}
