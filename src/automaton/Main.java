package automaton;

import automaton.file.FileManager;
import automaton.helper.AlertBox;
import automaton.helper.InformBox;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Grid;
import models.rules.Rule;
import models.rules.RuleAdvanced;
import models.rules.RuleSet;
import models.rules.RuleSimple;
import sun.awt.X11.Visual;

import java.io.File;
import java.net.Authenticator;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.Exchanger;

public class Main extends Application implements Initializable{

    private Stage window;
    private ResizableCanvas canvas;
    private Grid grid ;
    private int gridWidth = 600, gridHeight = 600;
    private int steps;
    private Visualization v;
    private double mouseX, mouseY;
    private static RuleSet rules = new RuleSet();
    private ObservableList<RuleSimple> observable_simple_rules = FXCollections.observableArrayList();
    private ObservableList<RuleAdvanced> observable_advanced_rules = FXCollections.observableArrayList();
    private GraphicsContext g;
    private Thread t;

    @FXML
    ScrollPane pane = new ScrollPane();
    @FXML
    private ListView<RuleSimple> listview_rules_simple = new ListView<>();
    @FXML
    private ListView<RuleAdvanced> listview_rules_advanced = new ListView<>();
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
    MenuItem menu_save_grid;
    @FXML
    MenuItem menu_save_rules;
    @FXML
    MenuItem menu_load;

