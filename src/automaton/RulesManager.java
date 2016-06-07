package automaton;

import automaton.creator.AdvancedRuleCreator;
import automaton.creator.SimpleRuleCreator;
import automaton.helper.InformBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.rules.Rule;
import models.rules.RuleAdvanced;
import models.rules.RuleSet;
import models.rules.RuleSimple;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class RulesManager implements Initializable {
    private Stage window;
    private static RuleSet rules;
    private ObservableList<RuleSimple> observable_simple_rules = FXCollections.observableArrayList();
    private ObservableList<RuleAdvanced> observable_advanced_rules = FXCollections.observableArrayList();


    @FXML
    private ListView<RuleSimple> listview_rules_simple = new ListView<>();
    @FXML
    private ListView<RuleAdvanced> listview_rules_advanced = new ListView<>();
    @FXML
    private Button btn_define_simple_rule;
    @FXML
    private Button btn_define_advanced_rule;

    public RulesManager(RuleSet _rules) {
        rules = _rules;
    }
    public RulesManager() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Rule rule : rules.getList()){
            if(rule.type() == "advanced"){
                observable_advanced_rules.add((RuleAdvanced)rule);
            }else{
                observable_simple_rules.add((RuleSimple)rule);
            }
        }
        listview_rules_simple.setItems(observable_simple_rules);
        listview_rules_simple.setCellFactory(lv  -> new ListCell<RuleSimple>(){
            @Override
            public void updateItem(RuleSimple item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox();
                    Button btn_del = new Button("del");

                    btn_del.setOnAction(event -> {
                        rules.remove(item);
                        listview_rules_simple.getItems().remove(item);
                        observable_simple_rules.remove(item);
                    });
                    String text = item.toString(); // get text from item

                    hbox.setAlignment(Pos.CENTER);
                    hbox.getChildren().addAll(new Label(text), btn_del);

                    setGraphic(hbox);
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
                    HBox hbox = new HBox();
                    Button btn_del = new Button("del");
                    btn_del.setOnAction(event -> {
                        rules.remove(item);
                        listview_rules_advanced.getItems().remove(item);
                        observable_advanced_rules.remove(item);
                    });

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
                    hbox.getChildren().addAll(canvas, new Label(text), btn_del);
                    setGraphic(hbox);
                }
            }
        });
    }

    public RuleSet display() throws Exception{
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent layout = FXMLLoader.load(getClass().getResource("manager.fxml"));
        window.setTitle("Rules manager");
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        System.out.println("List of rules updated.");
        return rules;
    }

    /**
     * Opens a window for defining a Simple Rule
     * @throws Exception Error when calling display()
     */
    public void defineSimpleRule() throws Exception{
        SimpleRuleCreator creator = new SimpleRuleCreator();
        RuleSimple simple = creator.display();
        if(simple != null) {
            if(rules.add(simple)) {
                observable_simple_rules.add(simple);
                listview_rules_simple.refresh();
            }else{
                InformBox.display("Rule collision", "Can't add the rule, it collides with existing ones.");
            }
        }
    }

    /**
     *  Opens a window for defining a Advanced Rule
     * @throws Exception Error when calling display()
     */
    public void defineAdvancedRule() throws Exception {
        AdvancedRuleCreator creator = new AdvancedRuleCreator();
        RuleAdvanced advanced = creator.display();
        if(advanced != null) {
            if(rules.add(advanced)) {
                observable_advanced_rules.add(advanced);
                listview_rules_advanced.refresh();
            }else{
                InformBox.display("Rule collision", "Can't add the rule, it collides with existing ones.");
            }
        }
    }


}
