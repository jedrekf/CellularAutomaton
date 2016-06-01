package automaton;

import automaton.helper.AlertBox;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Grid;
import models.rules.IRule;
import models.rules.RuleSet;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable{

    private Stage window;
    private ResizableCanvas canvas;
    private double canvasWidth, canvasHeight;
    private double tileSize, padding;
    private static RuleSet rules = new RuleSet();
    private ObservableList<String> observable_rules = FXCollections.observableArrayList();;
    private GraphicsContext g;
    @FXML
    private StackPane stackPane;
    @FXML
    private ListView<String> listview_rules = new ListView<>();

    /**
     * Initializes the canvas
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasWidth = 400;
        canvasHeight = 400;
        listview_rules.setItems(observable_rules);

        canvas = new ResizableCanvas();
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvasWidth, canvasHeight);
        g.setFill(Color.LIGHTBLUE);
        for (int x = 0; x < canvasWidth; x += (tileSize + padding)) {
            for (int y = 0; y < canvasHeight; y += (tileSize + padding)) {
                double offsetY = (y%(2*(tileSize + padding))) == 0 ? (tileSize + padding) /2 : 0;
                g.fillOval(x-tileSize+offsetY,y-tileSize,tileSize+tileSize,tileSize+tileSize);
            }
        }
        stackPane.getChildren().add(canvas);
    }

    /**
     * Clears the canvas and draws the given grid on a canvas.
     * @param canvas
     * @param grid
     */
    public void updateCanvas(Canvas canvas, Grid grid){

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
        if(rules != null){
            observable_rules.clear(); //repopulate the list each time it's returned
            for(IRule rule : rules.getList()) {
                observable_rules.add(rule.toString());
            }
        }
    }

    public void appExit(){
        boolean answer = AlertBox.display("Exiting the application.","Are you sure you want to exit?");
        if(answer) {
            window.close();
        }
    }



}
