package com.androidcalc.ertai87.androidcalc.model;

import java.util.Stack;

public class BasicModel implements Model{

    private Operation operation;
    private boolean newnum;
    private Stack<String> memory;
    private boolean overrideZeroDisplay = false; //flag to override display of 0 when input sequence is # - op - # - op

    public BasicModel(){
        operation = new Operation();
        newnum = true;
        memory = new Stack<String>();
    }

    public void eval() {
        operation = operation.eval();
        newnum = true;
    }

    @Override
    public void add() {
        operation = operation.eval();
        operation.setOperation('+');
        overrideZeroDisplay = true;
        newnum = true;
    }

    @Override
    public void subtract() {
        operation = operation.eval();
        operation.setOperation('-');
        overrideZeroDisplay = true;
        newnum = true;
    }

    @Override
    public void multiply() {
        operation = operation.eval();
        operation.setOperation('*');
        overrideZeroDisplay = true;
        newnum = true;
    }

    @Override
    public void divide(){
        operation = operation.eval();
        operation.setOperation('/');
        overrideZeroDisplay = true;
        newnum = true;
    }

    @Override
    public void inputNum(String num){
        overrideZeroDisplay = false;
        if (newnum) {
            if ("0".equals("num")) {
                if ("0".equals(operation.getCurrentOperand())) return;
            }else{
                newnum = false;
            }
            operation.setCurrentOperand(num);
        }else{
            operation.setCurrentOperand(operation.getCurrentOperand() + num);
        }
    }

    @Override
    public void inputPoint(){
        overrideZeroDisplay = false;
        if (newnum){
            operation.setCurrentOperand("0.");
            newnum = false;
        }else{
            operation.setCurrentOperand(operation.getCurrentOperand() + ".");
        }
    }

    @Override
    public void resetCurrent(){
        if (!newnum) {
            operation.setCurrentOperand("0");
            newnum = true;
        }else{
            operation = new Operation();
        }
    }

    @Override
    public void memPush(){
        memory.push(operation.getCurrentOperand());
        newnum = true;
    }

    @Override
    public void memPop(){
        operation.setCurrentOperand(memory.pop());
        newnum = true;
    }

    @Override
    public void memClear(){
        memory = new Stack<String>();
    }

    @Override
    public String getDisplayVal(){
        if (overrideZeroDisplay){
            return operation.getFirstOperand();
        }else{
            return operation.getCurrentOperand();
        }
    }

}
