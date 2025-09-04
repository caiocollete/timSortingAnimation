package com.caiocollete.sortinganimation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.io.IOException;

public class HelloApplication extends Application {
    AnchorPane pane;
    Button botao_inicio;
    private Button vet[];

    @Override
    public void start(Stage stage) throws Exception
    {
        stage.setTitle("Pesquisa e Ordenacao");
        pane = new AnchorPane();
        botao_inicio = new Button();
        botao_inicio.setLayoutX(10); botao_inicio.setLayoutY(100);
        botao_inicio.setText("Inicia...");
        botao_inicio.setOnAction(e->{ move_botoes();});
        pane.getChildren().add(botao_inicio);
        vet = new Button[2];
        vet[0] = new Button("4");
        vet[0].setLayoutX(100); vet[0].setLayoutY(200);
        vet[0].setMinHeight(40); vet[0].setMinWidth(40);
        vet[0].setFont(new Font(14));
        pane.getChildren().add(vet[0]);
        vet[1] = new Button("2");
        vet[1].setLayoutX(180); vet[1].setLayoutY(200);
        vet[1].setMinHeight(40); vet[1].setMinWidth(40);
        vet[1].setFont(new Font(14));
        pane.getChildren().add(vet[1]);
        vet[2] = new Button("8");
        vet[2].setLayoutX(180); vet[1].setLayoutY(200);
        vet[2].setMinHeight(40); vet[1].setMinWidth(40);
        vet[2].setFont(new Font(14));
        pane.getChildren().add(vet[2]);
        vet[3] = new Button("6");
        vet[3].setLayoutX(180); vet[1].setLayoutY(200);
        vet[3].setMinHeight(40); vet[1].setMinWidth(40);
        vet[3].setFont(new Font(14));
        pane.getChildren().add(vet[3]);
        vet[4] = new Button("1");
        vet[4].setLayoutX(180); vet[1].setLayoutY(200);
        vet[4].setMinHeight(40); vet[1].setMinWidth(40);
        vet[4].setFont(new Font(14));
        pane.getChildren().add(vet[4]);
        vet[5] = new Button("9");
        vet[5].setLayoutX(180); vet[1].setLayoutY(200);
        vet[5].setMinHeight(40); vet[1].setMinWidth(40);
        vet[5].setFont(new Font(14));
        pane.getChildren().add(vet[5]);
        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
    public void move_botoes()
    {
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() {
//permutação na tela
                for (int i = 0; i < 10; i++) {
                    Platform.runLater(() -> vet[0].setLayoutY(vet[0].getLayoutY() + 5));
                    Platform.runLater(() -> vet[1].setLayoutY(vet[1].getLayoutY() - 5));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 16; i++) {
                    Platform.runLater(() -> vet[0].setLayoutX(vet[0].getLayoutX() + 5));
                    Platform.runLater(() -> vet[1].setLayoutX(vet[1].getLayoutX() - 5));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 10; i++) {
                    Platform.runLater(() -> vet[0].setLayoutY(vet[0].getLayoutY() - 5));
                    Platform.runLater(() -> vet[1].setLayoutY(vet[1].getLayoutY() + 5));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//permutação na memória
                Button aux = vet[0];
                vet[0] = vet[1];
                vet[1] = aux;
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}