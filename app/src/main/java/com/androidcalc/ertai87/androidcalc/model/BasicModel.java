package com.androidcalc.ertai87.androidcalc.model;

import java.util.Stack;

public class BasicModel extends Model {

    public BasicModel() {
        operation = new DecimalOperation();
        base = 10;
        newnum = true;
        memory = new Stack<>();
    }

    @Override
    public void doBinaryOp(char op) {
        eval();
        operation.setOperation(op);
        overrideZeroDisplay = true;
    }
}
