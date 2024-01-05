package org.example;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer implements Runnable {
    Socket socket;
    String user_name;
    Map<String, Socket> users = new HashMap<String, Socket>();

    public ChatServer(Socket socket, Map<String, Socket> users,String user_name) {
        this.socket = socket;
        this.users = users;
        this.user_name = user_name;
    }

    private void sendMessage(String message, String receiver){
        OutputStream os = null;
        Socket socket_receiver = users.get(receiver);
        try {
            os = socket_receiver.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);
            pw.println(message+"=>"+user_name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (true){
                String message = br.readLine();
                String []splits = message.split("=>");
                sendMessage(splits[0], splits[1]);
                //System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
