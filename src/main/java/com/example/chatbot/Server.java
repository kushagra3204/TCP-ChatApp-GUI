package com.example.chatbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    void sendMessage(String message){

    }
    void StartServer() throws IOException {
        new Thread(() -> {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(5008);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Socket s = null;
            DataInputStream dis = null;
            DataOutputStream dos = null;
            while(true)
            {
                try {
                    s = ss.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    dis = new DataInputStream(s.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    dos = new DataOutputStream(s.getOutputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Thread t = new SocketHandler(s,dis,dos);
                t.start();
            }
        }).start();
    }
}

class SocketHandler extends Thread {
    Socket s = null;
    static int count = 0;
    static HashMap<Integer,String> clients = new HashMap<>();

    DataInputStream dis = null;
    DataOutputStream dos = null;
    HelloApplication ha = new HelloApplication();

    public SocketHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }
    public void run(){
        if(clients.get(s.getPort())==null)
        {
            clients.put(s.getPort(),"Client"+(count++));
        }
        ha.clientMessage("\n---- "+clients.get(s.getPort())+" Joined. ----\n\n");
        ha.setClients(clients);

        try{
            while(true) {

                if(!HelloApplication.wantToSendMessage)
                {
                    // Read from Client
                    String message = dis.readUTF();
                    message=clients.get(s.getPort())+":\n"+message+'\n';
                    ha.clientMessage(message);
                    System.out.println(message);
                    HelloApplication.wantToSendMessage=true;
                }
                else {
                    sleep(100);
                    if(ha.ServerMessage()!=null && s.getPort() == ha.getCurrentClientPort())
                    {
                        dos.writeUTF(ha.ServerMessage());
                        ha.clientMessage("Server message to "+clients.get(s.getPort())+":\n"+ha.ServerMessage()+'\n');
                        HelloApplication.serverMessage = null;
                        HelloApplication.wantToSendMessage=false;
                    }
                }


            }
        } catch (IOException e) {
            System.out.println("Connection lost with "+clients.get(s.getPort())+".");
            clients.remove(s.getPort());
            ha.setClients(clients);
        } catch (InterruptedException e) {
            System.out.println("Connection lost with "+clients.get(s.getPort())+".");
        }
    }
}