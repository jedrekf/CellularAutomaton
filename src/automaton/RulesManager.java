package automaton;

import automaton.creator.AdvancedRuleCreator;
import automaton.creator.SimpleRuleCreator;
import com.sun.deploy.security.ruleset.Rule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.rules.IRule;
import models.rules.RuleAdvanced;
import models.rules.RuleSet;
import models.rules.RuleSimple;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class RulesManager implements Initializable {
    private Stage window;
    private static RuleSet rules;
    private ObservableList<String> observable_rules = FXCollections.observableArrayList();;

    @FXML
    private ListView<String> listview_rules = new ListView<>();
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
        for(IRule rule : rules.getList()){
            observable_rules.add(rule.toString());
        }
        listview_rules.setItems(observable_rules);
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
            rules.add(simple);
            observable_rules.add(simple.toString());
            listview_rules.setItems(observable_rules);
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
            rules.add(advanced);
            observable_rules.add(advanced.toString());
            listview_rules.setItems(observable_rules);
        }
    }


}
