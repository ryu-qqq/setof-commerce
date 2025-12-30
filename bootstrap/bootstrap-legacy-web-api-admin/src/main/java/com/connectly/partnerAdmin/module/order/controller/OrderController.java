package com.connectly.partnerAdmin.module.order.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.common.filter.BaseDateTimeRangeFilter;
import com.connectly.partnerAdmin.module.common.filter.BaseRoleFilter;
import com.connectly.partnerAdmin.module.order.dto.OrderDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderDateDashboardResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderHistoryResponse;
import com.connectly.partnerAdmin.module.order.dto.OrderResponse;
import com.connectly.partnerAdmin.module.order.dto.SettlementResponse;
import com.connectly.partnerAdmin.module.order.dto.UpdateOrderResponse;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.dto.response.OrderListResponse;
import com.connectly.partnerAdmin.module.order.filter.OrderFilter;
import com.connectly.partnerAdmin.module.order.service.OrderFetchService;
import com.connectly.partnerAdmin.module.order.service.OrderHistoryFetchService;
import com.connectly.partnerAdmin.module.order.service.OrderUpdateService;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentInfo;
import com.connectly.partnerAdmin.module.shipment.service.ShipmentQueryService;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class OrderController <T extends UpdateOrder>{

    private final OrderFetchService orderFetchService;
    private final OrderHistoryFetchService orderHistoryFetchService;

    private final OrderUpdateService<T> orderUpdateService;
    private final ShipmentQueryService shipmentQueryService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> fetchOrder(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(ApiResponse.success(orderFetchService.fetchOrder(orderId)));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<CustomPageable<OrderListResponse>>> getOrders(@ModelAttribute @Validated OrderFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(orderFetchService.fetchOrders(filter, pageable)));
    }

    @GetMapping("/settlements")
    public ResponseEntity<ApiResponse<CustomPageable<SettlementResponse>>> getSettlements(@ModelAttribute @Validated BaseDateTimeRangeFilter filter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(orderFetchService.fetchSettlements(filter, pageable)));
    }

    @GetMapping("/order/history/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderHistoryResponse>>> getOrderHistory(@PathVariable("orderId") long orderId){
        return ResponseEntity.ok(ApiResponse.success(orderHistoryFetchService.fetchOrderHistories(orderId)));
    }


    @PutMapping("/order")
    public ResponseEntity<ApiResponse<UpdateOrderResponse>> modifyOrderStatus(@RequestBody @Validated T dto){
        return ResponseEntity.ok(ApiResponse.success(orderUpdateService.updateOrder(dto)));
    }

    @PutMapping("/orders")
    public ResponseEntity<ApiResponse<List<UpdateOrderResponse>>> modifyOrderStatusList(@RequestBody @Validated List<T> dto){
        return ResponseEntity.ok(ApiResponse.success(orderUpdateService.updateOrders(dto)));
    }

    @PatchMapping("/shipment/order/{orderId}")
    public ResponseEntity<ApiResponse<ShipmentInfo>> modifyOrderStatusList(@PathVariable("orderId") long orderId, @RequestBody @Validated ShipmentInfo shipmentInfo){
        return ResponseEntity.ok(ApiResponse.success(shipmentQueryService.deliveryStart(orderId, shipmentInfo)));
    }

    @GetMapping("/order/today-dashboard")
    public ResponseEntity<ApiResponse<OrderDashboardResponse>> getOrderTodayDashboard(@ModelAttribute @Validated BaseRoleFilter filter) {
        return ResponseEntity.ok(ApiResponse.success(orderFetchService.fetchOrderTodayDashboard(filter)));
    }

    @GetMapping("/order/date-dashboard")
    public ResponseEntity<ApiResponse<OrderDateDashboardResponse>> getOrderDateDashboard(@ModelAttribute @Validated BaseDateTimeRangeFilter filter) {
        return ResponseEntity.ok(ApiResponse.success(orderFetchService.fetchOrderDateDashboard(filter)));
    }

}
