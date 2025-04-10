package com.example.chatapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import com.example.chatapplication.controller.Client;

public class Server {

    @FXML
    private AnchorPane ServerAp;

    @FXML
    private Label lblError;

    @FXML
    private TextArea ServerTextArea;

    @FXML
    private TextField ServerTextField;

    @FXML
    private Button btnServerSend;

    @FXML
    void btnServerSendOnAction(ActionEvent event) {

    }

}
