package com.setof.connectly.module.order.controller;

import com.setof.connectly.module.order.dto.OrderCountDto;
import com.setof.connectly.module.order.dto.fetch.OrderHistoryResponse;
import com.setof.connectly.module.order.dto.fetch.OrderResponse;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.filter.OrderFilter;
import com.setof.connectly.module.order.dto.query.UpdateOrder;
import com.setof.connectly.module.order.dto.slice.OrderSlice;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.query.update.OrderQueryService;
import com.setof.connectly.module.payload.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController<T extends UpdateOrder> {
    private final OrderFindService orderFindService;

    private final OrderQueryService<T> orderQueryService;

    @GetMapping("/order/history/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderHistoryResponse>>> fetchOrderHistory(
            @PathVariable("orderId") long orderId) {
        return ResponseEntity.ok(
                ApiResponse.success(orderFindService.fetchOrderHistories(orderId)));
    }

    @PutMapping("/order")
    public ResponseEntity<ApiResponse<List<UpdateOrderResponse>>> updateOrders(
            @RequestBody T updateOrder) {
        return ResponseEntity.ok(ApiResponse.success(orderQueryService.updateOrder(updateOrder)));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<OrderSlice<OrderResponse>>> fetchOrders(
            @ModelAttribute @Validated OrderFilter filterDto, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(orderFindService.fetchOrders(filterDto, pageable)));
    }

    @GetMapping("/order/status-counts")
    public ResponseEntity<ApiResponse<List<OrderCountDto>>> fetchOrderCounts(
            @RequestParam List<OrderStatus> orderStatuses) {
        return ResponseEntity.ok(
                ApiResponse.success(orderFindService.fetchOrderCounts(orderStatuses)));
    }
}
