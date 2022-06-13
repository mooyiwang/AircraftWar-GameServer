package com.hit.sz.lib.server;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.UserData;
import com.hit.sz.lib.server.execute.LoginVerify;
import com.hit.sz.lib.server.execute.NameCheck;
import com.hit.sz.lib.server.execute.SendUser;
import com.hit.sz.lib.server.execute.Signup;
import com.hit.sz.lib.server.execute.UpdateUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class GameServer {

    private LinkedList<UserData> users;
    public LinkedList<UserData> getUsers() {
        return users;
    }

    private int connted_num;
    private LinkedList<Socket> connted_socket;

    public static void main(String args[]){
        new GameServer();
    }

    public GameServer(){
        users = new LinkedList<>();
        users.add(new UserData(3, "www", "1w", 200));
        connted_num = 0;
        connted_socket = new LinkedList<>();
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect:" + " player"+ connted_num + " socket:" + socket);
                connted_num++;
                connted_socket.add(socket);
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
                            new Thread(new LoginVerify(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 1:
                            new Thread(new Signup(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 6:
                            new Thread(new NameCheck(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 7:
                            new Thread(new SendUser(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 8:
                            new Thread(new UpdateUser(dataPackage, objIn, objOut, users)).start();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }



    }

}