package com.hit.sz.lib.server;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.CheckData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.LoginData;
import com.hit.sz.lib.data.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class GameServer {

    private LinkedList<UserData> users;

    public static void main(String args[]){
        new GameServer();
    }

    public GameServer(){
        users = new LinkedList<>();
        users.add(new UserData(3, "www", "1w", 200, 3));
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect" + socket);
                new Thread(new Service(socket)).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    class Service implements Runnable{

        private Socket socket;
        private InputStream in;
        private OutputStream out;
        private MyObjectInputStream objIn;
        private ObjectOutputStream objOut;
        private DataPackage dataPackage;

        public Service(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                objIn = new MyObjectInputStream(in);
                objOut = new ObjectOutputStream(out);

                while(true){
                    dataPackage = (DataPackage) objIn.readObject();
                    switch (dataPackage.getType()){
                        case 0:
                            new Thread(new LoginVerify(dataPackage)).start();
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        class LoginVerify extends Thread{
            private DataPackage dataPackage;

            public LoginVerify(DataPackage data) {
                this.dataPackage = data;
            }

            @Override
            public void run(){
                LoginData loginData = (LoginData)dataPackage;
                DataPackage sendData;
                boolean isSucc = false;
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
    }

}