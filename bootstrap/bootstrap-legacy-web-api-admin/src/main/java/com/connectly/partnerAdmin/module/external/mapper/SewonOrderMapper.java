package com.connectly.partnerAdmin.module.external.mapper;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.connectly.partnerAdmin.module.external.dto.order.sellic.SellicOrder;
import com.connectly.partnerAdmin.module.external.enums.sellic.SellicStieName;
import com.connectly.partnerAdmin.module.external.exception.ExternalMallProductNotFoundException;
import com.connectly.partnerAdmin.module.generic.money.Money;
import com.connectly.partnerAdmin.module.order.dto.query.BaseOrderSheet;
import com.connectly.partnerAdmin.module.order.dto.query.OrderSheet;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.payment.dto.query.CreatePayment;
import com.connectly.partnerAdmin.module.payment.enums.PaymentMethodEnum;
import com.connectly.partnerAdmin.module.product.dto.ProductFetchResponse;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupDetailResponse;


@Component
public class SewonOrderMapper extends AbstractExternalOrderMapper<SellicOrder, SellicOrder> {

    private static final Set<Long> DISNEY_SELLER = Set.of(56L, 57L, 58L, 59L, 60L, 61L, 62L, 63L, 64L, 65L, 66L, 67L, 68L, 69L, 70L, 71L, 72L, 73L,
        74L,
        75L,
        76L,
        77L,
        78L,
        79L,
        80L,
        81L,
        82L,
        83L,
        84L,
        85L,
        86L,
        87L,
        88L
    );

    @Override
    public CreatePayment toCreatePayment(SellicOrder sellicOrder, List<OrderSheet> toCreateOrders) {

        Optional<OrderSheet> any = toCreateOrders.stream()
            .filter(orderSheet -> DISNEY_SELLER.contains(orderSheet.getSellerId()))
            .findAny();

        Money paymentAmount;

        if(any.isPresent()){
            paymentAmount = Money.wons(sellicOrder.getPaymentAmount());
        }else{
            paymentAmount = toCreateOrders.stream()
                .map(OrderSheet::getOrderAmount)
                .reduce(Money.ZERO, Money::plus);
        }

        ZonedDateTime zoned = OffsetDateTime.parse(sellicOrder.getOrderDate())
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"));

        return CreatePayment.builder()
                .payAmount(paymentAmount)
                .mileageAmount(DEFAULT_MILEAGE_AMOUNT)
                .payMethod(PaymentMethodEnum.CARD)
                .shippingInfo(toUserShippingInfo(sellicOrder))
                .orders(toCreateOrders)
                .userId(DEFAULT_USER_ID)
                .siteName(SiteName.SEWON)
                .paymentUniqueId(generatePaymentUniqueKey(sellicOrder))
                .paymentDate(zoned.toLocalDateTime())
                .build();
    }

    @Override
    public BaseOrderSheet toCreateOrder(SellicOrder sellicOrder, ProductGroupDetailResponse productGroupsResponse){

        long sellerId = productGroupsResponse.getProductGroup().getSellerId();
        Money salePrice = Money.wons((productGroupsResponse.getProductGroup().getPrice().getCurrentPrice()));

        Optional<ProductFetchResponse> product = getProduct(productGroupsResponse.getProducts(), sellicOrder.getOptionName());
        if(product.isEmpty()) throw new ExternalMallProductNotFoundException(SiteName.SEWON.getName(), String.valueOf(sellicOrder.getExternalProductGroupId()));

        return BaseOrderSheet.builder()
                .orderAmount(salePrice)
                .productId(product.get().getProductId())
                .sellerId(sellerId)
                .quantity(sellicOrder.getQty())
                .userId(DEFAULT_USER_ID)
                .build();
    }

    private String generatePaymentUniqueKey(SellicOrder sellicOrder){
        SellicStieName sellicStieName = SellicStieName.of(sellicOrder.getMallId());
        return String.format("%s_%s_%s", SiteName.SEWON.getName(), sellicStieName.name(), sellicOrder.getOrderId());
    }

    private Optional<ProductFetchResponse> getProduct(Set<ProductFetchResponse> products, String optionName){
        String normalizedOption = normalize(optionName);

        // 1차: 정확히 일치
        Optional<ProductFetchResponse> exactMatch = products.stream()
            .filter(p -> {
                String normalize = normalize(p.getOption());
                return normalizedOption.equals(normalize);
            })
            .findFirst();

        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // 2차: 옵션 순서 바꿔서 비교 (블랙S vs S블랙)
        return products.stream()
            .filter(p -> matchesWithReversedOptions(p, normalizedOption))
            .findFirst();
    }

    private boolean matchesWithReversedOptions(ProductFetchResponse product, String normalizedExternalOption) {
        if (product.getOptions() == null || product.getOptions().size() ==1) {
            return normalizedExternalOption.equals("단일상품") || normalizedExternalOption.isEmpty();
        }


        if (product.getOptions().size() < 2) {
            return false;
        }

        // 옵션들의 optionValue를 역순으로 조합해서 비교
        List<String> optionValues = product.getOptions().stream()
            .map(opt -> normalize(opt.getOptionValue()))
            .filter(v -> !v.isEmpty())
            .toList();

        if (optionValues.size() < 2) {
            return false;
        }

        // 모든 순열 조합 생성해서 비교
        return generatePermutations(optionValues).stream()
            .map(perm -> String.join("", perm))
            .anyMatch(combined -> combined.equals(normalizedExternalOption));
    }

    private List<List<String>> generatePermutations(List<String> values) {
        List<List<String>> result = new ArrayList<>();
        if (values.size() <= 1) {
            result.add(new ArrayList<>(values));
            return result;
        }

        // 2개 옵션인 경우 간단히 처리
        if (values.size() == 2) {
            result.add(List.of(values.get(0), values.get(1)));
            result.add(List.of(values.get(1), values.get(0)));
            return result;
        }


        return result;
    }

    private String normalize(String input) {
        if (input == null) {
            return "";
        }
        return input
            .toLowerCase()  // 대소문자 무시
            .replaceAll("(?i)DEFAULT_ONE|DEFAULT_TWO|COLOR|SIZE|AMP", "")
            .replaceAll("추가안함", "")
            .replaceAll("[^a-z0-9가-힣]", "");  // 영문, 숫자, 한글만 남기고 모든 특수문자 제거
    }


}
