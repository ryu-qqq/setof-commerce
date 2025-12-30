package com.connectly.partnerAdmin.module.external.service.order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.coreServer.ExternalProductFetchInterface;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;
import com.connectly.partnerAdmin.module.external.core.ExMallOrder;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderProduct;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.mapper.ExternalOrderMapper;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderRepository;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.service.BasePaymentService;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;

import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public abstract class AbstractOrderExternalService<T extends ExMallOrder, R extends ExMallOrderProduct> implements ExternalOrderService<T> {

    private final BasePaymentService basePaymentService;
    private final ExternalProductFetchInterface externalProductFetchInterface;
    private final ProductGroupFetchService productGroupFetchService;
    private final ExternalOrderRepository externalOrderRepository;
    private final ExternalOrderMapper<T, R> externalOrderMapper;

    protected ExternalOrder saveInterlockingOrder(T t, Order order){
        ExternalOrder externalOrder = toInterlockingOrder(t, order);
        return externalOrderRepository.save(externalOrder);
    }


    protected Map<Long, ProductGroupDetailResponse> toProductGroupIdMap(List<Long> productGroupIds){
        List<ProductGroupDetailResponse> productGroupDetailResponses = fetchProductGroup(productGroupIds);
        return productGroupDetailResponses.stream()
                .collect(Collectors.toMap(p -> p.getProductGroup().getProductGroupId(), Function.identity(),
                        (productGroupFetchDto, productGroupFetchDto2) -> productGroupFetchDto2));
    }

    protected OrderSheet toCreateOrder(R r, ProductGroupDetailResponse productGroupsResponse){
        return externalOrderMapper.toCreateOrder(r, productGroupsResponse);
    }

    protected CreatePayment toCreatePayment(T t, List<OrderSheet> createOrders){
        return externalOrderMapper.toCreatePayment(t, createOrders);
    }

    protected List<Order> doPay(CreatePayment createPayment){
        return basePaymentService.doPay(createPayment);
    }

    private List<ProductGroupDetailResponse> fetchProductGroup(List<Long> productGroupIds){
        return productGroupFetchService.fetchProductGroups(productGroupIds);
    }

    protected DefaultExternalProductGroupContext fetchBySiteIdAndExternalProductGroupId(long siteId, String externalProductGroupId){
        return externalProductFetchInterface.fetchBySiteIdAndExternalProductGroupId(siteId, externalProductGroupId);
    }

    private ExternalOrder toInterlockingOrder(T t, Order order){
        return ExternalOrder.builder()
                .siteId(t.getSiteName().getSiteId())
                .paymentId(order.getPayment().getId())
                .orderId(order.getId())
                .externalIdx(t.getExMallOrderId())
                .externalOrderPkId(t.getExMallOrderCode())
                .build();
    }



}
