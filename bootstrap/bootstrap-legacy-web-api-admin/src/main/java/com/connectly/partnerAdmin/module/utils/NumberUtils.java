package com.connectly.partnerAdmin.module.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {


    public static BigDecimal makeDecimalFormat(BigDecimal value){
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        String formattedResult = decimalFormat.format(value);
        BigDecimal aBigDecimal = new BigDecimal(formattedResult);
        return aBigDecimal.setScale(0, RoundingMode.CEILING);
    }


}
