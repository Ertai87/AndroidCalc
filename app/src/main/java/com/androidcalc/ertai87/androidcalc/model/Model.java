package com.androidcalc.ertai87.androidcalc.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by ertai87 on 29/06/16.
 */
public interface Model {
    public void add();
    public void subtract();
    public void multiply();
    public void divide();
    public void eval();
    public void inputNum(String num);
    public void inputPoint();
    public void resetCurrent();
    public void memPush();
    public void memPop();
    public void memClear();
    public String getDisplayVal();
}
