package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.androidcalc.ertai87.androidcalc.common.CalcUtils.convertBaseToDecimal;
import static com.androidcalc.ertai87.androidcalc.common.CalcUtils.convertDecimalToBase;

class Operation implements Serializable {
    private String operands[];
    private char operation;
    int base;

    Operation() {
        operands = new String[2];
        operands[0] = "";
        operands[1] = "";
        operation = 0;
    }

    Operation(Operation oldOperation, int fromBase, int toBase){
        operands = new String[2];
        if (!"".equals(oldOperation.operands[0])){
            operands[0] = convertDecimalToBase(convertBaseToDecimal(oldOperation.operands[0], fromBase), toBase);
        }else{
            operands[0] = "";
        }
        if (!"".equals(oldOperation.operands[1])){
            operands[1] = convertDecimalToBase(convertBaseToDecimal(oldOperation.operands[1], fromBase), toBase);
        }else{
            operands[1] = "";
        }
        operation = oldOperation.operation;
    }

    void eval() {
        if (operation == 0) return;
        BigDecimal op1 = new BigDecimal("".equals(operands[0]) ? "0" : convertBaseToDecimal(operands[0], base));
        BigDecimal op2 = new BigDecimal("".equals(operands[1]) ? "0" : convertBaseToDecimal(operands[1], base));
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
        operands = new String[]{convertDecimalToBase(result.stripTrailingZeros().toPlainString(), base), ""};
        operation = 0;
    }

    void evalUnary(char op){
        if (operation != 0 && "".equals(operands[1])) operation = 0;
        BigDecimal val = new BigDecimal("".equals(getCurrentOperand()) ? "0" : convertBaseToDecimal(getCurrentOperand(), base));
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
        setCurrentOperand(convertDecimalToBase(val.stripTrailingZeros().toPlainString(), base));
    }

    private void setFirstOperand(String val) {
        operands[0] = val;
    }

    private void setSecondOperand(String val) {
        operands[1] = val;
    }

    void setCurrentOperand(String val) {
        if (operation == 0) {
            setFirstOperand(val);
        } else {
            setSecondOperand(val);
        }
    }

    String getFirstOperand() {
        return ("".equals(operands[0]) ? "0" : operands[0]);
    }

    private String getSecondOperand() {
        return ("".equals(operands[1]) ? "0" : operands[1]);
    }

    String getCurrentOperand() {
        if (operation == 0) {
            return getFirstOperand();
        } else {
            return getSecondOperand();
        }
    }

    void setOperation(char op) {
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
