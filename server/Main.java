package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(9090);
            Map<String, Socket> users = new HashMap<String, Socket>();

            while (true){
                Socket s = ss.accept();
                String user_name = "user"+(users.size()+1);
                users.put(user_name,s);
                ChatServer chatServer = new ChatServer(s, users, user_name);
                new Thread(chatServer).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

