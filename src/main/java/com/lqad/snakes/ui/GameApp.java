package com.lqad.snakes.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // sometimes returns value sometimes no

import com.lqad.snakes.engine.GameEngin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog; // to select 2 or 3 or 4 players
import javafx.scene.layout.BorderPane; // content contianer and board Pane is the layout manager
import javafx.stage.Stage; // Window

public class GameApp extends Application {

    @Override
    public void start(Stage stage) { // built in and a must to do 

        int playerCount = askPlayerCount();
        
        
        List<String> names = new ArrayList<>();
        for(int i=1; i<=playerCount; i++) {
            names.add("Player " + i); // Palyer 1 , Palyer 2 .....
        }

        
        GameEngin engine = new GameEngin(names); // Backend

        
        BoardView boardView = new BoardView(engine); // Frontend 

        BorderPane root = new BorderPane();
        root.setCenter(boardView); // Top Left  "Center"  Right Bottom




        stage.setScene(new Scene(root));
        stage.setTitle("Snakes & Ladders - JavaFX Edition");
        stage.show();
    }

    private int askPlayerCount() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(2, 1, 2, 3, 4);
        dialog.setTitle("Players");
        dialog.setHeaderText("Select Number of Players");
        dialog.setContentText("Players:");
        Optional<Integer> result = dialog.showAndWait();
        return result.orElse(2);
    }

    public static void main(String[] args) {
        launch();
    }
}