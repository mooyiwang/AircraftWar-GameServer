package com.hit.sz.lib.data;

import java.io.Serializable;

public abstract class DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    protected int type;



    public DataPackage(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
