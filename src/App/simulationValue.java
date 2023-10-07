package App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class simulationValue implements Initializable {
    @FXML private Label messageBox;
    @FXML private TableView valueTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageBox.setText("the commited values");
    }
}
