package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.UpdateUserData;
import com.hit.sz.lib.data.UserData;

import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class UpdateUser extends Thread{
    private DataPackage acceptData;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<UserData> users;

    public UpdateUser(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<UserData> users) {
        this.acceptData = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.users = users;
    }

    @Override
    public void run(){
        UpdateUserData updateData = (UpdateUserData) acceptData;
        //TODO 数据库修改操作
        for(UserData user:users){
            if(user.getName().equals(updateData.getName())){
                user.setBonus(updateData.getBonus());
                break;
            }
        }
    }
}
