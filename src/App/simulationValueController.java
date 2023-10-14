package App;

import DTO.DTOResultOfPrepareSimulation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static DesktopUI.DesktopUI.appController;

public class simulationValueController  implements Initializable {
    @FXML
    private Label messageBox;

    @FXML
    private TableView<environmentOriginalSimulationValuesTable> valueTable;

    @FXML
    private TableView<entityOriginalSimulationValuesTable> populationTable;

    @FXML
    private TableColumn<environmentOriginalSimulationValuesTable, String> environmentColumn;

    @FXML
    private TableColumn<environmentOriginalSimulationValuesTable, String> valueColumn;

    @FXML
    private TableColumn<entityOriginalSimulationValuesTable, String> entityColumn;

    @FXML
    private TableColumn<entityOriginalSimulationValuesTable , Integer> populationColumn;

    private ObservableList<environmentOriginalSimulationValuesTable> environmentVariableTableData = FXCollections.observableArrayList();

    private ObservableList<entityOriginalSimulationValuesTable> entityPopulationTableData = FXCollections.observableArrayList();


    private Scene appControllerScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        environmentColumn.setCellValueFactory(new PropertyValueFactory<>("environment"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entity"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));

        valueTable.setItems(environmentVariableTableData);
        populationTable.setItems(entityPopulationTableData);

    }

    public void fillTables(Map<String, Integer> entityPopulation, DTOResultOfPrepareSimulation resultOfPrepareSimulation){
        entityPopulation.entrySet().stream().forEach(entry -> entityPopulationTableData.add(new entityOriginalSimulationValuesTable(entry.getKey(), entry.getValue())));
        resultOfPrepareSimulation.getEnvironmentVariablesValuesList().stream().forEach(environment -> environmentVariableTableData.add(new environmentOriginalSimulationValuesTable(environment.getName(), environment.getValue().toString())));
    }


    public void setAppControllerScene(Scene appControllerScene){
        this.appControllerScene = appControllerScene;
    }

    @FXML
    void continueButton(ActionEvent event) {
        environmentVariableTableData.clear();
        entityPopulationTableData.clear();
        valueTable.refresh();
        populationTable.refresh();
        appController.startASimulation();


    }


}
