package automaton.creator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Jedrek on 2016-04-28.
 */
public class SimpleRuleCreator {
    private Stage window;
    public void display() throws Exception{
        window = new Stage();
        Parent layout = FXMLLoader.load(getClass().getResource("simple_rule.fxml"));

        window.setTitle("Simple rule creator");


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

}
