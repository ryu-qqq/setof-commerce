package com.setof.connectly.module.utils;

import com.setof.connectly.module.common.mapper.CursorValueProvider;
import com.setof.connectly.module.display.enums.component.OrderType;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class SortUtils {

    public static String setCursorValue(
            CursorValueProvider cursorValueProvider, OrderType orderType) {
        if (orderType == null) return null;

        switch (orderType) {
            case RECENT:
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return cursorValueProvider.getInsertDate().format(formatter);
            case HIGH_PRICE:
            case LOW_PRICE:
                return String.valueOf(cursorValueProvider.getSalePrice());
            case HIGH_DISCOUNT:
            case LOW_DISCOUNT:
                return String.valueOf(cursorValueProvider.getDiscountRate());
            case HIGH_RATING:
                return String.valueOf(cursorValueProvider.getAverageRating());
            case REVIEW:
                return String.valueOf(cursorValueProvider.getReviewCount());
            case RECOMMEND:
                return String.valueOf(cursorValueProvider.getScore());
            default:
                return null;
        }
    }

    public static Comparator<CursorValueProvider> getComparatorBasedOnOrderType(
            OrderType orderType) {
        if (orderType == OrderType.RECOMMEND) {
            return Comparator.comparingDouble(CursorValueProvider::getScore).reversed();
        } else if (orderType == OrderType.REVIEW) {
            return Comparator.comparingDouble(CursorValueProvider::getReviewCount).reversed();
        } else if (orderType == OrderType.RECENT) {
            return Comparator.comparingLong(CursorValueProvider::getId).reversed();
        } else if (orderType == OrderType.HIGH_RATING) {
            return Comparator.comparingDouble(CursorValueProvider::getAverageRating).reversed();
        } else if (orderType == OrderType.LOW_PRICE) {
            return Comparator.comparingDouble(CursorValueProvider::getSalePrice);
        } else if (orderType == OrderType.HIGH_PRICE) {
            return Comparator.comparingDouble(CursorValueProvider::getSalePrice).reversed();
        } else if (orderType == OrderType.LOW_DISCOUNT) {
            return Comparator.comparingDouble(CursorValueProvider::getDiscountRate);
        } else if (orderType == OrderType.HIGH_DISCOUNT) {
            return Comparator.comparingDouble(CursorValueProvider::getDiscountRate).reversed();
        } else {
            return null;
        }
    }
}
