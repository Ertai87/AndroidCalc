package com.androidcalc.ertai87.androidcalc.model;

import java.util.Stack;

public class BasicModel extends Model {

    public BasicModel() {
        super();
    }

    public BasicModel(RPNModel old){
        super();
        base = old.base;
        operation = Operation.getOperation(base);
        if (old.getCurrentValue() != ""){
            operation.setCurrentOperand(old.getCurrentValue());
            newnum = old.newnum;
        }
    }

    @Override
    public void doBinaryOp(char op) {
        eval();
        operation.setOperation(op);
        overrideZeroDisplay = true;
    }
}
