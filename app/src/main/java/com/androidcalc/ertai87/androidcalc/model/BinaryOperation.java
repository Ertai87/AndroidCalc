package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;

class BinaryOperation extends Operation implements Serializable {

    BinaryOperation(Operation oldOperation, int fromBase){
        super(oldOperation, fromBase, 2);
        base = 2;
    }

    BinaryOperation(){
        super();
        base = 2;
    }
}
