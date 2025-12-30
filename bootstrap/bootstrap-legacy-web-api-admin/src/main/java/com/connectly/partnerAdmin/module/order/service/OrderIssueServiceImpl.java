package com.connectly.partnerAdmin.module.order.service;

import com.connectly.partnerAdmin.module.order.dto.OrderSnapShotQueryDto;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.mapper.OrderMapper;
import com.connectly.partnerAdmin.module.order.repository.OrderRepository;
import com.connectly.partnerAdmin.module.payment.entity.Payment;
import com.connectly.partnerAdmin.module.seller.core.BaseSellerContext;
import com.connectly.partnerAdmin.module.seller.core.BusinessSellerContext;
import com.connectly.partnerAdmin.module.seller.service.SellerFetchService;
import com.connectly.partnerAdmin.module.shipment.service.ShipmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderIssueServiceImpl implements OrderIssueService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final SellerFetchService sellerFetchService;
    private final ShipmentQueryService shipmentQueryService;
    private final SettlementQueryService settlementQueryService;
    private final OrderSnapShotFetchService orderSnapShotFetchService;

    @Override
    public List<Order> doOrders(Payment payment, List<? extends OrderSheet> orderSheets) {

        List<Long> productIds = orderSheets.stream().map(OrderSheet::getProductId).toList();
        List<OrderSnapShotQueryDto> orderSnapShotQueries = orderSnapShotFetchService.fetchProductQueryForSnapShot(productIds);
        Map<Long, OrderSnapShotQueryDto> productMap = toProductMap(orderSnapShotQueries);

        List<Order> orders = orderSheets.stream()
                .map(orderSheet -> {
                    OrderSnapShotQueryDto orderSnapShotQueryDto = productMap.get(orderSheet.getProductId());
                    return orderMapper.toOrder(payment, orderSheet, orderSnapShotQueryDto);
                })
                .toList();

        List<Order> savedOrders = orderRepository.saveAll(orders);
        List<BusinessSellerContext> businessSellerContexts = fetchBusinessSellerContexts(savedOrders);
        long paymentSnapShotShippingId = payment.getPaymentSnapShotShippingAddress().getId();
        Map<Long, BusinessSellerContext> businessSellerContextMap = toBusinessSellerContextMap(businessSellerContexts);

        saveShipments(paymentSnapShotShippingId, orders, businessSellerContextMap);
        saveSettlement(orders, businessSellerContextMap);

        return savedOrders;
    }



    private void saveShipments(long paymentSnapShotShippingId, List<Order> orders, Map<Long, BusinessSellerContext> businessSellerContextMap){
        orders.forEach(order -> {
            BusinessSellerContext businessSellerContext = businessSellerContextMap.get(order.getSellerId());
            shipmentQueryService.saveShipment(paymentSnapShotShippingId, order, businessSellerContext);
        });
    }

    private void saveSettlement(List<Order> orders, Map<Long, BusinessSellerContext> businessSellerContextMap){
        orders.forEach(order -> {
            BusinessSellerContext businessSellerContext = businessSellerContextMap.get(order.getSellerId());
            settlementQueryService.saveSettlement(order, businessSellerContext);
        });
    }

    private List<BusinessSellerContext> fetchBusinessSellerContexts(List<Order> savedOrders) {
        List<Long> sellerIds = savedOrders.stream().map(Order::getSellerId).toList();
        return sellerFetchService.fetchSellersBusinessInfo(sellerIds);
    }


    private Map<Long, BusinessSellerContext> toBusinessSellerContextMap(List<BusinessSellerContext> businessSellerContexts) {
        return businessSellerContexts.stream()
                .collect(Collectors.toMap(BaseSellerContext::getSellerId, Function.identity(),
                        (existing, replacement) -> existing));
    }


    private Map<Long, OrderSnapShotQueryDto> toProductMap(List<OrderSnapShotQueryDto> orderSnapShotQueries) {
        return orderSnapShotQueries.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getOrderSnapShotProductGroup().getProduct().getId(),
                        Function.identity(),
                        (v1, v2) -> v1
                ));
    }


}
