package com.hit.sz.lib.server;

import com.hit.sz.lib.IOStream.MyObjectInputStream;
import com.hit.sz.lib.data.BattleData;
import com.hit.sz.lib.data.DataPackage;
import com.hit.sz.lib.data.RecordData;
import com.hit.sz.lib.data.UserData;
import com.hit.sz.lib.server.execute.LoginVerify;
import com.hit.sz.lib.server.execute.NameCheck;
import com.hit.sz.lib.server.execute.PlayerMatch;
import com.hit.sz.lib.server.execute.RanklistReturn;
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

    private final LinkedList<UserData> users;
    public LinkedList<UserData> getUsers() {
        return users;
    }
    private final LinkedList<RecordData> records;

    private int connted_num;
    private LinkedList<Service> connted_player;
    private LinkedList<Integer> waiting;
    private LinkedList<Integer> matched;
    private boolean[] needTrans;


    public static void main(String args[]){
        new GameServer();
    }

    public GameServer(){
        users = new LinkedList<>();
        users.add(new UserData(3, "wwww", "1w", 200));

        records = new LinkedList<>();
        records.add(new RecordData(12,"22-6-1","WangMuyi",666,"Hard"));
        records.add(new RecordData(12,"22-5-28","Wang",1000,"Medium"));
        records.add(new RecordData(12,"22-5-27","WangYifu",233,"Easy"));

        users.add(new UserData(3, "yyy", "1f",200));

        connted_num = 0;
        connted_player = new LinkedList<>();
        waiting = new LinkedList<>();
        matched = new LinkedList<>();
        needTrans = new boolean[]{false, false};
        try{
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("local host:" + addr);

            //创建server socket
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("listen port 9999");

            new Thread(new PlayCommu(matched, connted_player)).start();

            while(true){
                System.out.println("waiting client connect");
                Socket socket = serverSocket.accept();
                System.out.println("accept client connect:" + " player"+ connted_num + " socket:" + socket);
                Service player = new Service(socket, connted_num);
                connted_player.add(player);
                new Thread(player).start();
                connted_num++;
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
        private int playerNo;

        public Service(Socket socket, int playerNo){
            this.socket = socket;
            this.playerNo = playerNo;
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
                        case 5:
                            if(needTrans[0]==false){
                                needTrans[0] = true;
                            }
                            else if(needTrans[0]==true && needTrans[1]==false){
                                needTrans[1] = true;
                            }
                            break;
                        case 6:
                            new Thread(new NameCheck(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 7:
                            new Thread(new SendUser(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 8:
                            new Thread(new UpdateUser(dataPackage, objIn, objOut, users)).start();
                            break;
                        case 9:
                            new Thread(new PlayerMatch(dataPackage, objIn, objOut, waiting, matched, playerNo));
                            break;
                        case 13: //返回ranklist列表
                            waiting.add(socket);
                            new Thread(new RanklistReturn(dataPackage, objIn, objOut,records));
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        public InputStream getIn() {
            return in;
        }

        public OutputStream getOut() {
            return out;
        }

        public MyObjectInputStream getObjIn() {
            return objIn;
        }

        public ObjectOutputStream getObjOut() {
            return objOut;
        }
    }



    class PlayCommu implements Runnable {
        private MyObjectInputStream objIn_Player1;
        private ObjectOutputStream objOut_Player1;
        private MyObjectInputStream objIn_Player2;
        private ObjectOutputStream objOut_Player2;
        private BattleData dataFromPlayer1;
        private BattleData dataFromPlayer2;
        private LinkedList<Integer> matched;
        private LinkedList<Service> connted_player;

        public PlayCommu(LinkedList<Integer> matched,LinkedList<Service> connted_player) {
            this.matched = matched;
            this.connted_player = connted_player;
        }

        @Override
        public void run() {
            while(true){
                if(needTrans[0] && needTrans[1]){
                    if(matched.size()==2){
                        objIn_Player1 = connted_player.get(matched.get(0)).getObjIn();
                        objIn_Player2 = connted_player.get(matched.get(1)).getObjIn();
                        objOut_Player1 = connted_player.get(matched.get(0)).getObjOut();
                        objOut_Player2 = connted_player.get(matched.get(2)).getObjOut();
                    }
                    while (true){
                        try {
                            dataFromPlayer1 = (BattleData) objIn_Player1.readObject();
                            dataFromPlayer2 = (BattleData) objIn_Player2.readObject();
                            if(dataFromPlayer1.getCurLife()<=0 || dataFromPlayer2.getCurLife()<=0){
                                needTrans[0]=false;needTrans[1]=false;
                                break;
                            }
                            objOut_Player2.writeObject(dataFromPlayer1);
                            objOut_Player1.writeObject(dataFromPlayer2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}