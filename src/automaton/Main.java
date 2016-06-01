package automaton;

import automaton.helper.AlertBox;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.rules.RuleSet;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable{

    private Stage window;
    private ResizableCanvas canvas;
    private static RuleSet rules = new RuleSet();
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas = new ResizableCanvas();
        canvas.setWidth(400);
        canvas.setHeight(400);
        stackPane.getChildren().add(canvas);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        window = primaryStage;
        window.setTitle("Cellular Automaton");
        window.setOnCloseRequest(e -> {
            e.consume();
            appExit();
        });

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void manageRules() throws Exception {
        System.out.println("manager");
        RulesManager manager = new RulesManager(rules);
        rules = manager.display();
    }

    public void appExit(){
        boolean answer = AlertBox.display("Exiting the application.","Are you sure you want to exit?");
        if(answer) {
            window.close();
        }
    }



}
