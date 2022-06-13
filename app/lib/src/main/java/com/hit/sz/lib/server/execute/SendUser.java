package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.UserData;
import com.hit.sz.lib.data.UserDataReq;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class SendUser extends Thread{
    private DataPackage acceptData;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<UserData> users;

    public SendUser(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<UserData> users) {
        this.acceptData = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.users = users;
    }

    @Override
    public void run(){
        UserDataReq userDataReq = (UserDataReq) acceptData;
        //TODO 数据库查找操作
        for(UserData user:users){
            if(user.getName().equals(userDataReq.getName())){
                try {
                    objOut.writeObject(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
