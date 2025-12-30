package com.connectly.partnerAdmin.module.external.service.order;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.common.exception.ProviderMappingException;
import com.connectly.partnerAdmin.module.coreServer.ExternalProductFetchInterface;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroup;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicOrder;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.external.mapper.ExternalOrderMapper;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderRepository;
import com.connectly.partnerAdmin.module.external.service.order.sewon.SellicOrderUpdateService;
import com.connectly.partnerAdmin.module.external.service.order.sewon.SellicOrderUpdateServiceProvider;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.entity.Order;
import com.connectly.partnerAdmin.module.order.enums.OrderStatus;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.service.BasePaymentService;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SellicOrderIssueService extends AbstractOrderExternalService<SellicOrder, SellicOrder> {

    private static final String NOT_SUPPORTED_ORDER_STATUS_MSG = "SELLIC 주문 연동에 지원하지 않는 주문 상태입니다.";
    private static final String ERROR_MSG = "SELLIC 주문 연동에 지원하지 않는 주문 상태: %s";

    private final SellicOrderUpdateServiceProvider<UpdateOrder> sellicOrderUpdateServiceProvider;

    public SellicOrderIssueService(BasePaymentService basePaymentService,
                                   ExternalProductFetchInterface externalProductFetchInterface,
                                   ProductGroupFetchService productGroupFetchService,
                                   ExternalOrderRepository externalOrderRepository,
                                   ExternalOrderMapper<SellicOrder, SellicOrder> externalOrderMapper,
                                   SellicOrderUpdateServiceProvider<UpdateOrder> sellicOrderUpdateServiceProvider) {
        super(basePaymentService, externalProductFetchInterface, productGroupFetchService, externalOrderRepository,
            externalOrderMapper);
        this.sellicOrderUpdateServiceProvider = sellicOrderUpdateServiceProvider;
    }

    @Override
    public List<ExternalOrder> syncOrder(SellicOrder sellicOrder) {

        DefaultExternalProductGroupContext externalProductGroupContext = fetchBySiteIdAndExternalProductGroupId(
            sellicOrder.getSiteId(), String.valueOf(sellicOrder.getExternalProductGroupId()));

        DefaultExternalProductGroup externalProductGroup = externalProductGroupContext.externalProductGroup();

        Map<Long, ProductGroupDetailResponse> productGroupIdMap = toProductGroupIdMap(Collections.singletonList(externalProductGroup.getProductGroupId()));
        ProductGroupDetailResponse productGroupDetailResponse = productGroupIdMap.get(externalProductGroup.getProductGroupId());

        if(productGroupDetailResponse == null) throw new ExternalMallProductNotFoundException(getSiteName().getName(), String.valueOf(sellicOrder.getExternalProductGroupId()));

        OrderSheet orderSheet = toCreateOrder(sellicOrder, productGroupDetailResponse);
        CreatePayment createPayment = toCreatePayment(sellicOrder, Collections.singletonList(orderSheet));

        List<Order> orders = doPay(createPayment);


        return orders.stream()
                .map(order -> saveInterlockingOrder(sellicOrder, order))
                .collect(Collectors.toList());

    }


    @Override
    public void syncOrdersUpdate(ExMallOrderUpdate<? extends UpdateOrder> exMallOrderUpdate) {
        OrderStatus orderStatus = exMallOrderUpdate.getUpdateOrder().getOrderStatus();
        try{
            SellicOrderUpdateService<UpdateOrder> updateOrderSellicOrderUpdateService = sellicOrderUpdateServiceProvider.get(orderStatus);
            updateOrderSellicOrderUpdateService.updateOrder((ExMallOrderUpdate<UpdateOrder>) exMallOrderUpdate);
        }catch (ProviderMappingException ex){
            log.warn(NOT_SUPPORTED_ORDER_STATUS_MSG);
        }catch (Exception ex) {
            log.error(ERROR_MSG, ex);
        }
    }


    @Override
    public SiteName getSiteName() {
        return SiteName.SEWON;
    }




}
