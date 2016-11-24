package com.androidcalc.ertai87.androidcalc.common;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CalcUtils {

    public static String convertBaseToDecimal(String num, int base) {
        if (base == 10) return num;
        int pointIndex = (num.contains(".") ? num.indexOf(".") : num.length());
        int power;
        boolean neg = false;

        BigInteger intpart = new BigInteger(num.substring(0, pointIndex), base);

        if (pointIndex == num.length()) return intpart.toString();
        if (pointIndex == num.length() - 1) return intpart.toString() + ".";

        if (intpart.compareTo(BigInteger.ZERO) == -1) {
            neg = true;
            intpart = BigInteger.ZERO.subtract(intpart);
        }

        BigDecimal val = new BigDecimal(intpart.toString());
        power = 0;
        for (int i = pointIndex + 1; i < num.length(); i++) {
            power--;
            BigDecimal digit;
            try {
                digit = new BigDecimal("" + num.charAt(i));
            } catch (NumberFormatException e) {
                digit = new BigDecimal("" + (num.charAt(i) - 'A' + 10));
            }
            double baseConversion = Math.pow(base, power);
            val = val.add(digit.multiply(new BigDecimal(baseConversion)));
        }
        return (neg ? "-" : "") + val;
    }

    public static String convertDecimalToBase(String num, int base) {
        if (base == 10) return num;
        String ret;
        int pointIndex = num.indexOf(".");
        boolean neg = false;
        BigDecimal n = ("".equals(num) ? BigDecimal.ZERO : new BigDecimal(num));
        BigDecimal b = new BigDecimal(base);

        if (n.compareTo(BigDecimal.ZERO) == -1) {
            neg = true;
            n = BigDecimal.ZERO.subtract(n);
        }
        ret = n.toBigInteger().toString(base).toUpperCase();
        n = n.remainder(BigDecimal.ONE);

        if (pointIndex != -1) {
            ret += ".";
            for (int i = 0; i <= CalcConstants.MAX_DIGITS && n.compareTo(BigDecimal.ZERO) != 0; i++) {
                n = n.multiply(b);
                if (n.intValue() < 10) {
                    ret += n.intValue();
                } else {
                    ret += (char) ('A' + n.intValue() - 10);
                }
                n = n.remainder(BigDecimal.ONE);
            }
        }
        if (neg) ret = "-" + ret;
        return ret;
    }
}
