package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;

class DecimalOperation extends Operation implements Serializable {
    DecimalOperation() {
        super();
        base = 10;
    }

    DecimalOperation(Operation oldOperation, int fromBase){
        super(oldOperation, fromBase, 10);
        base = 10;
    }
}
