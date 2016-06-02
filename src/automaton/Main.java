package automaton;

import automaton.helper.AlertBox;
import com.sun.org.apache.xml.internal.security.Init;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private Grid grid;
    private double mouseX, mouseY;
    private double canvasWidth, canvasHeight;
    private int gridWidth, gridHeight;
    private static RuleSet rules = new RuleSet();
    private ObservableList<String> observable_rules = FXCollections.observableArrayList();;
    private GraphicsContext g;

    @FXML
    ScrollPane pane = new ScrollPane();
    @FXML
    private ListView<String> listview_rules = new ListView<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridWidth = 1000;
        gridHeight = 1000;
        grid = new Grid(gridWidth, gridHeight);

        canvasWidth = 1000;
        canvasHeight = 1000;
        listview_rules.setItems(observable_rules);
        canvas = new ResizableCanvas(grid);
        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);
        pane.setContent(canvas);

        canvas.setOnMouseClicked(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            canvas.clickCell(mouseX, mouseY);
        });

        canvas.setOnScroll(event -> {
            canvas.zoomCanvas(event.getDeltaX());
        });
    }


    /**
     * Clears the canvas and draws the given grid on a canvas.
     * @param canvas
     * @param grid
     */
    public void updateCanvas(ResizableCanvas canvas, Grid grid){
        canvas.drawGrid(grid);
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
        System.out.println("width " + pane.widthProperty() + " height " + pane.heightProperty());
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

