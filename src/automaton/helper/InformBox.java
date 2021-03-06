package automaton.helper;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Jedrek on 2016-05-21.
 */
public class InformBox {
    /**
     * Displays a Message
     * @param title title of a window
     * @param message Message in a window
     */
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button okButton = new Button("Ok");
        okButton.setOnAction(e -> {
            window.close();
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }

}
