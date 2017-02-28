package com.androidcalc.ertai87.androidcalc.model;

import java.util.Stack;

public class RPNModel extends Model {

    public RPNModel(){
        super();
    }

    public RPNModel(BasicModel old){
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
        if (memory.isEmpty()) return;
        operation.setOperation(op);
        operation.setCurrentOperand(memory.pop());
        eval();
    }

}
