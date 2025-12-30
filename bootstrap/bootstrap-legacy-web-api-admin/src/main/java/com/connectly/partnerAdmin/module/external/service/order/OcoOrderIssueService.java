package com.connectly.partnerAdmin.module.external.service.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.common.exception.ProviderMappingException;
import com.connectly.partnerAdmin.module.coreServer.ExternalProductFetchInterface;
import com.connectly.partnerAdmin.module.coreServer.response.DefaultExternalProductGroupContext;
import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrder;
import com.connectly.partnerAdmin.module.external.dto.order.oco.OcoOrderItem;
import com.connectly.partnerAdmin.module.external.entity.ExternalOrder;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.external.mapper.ExternalOrderMapper;
import com.connectly.partnerAdmin.module.external.repository.order.ExternalOrderRepository;
import com.connectly.partnerAdmin.module.external.service.order.oco.OcoOrderUpdateService;
import com.connectly.partnerAdmin.module.external.service.order.oco.OcoOrderUpdateServiceProvider;
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
public class OcoOrderIssueService extends AbstractOrderExternalService<OcoOrder, OcoOrderItem> {

    private static final String NOT_SUPPORTED_ORDER_STATUS_MSG = "OCO 주문 연동에 지원하지 않는 주문 상태입니다.";
    private static final String ERROR_MSG = "OCO 주문 연동에 지원하지 않는 주문 상태: %s";

    private final OcoOrderUpdateServiceProvider<UpdateOrder> ocoOrderUpdateServiceProvider;

    public OcoOrderIssueService(BasePaymentService basePaymentService,
                                ExternalProductFetchInterface externalProductFetchInterface,
                                ProductGroupFetchService productGroupFetchService,
                                ExternalOrderRepository externalOrderRepository,
                                ExternalOrderMapper<OcoOrder, OcoOrderItem> externalOrderMapper,
                                OcoOrderUpdateServiceProvider<UpdateOrder> ocoOrderUpdateServiceProvider) {
        super(basePaymentService, externalProductFetchInterface, productGroupFetchService, externalOrderRepository,
            externalOrderMapper);
        this.ocoOrderUpdateServiceProvider = ocoOrderUpdateServiceProvider;
    }
    @Override
    public List<ExternalOrder> syncOrder(OcoOrder ocoOrder) {
        List<DefaultExternalProductGroupContext> externalProductMappings = fetchExternalProductGroups(ocoOrder);


        Map<Long, DefaultExternalProductGroupContext> externalMappingProductGroupIdMap = getProductGroupIdMap(externalProductMappings);
        Map<String, DefaultExternalProductGroupContext> externalIdMap = getExternalIdMap(externalProductMappings);

        Map<Long, OcoOrderItem> externalOrderProductMap = getExternalOrderProductMap(ocoOrder.getOrderItemList());
        Map<Long, ProductGroupDetailResponse> productGroupIdMap = toProductGroupIdMap(new ArrayList<>(externalMappingProductGroupIdMap.keySet()));

        List<OrderSheet> orderSheets = new ArrayList<>();
        externalIdMap.forEach((externalId, interlockingProductDto) -> {
            OcoOrderItem ocoOrderItem = externalOrderProductMap.get(Long.parseLong(externalId));
            if(ocoOrderItem == null) throw new ExternalMallProductNotFoundException(getSiteName().getName(), externalId);

            ocoOrderItem.setProductGroupId(interlockingProductDto.externalProductGroup().getProductGroupId());
            ProductGroupDetailResponse productGroupDetailResponse = productGroupIdMap.get(interlockingProductDto.externalProductGroup().getProductGroupId());

            if (productGroupDetailResponse == null) throw new ExternalMallProductNotFoundException(getSiteName().getName(), externalId);
            OrderSheet orderSheet = toCreateOrder(ocoOrderItem, productGroupDetailResponse);
            orderSheets.add(orderSheet);
        });

        CreatePayment createPayment = toCreatePayment(ocoOrder, orderSheets);
        List<Order> orders = doPay(createPayment);


        return orders.stream()
                .map(order -> saveInterlockingOrder(ocoOrder, order))
                .collect(Collectors.toList());
    }

    private List<DefaultExternalProductGroupContext> fetchExternalProductGroups(OcoOrder ocoOrder){
        return ocoOrder.getProductGroupIds().stream()
            .map(id -> fetchBySiteIdAndExternalProductGroupId(ocoOrder.getSiteId(), String.valueOf(id)))
            .toList();
    }

    private Map<Long, DefaultExternalProductGroupContext> getProductGroupIdMap(List<DefaultExternalProductGroupContext> externalProductMappings){
        return externalProductMappings.stream()
                .collect(Collectors.toMap(e -> e.externalProductGroup().getProductGroupId(), Function.identity(),
                        (e1, e2) -> e2));
    }

    private Map<String, DefaultExternalProductGroupContext> getExternalIdMap(List<DefaultExternalProductGroupContext> externalProductMappings){
        return externalProductMappings.stream()
                .collect(Collectors.toMap(e -> e.externalProductGroup().getExternalProductGroupId(), Function.identity(),
                        (e1, e2) -> e2));
    }

    private Map<Long, OcoOrderItem> getExternalOrderProductMap(List<OcoOrderItem> orderItemList){
        return orderItemList.stream()
                .collect(Collectors.toMap(OcoOrderItem::getPid, Function.identity(),
                        (e1, e2) -> e2));
    }


    @Override
    public void syncOrdersUpdate(ExMallOrderUpdate<? extends UpdateOrder> exMallOrderUpdate) {
        OrderStatus orderStatus = exMallOrderUpdate.getUpdateOrder().getOrderStatus();
        try{
            OcoOrderUpdateService<UpdateOrder> updateOrderOcoOrderUpdateService = ocoOrderUpdateServiceProvider.get(orderStatus);
            updateOrderOcoOrderUpdateService.updateOrder((ExMallOrderUpdate<UpdateOrder>) exMallOrderUpdate);
        }catch (ProviderMappingException ex){
            log.warn(NOT_SUPPORTED_ORDER_STATUS_MSG);
        }catch (Exception ex) {
            log.error(ERROR_MSG, ex);
        }

    }


    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

}
