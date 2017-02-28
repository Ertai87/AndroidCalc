package com.androidcalc.ertai87.androidcalc.model;

import java.io.Serializable;

class OctalOperation extends Operation implements Serializable {

    OctalOperation(Operation oldOperation, int fromBase){
        super(oldOperation, fromBase, 8);
        base = 8;
    }

    public OctalOperation() {
        super();
        base = 8;
    }
}
