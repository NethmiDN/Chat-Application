package com.example.chatapplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class Server extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(javafx.stage.Stage stage) throws Exception {
        stage.setTitle("Chat Application");
        stage.setScene(new javafx.scene.Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/chatapplication/server.fxml")))));
        stage.show();
    }
}
