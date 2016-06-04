package automaton;

import automaton.file.FileManager;
import automaton.helper.AlertBox;
import automaton.helper.InformBox;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Grid;
import models.rules.Rule;
import models.rules.RuleSet;
import sun.awt.X11.Visual;

import java.io.File;
import java.net.Authenticator;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Exchanger;

public class Main extends Application implements Initializable{

    private Stage window;
    private ResizableCanvas canvas;
    private Grid grid ;
    private int gridWidth = 500, gridHeight = 400;
    private int steps;
    private Visualization v;
    private double mouseX, mouseY;
    private static RuleSet rules = new RuleSet();
    private ObservableList<String> observable_rules = FXCollections.observableArrayList();;
    private GraphicsContext g;
    private boolean running;

    @FXML
    ScrollPane pane = new ScrollPane();
    @FXML
    private ListView<String> listview_rules = new ListView<>();
    @FXML
    Button btn_start ;
    @FXML
    Button btn_pause;
    @FXML
    Button btn_stop;
    @FXML
    TextField txt_steps;
    @FXML
    Button btn_step;
    @FXML
    Button btn_clear_grid;
    @FXML
    MenuItem menu_save;
    @FXML
    MenuItem menu_load;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new Grid(gridWidth, gridHeight);
        listview_rules.setItems(observable_rules);
        canvas = new ResizableCanvas(grid);
        canvas.setWidth(2000);
        canvas.setHeight(1000);
        pane.setContent(canvas);
        v= new Visualization(canvas, grid, rules, steps);

        //so that you can't scroll the pane with the mouse scroll
        pane.setOnScroll(event -> event.consume());
        pane.setOnDragDone(event -> event.consume());

        canvas.setOnMouseClicked(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            canvas.clickCell(mouseX, mouseY);
        });

        canvas.setOnScroll(event -> {
            canvas.zoomCanvas(event.getDeltaY());
        });

        btn_start.setOnMouseClicked(event -> {
            try {
                steps = Integer.parseInt(txt_steps.getText());
                if(steps <= 0 || steps > 100) throw new Exception("Steps should be between <1,100>");

                grid = canvas.getGrid();
                v= new Visualization(canvas, grid, rules, steps);
                if(!running) {
                    Thread t = new Thread(() -> {
                        try {
                            v.start();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    t.run();
                }

            }catch(Exception e){
                InformBox.display("wrong input", "provide step between <1,100>");
            }
        });
        btn_pause.setOnMouseClicked(event -> {
            v.pause();
        });

        btn_step.setOnMouseClicked(event -> {
            try {
                steps = Integer.parseInt(txt_steps.getText());
                if (steps <= 0 || steps > 100) throw new Exception("Steps should be between <1,100>");
                v.step(steps);
            }catch(Exception e){
                InformBox.display("wrong input", "provide step between <1,100>");
            }

        });

        btn_clear_grid.setOnAction(event -> {
            grid = new Grid(gridWidth, gridHeight);
            canvas.drawGrid(grid);
        });

        menu_save.setOnAction(event -> {
            FileChooser choose = new FileChooser();
            choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.ca)", "*.ca"));
            File f = choose.showSaveDialog(window);
            if(!f.getName().contains(".")) {
                f = new File(f.getAbsolutePath() + ".ca");
                String path = (f.getAbsolutePath());
                FileManager.save(new AutomatonState(grid, rules), path);
            }
        });

        menu_load.setOnAction(event -> {
            FileChooser choose = new FileChooser();
            choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CA", "*.ca"));
            File f = choose.showOpenDialog(window);

                AutomatonState as = (AutomatonState)FileManager.read(f.getAbsolutePath());
            if( as != null){
                grid = as.grid;
                rules = as.rules;
                for (Rule rule : rules.getList()) {
                    observable_rules.add(rule.toString());
                }
                canvas.drawGrid(grid);

            }
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
            for(Rule rule : rules.getList()) {
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

