package com.connectly.partnerAdmin.module.external.aop;


import com.connectly.partnerAdmin.module.external.core.ExMallOrderUpdate;
import com.connectly.partnerAdmin.module.external.dto.order.ExternalOrderMappingDto;
import com.connectly.partnerAdmin.module.external.dto.order.InterlockingOrderUpdate;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderFetchService;
import com.connectly.partnerAdmin.module.external.service.order.ExternalOrderIssueService;
import com.connectly.partnerAdmin.module.order.dto.query.UpdateOrder;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class SyncOrderUpdateAsepect {
    private final ExternalOrderFetchService externalOrderFetchService;
    private final ExternalOrderIssueService externalOrderIssueService;

    @Pointcut("execution(* com.connectly.partnerAdmin.module.order.service.OrderUpdateServiceImpl.updateOrder(..))")
    private void syncExMallOrder(){}

    @Pointcut("execution(* com.connectly.partnerAdmin.module.order.service.OrderUpdateServiceImpl.updateOrders(..))")
    private void syncExMallOrders(){}

    @Around(value = "syncExMallOrder()", argNames = "pjp")
    public Object interlockingExMallOrder(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        Object[] args = pjp.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof UpdateOrder) {
            UpdateOrder updateOrder = (UpdateOrder) args[0];
            interlockingOrders(Collections.singletonList(updateOrder));
            return proceed;
        }
        return proceed;
    }


    @Around(value = "syncExMallOrders()", argNames = "pjp")
    public Object interlockingExMallOrders(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();

        Object[] args = pjp.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof List) {
            List<?> list = (List<?>) args[0];
            if (!list.isEmpty() && list.get(0) instanceof UpdateOrder) {
                List<? extends UpdateOrder> updateOrders = (List<? extends UpdateOrder>) list;
                interlockingOrders(updateOrders);
                return proceed;
            }
        }
        return proceed;
    }

    private void interlockingOrders(List<? extends UpdateOrder> updateOrders){
        Map<Long, ? extends UpdateOrder> updateOrderMap = mapUpdateOrdersById(updateOrders);
        Map<Long, ExternalOrderMappingDto> interlockingOrderMap = mapInterlockingOrdersById(updateOrderMap.keySet());
        List<ExMallOrderUpdate<? extends UpdateOrder>> interlockingOrderUpdates = groupOrdersBySiteName(interlockingOrderMap, updateOrderMap);
        externalOrderIssueService.syncOrdersUpdate(interlockingOrderUpdates);

    }

    private Map<Long, ? extends UpdateOrder> mapUpdateOrdersById(List<? extends UpdateOrder> updateOrders) {
        return updateOrders.stream()
                .collect(Collectors.toMap(UpdateOrder::getOrderId, Function.identity(), (existing, replacement) -> replacement));
    }

    private Map<Long, ExternalOrderMappingDto> mapInterlockingOrdersById(Set<Long> orderIds) {
        List<ExternalOrderMappingDto> interlockingOrders = externalOrderFetchService.doesHasSyncOrders(new ArrayList<>(orderIds));
        return interlockingOrders.stream()
                .collect(Collectors.toMap(ExternalOrderMappingDto::getOrderId, Function.identity(), (existing, replacement) -> replacement));
    }

    private List<ExMallOrderUpdate<? extends UpdateOrder>> groupOrdersBySiteName(Map<Long, ExternalOrderMappingDto> orderIdMap, Map<Long, ? extends UpdateOrder> updateOrderMap) {
        return orderIdMap.values().stream()
                .filter(orderMapping -> updateOrderMap.containsKey(orderMapping.getOrderId()))
                .map(orderMapping ->
                        InterlockingOrderUpdate.builder()
                        .updateOrder(updateOrderMap.get(orderMapping.getOrderId()))
                        .orderId(orderMapping.getOrderId())
                        .exMallOrderId(orderMapping.getExternalIdx())
                        .siteName(SiteName.of(orderMapping.getSiteId()))
                        .build())
                .collect(Collectors.toList());
    }



}
