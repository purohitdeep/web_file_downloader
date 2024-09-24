package com.download.file.filedownloader;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDownloaderJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Downloader");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        TextField urlField = new TextField();
        urlField.setPromptText("Enter the base URL");
        GridPane.setConstraints(urlField, 0, 0);
        grid.getChildren().add(urlField);

        TextField numFilesField = new TextField();
        numFilesField.setPromptText("Number of files");
        GridPane.setConstraints(numFilesField, 0, 1);
        grid.getChildren().add(numFilesField);

        TextField folderField = new TextField();
        folderField.setPromptText("Download folder");
        GridPane.setConstraints(folderField, 0, 2);
        grid.getChildren().add(folderField);

        ProgressBar progressBar = new ProgressBar(0);
        GridPane.setConstraints(progressBar, 0, 3);
        grid.getChildren().add(progressBar);

        Button downloadBtn = new Button("Download");
        GridPane.setConstraints(downloadBtn, 0, 4);
        grid.getChildren().add(downloadBtn);

        downloadBtn.setOnAction(e -> {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    String baseUrl = urlField.getText();
                    int numFiles = Integer.parseInt(numFilesField.getText());
                    String folder = folderField.getText();
                    HttpClient client = HttpClient.newHttpClient();
                    Path folderPath = Path.of(folder);

                    if (!Files.exists(folderPath)) {
                        Files.createDirectories(folderPath);
                    }

                    for (int i = 1; i <= numFiles; i++) {
                        String fileUrl = baseUrl.replace("01.mp3?_=1", String.format("%02d.mp3?_=%d", i, i));
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(fileUrl))
                                .build();

                        Path filePath = folderPath.resolve(String.format("%02d.mp3", i));
                        client.send(request, HttpResponse.BodyHandlers.ofFile(filePath));

                        // Update progress within the call method
                        updateProgress(i, numFiles);
                    }

                    return null;
                }
            };

            progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


