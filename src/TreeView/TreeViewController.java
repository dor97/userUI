package TreeView;

import App.AppController;
import App.QueueManagement;
import DTO.*;
import httpClient.clientCommunication;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TreeViewController implements Initializable {
    private AppController mainController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private BorderPane borderPaneTreeView;
    private String pathToSimulation = null;
    private List<DTOEntityData> entities;
    private Alert alert;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void setAlert(Alert alert){
        this.alert = alert;
    }

    public void displayFileDetails(clientCommunication communication)throws Exception {
//        if(pathToSimulation == null || !pathToSimulation.equals(absolutePath)){
//            detailsTreeView.setRoot(null);
//            mainController.clearSimulation();
//            engine.loadSimulation(absolutePath);
//            engine.bindAndGetThreadPoolDetails2(poolThreadList);
//            pathToSimulation = absolutePath;
//
//        }
//        }


        DTOSimulationsDetails allDetails = communication.getAllSimulationDetails();
        TreeItem<DTOSimulationDetailsItem> rootItem = new TreeItem<>(allDetails);
        for(DTOSimulationDetails details : allDetails.getSimulationsDetails().values()){
            TreeItem<DTOSimulationDetailsItem> item = displaySimulation(details);
            rootItem.getChildren().add(item);
        }

//        DTOSimulationDetails details = engine.getSimulationDetails("user");
//
//        TreeItem<DTOSimulationDetailsItem> rootItem = new TreeItem<>(details);
//        TreeItem<DTOSimulationDetailsItem> entityItem = new TreeItem<>(new DTOSimulationDetailsItem("Entities"));
//        TreeItem<DTOSimulationDetailsItem> ruleItem = new TreeItem<>(new DTOSimulationDetailsItem("Rules"));
//        TreeItem<DTOSimulationDetailsItem> envItem = new TreeItem<>(new DTOSimulationDetailsItem("Environment Variables"));
//        TreeItem<DTOSimulationDetailsItem> gridItem = new TreeItem<>(new DTOSimulationDetailsItem("Grid"));
//        TreeItem<DTOSimulationDetailsItem> terminationItem = new TreeItem<>(new DTOSimulationDetailsItem("Termination"));
//
//        rootItem.getChildren().add(getEntities(details, entityItem));
//        rootItem.getChildren().add(getRules(details, ruleItem));
//        rootItem.getChildren().add(getEnvVar(details, envItem));
//        rootItem.getChildren().add(getGrid(details,gridItem));
//        rootItem.getChildren().add(getTermination(details,terminationItem));

        Platform.runLater(() -> {detailsTreeView.setRoot(rootItem);
        mainController.getTreeViewComponent().setLeft(detailsTreeView);});
    }

    private TreeItem<DTOSimulationDetailsItem> displaySimulation(DTOSimulationDetails details){
        TreeItem<DTOSimulationDetailsItem> rootItem = new TreeItem<>(details);
        TreeItem<DTOSimulationDetailsItem> entityItem = new TreeItem<>(new DTOSimulationDetailsItem("Entities"));
        TreeItem<DTOSimulationDetailsItem> ruleItem = new TreeItem<>(new DTOSimulationDetailsItem("Rules"));
        TreeItem<DTOSimulationDetailsItem> envItem = new TreeItem<>(new DTOSimulationDetailsItem("Environment Variables"));
        TreeItem<DTOSimulationDetailsItem> gridItem = new TreeItem<>(new DTOSimulationDetailsItem("Grid"));
        //TreeItem<DTOSimulationDetailsItem> terminationItem = new TreeItem<>(new DTOSimulationDetailsItem("Termination"));

        rootItem.getChildren().add(getEntities(details, entityItem));
        rootItem.getChildren().add(getRules(details, ruleItem));
        rootItem.getChildren().add(getEnvVar(details, envItem));
        rootItem.getChildren().add(getGrid(details,gridItem));
        //rootItem.getChildren().add(getTermination(details,terminationItem));

        return rootItem;
    }

    private TreeItem<DTOSimulationDetailsItem> getTermination(DTOSimulationDetails details, TreeItem<DTOSimulationDetailsItem> terminationItem) {

        TreeItem termination = new TreeItem<>(details.getTerminationList());
        return termination;
    }

    private TreeItem<DTOSimulationDetailsItem> getGrid(DTOSimulationDetails details, TreeItem<DTOSimulationDetailsItem> gridItem) {

        TreeItem grid = new TreeItem<>(details.getGridSize());
        return grid;
    }

    private TreeItem<DTOSimulationDetailsItem> getEnvVar(DTOSimulationDetails details, TreeItem<DTOSimulationDetailsItem> envItem) {

        for(DTOEnvironmentVariables environmentVariable : details.getEnvironmentVariables()){
            TreeItem<DTOSimulationDetailsItem> env = new TreeItem<>(environmentVariable);
            envItem.getChildren().add(env);
        }
        return envItem;
    }

    private TreeItem<DTOSimulationDetailsItem> getRules(DTOSimulationDetails details, TreeItem<DTOSimulationDetailsItem> ruleItem) {

        for(DTORuleData ruleData : details.getRulesList()){
            TreeItem<DTOSimulationDetailsItem> rule = new TreeItem<>(ruleData);
            for(DTOActionData actionData : ruleData.getActions()){
                rule.getChildren().add(new TreeItem<>(actionData));
            }
            ruleItem.getChildren().add(rule);
        }
        return ruleItem;
    }

    private TreeItem<DTOSimulationDetailsItem> getEntities(DTOSimulationDetails details, TreeItem<DTOSimulationDetailsItem> entityItem) {

        entities = details.getEntitysList();
        for(DTOEntityData entityData : details.getEntitysList()){
            TreeItem<DTOSimulationDetailsItem> entity = new TreeItem<>(entityData);
            for(DTOPropertyData propertyData : entityData.getPropertList()){
                entity.getChildren().add(new TreeItem<>(propertyData));
            }
            entityItem.getChildren().add(entity);
        }
        return entityItem;
    }

    public void selectItem(){
        TreeItem<DTOSimulationDetailsItem> item = detailsTreeView.getSelectionModel().getSelectedItem();

        if(item != null){
            item.getValue().getData().ifPresent(putData -> putData(putData));
        }
    }

    private void putData(Map<String, String> data){
        for(String d : data.values()){
            System.out.println(d);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        detailsTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null && newValue.getChildren().isEmpty()){
                TreeItem<DTOSimulationDetailsItem> selectedTreeItem = newValue;
                DTOSimulationDetailsItem selectedValue = selectedTreeItem.getValue();
                mainController.displayTreeItemsDetails(selectedValue);
            }
        }));
    }

    public List<DTOEntityData> getEntities(){

        return entities;
    }
}
