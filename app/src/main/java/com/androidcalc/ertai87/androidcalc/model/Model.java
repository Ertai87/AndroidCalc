package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.androidcalc.ertai87.androidcalc.common.CalcUtils.convertBaseToDecimal;
import static com.androidcalc.ertai87.androidcalc.common.CalcUtils.convertDecimalToBase;

public abstract class Model implements Serializable {

    Operation operation;
    int base;
    boolean newnum;
    Stack<String> memory;
    boolean overrideZeroDisplay = false; //flag to override display of 0 when input sequence is # - op - # - op

    public Model() {
        operation = new DecimalOperation();
        base = 10;
        newnum = true;
        memory = new Stack<>();
    }

    public abstract void doBinaryOp(char op);

    public void eval() {
        operation.eval();
        newnum = true;
    }

    public void inputPoint() {
        overrideZeroDisplay = false;
        if (newnum) {
            operation.setCurrentOperand("0.");
            newnum = false;
        } else if (!operation.getCurrentOperand().contains(".")){
            operation.setCurrentOperand(operation.getCurrentOperand() + ".");
        }
    }

    public void inputNeg(){
        if (!newnum && !operation.getCurrentOperand().contains("-")) {
            operation.setCurrentOperand("-" + operation.getCurrentOperand());
        }else if (operation.getCurrentOperand().contains("-")){
            operation.setCurrentOperand(operation.getCurrentOperand().substring(1));
        }
    }

    public void inputNum(String num) {
        overrideZeroDisplay = false;
        if (newnum) {
            if ("0".equals(num)) {
                if ("0".equals(operation.getCurrentOperand())) return;
            } else {
                newnum = false;
            }
            operation.setCurrentOperand(num);
        } else {
            operation.setCurrentOperand(operation.getCurrentOperand() + num);
        }
    }

    public void doUnaryOp(char op){
        operation.evalUnary(op);
        newnum = true;
    }

    public void resetCurrent() {
        if (!newnum) {
            operation.setCurrentOperand("0");
            newnum = true;
        } else {
            operation = Operation.getOperation(base);
        }
    }

    public void resetOperation(){
        operation = Operation.getOperation(base);
    }

    public void memPush() {
        memory.push(operation.getCurrentOperand());
        operation.setCurrentOperand("");
        newnum = true;
    }

    public boolean memPop() {
        if (!memory.empty()) {
            operation.setCurrentOperand(memory.pop());
            overrideZeroDisplay = false;
            newnum = true;
            return true;
        }
        return false;
    }

    public void memClear() {
        memory = new Stack<>();
    }

    public void setBase(int newBase) {
        if (base == newBase) return;
        switch(newBase){
            case 2:
                operation = new BinaryOperation(operation, base);
                break;
            case 8:
                operation = new OctalOperation(operation, base);
                break;
            case 10:
                operation = new DecimalOperation(operation, base);
                break;
            case 16:
                operation = new HexadecimalOperation(operation, base);
                break;
        }
        List<String> newMemory = new ArrayList<>();
        while(!memory.empty()){
            newMemory.add(convertDecimalToBase(convertBaseToDecimal(memory.pop(), base), newBase));
        }
        memory = new Stack<>();
        for (int i = newMemory.size() - 1; i >= 0; i--){
            memory.push(newMemory.get(i));
        }
        base = newBase;
    }

    public int getBase() {
        return base;
    }

    public boolean getNewNum() {
        return newnum;
    }

    public String getDisplayVal() {
        if (overrideZeroDisplay) {
            return operation.getFirstOperand();
        } else {
            return operation.getCurrentOperand();
        }
    }

    public int getMemSize() {
        return memory.size();
    }

    public String getCurrentValue(){
        return operation.getCurrentOperand();
    }
}
