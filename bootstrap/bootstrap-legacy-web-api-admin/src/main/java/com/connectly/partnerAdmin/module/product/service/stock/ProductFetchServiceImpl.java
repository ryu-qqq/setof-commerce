package com.connectly.partnerAdmin.module.product.service.stock;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.product.dto.ProductFetchDto;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant;
import com.connectly.partnerAdmin.module.product.exception.ProductNotFoundException;
import com.connectly.partnerAdmin.module.product.mapper.stock.OptionMapper;
import com.connectly.partnerAdmin.module.product.repository.stock.ProductFetchRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductFetchServiceImpl implements ProductFetchService{

    private final ProductFetchRepository productFetchRepository;
    private final OptionMapper optionMapper;

    @Override
    public Set<ProductFetchResponse> fetchProducts(List<Long> productGroupIds){
        List<ProductFetchDto> rawProducts = productFetchRepository.fetchProducts(productGroupIds);
        return optionMapper.toProductFetchResponse(rawProducts);
    }

    @Override
    public Set<ProductFetchResponse> fetchProducts(Long productGroupIds){
        List<ProductFetchDto> rawProducts = productFetchRepository.fetchProducts(productGroupIds);
        return optionMapper.toProductFetchResponse(rawProducts);
    }


    @Override
    public Product fetchProductEntity(long productId){
        return productFetchRepository.fetchProductEntity(productId)
                .orElseThrow(()-> new ProductNotFoundException(ProductErrorConstant.PRODUCT_NOT_FOUND_MSG + productId));
    }


    @Override
    public List<Product> fetchProductEntities(Long productGroupId, List<Long> productIds) {
        return productFetchRepository.fetchProductEntities(productGroupId, productIds);
    }



}
