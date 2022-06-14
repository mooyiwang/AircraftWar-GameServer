package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.NameCheckData;
import com.hit.sz.lib.data.RecordData;
import com.hit.sz.lib.data.RecordListData;
import com.hit.sz.lib.data.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class RanklistReturn extends Thread{
    private DataPackage acceptData;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<RecordData> records;


    public RanklistReturn(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut,  LinkedList<RecordData> records) {
        this.acceptData = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.records = records;
    }

    @Override
    public void run(){
        RecordData recordData = (RecordData) acceptData;

        records.add(recordData);

        DataPackage sendData = new RecordListData(2, records);

        try {
            objOut.writeObject(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

