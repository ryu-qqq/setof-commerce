package com.setof.connectly.module.product.service.stock.fetch;

import com.setof.connectly.module.exception.payment.ExceedStockException;
import com.setof.connectly.module.product.dto.stock.StockDto;
import com.setof.connectly.module.product.service.individual.fetch.ProductFindService;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealTimeStockCheckServiceImpl implements RealTimeStockCheckService {

    public static final String CODE = "https://m.mustit.co.kr/product/product_detail/";
    public static final String[] SOLD_OUT_TAGS = {
        "#productDetailInfo > div:nth-child(13) > div:nth-child(2) > input",
        "#productDetailInfo > div:nth-child(12) > div:nth-child(2) > input",
        "productDetailInfo > div.order_btn_container > div.btn_box > input"
    };

    public static final String MULTI_OPTION_LINE = "#productOption > ul > li";
    public static final String MULTI_OPTION_CART_LINE = "class_cart_in.addOption";
    public static final String SINGLE_OPTION_LINE_1 =
            "#productDetailInfo > div:nth-child(10) > div > div:nth-child(2) > div >"
                    + " button:nth-child(1)";
    public static final String SINGLE_OPTION_LINE_2 =
            "#productDetailInfo > div:nth-child(9) > div > div:nth-child(2) > div >"
                    + " button:nth-child(1)";
    public static final String SINGLE_CART_LINE = "class_cart_in.changeStock";

    private static final String PARSING_OPTION_ERROR_MSG =
            "머스트잇 멀티 옵션 제품 재고 파싱에 실패했습니다. 실패 처리 하겠습니다.";
    private static final String PARSING_SINGLE_ERROR_MSG =
            "머스트잇 싱글 옵션 제품 재고 파싱에 실패했습니다. 실패 처리 하겠습니다.";

    private final ProductFindService productFindService;

    private final ApplicationEventPublisher eventPublisher;

    public void isOutOfStock(StockDto findStock, int qty, Integer mustItQty) {
        if (mustItQty != null) {
            if (mustItQty - qty < 0) {
                processOutOfStock(findStock);
            }
        } else {
            processOutOfStock(findStock);
        }
    }

    public void processOutOfStock(StockDto findStock) {
        // List<Long> productIds = Collections.singletonList(findStock.getProductId());
        // eventPublisher.publishEvent(new ProductStatusUpdateEvent(productIds, Yn.Y));
        throw new ExceedStockException(findStock.getProductGroupName(), 0);
    }

    @Override
    public void isEnoughProduct(StockDto findStock, long productId, int qty) {
        Document document = null;
        Connection conn = Jsoup.connect(CODE + findStock.getCrawlProductSku());
        try {
            document = conn.get();
        } catch (Exception e) {
            throw new RuntimeException("MUSTIT 사이트 접속 실패....");
        }

        if (isProductEnded(document)) {
            isOutOfStock(findStock, qty, null);
        }
        if (isSoldOutBasedOnTags(document, findStock.getCrawlProductSku())) {
            isOutOfStock(findStock, qty, null);
        }

        isOptionAvailable(document, findStock, qty);
    }

    private void isOptionAvailable(Document document, StockDto findStock, int qty) {
        Optional<String> optionNameOpt =
                productFindService.fetchOptionName(findStock.getProductId());
        String targetOptionName = "";
        if (optionNameOpt.isPresent()) {
            targetOptionName = cleanOptionName(optionNameOpt.get());
        }

        if (hasMultipleOptions(document)) {
            checkMultipleOptions(document, targetOptionName, findStock, qty);
        } else {
            checkSingleOption(document, findStock, qty);
        }
    }

    private void checkMultipleOptions(
            Document document, String cleanedOptionName, StockDto findStock, int qty) {
        Elements stockList = document.select(MULTI_OPTION_LINE);
        if (stockList.size() > 0) {
            boolean matched = false;
            for (Element e : stockList) {
                int idx = e.toString().indexOf(MULTI_OPTION_CART_LINE);
                String[] sliceOptionNameLine = getSliceOptionNameLine(e.toString(), idx);
                String s3 = cleanOptionName(sliceOptionNameLine[0]);
                if (s3.equals(cleanedOptionName)) {
                    int mustItQty = Integer.parseInt(cleanOptionName(sliceOptionNameLine[1]));
                    isOutOfStock(findStock, qty, mustItQty);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                log.error(PARSING_OPTION_ERROR_MSG);
                isOutOfStock(findStock, qty, null);
            }
        }
    }

    private void checkSingleOption(Document document, StockDto findStock, int qty) {
        boolean isStockChecked = false;

        String[] selectors = {SINGLE_OPTION_LINE_1, SINGLE_OPTION_LINE_2};
        for (String selector : selectors) {
            Elements e = document.select(selector);
            int idx = e.toString().indexOf(SINGLE_CART_LINE);
            if (idx > 0) {
                String[] sliceOptionNameLine = getSliceOptionNameLine(e.toString(), idx);
                int mustItQty =
                        Integer.parseInt(
                                cleanOptionName(
                                        sliceOptionNameLine[sliceOptionNameLine.length - 1]));
                isOutOfStock(findStock, qty, mustItQty);
                isStockChecked = true;
                break;
            }
        }

        if (!isStockChecked) {
            log.error(PARSING_SINGLE_ERROR_MSG);
            isOutOfStock(findStock, qty, null);
        }
    }

    private String[] getSliceOptionNameLine(String ele, int idx) {
        String substring = ele.substring(idx);
        String s = extractOptionInfo(substring);
        return s.split(",");
    }

    private String extractOptionInfo(String html) {
        String regex = "addOption\\((.*?)\\) ;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            // 매치된 내용의 첫 번째 그룹을 반환합니다.
            return matcher.group(1);
        }
        return ""; // 매치되는 내용이 없으면 빈 문자열 반환
    }

    private boolean hasMultipleOptions(Document document) {
        return !document.select(MULTI_OPTION_LINE).isEmpty();
    }

    private boolean isProductEnded(Document document) {
        return document.body().toString().contains("<body></body>");
    }

    private boolean isSoldOutBasedOnTags(Document document, long crawlProductSku) {
        for (String tag : SOLD_OUT_TAGS) {
            Elements elements = document.select(tag);
            if (elements.toString().replaceAll(" ", "").contains("품절상품")) {
                log.info("해당 상품은 품절 상태입니다. ITEM_CODE = {}", crawlProductSku);
                return true; // 품절 상태인 경우
            }
        }
        return false; // 품절 상태가 아닌 경우
    }

    private String cleanOptionName(String optionName) {
        return optionName
                .replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "")
                .replaceAll("[ㅡㅡ]", "")
                .replaceAll("[']", "");
    }
}
