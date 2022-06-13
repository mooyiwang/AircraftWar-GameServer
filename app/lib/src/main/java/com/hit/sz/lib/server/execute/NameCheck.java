package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.NameCheckData;
import com.hit.sz.lib.data.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class NameCheck extends Thread{
    private DataPackage acceptData;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<UserData> users;

    public NameCheck(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<UserData> users) {
        this.acceptData = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.users = users;
    }

    @Override
    public void run(){
        NameCheckData nameCheckData = (NameCheckData)acceptData;
        DataPackage sendData;
        boolean notSame = true;
        //TODO 可改为数据库操作
        for(UserData user:users){
            if(user.getName().equals(nameCheckData.getName())){
                notSame = false;
                break;
            }
        }

        sendData = new CheckData(2, notSame);
        try {
            objOut.writeObject(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
