package com.connectly.partnerAdmin.module.external.service.order.lf;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.connectly.partnerAdmin.module.coreServer.ExternalProductFetchInterface;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroup;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.mapper.LfOrderMapper;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderRepository;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderFetchService;
import com.connectly.partnerAdmin.module.notification.service.slack.SlackOrderIssueService;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.service.BasePaymentService;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LfOrderIssueService {

    private static final Logger log = LoggerFactory.getLogger(LfOrderIssueService.class);
    private final ExternalOrderFetchService externalOrderFetchService;
    private final ExternalProductFetchInterface externalProductFetchInterface;
    private final ProductGroupFetchService productGroupFetchService;
    private final BasePaymentService basePaymentService;
    private final ExternalOrderRepository externalOrderRepository;
    private final SlackOrderIssueService slackOrderIssueService;


    public long syncOrder(LfOrderRequestResponseDto responseDto) {

        Map<Integer, List<LfOrderRequestResponseDto.Body>> collect = responseDto.body().stream()
            .collect(Collectors.groupingBy(b -> b.order().orderNo()));


        collect.forEach((key, value) -> {
            boolean b = externalOrderFetchService.doesHasSyncOrder(9L, key);
            if (!b) {

                value.forEach(body -> {
                    DefaultExternalProductGroupContext externalProductGroupContext = externalProductFetchInterface.fetchBySiteIdAndExternalProductGroupId(
                        9L, body.order().productCode());

                    DefaultExternalProductGroup defaultExternalProductGroup = externalProductGroupContext.externalProductGroup();
                    List<ProductGroupDetailResponse> productGroupDetailResponses = productGroupFetchService.fetchProductGroups(
                        List.of(defaultExternalProductGroup.getProductGroupId()));

                    if (productGroupDetailResponses.getFirst()
                        != null) {
                        ProductGroupDetailResponse productGroupDetailResponse = productGroupDetailResponses.getFirst();
                        OrderSheet orderSheet = LfOrderMapper.toCreateOrder(body.order(), productGroupDetailResponse);
                        CreatePayment createPayment = LfOrderMapper.toCreatePayment(body.order(),
                            Collections.singletonList(orderSheet));
                        List<Order> orders = basePaymentService.doPay(createPayment);

                        sendSlack(orders);

                        orders.forEach(o -> {
                            ExternalOrder interlockingOrder = toInterlockingOrder(body.order(), o);
                            externalOrderRepository.save(interlockingOrder);
                        });
                    }

                });

            }

        });

        return responseDto.body().size();

    }

    private ExternalOrder toInterlockingOrder(LfOrderRequestResponseDto.Order lfOrder, Order order){
        return ExternalOrder.builder()
            .siteId(9L)
            .paymentId(order.getPayment().getId())
            .orderId(order.getId())
            .externalIdx(lfOrder.orderNo())
            .externalOrderPkId(String.valueOf(lfOrder.ordDtlNo()))
            .build();
    }

    private void sendSlack(List<Order> orders){
        if(orders == null || orders.isEmpty()) return;
        Order first = orders.getFirst();
        try {
            long id = first.getPayment().getId();

            slackOrderIssueService.sendSlackMessage(id);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }



}
