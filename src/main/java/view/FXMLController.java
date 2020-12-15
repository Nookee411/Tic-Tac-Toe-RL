package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.awt.event.ActionEvent;

public class FXMLController {
    @FXML
    private GridPane grid;

    @FXML
    private void click(ActionEvent event){
        grid.add(new Button("text"),0,0);
    }
}
