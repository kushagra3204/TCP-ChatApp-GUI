package com.example.chatbot;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient2
{
    public static void main(String[] args) throws IOException {
        boolean stop=false;
        Scanner in = new Scanner(System.in);
        try {
            Socket c1 = new Socket("localhost", 5008);
            System.out.println("Connection Established");
            while(true)
            {
                //Send to Server
                OutputStream os = c1.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                System.out.print("Enter a message to send Server: ");
                String str=in.nextLine();
                dos.writeUTF(str);
                if(str.equalsIgnoreCase("exit"))
                    break;
                //Read from Server
                InputStream is = c1.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                String message = dis.readUTF();
                System.out.println(message);
            }
        }
        catch (Exception e){
            System.out.println("No connection");
        }
    }
}