package com.connectly.partnerAdmin.module.external.service.order;

import com.connectly.partnerAdmin.module.external.dto.order.ExcelOrderSheet;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.external.mapper.ExternalOrderMapper;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderRepository;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.service.BasePaymentService;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class ExternalOrderExcelIssueServiceImpl implements ExternalOrderExcelIssueService{

    private final ProductGroupFetchService productGroupFetchService;
    private final BasePaymentService basePaymentService;
    private final ExternalOrderMapper<ExcelOrderSheet, ExcelOrderSheet> externalOrderMapper;
    private final ExternalOrderRepository externalOrderRepository;


    @Override
    public List<ExternalOrder> syncOrders(List<ExcelOrderSheet> excelOrderSheets) {
        List<Long> productGroupIds = excelOrderSheets.stream().map(ExcelOrderSheet::getProductGroupId).toList();


        List<ProductGroupDetailResponse> productGroupDetailResponses = productGroupFetchService.fetchProductGroups(productGroupIds);
        Map<Long, ProductGroupDetailResponse> productGroupIdMap = toProductGroupIdMap(productGroupDetailResponses);


        return excelOrderSheets.stream()
                .flatMap(excelOrderSheet -> {
                    ProductGroupDetailResponse productGroupDetailResponse = productGroupIdMap.get(excelOrderSheet.getProductGroupId());
                    if (productGroupDetailResponse == null) {
                        throw new ExternalMallProductNotFoundException(excelOrderSheet.getSiteName().getName(), String.valueOf(excelOrderSheet.getProductGroupId()));
                    }
                    OrderSheet createOrder = externalOrderMapper.toCreateOrder(excelOrderSheet, productGroupDetailResponse);
                    CreatePayment createPayment = externalOrderMapper.toCreatePayment(excelOrderSheet, Collections.singletonList(createOrder));
                    List<Order> orders = basePaymentService.doPay(createPayment);
                    return orders.stream()
                            .map(order -> saveInterlockingOrder(excelOrderSheet, order));
                })
                .collect(Collectors.toList());
    }

    private Map<Long, ProductGroupDetailResponse> toProductGroupIdMap(List<ProductGroupDetailResponse> productGroupDetailResponses){
        return productGroupDetailResponses.stream()
                .collect(Collectors.toMap(ProductGroupDetailResponse::getProductGroupId, Function.identity(),
                        (v1, v2) -> v1)
                );
    }

    private ExternalOrder saveInterlockingOrder(ExcelOrderSheet t, Order order){
        ExternalOrder externalOrder = toInterlockingOrder(t, order);
        return externalOrderRepository.save(externalOrder);
    }


    private ExternalOrder toInterlockingOrder(ExcelOrderSheet t, Order order){
        return ExternalOrder.builder()
                .siteId(t.getSiteName().getSiteId())
                .paymentId(order.getPayment().getId())
                .orderId(order.getId())
                .externalIdx(t.getExMallOrderId())
                .externalOrderPkId(t.getExMallOrderCode())
                .build();
    }

}
