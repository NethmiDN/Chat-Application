package com.example.chatapplication.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import com.example.chatapplication.controller.Server;

public class Client {

    @FXML
    private AnchorPane ClientAp;

    @FXML
    private TextArea ClientTextArea;

    @FXML
    private Label lblError;

    @FXML
    private TextField ClientTextField;

    @FXML
    private Button btnClientSend;

    public void initialize() {
        ClientTextField.requestFocus();
    }
    @FXML
    void btnClientSendOnAction(ActionEvent event) {
        if (areFieldsEmpty()) {
            showErrorMessage("*Required fields cannot be empty.");
        }else {
            try {
                Socket socket = new Socket("127.0.0.1", 3000);
                System.out.println("Connected to server");

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);

                String msg = "", reply = "";

                while (true) {
                    System.out.print("You: ");
                    msg = scanner.nextLine(); // client message
                    dataOutputStream.writeUTF(msg); // send to server
                    dataOutputStream.flush();

                    reply = dataInputStream.readUTF(); // receive from server

                    System.out.println("Server: " + reply);
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private boolean areFieldsEmpty() {
        return ClientTextField.getText().isEmpty();
    }

    private void showErrorMessage(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-alignment: center");
    }

}
