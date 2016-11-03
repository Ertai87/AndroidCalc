package com.androidcalc.ertai87.androidcalc.model;

import com.androidcalc.ertai87.androidcalc.common.CalcConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

public class Operation implements Serializable {
    private String operands[];
    private char operation;

    public Operation() {
        operands = new String[2];
        operands[0] = "";
        operands[1] = "";
        operation = 0;
    }

    public Operation(String op1, char op, String op2) {
        operands = new String[]{op1, op2};
        operation = op;
    }

    public Operation(String[] ops, char op) {
        operands = ops;
        operation = op;
    }

    public Operation(String op1) {
        operands = new String[]{op1, ""};
        operation = 0;
    }

    public Operation eval() {
        if (operation == 0) return this;
        BigDecimal op1 = new BigDecimal(operands[0] == "" ? "0" : operands[0]);
        BigDecimal op2 = new BigDecimal(operands[1] == "" ? "0" : operands[1]);
        BigDecimal result;
        switch (operation) {
            case '+':
                result = op1.add(op2);
                break;
            case '-':
                result = op1.subtract(op2);
                break;
            case '*':
                result = op1.multiply(op2);
                break;
            case '/':
                result = op1.divide(op2, 30, BigDecimal.ROUND_DOWN);
                break;
            default:
                throw new ArithmeticException("Operation not found");
        }
        return new Operation(result.stripTrailingZeros().toPlainString());
    }

    public void evalUnary(char op){
        if (operation != 0 && "".equals(operands[1])) operation = 0;
        BigDecimal val = new BigDecimal(getCurrentOperand() == "" ? "0" : getCurrentOperand());
        switch(op){
            case '^':
                val = val.pow(2);
                break;
            case '/':
                val = new BigDecimal(Math.sqrt(val.doubleValue()));
                break;
            case 's':
                val = new BigDecimal(Math.sin(val.doubleValue()));
                break;
            case 'c':
                val = new BigDecimal(Math.cos(val.doubleValue()));
                break;
            case 't':
                val = new BigDecimal(Math.tan(val.doubleValue()));
                break;
            case 'l':
                val = new BigDecimal(Math.log10(val.doubleValue()));
                break;
            case 'd':
                val = new BigDecimal(Math.asin(val.doubleValue()));
                break;
            case 'v':
                val = new BigDecimal(Math.acos(val.doubleValue()));
                break;
            case 'y':
                val = new BigDecimal(Math.atan(val.doubleValue()));
                break;
            case ';':
                val = new BigDecimal(Math.exp(val.doubleValue()));
                break;
            default:
                break;
        }
        setCurrentOperand(val.stripTrailingZeros().toPlainString());
    }

    public void setFirstOperand(String val) {
        operands[0] = val;
    }

    public void setSecondOperand(String val) {
        operands[1] = val;
    }

    public void setCurrentOperand(String val) {
        if (operation == 0) {
            setFirstOperand(val);
        } else {
            setSecondOperand(val);
        }
    }

    public String getFirstOperand() {
        return (operands[0] == "" ? "0" : operands[0]);
    }

    public String getSecondOperand() {
        return (operands[1] == "" ? "0" : operands[1]);
    }

    public String getCurrentOperand() {
        if (operation == 0) {
            return getFirstOperand();
        } else {
            return getSecondOperand();
        }
    }

    public void setOperation(char op) {
        operation = op;
    }

    @Override
    public String toString(){
        if ("".equals (operands[0])) return "";
        if (operation == 0) return operands[0];
        if ("".equals(operands[1])) return operands[0] + " " + operation;
        return operands[0] + " " + operation + " " + operands[1];
    }
}
