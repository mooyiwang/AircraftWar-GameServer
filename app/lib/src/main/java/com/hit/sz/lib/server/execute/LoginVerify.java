package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.LoginData;
import com.hit.sz.lib.data.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class LoginVerify extends Thread{
    private DataPackage dataPackage;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<UserData> users;

    public LoginVerify(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<UserData> users) {
        this.dataPackage = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.users = users;
    }

    @Override
    public void run(){
        LoginData loginData = (LoginData)dataPackage;
        DataPackage sendData;
        boolean isSucc = false;
        //TODO 可改为数据库操作
        for(UserData user:users){
            if(user.getName().equals(loginData.getName()) && user.getPassword().equals(loginData.getPwd())){
                isSucc = true;
                break;
            }
        }
        if(isSucc){
            sendData = new CheckData(2, true);
        }
        else{
            sendData = new CheckData(2, false);
        }
        try {
            objOut.writeObject(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}