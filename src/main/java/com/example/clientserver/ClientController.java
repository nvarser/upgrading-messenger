package com.example.clientserver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ClientController implements Initializable
{
    @FXML
    private Button SendingButtonClient;
    @FXML
    private TextField SendingTextFieldClient;
    @FXML
    private VBox VBoxMainClient;
    @FXML
    private ScrollPane ScrollPaneMainClient;
    private Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            client = new Client(new Socket("localhost",1234));
            System.out.println("Connected to server");
        }catch(IOException e){
            e.printStackTrace();
        }

        VBoxMainClient.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ScrollPaneMainClient.setVvalue((Double) t1);
            }
        });

        client.receiveMessageFromServer(VBoxMainClient);
        SendingButtonClient.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = SendingTextFieldClient.getText();
                if(!messageToSend.isEmpty()){
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5,5,5,10));
                    Date currentTime = new Date();
                    SimpleDateFormat formatOfTime = new SimpleDateFormat("HH:mm");
                    Text timeOfMessage = new Text(formatOfTime.format(currentTime));

                    Text text = new Text(messageToSend);
                    TextFlow textFlowMessage = new TextFlow(text);
                    TextFlow textFlowTimeOfMessage = new TextFlow(timeOfMessage);

                    textFlowMessage.setStyle("-fx-background-color: rgb(15,125,242);"
                                    + "-fx-background-radius: 20px;"
                                    + "-fx-color-label-visible: rgb(239,242,255);");
                    textFlowMessage.setPadding(new Insets(5,10,5,10));

                    textFlowTimeOfMessage.setPadding(new Insets(6,10,0,0));

                    timeOfMessage.setFill(Color.color(0,0,0));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hbox.getChildren().add(textFlowTimeOfMessage);
                    hbox.getChildren().add(textFlowMessage);
                    VBoxMainClient.getChildren().add(hbox);

                    client.sendMessageToServer(messageToSend);

                    SendingTextFieldClient.clear();
                }
            }
        });
    }

    public static void addLabel(String msgFromServer, VBox vBox){
        HBox hBox= new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));
        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color:rgb(233,233,235);"
                + "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
