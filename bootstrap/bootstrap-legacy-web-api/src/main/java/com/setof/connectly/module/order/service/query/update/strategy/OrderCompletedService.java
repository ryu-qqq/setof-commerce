package com.setof.connectly.module.order.service.query.update.strategy;

import com.setof.connectly.module.discount.dto.DiscountUseDto;
import com.setof.connectly.module.discount.service.history.DiscountUseHistoryQueryService;
import com.setof.connectly.module.order.dto.fetch.UpdateOrderResponse;
import com.setof.connectly.module.order.dto.query.NormalOrder;
import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.order.dto.snapshot.OrderSnapShot;
import com.setof.connectly.module.order.dto.snapshot.SnapShotQueryDto;
import com.setof.connectly.module.order.entity.order.Order;
import com.setof.connectly.module.order.enums.OrderStatus;
import com.setof.connectly.module.order.enums.SnapShotEnum;
import com.setof.connectly.module.order.mapper.OrderMapper;
import com.setof.connectly.module.order.mapper.OrderSnapShotMapper;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.order.service.fetch.OrderSnapShotFindService;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapShotService;
import com.setof.connectly.module.order.service.query.snapshot.OrderSnapsShotSaveStrategy;
import com.setof.connectly.module.order.service.status.OrderStatusProcessor;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import com.setof.connectly.module.shipment.service.ShipmentQueryService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderCompletedService<T extends OrderSnapShot>
        extends AbstractOrderUpdateService<NormalOrder> {
    private final OrderSnapShotFindService orderSnapShotFindService;
    private final OrderSnapShotMapper orderSnapShotMapper;
    private final OrderSnapsShotSaveStrategy<T> orderSnapsShotSaveStrategy;
    private final StockReservationService stockReservationService;
    private final DiscountUseHistoryQueryService discountUseHistoryQueryService;
    private final ShipmentQueryService shipmentQueryService;

    public OrderCompletedService(
            OrderFindService orderFindService,
            OrderMapper orderMapper,
            OrderStatusProcessor orderStatusProcessor,
            OrderSnapShotFindService orderSnapShotFindService,
            OrderSnapShotMapper orderSnapShotMapper,
            OrderSnapsShotSaveStrategy<T> orderSnapsShotSaveStrategy,
            StockReservationService stockReservationService,
            DiscountUseHistoryQueryService discountUseHistoryQueryService,
            ShipmentQueryService shipmentQueryService) {
        super(orderFindService, orderMapper, orderStatusProcessor);
        this.orderSnapShotFindService = orderSnapShotFindService;
        this.orderSnapShotMapper = orderSnapShotMapper;
        this.orderSnapsShotSaveStrategy = orderSnapsShotSaveStrategy;
        this.stockReservationService = stockReservationService;
        this.discountUseHistoryQueryService = discountUseHistoryQueryService;
        this.shipmentQueryService = shipmentQueryService;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return OrderStatus.ORDER_COMPLETED;
    }

    @Override
    public List<UpdateOrderResponse> updateOrder(NormalOrder normalOrder) {

        List<Order> orders = fetchOrders(normalOrder.getPaymentId());

        if (normalOrder.isSaveOrderSnapShot()) {
            saveOrderSnapShot(normalOrder.getOrderId(), normalOrder.getPaymentId());
            shipmentQueryService.saveShipments(normalOrder.getPaymentId(), orders);
        }

        return orders.stream()
                .map(order -> updateOrderStatus(order, normalOrder.getOrderStatus()))
                .map(order -> toUpdateOrderResponse(normalOrder.getOrderStatus(), order, "", ""))
                .collect(Collectors.toList());
    }

    private void saveOrderSnapShot(long orderId, long paymentId) {
        List<OrderProductSnapShotQueryDto> orderProducts =
                orderSnapShotFindService.fetchOrderProductForSnapShot(paymentId);
        SnapShotQueryDto snapShotQueryDto = orderSnapShotMapper.toSnapShots(orderProducts);
        for (SnapShotEnum snapShotEnum : SnapShotEnum.values()) {
            OrderSnapShotService<T> tOrderSnapShotService =
                    orderSnapsShotSaveStrategy.get(snapShotEnum);
            Set<? extends OrderSnapShot> snapShot = snapShotQueryDto.getSnapShot(snapShotEnum);
            tOrderSnapShotService.saveSnapShot((Set<T>) snapShot);
        }
        stockReservationService.purchasedAll(paymentId);

        List<DiscountUseDto> discountUses =
                orderProducts.stream()
                        .map(
                                o -> {
                                    return new DiscountUseDto(
                                            o.getUserId(),
                                            o.getName(),
                                            paymentId,
                                            o.getOrderId(),
                                            o.getProductGroupId(),
                                            o.getProductGroupDetails().getSellerId());
                                })
                        .collect(Collectors.toList());

        discountUseHistoryQueryService.saveDiscountUseHistories(paymentId, discountUses);
    }
}
