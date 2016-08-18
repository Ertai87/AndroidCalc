package com.androidcalc.ertai87.androidcalc.model;

import java.math.BigDecimal;

import lombok.Data;

/**
 * Created by ertai87 on 29/06/16.
 */
public interface Model {
    public String add();
    public String subtract();
    public String multiply();
    public String divide();
    public String eval();
    public String inputNum(String num);
    public String inputPoint();
    public void resetCurrent();
    public String getDisplayVal();
}
