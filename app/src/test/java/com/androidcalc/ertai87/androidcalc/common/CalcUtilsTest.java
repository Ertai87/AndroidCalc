package com.androidcalc.ertai87.androidcalc.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalcUtilsTest {
    @Test
    public void TestSimpleConvertDecimalToBinary(){
        assertEquals(CalcUtils.convertDecimalToBase("2", 2), "10");
    }

    @Test
    public void TestSimpleConvertDecimalToOctal(){
        assertEquals(CalcUtils.convertDecimalToBase("8", 8), "10");
    }

    @Test
    public void TestSimpleConvertDecimalToHexadecimal(){
        assertEquals(CalcUtils.convertDecimalToBase("16", 16), "10");
    }

    @Test
    public void TestNegativeConvertDecimalToBinary(){
        assertEquals(CalcUtils.convertDecimalToBase("-2", 2), "-10");
    }

    @Test
    public void TestNegativeConvertDecimalToOctal(){
        assertEquals(CalcUtils.convertDecimalToBase("-8", 8), "-10");
    }

    @Test
    public void TestNegativeConvertDecimalToHexadecimal(){
        assertEquals(CalcUtils.convertDecimalToBase("-16", 16), "-10");
    }

    @Test
    public void TestConvertToHexWithAlpha(){
        assertEquals(CalcUtils.convertDecimalToBase("10", 16), "A");
    }

    @Test
    public void TestSimpleConvertBinaryToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("10", 2), "2");
    }

    @Test
    public void TestSimpleConvertOctalToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("10", 8), "8");
    }

    @Test
    public void TestSimpleConvertHexadecimalToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("10", 16), "16");
    }

    @Test
    public void TestNegativeConvertBinaryToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("-10", 2), "-2");
    }

    @Test
    public void TestNegativeConvertOctalToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("-10", 8), "-8");
    }

    @Test
    public void TestNegativeConvertHexadecimalToDecimal(){
        assertEquals(CalcUtils.convertBaseToDecimal("-10", 16), "-16");
    }

    @Test
    public void TestConvertFromHexWithAlpha(){
        assertEquals(CalcUtils.convertBaseToDecimal("A", 16), "10");
    }
}
