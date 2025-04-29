package com.example.chatapplication.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Client {

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtMessage;

    @FXML
    private Button btnDownload;

    @FXML
    private ProgressBar progressBar;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private File receivedFile;
    private byte[] receivedFileBytes;

    public void initialize() {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 4000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String message = dataInputStream.readUTF();
                    if (message.equals("DOCUMENT")) {
                        String fileName = dataInputStream.readUTF();
                        int length = dataInputStream.readInt();
                        receivedFileBytes = new byte[length];
                        dataInputStream.readFully(receivedFileBytes);

                        receivedFile = new File(fileName);
                        txtArea.appendText("Document received: " + fileName + "\n");
                        Platform.runLater(() -> btnDownload.setDisable(false));
                    } else {
                        txtArea.appendText("Server: " + message + "\n");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void sendOnAction(ActionEvent event) throws IOException {
        dataOutputStream.writeUTF(txtMessage.getText());
        dataOutputStream.flush();
    }

    @FXML
    void sendDocumentOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                dataOutputStream.writeUTF("DOCUMENT");
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeInt(fileBytes.length);
                dataOutputStream.write(fileBytes);
                dataOutputStream.flush();
                txtArea.appendText("Document sent: " + file.getName() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void downloadOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(receivedFile.getName());
        File saveFile = fileChooser.showSaveDialog(new Stage());
        if (saveFile != null) {
            new Thread(() -> {
                try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                    Platform.runLater(() -> progressBar.setProgress(0));
                    for (int i = 0; i < receivedFileBytes.length; i++) {
                        fos.write(receivedFileBytes[i]);
                        final double progress = (double) (i + 1) / receivedFileBytes.length;
                        Platform.runLater(() -> progressBar.setProgress(progress));
                    }
                    fos.flush();
                    Platform.runLater(() -> {
                        progressBar.setProgress(1);
                        txtArea.appendText("File downloaded: " + saveFile.getName() + "\n");
                        btnDownload.setDisable(true);
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}