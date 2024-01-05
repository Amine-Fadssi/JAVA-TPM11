package com.example.chatinterface_1.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;

    @FXML public ListView<String> chat_list;
    @FXML public TextArea message_field;
    @FXML public TextField destinataire_field;
    @FXML public Button send_button;
    ObservableList<String> data_messages = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            chat_list.setItems(data_messages);
            socket = new Socket("localhost",9090);

            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            pw = new PrintWriter(osw, true);

            Thread thread = getThread();
            thread.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Thread getThread() {
        Runnable runnable = () -> {
            while (true){
                String message = null;
                try {
                    message = br.readLine();
                    System.out.println(message);
                    String[] finalMessage = message.split("=>");
                    Platform.runLater(()->data_messages.add(finalMessage[1]+": "+finalMessage[0]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        return new Thread(runnable);
    }

    public void send_message(){
        String message = message_field.getText()+"=>"+destinataire_field.getText();
        data_messages.add("moi: "+message.split("=>")[0]);
        pw.println(message);
    }
}