    /**
     * Initializes the program
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new Grid(gridWidth, gridHeight);
        listview_rules_simple.setItems(observable_simple_rules);
        listview_rules_simple.setCellFactory(lv  -> new ListCell<RuleSimple>(){
            @Override
            public void updateItem(RuleSimple item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String text = item.toString();
                    setText(text);
                }
            }
        });
        listview_rules_advanced.setItems(observable_advanced_rules);
        listview_rules_advanced.setCellFactory(lv  -> new ListCell<RuleAdvanced>(){
            @Override
            public void updateItem(RuleAdvanced item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String text = (item.getOutcome() == 1) ? "Then the cell is alive." : "Then the cell is dead.";
                    int cellSize = 10;
                    Canvas canvas = new Canvas((cellSize+1)*5, (cellSize+1)*5);
                    GraphicsContext g = canvas.getGraphicsContext2D();
                    g.clearRect(0, 0, cellSize*5, cellSize*5);
                    int cells[][] = item.getCells();

                    for(int i=0; i<cells.length; i++){
                        for (int j = 0; j < cells[0].length; j++) {
                            if(cells[i][j] == 1){
                                g.setFill(Color.BLACK);
                                g.fillRect(i*(cellSize+1),j*(cellSize+1),cellSize,cellSize);
                            }else if (i == 2 && j == 2){
                                g.setFill(new Color(0.0, 0.8, 0.0, 1));
                                g.fillRect(i*(cellSize+1),j*(cellSize+1),cellSize,cellSize);
                            }else{
                                g.setFill(new Color(0.9, 0.9, 0.9, 1));
                                g.fillRect(i*(cellSize+1),j*(cellSize+1),cellSize,cellSize);
                            }
                        }
                    }
                    setGraphic(canvas);
                    setText(text);
                }
            }
        });

        canvas = new ResizableCanvas(grid);
        canvas.setWidth(1366);
        canvas.setHeight(768);
        pane.setContent(canvas);
        v = new Visualization(canvas, grid, rules);

        canvas.setOnMouseClicked(event -> {
            mouseX = event.getX();
            mouseY = event.getY();
            canvas.clickCell(mouseX, mouseY);
        });

        canvas.setOnScroll(event -> {
            canvas.zoomCanvas(event.getDeltaY());
        });

        btn_start.setOnMouseClicked(event -> {
            t = new Thread(() -> {
                try {
                    v.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.run();
        });
        btn_pause.setOnMouseClicked(event -> {
            v.pause();
        });
        btn_stop.setOnAction(event -> {
            v.stop();
        });
        btn_step.setOnMouseClicked(event -> {
            try {
                steps = Integer.parseInt(txt_steps.getText());
            }catch(Exception e){
                InformBox.display("wrong input", "provide step between <1,20>");
                return;
            }

            if (steps <= 0 || steps > 20)
                InformBox.display("wrong input", "provide step between <1,20>");
            else {
                t = new Thread(() -> {
                    try {
                        v.step(steps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                t.run();

            }
        });

        btn_clear_grid.setOnAction(event -> {
            grid.clear();
            canvas.drawGrid(grid);
            v.pause();
            v.unlock();
        });

        menu_save_grid.setOnAction(event -> {
            v.pause();
            FileChooser choose = new FileChooser();
            choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CA GRID", "*.ca_grid"));

            File f = choose.showSaveDialog(window);
            if(f!=null)
            if(!f.getName().contains(".")) {
                f = new File(f.getAbsolutePath() + ".ca_grid");
                String path = (f.getAbsolutePath());
                FileManager.saveGrid(grid, path);
            }
        });
        menu_save_rules.setOnAction(event -> {
            v.pause();
            FileChooser choose = new FileChooser();
            choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CA RULES", "*.ca_rules"));

            File f = choose.showSaveDialog(window);
            if(f != null)
            if(!f.getName().contains(".")) {
                f = new File(f.getAbsolutePath() + ".ca_rules");
                String path = (f.getAbsolutePath());
                FileManager.saveRules(rules, path);
            }
        });
        menu_load.setOnAction(event -> {
            v.pause();
            FileChooser choose = new FileChooser();
            choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CellularAutomaton Files", "*.ca_grid", "*.ca_rules"));
            //choose.getExtensionFilters().add(new FileChooser.ExtensionFilter("CA RULES", "*.ca_rules"));
            File f = choose.showOpenDialog(window);
            if(f != null) {
                String fileName= f.getName();
                String fileExtension = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
                if(fileExtension.compareTo("ca_grid") == 0){
                    Grid tmpGrid = (Grid) FileManager.read(f.getAbsolutePath());
                    if(tmpGrid != null){
                        grid = tmpGrid;
                        v = new Visualization(canvas, grid, rules);
                        canvas.drawGrid(grid);
                    }
                }else if(fileExtension.compareTo("ca_rules") == 0){
                    RuleSet tmpRules = (RuleSet) FileManager.read(f.getAbsolutePath());
                    if(tmpRules != null){
                        rules = tmpRules;
                        v = new Visualization(canvas, grid, rules);
                        for (Rule rule : rules.getList()) {
                            if(rule.type() == "simple") {
                                observable_simple_rules.clear();
                                observable_simple_rules.add((RuleSimple) rule);
                                listview_rules_simple.refresh();
                            }
                            else {
                                observable_advanced_rules.clear();
                                observable_advanced_rules.add((RuleAdvanced) rule);
                                listview_rules_advanced.refresh();
                            }
                        }
                    }
                }
            }
        });
    }


    /**
     * Clears the canvas and draws the given grid on a canvas.
     * @param canvas Canvas to draw on
     * @param grid Grid to be drawn
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

    /**
     * Displays a rule manager
     * @throws Exception
     */
    public void manageRules() throws Exception {
        System.out.println("manager");
        RulesManager manager = new RulesManager(rules);
        rules = manager.display();
        if(rules != null){
            observable_advanced_rules.clear(); //repopulate the list each time it's returned
            observable_simple_rules.clear();
            for(Rule rule : rules.getList()) {
                if(rule.type() == "advanced") observable_advanced_rules.add((RuleAdvanced) rule);
                else if(rule.type() == "simple") observable_simple_rules.add((RuleSimple) rule);
            }
            listview_rules_simple.refresh();
            listview_rules_advanced.refresh();
        }
    }

    /**
     * Close the Application
     */
    private void appExit(){
        boolean answer = AlertBox.display("Exiting the application.","Are you sure you want to exit?");
        if(answer) {
            window.close();
        }
    }


}

