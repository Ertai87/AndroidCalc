package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;

class HexadecimalOperation extends Operation implements Serializable {

    HexadecimalOperation(Operation oldOperation, int fromBase){
        super(oldOperation, fromBase, 16);
        base = 16;
    }

    public HexadecimalOperation() {
        super();
        base = 16;
    }
}
