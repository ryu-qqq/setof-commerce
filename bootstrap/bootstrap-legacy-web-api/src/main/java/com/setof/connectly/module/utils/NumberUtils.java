package com.setof.connectly.module.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

    public static long doubleToLong(double value) {
        String format = String.format("%.0f", Math.floor(value * 10) / 10);
        return Long.parseLong(format);
    }

    public static double makeDecimalFormat(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        String formattedResult = decimalFormat.format(value);
        double aDouble = Double.parseDouble(formattedResult);
        return Math.ceil(aDouble);
    }

    public static long downDotNumber(double userGradeRatio, double salePrice) {
        BigDecimal mileageReserveRateBD = BigDecimal.valueOf(userGradeRatio);
        BigDecimal salePriceBD = BigDecimal.valueOf(salePrice);
        BigDecimal expectedMileageAmountBD = salePriceBD.multiply(mileageReserveRateBD);
        BigDecimal bigDecimal = expectedMileageAmountBD.setScale(0, RoundingMode.DOWN);
        return bigDecimal.longValue();
    }

    public static BigDecimal getProPortion(
            double totalOrderAmount, long orderAmount, double usedMileageAmount) {
        if (totalOrderAmount == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal proportion =
                BigDecimal.valueOf(orderAmount)
                        .divide(BigDecimal.valueOf(totalOrderAmount), 2, RoundingMode.HALF_UP);

        return BigDecimal.valueOf(usedMileageAmount)
                .multiply(proportion)
                .setScale(0, RoundingMode.DOWN);
    }
}
