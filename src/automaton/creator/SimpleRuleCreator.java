package automaton.creator;

import automaton.helper.InformBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.rules.RuleSimple;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class SimpleRuleCreator implements Initializable {
    private Stage window;

    private static RuleSimple newRuleSimple;
    @FXML
    private ComboBox<String> combo_amount;
    @FXML
    private ComboBox<String> combo_outcome;
    @FXML
    private TextField txt_cell_count;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options_combo_amount = FXCollections.observableArrayList("more than","less than");
        combo_amount.setValue(options_combo_amount.get(0));
        combo_amount.setItems(options_combo_amount);

        ObservableList<String> options_combo_outcome = FXCollections.observableArrayList("alive","dead");
        combo_outcome.setValue(options_combo_outcome.get(0));
        combo_outcome.setItems(options_combo_outcome);

        btn_save.setOnAction(event -> {
            String condition = null;
            int aliveNeighbours =0, outcome = -1;
            if(combo_amount.getValue() == "more than"){
                condition = "more";
            }else if(combo_amount.getValue() == "less than"){
                condition = "less";
            }
            if(combo_outcome.getValue() == "dead"){
                outcome = 0;
            }else if(combo_outcome.getValue() == "alive"){
                outcome = 1;
            }
            try {
                aliveNeighbours = Integer.parseInt(txt_cell_count.getText());
                if(aliveNeighbours < 1 || aliveNeighbours > 23){
                    InformBox.display("Wrong input!", "Put the value between 0-24");
                }else{
                    newRuleSimple = new RuleSimple(aliveNeighbours, condition, outcome);
                    Window stage = btn_save.getScene().getWindow();
                    System.out.println(newRuleSimple.toString());
                    stage.hide();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        btn_cancel.setOnAction(event -> {
            Window stage = btn_cancel.getScene().getWindow();
            newRuleSimple = null;
            stage.hide();
        });
    }


    public RuleSimple display() throws Exception{
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent layout = FXMLLoader.load(getClass().getResource("simple_rule.fxml"));
        window.setTitle("Simple rule creator");
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        if(newRuleSimple != null)
            System.out.println("Rule simple created. " + newRuleSimple.toString());
        return newRuleSimple;
    }

}
