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
            return convertToBase(operation.getCurrentOperand(), base);
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
        BigInteger intpart = n.toBigInteger();
        n = n.remainder(BigDecimal.ONE);
        while (intpart.compareTo(BigInteger.ZERO) != 0) {
            if (intpart.remainder(b.toBigInteger()).compareTo(new BigInteger("9")) == 1) {
                ret = "" + ((char) ('A' + intpart.remainder(b.toBigInteger()).intValue() - 10)) + ret;
            } else {
                ret = "" + intpart.remainder(b.toBigInteger()) + ret;
            }
            intpart = intpart.divide(b.toBigInteger());
        }
        if (ret == "") {
            ret = "0";
        }
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
        Integer pointIndex = (num.contains(".") ? num.indexOf(".") : null);
        int power = 0;
        int intpart = 0;
        for (int i = (pointIndex != null ? pointIndex - 1 : num.length() - 1); i >= 0; i--) {
            try {
                intpart += Integer.parseInt("" + num.charAt(i)) * Math.pow(base, power);
            } catch (NumberFormatException e) {
                intpart += (num.charAt(i) - 'A' + 10) * Math.pow(base, power);
            }
            power++;
        }

        if (pointIndex == null) return "" + intpart;
        if (pointIndex == num.length() - 1) return "" + intpart + ".";

        double val = (double) intpart;
        power = 0;
        for (int i = pointIndex + 1; i < num.length(); i++) {
            power--;
            try {
                val += Integer.parseInt("" + num.charAt(i)) * Math.pow(base, power);
            } catch (NumberFormatException e) {
                val += (num.charAt(i) - 'A' + 10) * Math.pow(base, power);
            }
        }
        return "" + val;
    }

    private String addDigit(String appendTo, String appendWith) {
        if ("0".equals(appendTo)) return convertToDecimal(appendWith);
        return convertToDecimal(convertToBase(appendTo, base) + appendWith);
    }

}
