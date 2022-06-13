package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.SignupData;
import com.hit.sz.lib.data.UserData;

import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class Signup extends Thread{
    private DataPackage dataPackage;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<UserData> users;

    public Signup(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<UserData> users) {
        this.dataPackage = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.users = users;
    }
    @Override
    public void run(){
        SignupData signupData = (SignupData) dataPackage;
        UserData newUser = new UserData(3, signupData.getName(), signupData.getPwd(), 0);
        users.add(newUser);
    }

}
