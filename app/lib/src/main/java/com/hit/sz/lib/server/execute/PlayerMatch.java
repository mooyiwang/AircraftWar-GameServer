package com.hit.sz.lib.server.execute;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class PlayerMatch extends Thread{
    private DataPackage dataPackage;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private LinkedList<Integer> waiting;
    private LinkedList<Integer> matched;
    private int playerNo;

    public PlayerMatch(DataPackage data, MyObjectInputStream objIn, ObjectOutputStream objOut, LinkedList<Integer> waiting, LinkedList<Integer> matched, int playerNo) {
        this.dataPackage = data;
        this.objIn = objIn;
        this.objOut = objOut;
        this.waiting = waiting;
        this.matched = matched;
        this.playerNo = playerNo;
    }

    @Override
    public void run(){
        while(true){
            if(matched.size()==2 && matched.get(0).equals(playerNo)){
                DataPackage sendData = new CheckData(2, true);
                try {
                    objOut.writeObject(sendData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            if(matched.size()==1 && !matched.get(0).equals(playerNo)){
                matched.add(playerNo);
                DataPackage sendData = new CheckData(2, true);
                try {
                    objOut.writeObject(sendData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            if(matched.size()==0){
                matched.add(playerNo);
            }
        }
    }
}
