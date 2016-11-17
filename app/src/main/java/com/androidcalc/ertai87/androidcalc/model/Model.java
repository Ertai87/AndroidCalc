package com.androidcalc.ertai87.androidcalc.model;

import com.androidcalc.ertai87.androidcalc.common.CalcConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;

import lombok.Data;

/**
 * Created by ertai87 on 29/06/16.
 */
public abstract class Model implements Serializable {

    Operation operation;
    int base;
    int zeroesAfterDecimalPoint = 0;
    boolean newnum;
    Stack<String> memory;
    boolean overrideZeroDisplay = false; //flag to override display of 0 when input sequence is # - op - # - op

    public abstract void doBinaryOp(char op);

    public void eval() {
        operation = operation.eval();
        newnum = true;
    }

    public void inputPoint() {
        overrideZeroDisplay = false;
        if (newnum) {
            operation.setCurrentOperand("0.");
            newnum = false;
            zeroesAfterDecimalPoint = 0;
        } else {
            operation.setCurrentOperand(operation.getCurrentOperand() + ".");
        }
    }

    public void inputNeg(){
        if (!newnum) {
            operation.setCurrentOperand("-" + operation.getCurrentOperand());
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
            operation.setCurrentOperand(addDigit("0", num));
        } else {
            operation.setCurrentOperand(addDigit(operation.getCurrentOperand(), num));
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
            operation = new Operation();
        }
    }

    public void resetOperation(){
        operation = new Operation();
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
        memory = new Stack<String>();
    }

    public int getMemSize() {
        return memory.size();
    }

    public void setBase(int newBase) {
        base = newBase;
        zeroesAfterDecimalPoint = 0;
    }

    public int getBase() {
        return base;
    }

    public boolean getNewNum() {
        return newnum;
    }

    public String getDisplayVal() {
        if (overrideZeroDisplay) {
            return convertToBase(operation.getFirstOperand(), base);
        } else {
            return appendZeroes(convertToBase(operation.getCurrentOperand(), base));
        }
    }

    private String convertToBase(String num, int base) {
        String ret = "";
        int pointIndex = num.indexOf(".");
        boolean neg = false;
        BigDecimal n = ("".equals(num) ? BigDecimal.ZERO : new BigDecimal(num));
        BigDecimal b = new BigDecimal(base);

        if (n.compareTo(BigDecimal.ZERO) == -1) {
            neg = true;
            n = BigDecimal.ZERO.subtract(n);
        }
        ret = n.toBigInteger().toString(base).toUpperCase();
        n = n.remainder(BigDecimal.ONE);

        if (pointIndex != -1) {
            ret += ".";
            for (int i = 0; i <= CalcConstants.MAX_DIGITS && n.compareTo(BigDecimal.ZERO) != 0; i++) {
                n = n.multiply(b);
                if (n.intValue() < 10) {
                    ret += n.intValue();
                } else {
                    ret += (char) ('A' + n.intValue() - 10);
                }
                n = n.remainder(BigDecimal.ONE);
            }
        }
        if (neg) ret = "-" + ret;
        return ret;
    }

    private String convertToDecimal(String num) {
        if (base == 10) return num;
        int pointIndex = (num.contains(".") ? num.indexOf(".") : num.length());
        int power = 0;
        boolean neg = false;

        BigInteger intpart = new BigInteger(num.substring(0, pointIndex), base);

        if (pointIndex == num.length()) return intpart.toString();
        if (pointIndex == num.length() - 1) return intpart.toString() + ".";

        if (intpart.compareTo(BigInteger.ZERO) == -1) {
            neg = true;
            intpart = BigInteger.ZERO.subtract(intpart);
        }

        BigDecimal val = new BigDecimal(intpart.toString());
        power = 0;
        for (int i = pointIndex + 1; i < num.length(); i++) {
            power--;
            BigDecimal digit;
            try {
                digit = new BigDecimal("" + num.charAt(i));
            } catch (NumberFormatException e) {
                digit = new BigDecimal("" + (int)(num.charAt(i) - 'A' + 10));
            }
            double baseConversion = Math.pow(base, power);
            val = val.add(digit.multiply(new BigDecimal(baseConversion)));
        }
        return (neg ? "-" : "") + val;
    }

    private String addDigit(String appendTo, String appendWith) {
        if ("0".equals(appendTo)) return convertToDecimal(appendWith);
        if ("0".equals(appendWith) && appendTo.indexOf(".") != -1){
            zeroesAfterDecimalPoint ++;
            return appendTo;
        }else{
            String ret = convertToDecimal(appendZeroes(convertToBase(appendTo, base)) + appendWith);
            zeroesAfterDecimalPoint = 0;
            return ret;
        }
    }

    private String appendZeroes(String num){
        if (num.indexOf(".") != -1) {
            for (int i = 0; i < zeroesAfterDecimalPoint; i++) {
                num += '0';
            }
        }
        return num;
    }
}
