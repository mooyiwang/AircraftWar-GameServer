package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class PlayerMatch extends Thread{
    private DataPackage dataPackage;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<Socket> sockets;

    public PlayerMatch(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<Socket> sockets) {
        this.dataPackage = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.sockets = sockets;
    }

    @Override
    public void run(){
        while(true){
            if(sockets.size()>=2){
                DataPackage sendData = new CheckData(2, true);

                try {
                    objOut.writeObject(sendData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
