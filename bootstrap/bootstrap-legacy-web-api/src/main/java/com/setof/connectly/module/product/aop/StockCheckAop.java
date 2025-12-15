package com.setof.connectly.module.product.aop;

import com.setof.connectly.module.cart.entity.embedded.CartDetails;
import com.setof.connectly.module.payment.dto.payment.CreatePayment;
import com.setof.connectly.module.payment.dto.payment.CreatePaymentInCart;
import com.setof.connectly.module.product.dto.stock.Sku;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.product.service.stock.StockReservationService;
import com.setof.connectly.module.product.service.stock.fetch.StockCheckService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class StockCheckAop {

    private final StockReservationService stockReservationService;
    private final StockCheckService stockCheckService;

    @Pointcut(value = "execution(* com.setof.connectly.module.payment.service.pay.*.doPay(..))")
    private void checkStockWhenPay() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.payment.service.pay.AbstractPayQueryService.doPayInCart(..))")
    private void checkStockWhenPayInCart() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.cart.service.query.CartQueryServiceImpl.insertOrUpdate(..))")
    private void checkStockWhenCartAdd() {}

    @Pointcut(
            value =
                    "execution(*"
                        + " com.setof.connectly.module.cart.service.query.CartQueryServiceImpl.updateCart(..))")
    private void checkStockWhenCartUpdate() {}

    @Around(value = "checkStockWhenPay() && args(arg)", argNames = "pjp,arg")
    public Object checkStockWhenPay(ProceedingJoinPoint pjp, Object arg) throws Throwable {
        if (arg instanceof CreatePayment) {
            CreatePayment createPayment = (CreatePayment) arg;

            List<StockDto> stocks =
                    createPayment.getOrders().stream()
                            .map(
                                    createOrderInCart ->
                                            new StockDto(
                                                    createOrderInCart.getProductId(),
                                                    createOrderInCart.getQuantity()))
                            .collect(Collectors.toList());

            processCheckStocks(stocks);
        }
        return pjp.proceed();
    }

    @Around(value = "checkStockWhenPayInCart() && args(arg)", argNames = "pjp,arg")
    public Object checkStockWhenPayInCart(ProceedingJoinPoint pjp, Object arg) throws Throwable {
        if (arg instanceof CreatePaymentInCart) {
            CreatePaymentInCart createPaymentInCart = (CreatePaymentInCart) arg;

            List<StockDto> stocks =
                    createPaymentInCart.getOrders().stream()
                            .map(
                                    createOrderInCart ->
                                            new StockDto(
                                                    createOrderInCart.getProductId(),
                                                    createOrderInCart.getQuantity()))
                            .collect(Collectors.toList());

            processCheckStocks(stocks);
        }
        return pjp.proceed();
    }

    @Around(value = "checkStockWhenCartAdd() && args(arg)", argNames = "pjp,arg")
    public Object checkStockWhenCartAdd(ProceedingJoinPoint pjp, Object arg) throws Throwable {
        if (arg instanceof List<?>) {
            List<?> wildList = (List<?>) arg;

            List<StockDto> stocks =
                    wildList.stream()
                            .filter(item -> (item instanceof CartDetails))
                            .map(item -> (CartDetails) item)
                            .map(cd -> new StockDto(cd.getProductId(), cd.getQuantity()))
                            .collect(Collectors.toList());

            processCheckStocks(stocks);
        }
        return pjp.proceed();
    }

    @Around(value = "checkStockWhenCartUpdate() && args(arg1, arg2)", argNames = "pjp,arg1, arg2")
    public Object checkStockWhenCartUpdate(ProceedingJoinPoint pjp, Object arg1, Object arg2)
            throws Throwable {
        if (arg2 instanceof CartDetails) {
            CartDetails cartDetails = (CartDetails) arg2;
            long productId = cartDetails.getProductId();
            int requestQuantity = cartDetails.getQuantity();
            processCheckStocks(Collections.singletonList(new StockDto(productId, requestQuantity)));
        }
        return pjp.proceed();
    }

    private void processCheckStocks(List<? extends Sku> skus) {
        stockCheckService.checkEnoughStocks(skus);
    }
}
