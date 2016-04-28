package automaton;

import automaton.creator.AdvancedRuleCreator;
import automaton.creator.SimpleRuleCreator;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class RulesManager {
        private Stage window;
        public void display() throws Exception{
                window = new Stage();
                Parent layout = FXMLLoader.load(getClass().getResource("manager.fxml"));

                window.setTitle("Rules manager");


                Scene scene = new Scene(layout);
                window.setScene(scene);
                window.show();
        }

        public void addRulesAndApply(){
                System.out.println("rules added.");
        }
        public void defineSimpleRule() throws Exception {
            SimpleRuleCreator creator = new SimpleRuleCreator();
            creator.display();
        }
        public void defineAdvancedRule() throws Exception {
            AdvancedRuleCreator creator = new AdvancedRuleCreator();
            creator.display();
        }

}
