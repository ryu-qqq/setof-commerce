package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.dto.order.OrderStatusTransferDto;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.repository.MessageQueueJdbcRepository;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderAlimTalkService extends AbstractAlimTalkService<List<OrderStatusTransferDto>> {

    private final OrderFindService orderFindService;
    private final OrderAlimTalkMessageConversion orderAlimTalkMessageConversion;

    public OrderAlimTalkService(
            MessageQueueJdbcRepository messageQueueJdbcRepository,
            OrderFindService orderFindService,
            OrderAlimTalkMessageConversion orderAlimTalkMessageConversion) {
        super(messageQueueJdbcRepository);
        this.orderFindService = orderFindService;
        this.orderAlimTalkMessageConversion = orderAlimTalkMessageConversion;
    }

    @Override
    public void sendAlimTalk(List<OrderStatusTransferDto> orderStatusTransfers) {
        Map<Long, OrderStatusTransferDto> orderIdMap = toOrderIdMap(orderStatusTransfers);
        List<ProductOrderSheet> productOrderSheets =
                orderFindService.fetchProductOrderSheets(new ArrayList<>(orderIdMap.keySet()));
        Map<Long, List<ProductOrderSheet>> paymentIdMap = toPaymentIdMap(productOrderSheets);
        Set<MessageQueueContext> messageContexts = new HashSet<>();

        for (List<ProductOrderSheet> sheets : paymentIdMap.values()) {
            sheets.forEach(
                    sheet -> {
                        if (sheet.getOrderStatus().isOrderCompleted()
                                || sheet.getOrderStatus().isClaimRequestOrder()) {
                            processMessageContexts(
                                    sheet, orderIdMap.get(sheet.getOrderId()), messageContexts);
                        }
                    });
        }

        if (!messageContexts.isEmpty()) {
            saveMessageQueueContexts(messageContexts);
        }
    }

    private void processMessageContexts(
            ProductOrderSheet sheet,
            OrderStatusTransferDto transferDto,
            Set<MessageQueueContext> messageContexts) {
        if (transferDto != null) {
            if (sheet.getOrderStatus().isClaimRequestOrder())
                sheet.setClaimReason(transferDto.getReason(), transferDto.getDetailReason());
        }
        messageContexts.addAll(orderAlimTalkMessageConversion.convert(sheet));
    }

    private Map<Long, OrderStatusTransferDto> toOrderIdMap(
            List<OrderStatusTransferDto> orderStatusTransfers) {
        return orderStatusTransfers.stream()
                .collect(
                        Collectors.toMap(
                                OrderStatusTransferDto::getOrderId,
                                Function.identity(),
                                (existing, newOne) -> newOne));
    }

    private Map<Long, List<ProductOrderSheet>> toPaymentIdMap(
            List<ProductOrderSheet> productOrderSheets) {
        return productOrderSheets.stream()
                .collect(Collectors.groupingBy(ProductOrderSheet::getPaymentId));
    }
}
