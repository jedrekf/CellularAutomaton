package automaton.creator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.Cell;
import models.rules.RuleAdvanced;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class AdvancedRuleCreator implements Initializable {
    private Stage window;
    private static RuleAdvanced newRuleAdvanced = null;
    @FXML
    private GridPane grid_pane;
    @FXML
    private ComboBox<String> combo_outcome;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> options_combo_outcome = FXCollections.observableArrayList("alive","dead");
        combo_outcome.setValue(options_combo_outcome.get(0));
        combo_outcome.setItems(options_combo_outcome);

        for(int i = 0; i<5; i++){
            for(int j=0; j<5; j++){
                ToggleButton btn = new ToggleButton();
                if(i == 2 && j ==2 ) btn.setDisable(true);
                grid_pane.add(btn, i, j);
            }
        }

        btn_save.setOnAction(event -> {
            int outcome = -1;

            Cell[][] cells =new Cell[5][5];

            if(combo_outcome.getValue() == "dead"){
                outcome = 0;
            }else if(combo_outcome.getValue() == "alive"){
                outcome = 1;
            }

            for(int i=0; i< 5; i++){
                for (int j = 0; j < 5; j++) {
                    ToggleButton btn = (ToggleButton)getNodeFromGridPane(grid_pane, i, j);
                    if(btn.isSelected()){
                        cells[i][j] = new Cell(1);
                    }else{
                        cells[i][j] = new Cell(0);
                    }
                }
            }
            newRuleAdvanced = new RuleAdvanced(cells, outcome);
            Window stage = btn_save.getScene().getWindow();
            stage.hide();
        });

        btn_cancel.setOnAction(event -> {
            Window stage = btn_cancel.getScene().getWindow();
            stage.hide();
        });
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Displays a creator for an Advanced rule
     * @return Returns created Advanced rule (null if not created)
     * @throws Exception
     */
    public RuleAdvanced display() throws Exception{
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent layout = FXMLLoader.load(getClass().getResource("advanced_rule.fxml"));

        window.setTitle("Advanced rule creator");
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        if(newRuleAdvanced != null)
            System.out.println(newRuleAdvanced.toString());
        return newRuleAdvanced;
    }


}
