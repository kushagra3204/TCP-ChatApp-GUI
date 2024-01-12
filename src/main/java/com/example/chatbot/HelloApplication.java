package com.example.chatbot;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    static HashMap<Integer,String> clients = new HashMap<>();
    static String serverMessage=null;
    String ClientMessage;
    static boolean wantToSendMessage=false;
    static ComboBox<Object> comboBox = new ComboBox<>();
    static TextField tf = new TextField();
    static TextArea rec = new TextArea();
    Pane g = new Pane();

    void setClients(HashMap<Integer,String> clients1){
        clients = clients1;
        ObservableList<Object> items = FXCollections.observableArrayList(clients.values());
        comboBox.setItems(items);
    }

    int getCurrentClientPort()
    {
        for (Map.Entry<Integer,String> entry : clients.entrySet()) {
            if (entry.getValue().equals(comboBox.getValue().toString())) {
                return entry.getKey();
            }
        }
        return 0;
    }


    void clientMessage(String message){
        if(message!=null || !message.equalsIgnoreCase("exit")){
            ClientMessage=message;
            System.out.println(message);
            rec.appendText(message+'\n');
        }
    }

    String ServerMessage(){
        return serverMessage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Server sc = new Server();

        comboBox.setLayoutY(505);
        comboBox.setLayoutX(50);
        comboBox.setMinWidth(110);
        comboBox.setMinHeight(30);
        g.getChildren().add(comboBox);

        rec.setMinHeight(400);
        rec.setEditable(false);
        rec.setMinWidth(500);
        rec.setLayoutX(50);
        rec.setLayoutY(50);



        Scene scene = new Scene(g, 600, 600);
        stage.setResizable(false);
        stage.setTitle("Hello!");

        tf.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
        tf.setPromptText("Enter your Message");
        tf.setFocusTraversable(false);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            tf.getParent().requestFocus();
            tf.setFocusTraversable(false);
        });
        tf.setMinWidth(300);
        tf.setMinHeight(40);
        tf.setLayoutY(500);
        tf.setLayoutX(170);

        Button send = new Button("Send");
        send.setMinWidth(70);
        send.setMinHeight(40);
        send.setLayoutX(480);
        send.setLayoutY(500);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                serverMessage=tf.getText();
                wantToSendMessage=true;
                tf.setText("");
            }
        };
        send.setOnAction(event);

        g.getChildren().add(send);
        g.getChildren().add(rec);
        g.getChildren().add(tf);

        stage.setScene(scene);
        stage.show();
        sc.StartServer();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}