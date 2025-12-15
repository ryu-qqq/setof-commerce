package com.setof.connectly.module.payment.validator;

import com.setof.connectly.module.event.dto.EventProductStockCheck;
import com.setof.connectly.module.event.service.product.EventProductFindService;
import com.setof.connectly.module.exception.event.EventProductQtyExceededException;
import com.setof.connectly.module.exception.payment.InvalidCashAndMileageUseException;
import com.setof.connectly.module.order.dto.OrderProductDto;
import com.setof.connectly.module.order.dto.order.OrderSheet;
import com.setof.connectly.module.order.service.fetch.OrderFindService;
import com.setof.connectly.module.payment.annotation.ValidPayment;
import com.setof.connectly.module.payment.dto.payment.BasePayment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentValidator implements ConstraintValidator<ValidPayment, BasePayment> {

    private final EventProductFindService eventProductFindService;
    private final OrderFindService orderFindService;

    public static final String MILEAGE_ONLY_ERROR_MSG = "해당 상품은 적립금으로만 결제할 수 있는 상품입니다.";
    public static final String CACHE_ONLY_ERROR_MSG = "해당 상품은 적립금을 사용할 수 없는 상품입니다.";
    public static final String MIX_ERROR_MSG = "적립금을 사용하려면 결제 금액이 최소 10,000원 이상이어야 합니다.";
    public static final String UNIT_ERROR_MSG = "적립금은 100원 단위로 사용할 수 있습니다.";
    public static final String EXPIRE_MILEAGE_MSG = "사용할 수 있는 적립금이 존재하지 않습니다.";
    public static final String OVER_USE_MILEAGE_MSG = "적립금 사용량을 초과했습니다.";

    @Override
    public boolean isValid(BasePayment value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        List<Long> productGroupIds = value.getProductGroupIds();
        List<EventProductStockCheck> eventProductStockChecks =
                eventProductFindService.fetchEventProductStockCheck(productGroupIds);
        return validatePaymentRestrictions(eventProductStockChecks, productGroupIds, value);
    }

    private boolean validatePaymentRestrictions(
            List<EventProductStockCheck> eventProductStockChecks,
            List<Long> productGroupIds,
            BasePayment value) {
        Map<Long, EventProductStockCheck> eventProductGroupMap =
                eventProductStockChecks.stream()
                        .collect(
                                Collectors.toMap(
                                        EventProductStockCheck::getProductGroupId,
                                        Function.identity(),
                                        (existing, replacement) -> existing));

        for (Long groupId : productGroupIds) {
            EventProductStockCheck stockCheck = eventProductGroupMap.get(groupId);
            if (stockCheck != null) {
                validatePurchaseLimits(stockCheck, value);
                validatePaymentType(stockCheck, value);
            } else {
                normalProductValidatePayment(value);
            }
        }

        return true;
    }

    private void validatePurchaseLimits(EventProductStockCheck stockCheck, BasePayment value) {
        if (stockCheck.getLimitYn().isYes()) {
            Set<OrderProductDto> orderProducts =
                    orderFindService.fetchProductsOrderedWithinPeriod(
                            stockCheck.getProductIds(), stockCheck.getEventPeriod());
            int totalPurchaseQty = getTotalPurchaseQty(orderProducts);
            List<OrderSheet> orders = value.getOrders();

            Map<Long, List<OrderSheet>> productGroupIdMap =
                    orders.stream().collect(Collectors.groupingBy(OrderSheet::getProductGroupId));
            List<OrderSheet> orderSheets = productGroupIdMap.get(stockCheck.getProductGroupId());
            long requestSum = orderSheets.stream().mapToLong(OrderSheet::getQuantity).sum();

            if (stockCheck.getLimitQty() < totalPurchaseQty + requestSum) {
                throw new EventProductQtyExceededException();
            }
        }
    }

    private void validatePaymentType(EventProductStockCheck stockCheck, BasePayment value) {
        switch (stockCheck.getEventPayType()) {
            case MILEAGE_ONLY:
                if (value.getPayAmount() > 0) {
                    throw new InvalidCashAndMileageUseException(MILEAGE_ONLY_ERROR_MSG);
                }
                break;
            case CASH_ONLY:
                if (value.getMileageAmount() > 0) {
                    throw new InvalidCashAndMileageUseException(CACHE_ONLY_ERROR_MSG);
                }
                break;
            case MIX:
                normalProductValidatePayment(value);
                break;
        }
    }

    private void normalProductValidatePayment(BasePayment value) {
        if (value.getMileageAmount() > 0 && value.getPayAmount() < 10000) {
            throw new InvalidCashAndMileageUseException(MIX_ERROR_MSG);
        }

        if (value.getMileageAmount() % 100 != 0) {
            throw new InvalidCashAndMileageUseException(UNIT_ERROR_MSG);
        }
    }

    private int getTotalPurchaseQty(Set<OrderProductDto> orderProducts) {
        return orderProducts.stream().mapToInt(OrderProductDto::getProductQuantity).sum();
    }
}
