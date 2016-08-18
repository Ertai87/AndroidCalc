package com.androidcalc.ertai87.androidcalc.model;

import java.math.BigDecimal;

/**
 * Created by ertai87 on 29/06/16.
 */
public class BasicModel implements Model{

    String[] operands; //The operands of the operation being modeled
    int operandswitch; //Which operand is being inputted currently
    boolean newnum; //Whether the number on the screen should be overwritten or appended by new inputs
    char op; //The current operation being modelled
    boolean zeroInput; //Flag to force evaluation with zero operand when necessary

    public BasicModel(){
        operands = new String[2];
        operands[0] = "0";
        operands[1] = "0";
        operandswitch = 0;
        newnum = true;
        zeroInput = false;
    }

    private void reset(){
        operandswitch = 0;
        newnum = true;
        zeroInput = false;
    }

    public String eval() {
        if (newnum && !zeroInput) return operands[0];
        BigDecimal op1 = new BigDecimal(operands[0]);
        BigDecimal op2 = new BigDecimal(operands[1]);
        switch (op) {
            case '+':
                op1 = op1.add(op2);
                break;
            case '-':
                op1 = op1.subtract(op2);
                break;
            case '*':
                op1 = op1.multiply(op2);
                break;
            case '/':
                op1 = op1.divide(op2, 30, BigDecimal.ROUND_DOWN);
                break;
        }
        operands[0] = op1.stripTrailingZeros().toPlainString();
        operands[1] = "0";
        op = 0;
        reset();
        return operands[0];
    }

    @Override
    public String add() {
        if (operandswitch == 1){
            eval();
        }
        op = '+';
        newnum = true;
        operandswitch = 1;
        return operands[0];
    }

    @Override
    public String subtract() {
        if (operandswitch == 1){
            eval();
        }
        op = '-';
        newnum = true;
        operandswitch = 1;
        return operands[0];
    }

    @Override
    public String multiply() {
        if (operandswitch == 1){
            eval();
        }
        op = '*';
        newnum = true;
        operandswitch = 1;
        return operands[0];
    }

    @Override
    public String divide(){
        if (operandswitch == 1){
            eval();
        }
        op = '/';
        newnum = true;
        operandswitch = 1;
        return operands[0];
    }

    @Override
    public String inputNum(String num){
        if (newnum) {
            operands[operandswitch] = num;
            if (!num.equals("0")) {
                newnum = false;
            }else{
                zeroInput = true;
            }
        }else{
            operands[operandswitch] += num;
        }
        return operands[operandswitch];
    }

    @Override
    public String inputPoint(){
        if (newnum){
            operands[operandswitch] = "0.";
            newnum = false;
        }else{
            operands[operandswitch] += ".";
        }
        return operands[operandswitch];
    }

    @Override
    public void resetCurrent(){
        if (!newnum) {
            operands[operandswitch] = "0";
            newnum = true;
        }else{
            operands = new String[2];
            operands[0] = "0";
            operands[1] = "0";
            operandswitch = 0;
            newnum = true;
        }
    }

    @Override
    public String getDisplayVal(){
        return operands[operandswitch];
    }
}
