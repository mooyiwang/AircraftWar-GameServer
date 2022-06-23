package com.hit.sz.lib.data;

import java.io.Serializable;
import java.util.LinkedList;

public class RecordListData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;

    private LinkedList<RecordData> records;

    public RecordListData(int type,  LinkedList<RecordData> records) {
        super(type);
        this.records = records;
    }

}