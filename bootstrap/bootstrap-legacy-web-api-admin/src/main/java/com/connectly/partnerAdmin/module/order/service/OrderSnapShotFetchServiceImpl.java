package com.connectly.partnerAdmin.module.order.service;


import com.connectly.partnerAdmin.module.order.dto.OrderProduct;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductGroupQueryDto;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotProductQueryDto;
import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.exception.OrderNotFoundException;
import com.connectly.partnerAdmin.module.order.repository.OrderSnapShotFetchRepository;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.exception.ProductErrorConstant;
import com.connectly.partnerAdmin.module.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderSnapShotFetchServiceImpl implements OrderSnapShotFetchService {

    private final OrderSnapShotFetchRepository orderSnapShotFetchRepository;

    @Override
    public List<OrderProduct> fetchOrderSnapShotProducts(Long paymentId, List<Long> orderIds){
        List<OrderProduct> orderProducts = orderSnapShotFetchRepository.fetchOrderProductOption(paymentId, orderIds);
        if(orderProducts.isEmpty()){
            String collect = orderIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            throw new OrderNotFoundException(collect);
        }
        return orderProducts;
    }

    @Override
    public List<OrderSnapShotQueryDto> fetchProductQueryForSnapShot(List<Long> productIds) {
        List<OrderSnapShotProductGroupQueryDto> orderSnapShotProductGroupQueries = orderSnapShotFetchRepository.fetchOrderSnapShotProductGroupQueryDto(productIds);
        List<OrderSnapShotProductQueryDto> orderSnapShotProductQueries = orderSnapShotFetchRepository.fetchOrderSnapShotProductQueryDto(productIds);

        Map<Product, OrderSnapShotProductGroupQueryDto> productMap = orderSnapShotProductGroupQueries.stream().collect(Collectors.toMap(OrderSnapShotProductGroupQueryDto::getProduct, Function.identity(), (existing, replacement) -> existing));

        return orderSnapShotProductQueries.stream().map(o -> {
            Product product = o.getProduct();
            OrderSnapShotProductGroupQueryDto orderSnapShotProductGroupQuery = productMap.get(product);
            if(orderSnapShotProductGroupQuery != null){
                return new OrderSnapShotQueryDto(orderSnapShotProductGroupQuery, o);
            }
            throw new ProductNotFoundException(ProductErrorConstant.PRODUCT_NOT_FOUND_MSG);
        }).toList();
    }



}
