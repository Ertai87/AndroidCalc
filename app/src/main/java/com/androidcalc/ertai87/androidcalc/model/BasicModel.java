package com.androidcalc.ertai87.androidcalc.model;

import java.util.Stack;

public class BasicModel extends Model{

    public BasicModel(){
        operation = new Operation();
        base = 10;
        newnum = true;
        memory = new Stack<String>();
    }

    @Override
    public void add() {
        eval();
        operation.setOperation('+');
        overrideZeroDisplay = true;
    }

    @Override
    public void subtract() {
        eval();
        operation.setOperation('-');
        overrideZeroDisplay = true;
    }

    @Override
    public void multiply() {
        eval();
        operation.setOperation('*');
        overrideZeroDisplay = true;
    }

    @Override
    public void divide(){
        eval();
        operation.setOperation('/');
        overrideZeroDisplay = true;
    }

}
