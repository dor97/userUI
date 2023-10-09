package App;

import httpClient.clientCommunication;
import httpClient.clientCommunication.*;
import DTO.*;
import TreeDetails.TreeDetailsController;
import TreeView.TreeViewController;
import httpClient.newRequestData;
import httpClient.typeOfNewRequestData;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AppController implements Initializable {

    @FXML private Button defaultStyleButton;
    @FXML private Button graphicDisplayButton;
    @FXML private Button stopSimulationButton;
    @FXML private Button resumeSimulationButton;
    @FXML private Label consistencyLabel;
    @FXML private Label averageLabel;
    @FXML private Button loadFileButton;
    @FXML private HBox mainHbox;
    @FXML private Tab resultsTab;
    @FXML private Tab newExecutionTab;
    @FXML private Button hotStyleButton;
    @FXML private Button coldStyleButton;
    @FXML private Button nextButton;
    @FXML private TabPane tabPane;
    @FXML private Tab DetailsTab;
    @FXML private BorderPane detailsBorderPane;
    @FXML private TableColumn<QueueManagement, String> statusColumn;
    @FXML private TableColumn<QueueManagement, Integer> amountColumn;
    @FXML private TableView<QueueManagement> queueManagementTable;
    @FXML private TreeView<String> resultsTreeView;
    @FXML private Button resultsGraphButton;
    @FXML private Button histogramButton;
    @FXML private Label consistencyValueLabel;
    @FXML private Label averageValueLabel;
    @FXML private Button pauseButton;
    @FXML private ListView<ExecutionListItem> executionListView;
    @FXML private TableView entitiesRunTable;
    @FXML private TableColumn entityRunColumn;
    @FXML private TableColumn populationRunColumn;
    @FXML private Button rerunButton;
    @FXML private Label ticksLabel;
    @FXML private Label ticksValueLabel;
    @FXML private Label secondsLabel;
    @FXML private Label secondsValueLabel;
    @FXML private HBox tablesHbox;
    @FXML private Button startSimulationButton;
    @FXML private Button clearSimulationButton;
    @FXML private TableColumn<EntitiesTable, TextField> populationColumn;
    @FXML private TableColumn<EntitiesTable, String> entityColumn;
    @FXML private TableView<EntitiesTable> entitiesTable;
    @FXML private TableColumn<EnvironmentVariableTable, TextField> valueColumn;
    @FXML private TableColumn<EnvironmentVariableTable, String > environmentVarColumn;
    @FXML private TableView<EnvironmentVariableTable> environmentVarTable;
    @FXML private BorderPane treeViewComponent;
    @FXML private TreeDetailsController treeDetailsController;
    @FXML private TreeViewController treeViewController;
    @FXML private TreeView<DTOSimulationDetailsItem> detailsTreeView;
    @FXML private TextField loadedFilePathTextBox;
    @FXML private TextArea exceptionArea;
    @FXML private ChoiceBox<String> simulationNameChoiceBox;
    @FXML private TextField amountToRunfiled;
    @FXML private TextField tickfiled;
    @FXML private TextField secfield;
    @FXML private Button submitButton;
    @FXML private Button executeButton;
    @FXML private TableView<requestTable> requestTable;
    @FXML private TableColumn<requestTable, Integer> requestIdColumn;
    @FXML private TableColumn<requestTable, String> simulationNameColumn;
    @FXML private TableColumn<requestTable, Integer> amountToRunColumn;
    @FXML private TableColumn<requestTable, approvementStatus> requestStatusColumn;
    @FXML private TableColumn<requestTable, Integer> currentlyRunningSimulationColumn;
    @FXML private TableColumn<requestTable, Integer> doneRunningColumn;
    @FXML private TableView<entityOriginalSimulationValuesTable> entityOriginValueTable;
    @FXML private TableColumn<entityOriginalSimulationValuesTable, String> entityNameOriginColumn;
    @FXML private TableColumn<entityOriginalSimulationValuesTable, Integer> populationOriginColumn;
    @FXML private TableView<environmentOriginalSimulationValuesTable> environmentOriginValueTable;
    @FXML private TableColumn<environmentOriginalSimulationValuesTable, String> environmentOriginColumn;
    @FXML private TableColumn<environmentOriginalSimulationValuesTable, String> valueOriginColumn;



    private simulationValue originalSimulationValue;
    private Map<Integer, Integer> requestIdToIndex = new HashMap<>();
    private Stage primaryStage;
    private Stage graphicDisplayStage;
    private clientCommunication communication;
    private ObservableList<EnvironmentVariableTable> environmentVariableTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesTable> entitiesTableData = FXCollections.observableArrayList();
    private ObservableList<EntitiesRunTable> entitiesRunTablesData = FXCollections.observableArrayList();
    private ObservableList<ExecutionListItem> executionListViewData = FXCollections.observableArrayList();
    private ObservableList<QueueManagement> queueManagementData = FXCollections.observableArrayList();
    private ObservableList<requestTable> requestTablesData = FXCollections.observableArrayList();
    private ObservableList<entityOriginalSimulationValuesTable> entityOriginValues = FXCollections.observableArrayList();
    private ObservableList<environmentOriginalSimulationValuesTable> environmentOriginValues = FXCollections.observableArrayList();

    private int simulationID;
    private myTask newTask = null;
    private Integer lastSimulationNum = 0;
    private Boolean isFirstSimulationForFile = true;
    private LineChart lastSimulationGraph;
    private BarChart lastSimulationHistogram;
    private ScatterChart simulationSpace;
    private String lastChosenPropertyForHistogram;
    private String lastChosenEntityForHistogram;
    private Alert alert = new Alert(Alert.AlertType.ERROR);
    private BooleanProperty isSimulationEnded = new SimpleBooleanProperty(false);
    private ParallelTransition pt;
    private Thread taskThread;
    private Integer choiceBoxAddIndex = 0;
    private String simulationChosenName = null;
    private Integer amountToRun;
    private Integer ticks;
    private Integer sec;
    private Map<Integer, List<entityOriginalSimulationValuesTable>> entityOriginalValuesMap = new HashMap<>();
    private Map<Integer, List<environmentOriginalSimulationValuesTable>> environmentOriginalValuesMap = new HashMap<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setTitle("Error");
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL mainFXML = getClass().getResource("/resources/simulationValue.fxml");
//        fxmlLoader.setLocation(mainFXML);
//        try {
//            originalSimulationValue = fxmlLoader.getController();
//            Parent root = fxmlLoader.load();
//            Scene scene = new Scene(root);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }

        rerunButton.setDisable(true);
        if (treeViewController != null && treeDetailsController != null) {
            treeViewController.setMainController(this);
            treeDetailsController.setMainController(this);
            treeViewController.setAlert(alert);
        }
        communication = new clientCommunication();
        resultsGraphButton.setDisable(true);
        histogramButton.setDisable(true);
        environmentVarColumn.setCellValueFactory(new PropertyValueFactory<>("envVarNameColumn"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        entityColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        entityRunColumn.setCellValueFactory(new PropertyValueFactory<>("entityName"));
        populationRunColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        simulationNameColumn.setCellValueFactory(new PropertyValueFactory<>("simulationName"));
        amountToRunColumn.setCellValueFactory(new PropertyValueFactory<>("amountToRun"));
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        currentlyRunningSimulationColumn.setCellValueFactory(new PropertyValueFactory<>("currentlyRunningSimulation"));
        doneRunningColumn.setCellValueFactory(new PropertyValueFactory<>("doneRunning"));

        entityNameOriginColumn.setCellValueFactory(new PropertyValueFactory<>("entity"));
        populationOriginColumn.setCellValueFactory(new PropertyValueFactory<>("population"));

        environmentOriginColumn.setCellValueFactory(new PropertyValueFactory<>("environment"));
        valueOriginColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        environmentOriginValueTable.setItems(environmentOriginValues);
        entityOriginValueTable.setItems(entityOriginValues);
        requestTable.setItems(requestTablesData);
        environmentVarTable.setItems(environmentVariableTableData);
        entitiesTable.setItems(entitiesTableData);
        entitiesRunTable.setItems(entitiesRunTablesData);
        queueManagementTable.setItems(queueManagementData);
        executionListView.setItems(executionListViewData);

        nextButton.setDisable(true);
        graphicDisplayButton.setDisable(true);
        pauseButton.setDisable(true);
        resumeSimulationButton.setDisable(true);
        stopSimulationButton.setDisable(true);

        isSimulationEnded.addListener((observable, oldValue, newValue) -> {
            displaySimulationResults();
            resultsGraphButton.setDisable(false);});


        executionListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            if(entityOriginalValuesMap.containsKey(newValue.getID())) {
                entityOriginValues.clear();
                entityOriginValues.addAll(entityOriginalValuesMap.get(newValue.getID()));
                entityOriginValueTable.refresh();
            }

            if (environmentOriginalValuesMap.containsKey(newValue.getID())){
                environmentOriginValues.clear();
                environmentOriginValues.addAll(environmentOriginalValuesMap.get(newValue.getID()));
                environmentOriginValueTable.refresh();
            }


            try {
                if (newTask != null && communication.getSimulationStatus(lastSimulationNum).getSimulationStatus() == Status.RUNNING) {
                    stopGettingDataUsingTask(newTask);
                }
            }catch (Exception e){
                alert.setContentText(e.getMessage());
                alert.show();
            }

            if (newValue !=null){
                ticksValueLabel.setText("0");
                secondsValueLabel.setText("0");
                entitiesRunTablesData.clear();
                resultsTreeView.setRoot(null);
                consistencyValueLabel.setText("");
                averageValueLabel.setText("");
                rerunButton.setDisable(true);
                histogramButton.setDisable(true);
                resultsGraphButton.setDisable(true);
                exceptionArea.setText("");

                ExecutionListItem selectedListItem = newValue;
                Integer selectedValue = selectedListItem.getID();

                lastSimulationNum = selectedValue;
                try {
                    communication.setChosenSimulationId(selectedValue);
                    if (communication.getSimulationStatus(lastSimulationNum).getSimulationStatus() == Status.RUNNING) {
                        pauseButton.setDisable(false);
                        resumeSimulationButton.setDisable(false);
                        stopSimulationButton.setDisable(false);
                    } else {
                        pauseButton.setDisable(true);
                        resumeSimulationButton.setDisable(true);
                        stopSimulationButton.setDisable(true);
                        nextButton.setDisable(true);
                        graphicDisplayButton.setDisable(true);
                    }


                    if (communication.getSimulationStatus(lastSimulationNum).getSimulationStatus() != Status.FINISHED) {
                        rerunButton.setDisable(true);
                    } else {
                        rerunButton.setDisable(false);
                    }
                    if (communication.getSimulationStatus(selectedValue).getSimulationStatus() == Status.WAITINGTORUN) {
                        newTask = null;
                        return;
                    }
                    newTask = new myTask();
                    //exceptionArea.promptTextProperty().bind(newTask.messageProperty());
                    resultsGraphButton.setDisable(true);
                    histogramButton.setDisable(true);
                    newTask.bindProperties(ticksValueLabel.textProperty(), secondsValueLabel.textProperty(), exceptionArea.promptTextProperty(), entitiesRunTablesData, isSimulationEnded);
                    getDataUsingTask(newTask);
                    if (communication.getSimulationStatus(selectedValue).getSimulationStatus() == Status.FINISHED) {
                        resultsGraphButton.setDisable(false);
                        displaySimulationResults();
                    }
                }catch (Exception e){
                    alert.setContentText(e.getMessage());
                    alert.show();
                }
            }
        }));
        //loadFileButton.setStyle();

        FadeTransition ft = new FadeTransition(Duration.millis(1000));//Duration.INDEFINITE
        ft.setFromValue(1);
        ft.setToValue(0.4);
        ft.setAutoReverse(true);
        ft.setCycleCount(Animation.INDEFINITE);
        pt = new ParallelTransition(loadFileButton, ft);
        pt.play();

        resultsTreeView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue !=null && newValue.getChildren().isEmpty()){
                histogramButton.setDisable(false);
                TreeItem<String > selectedTreeItem = newValue;
                String selectedValue = selectedTreeItem.getValue();
                lastChosenPropertyForHistogram = newValue.getValue();
                lastChosenEntityForHistogram = newValue.getParent().getValue();
                setConsistencyValue(newValue.getParent().getValue(), newValue.getValue());
                setAverageValue(newValue.getParent().getValue(), newValue.getValue());
            }
            else if (newValue !=null && !newValue.getChildren().isEmpty()){
                histogramButton.setDisable(true);
            }
        }));
        simulationNameChoiceBox.setOnAction((ActionEvent event) -> simulationChosenName = simulationNameChoiceBox.getValue());
        try {
            communication.fetchDataFromServer();
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }
        updateWorldDifenichan();
        updateThreadPoolDetailsLoop(queueManagementData);
        updateRequestTableOnLoop();
    }

    public void updateRequestTableOnLoop(){
        Thread thread = new Thread(() -> {while(true){
                                                try{
                                                    Thread.sleep(1000);
                                                    updateRequestTable();
                                                }catch (Exception e){

                                                }
                                                            }
                                                        });
        thread.setDaemon(true);
        thread.start();
    }

    public void updateRequestTable(){
        List<newRequestData> newRequestDataList = communication.getNewRequestDataList();
        if(newRequestDataList.size() > 0){
            for (newRequestData requestData : newRequestDataList){
                if(requestData.getType().equals(typeOfNewRequestData.NEW)){
                    requestTablesData.add(new requestTable(requestData.getId(), requestData.getSimulationName(), requestData.getAmountToRun(), requestData.getNewStatus(), requestData.getCurrentRun(), requestData.getDone()));
                    requestIdToIndex.put(requestData.getId(), requestTablesData.size() - 1);
                }else {
                    if(requestData.getStatusChanged()){
                        requestTablesData.get(requestIdToIndex.get(requestData.getId())).setRequestStatus(requestData.getNewStatus());
                    }
                    if (requestData.getCurrentRunChanged()){
                        requestTablesData.get(requestIdToIndex.get(requestData.getId())).setCurrentlyRunningSimulation(requestData.getCurrentRun());
                    }
                    if(requestData.getDoneChanged()){
                        requestTablesData.get(requestIdToIndex.get(requestData.getId())).setDoneRunning(requestData.getDone());
                    }
                    Platform.runLater(() -> requestTable.refresh());

                }
            }
        }
    }

    public void updateWorldDifenichan(){
        Thread thread = new Thread(() -> {
            while (true){
                                            try {
                                                Thread.sleep(1000);
                                                if(communication.getIsWorldDifenichanCollecenUpdated()) {
                                                    treeViewController.displayFileDetails(communication);
                                                    addWorldsToChoiceBox();}
                                            }catch (Exception e){
                                                System.out.println(e.getMessage());
                                            }
                                            }});
        thread.setDaemon(true);
        thread.start();
    }

    private void addWorldsToChoiceBox(){
        List<String> worldsToAdd = communication.getWorldDifenichanCollecen().stream().skip(choiceBoxAddIndex).collect(Collectors.toList());
        choiceBoxAddIndex += worldsToAdd.size();
        for (String worldName : worldsToAdd){
            simulationNameChoiceBox.getItems().add(worldName);
        }

    }

    public void stopGettingDataUsingTask(myTask task){
        synchronized (task) {
            taskThread.interrupt();
        }
    }

    public void getDataUsingTask(myTask task){
        synchronized (task) {
            taskThread = new Thread(task);
            taskThread.setDaemon(true);
//        synchronized (this.taskThread) {
//            this.taskThread = taskThread;
//        }
            taskThread.setName("TaskThread");
//        synchronized (simStatus) {
//            world = simStatus.get(simulationId).getWorld();
//            simStatus.get(simulationId).setTask(task);
//            simStatus.get(simulationId).setTaskThread(taskThread);
//        }
            task.loadClientCommunication(communication);
            taskThread.start();
        }

    }

    private void setAverageValue(String entity, String property) {
        try {
            if (!communication.getPostRunData(lastSimulationNum).getAvPropertyValue().containsKey(entity + "_" + property)) {
                averageValueLabel.setText("");
                return;
            }
            averageValueLabel.setText(communication.getPostRunData(lastSimulationNum).getAvPropertyValue().get(entity + "_" + property).getKey().toString());
        }catch (Exception e){

        }
    }

    private void displaySimulationResults() {

        fillResultsTreeView();
    }

    private void setConsistencyValue(String entity, String property) {
        try {
            if (!communication.getPostRunData(lastSimulationNum).getPropertyChangeByTick().containsKey(entity + "_" + property)) {
                consistencyValueLabel.setText("");
                return;
            }
            Float sum = 0f;
            List<Float> consistencies = communication.getPostRunData(lastSimulationNum).getPropertyChangeByTick().get(entity + "_" + property);
            Float size = (float) consistencies.size();
            for (Float consistency : consistencies) {
                sum += consistency;
            }
            Float result = sum / size;
            consistencyValueLabel.setText(result.toString());
        }catch (Exception e){

        }
    }

    private void fillResultsTreeView() {
        try {
            DTOSimulationDetailsPostRun results = communication.getPostRunData(lastSimulationNum);
            TreeItem<String> rootItem = new TreeItem<>(new String("Entities"));

            for (DTOEntitysProperties entity : results.getEntitysProperties().values()) {
                TreeItem<String> entityItem = new TreeItem<>(entity.getName());
                for (DTOProperty property : entity.getProperties()) {
                    entityItem.getChildren().add(new TreeItem<>(property.getName()));
                }
                rootItem.getChildren().add(entityItem);
            }
            resultsTreeView.setRoot(rootItem);
        }catch (Exception e){

        }
    }

    private LineChart createLastSimulationGraph() {

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Ticks");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("No.of entities");
        LineChart linechart = new LineChart(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Entities Per Tick");
        try {
            for (Pair<Integer, Integer> pair : communication.getPostRunData(lastSimulationNum).getNumOfEntitiesPerTick()) {
                int tick = pair.getKey();
                int entityAmount = pair.getValue();
                series.getData().add(new XYChart.Data(tick, entityAmount));
            }
        }catch (Exception e){

        }
        linechart.getData().add(series);
        return linechart;
    }

    private BarChart createLastSimulationHistogram() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Property Value");
        yAxis.setLabel("Amount");

        XYChart.Series series1 = new XYChart.Series();
        try {
            for (Map.Entry<Object, Integer> property : communication.getPostRunData(lastSimulationNum).getHistogram(lastChosenEntityForHistogram, lastChosenPropertyForHistogram).entrySet()) {
                series1.getData().add(new XYChart.Data(property.getKey().toString(), property.getValue()));
            }
        }catch (Exception e){

        }
        barChart.getData().addAll(series1);
        return barChart;
    }

    public void shutDownSystem(){
//        try {
//            communication.disposeOfThreadPool();
//        }catch (Exception e){
//
//        }
    }

    public void clearSimulation(){
        environmentVariableTableData.clear();
        entitiesTableData.clear();
        entitiesRunTablesData.clear();
        executionListViewData.clear();
        ticksValueLabel.setText("0");
        secondsValueLabel.setText("0");
        newTask = null;
        lastSimulationNum = 0;
        treeDetailsController.clearData();



    }

    public void setTreeViewComponentController(TreeViewController treeViewController) {
        this.treeViewController = treeViewController;
        treeViewController.setMainController(this);
        treeViewController.setAlert(alert);
    }

    public void setTreeDetailsComponentController(TreeDetailsController treeDetailsController) {
        this.treeDetailsController = treeDetailsController;
        treeDetailsController.setMainController(this);
    }
    @FXML
    void loadFile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        pt.stop();
        loadFileButton.setOpacity(1);

        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        loadedFilePathTextBox.setText(absolutePath);
        loadedFilePathTextBox.setDisable(true);
        System.out.println("get world ");
        try {
            //treeViewController.displayFileDetails(engine, absolutePath, queueManagementData);
            fillEnvironmentVariableTable(communication);
            fillEntitiesTable(communication);
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    private void fillEntitiesTable(clientCommunication communication) {
        try {
            DTOSimulationDetails details = communication.getASimulationDetails(communication.getChosenSimulationName());
            entitiesTableData.clear();
            for (DTOEntityData entity : details.getEntitysList()) {
                entitiesTableData.add(new EntitiesTable(entity.getName(), "0"));
            }
        }catch (Exception e){

        }
    }

    public void fillEnvironmentVariableTable(clientCommunication communication) {
        try {
            DTOSimulationDetails details = communication.getASimulationDetails(communication.getChosenSimulationName());
            environmentVariableTableData.clear();
            for (DTOEnvironmentVariables environmentVariable : details.getEnvironmentVariables()){
                environmentVariableTableData.add(new EnvironmentVariableTable(environmentVariable.getVariableName() + "("+ environmentVariable.getVariableType()+")", "", environmentVariable.getVariableName()));
            }
        }catch (Exception e){

        }

    }

    public BorderPane getTreeViewComponent(){
        return treeViewComponent;
    }

    public void setStage (Stage i_primaryStage){
        this.primaryStage = i_primaryStage;
    }

    public void displayTreeItemsDetails(DTOSimulationDetailsItem selectedValue) {
        treeDetailsController.displayTreeItemsDetails(selectedValue);
    }

    public void startSimulation(ActionEvent actionEvent) {
        try{
//            for (EnvironmentVariableTable environmentVariable : environmentVariableTableData){
//                if (!environmentVariable.getValue().getText().isEmpty()){
//                    try{
//                        engine.addEnvironmentVariableValue(environmentVariable.getEnvVarNameNoType(), environmentVariable.getValue().getText());
//                    }catch (Exception e){
//                        alert.setContentText(e.getMessage());
//                        alert.show();
//                        System.out.println(e.getMessage());
//                        return;
//                    }
//                }
//            }
//
//            for (EntitiesTable entity : entitiesTableData){
//                if (!entity.getPopulation().getText().equals("0")){
//                    try{
//                        engine.addPopulationToEntity(entity.getEntityName(), Integer.parseInt(entity.getPopulation().getText()));
//                    }catch (Exception e){
//                        alert.setContentText(e.getMessage());
//                        alert.show();
//                        System.out.println(e.getMessage());
//                        return;
//                    }
//                }
//            }//TODO change
            Integer requestId = communication.getRequestIdChosen();
            if(requestId == null){
                return;
            }
            Map<String, String> environmentVariableMap = environmentVariableTableData.stream().collect(Collectors.toMap(environmentVariable -> environmentVariable.getEnvVarNameNoType(), environmentVariable -> environmentVariable.getValue().getText()));
            Map<String, Integer> entityMap = entitiesTableData.stream().collect(Collectors.toMap(entity -> entity.getEntityName(), entity -> Integer.parseInt(entity.getPopulation().getText())));
            DTOResultOfPrepareSimulation resultOfPrepareSimulation = communication.prepareSimulation(requestId, communication.getUserName(), environmentVariableMap, entityMap);
            DTOSimulationId simulationId = communication.startSimulation();
            entityOriginalValuesMap.put(simulationId.getSimulationId(), entityMap.entrySet().stream().map(entry -> new entityOriginalSimulationValuesTable(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
            environmentOriginalValuesMap.put(simulationId.getSimulationId(), resultOfPrepareSimulation.getEnvironmentVariablesValuesList().stream().map(DTOEnvironment -> new environmentOriginalSimulationValuesTable(DTOEnvironment.getName(), DTOEnvironment.getValue().toString())).collect(Collectors.toList()));

            //communication.setChosenSimulationId(simulationId.getSimulationId());
            simulationID = simulationId.getSimulationId();
//            engine.setSimulation();
//            simulationID = engine.activeSimulation(new myTask());
            executionListViewData.add(new ExecutionListItem(simulationID));
            if(isFirstSimulationForFile){
                isFirstSimulationForFile = false;
                //engine.updateNewlyFinishedSimulationInLoop(executionListViewData);
                Thread thread = new Thread(() -> {  while(true)
                {List<Integer> ids = communication.getNewlyFinishedSimulation(communication.getPrevIndexForFinishedSimulation());
                    //ObservableList<ExecutionListItem> toRemove = FXCollections.observableArrayList();
                    for(Integer id : ids){
                        for(ExecutionListItem executionListItem : executionListViewData){
                            if(executionListItem.getID().equals(id)){
                                executionListItem.setToFinished();
                            }
                        }
                    }
                    Platform.runLater(() -> executionListView.refresh());

//                    for(Integer id : ids){
//                        for(ExecutionListItem executionListItem : executionListViewData){
//                            if(executionListItem.getID().equals(id)){
//                                toRemove.add(executionListItem);
//                            }
//                        }
//                    }

//                    Platform.runLater(() -> {for(ExecutionListItem executionListItem : toRemove){//TODO make logic when simulation ended
//                        executionListViewData.remove(executionListItem);
//                        executionListViewData.add(new ExecutionListItem(executionListItem.getID(), true));
//
//                    }});


                    if(ids.size() != 0) {
                        StringBuilder result = new StringBuilder();
                        result.append("The following simulations are done: ");
                        ids.stream().forEach(id -> result.append(id.toString() + "  "));
                        Platform.runLater(() -> { Alert fines = new Alert(Alert.AlertType.INFORMATION);
                                                  fines.setTitle("simulation/s finished");

                                                  //fines.setHeight(200);
                                                  fines.setContentText(result.toString());
                                                  fines.show();});
                    }
                    try{Thread.sleep(200);}catch (InterruptedException e){}}
                });
                thread.setDaemon(true);
                thread.start();
            }

        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println(e.getMessage());
        }
    }

    public void clearSimulation(ActionEvent actionEvent) {

        for (EnvironmentVariableTable environmentVariable : environmentVariableTableData){
            environmentVariable.getValue().setText("");
        }

        for (EntitiesTable entity : entitiesTableData){
            entity.getPopulation().setText("0");
        }
    }

    public void rerun(ActionEvent actionEvent) {
        try {
            if (communication.getSimulationStatus(lastSimulationNum).getSimulationStatus() != Status.FINISHED) {
                return;
            }

            if (lastSimulationNum != 0) {
                entitiesTableData.clear();
                DTODataForReRun dataForReRun = communication.getDataForRerun(lastSimulationNum);
                if (dataForReRun == null) {
                    return;
                }
                for (String entity : dataForReRun.getEntitiesPopulation().keySet()) {
                    entitiesTableData.add(new EntitiesTable(entity, dataForReRun.getEntitiesPopulation().get(entity).toString()));
                    //entitiesTableData.add(new EntitiesTable(entity.getName(), "0"));
                }

                for (EnvironmentVariableTable environmentVariableTable : environmentVariableTableData) {
                    if (dataForReRun.getEnvironmentsValues().containsKey(environmentVariableTable.getEnvVarNameNoType())) {
                        environmentVariableTable.setValueInText(dataForReRun.getEnvironmentsValues().get(environmentVariableTable.getEnvVarNameNoType()).toString());
                    } else {
                        environmentVariableTable.setValueInText("");
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public void pauseSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(false);
        pauseButton.setDisable(true);
        resumeSimulationButton.setDisable(false);
        graphicDisplayButton.setDisable(false);
        resultsGraphButton.setDisable(false);
        try {
            communication.pauseSimulation(lastSimulationNum);
        }catch (Exception e){

        }
        fillResultsTreeView();
    }

    public void stopSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(true);
        try {
            communication.stopSimulation(lastSimulationNum);
        }catch (Exception e){

        }
    }

    public void resumeSimulation(ActionEvent actionEvent) {
        nextButton.setDisable(true);
        resumeSimulationButton.setDisable(true);
        pauseButton.setDisable(false);
        graphicDisplayButton.setDisable(true);
        resultsGraphButton.setDisable(true);
        try {
            communication.resumeSimulation(lastSimulationNum);
        }catch (Exception e){

        }
        resultsTreeView.setRoot(null);
        averageValueLabel.setText("");
        consistencyValueLabel.setText("");
        histogramButton.setDisable(true);
        graphicDisplayButton.setDisable(true);
        resultsGraphButton.setDisable(true);
    }

    public void showGraph(ActionEvent actionEvent) {

        lastSimulationGraph = createLastSimulationGraph();

        ScrollPane scrollPane = new ScrollPane(lastSimulationGraph);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Stage stage = new Stage();
        stage.setTitle("Entities by Ticks Line Chart");
        Scene scene  = new Scene(scrollPane,650,450);
        stage.setScene(scene);
        stage.show();
    }

    public void showHistogram(ActionEvent actionEvent) {

        lastSimulationHistogram = createLastSimulationHistogram();
        ScrollPane scrollPane = new ScrollPane(lastSimulationHistogram);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Stage stage = new Stage();
        Scene scene  = new Scene(scrollPane,650,450);
        stage.setScene(scene);
        stage.show();
    }

    public void nextSimulationStep(ActionEvent actionEvent) {
        try {
            communication.moveOneStep(lastSimulationNum);
            if (simulationSpace != null) {
                createSimulationSpace(communication.getMap(lastSimulationNum));
            }
            consistencyValueLabel.setText("");
            averageValueLabel.setText("");
            fillResultsTreeView();
        }catch (Exception e){

        }
    }
    public void changeStyleHot(ActionEvent actionEvent) {
        DetailsTab.setStyle("-fx-background-color: yellow;");
        resultsTab.setStyle("-fx-background-color: red;");
        newExecutionTab.setStyle("-fx-background-color: orange;");
        mainHbox.setStyle("-fx-background-color: #f1ce47;");
        loadFileButton.setStyle("-fx-background-color: #ee7d0c;");
        detailsBorderPane.setStyle("-fx-background-color: #efba71;");
        statusColumn.setStyle("-fx-background-color: #efba71;");
        amountColumn.setStyle("-fx-background-color: #efba71;");
        environmentVarColumn.setStyle("-fx-background-color: #fc6b3c;");
        valueColumn.setStyle("-fx-background-color: #fc6b3c;");
        entityColumn.setStyle("-fx-background-color: #fc6b3c;");
        populationColumn.setStyle("-fx-background-color: #fc6b3c;");
        startSimulationButton.setStyle("-fx-background-color: #eac81d;");
        executionListView.setStyle("-fx-background-color: #fdc076;");
        entityRunColumn.setStyle("-fx-background-color: #FD7676FF;");
        populationRunColumn.setStyle("-fx-background-color: #FD7676FF;");
        resultsTreeView.setStyle("-fx-background-color: #B67878FF;");
        ticksLabel.setStyle("-fx-background-color: #ffde00;");
        secondsLabel.setStyle("-fx-background-color: #FF0000FF;");
        ticksValueLabel.setStyle("-fx-background-color: #ffde00;");
        secondsValueLabel.setStyle("-fx-background-color: #FF0000FF;");
        consistencyLabel.setStyle("-fx-background-color: #FF7300FF;");
        averageLabel.setStyle("-fx-background-color: #d97575;");
        consistencyValueLabel.setStyle("-fx-background-color: #FF7300FF;");
        averageValueLabel.setStyle("-fx-background-color: #d97575;");
        rerunButton.setStyle("-fx-background-color: #eac81d;");
        resumeSimulationButton.setStyle("-fx-background-color: #eac81d;");
        nextButton.setStyle("-fx-background-color: #fdc076;");
        histogramButton.setStyle("-fx-background-color: #ee7d0c;");
        resultsGraphButton.setStyle("-fx-background-color: #ee7d0c;");
        pauseButton.setStyle("-fx-background-color: #FF0000FF;");
        stopSimulationButton.setStyle("-fx-background-color: #FF0000FF;");
        clearSimulationButton.setStyle("-fx-background-color: #FF0000FF;");
        graphicDisplayButton.setStyle("-fx-background-color: #efba71;");
        queueManagementTable.refresh();
        environmentVarTable.refresh();
        entitiesTable.refresh();
        entitiesRunTable.refresh();
    }
    public void changeStyleCold(ActionEvent actionEvent) {
        DetailsTab.setStyle("-fx-background-color: #0BEAD0FF;");
        resultsTab.setStyle("-fx-background-color: #0B9CEAFF;");
        newExecutionTab.setStyle("-fx-background-color: #765EE1FF;");
        mainHbox.setStyle("-fx-background-color: #5EE197FF;");
        loadFileButton.setStyle("-fx-background-color: #094ee3;");
        detailsBorderPane.setStyle("-fx-background-color: #74EFA9FF;");
        statusColumn.setStyle("-fx-background-color: #0AD6F1FF;");
        amountColumn.setStyle("-fx-background-color: #0AD6F1FF;");
        environmentVarColumn.setStyle("-fx-background-color: #A777E5FF;");
        valueColumn.setStyle("-fx-background-color: #A777E5FF;");
        entityColumn.setStyle("-fx-background-color: #A777E5FF;");
        populationColumn.setStyle("-fx-background-color: #A777E5FF;");
        startSimulationButton.setStyle("-fx-background-color: #6193B2FF;");
        clearSimulationButton.setStyle("-fx-background-color: #8d77d9;");
        executionListView.setStyle("-fx-background-color: #1dea76;");
        entityRunColumn.setStyle("-fx-background-color: #4BA7E0FF;");
        populationRunColumn.setStyle("-fx-background-color: #4BA7E0FF;");
        resultsTreeView.setStyle("-fx-background-color: #7897b6;");
        ticksLabel.setStyle("-fx-background-color: #00d0ff;");
        secondsLabel.setStyle("-fx-background-color: #96b2bd;");
        ticksValueLabel.setStyle("-fx-background-color: #00d0ff;");
        secondsValueLabel.setStyle("-fx-background-color: #96b2bd;");
        consistencyLabel.setStyle("-fx-background-color: #9887b0;");
        averageLabel.setStyle("-fx-background-color: #c0afe7;");
        consistencyValueLabel.setStyle("-fx-background-color: #9887b0;");
        averageValueLabel.setStyle("-fx-background-color: #c0afe7;");
        rerunButton.setStyle("-fx-background-color: #1dea76;");
        resumeSimulationButton.setStyle("-fx-background-color: #1dea76;");
        nextButton.setStyle("-fx-background-color: #9c76fd;");
        histogramButton.setStyle("-fx-background-color: #00d0ff;");
        resultsGraphButton.setStyle("-fx-background-color: #00d0ff;");
        pauseButton.setStyle("-fx-background-color: #994de3;");
        stopSimulationButton.setStyle("-fx-background-color: #994de3;");
        graphicDisplayButton.setStyle("-fx-background-color: #96b2bd;");
        queueManagementTable.refresh();
        environmentVarTable.refresh();
        entitiesTable.refresh();
        entitiesRunTable.refresh();
    }

    public void showGraphicDisplay(ActionEvent actionEvent) {
        try {
            DTOMap graphicDisplay = communication.getMap(lastSimulationNum);
            graphicDisplayStage = new Stage();

            graphicDisplayStage.setTitle("Simulation Space");
            final NumberAxis xAxis = new NumberAxis(0, graphicDisplay.getCols(), 5);
            final NumberAxis yAxis = new NumberAxis(0, graphicDisplay.getRows(), 5);
            simulationSpace = new ScatterChart(xAxis, yAxis);

            createSimulationSpace(graphicDisplay);

            Scene scene = new Scene(simulationSpace, 600, 500);
            graphicDisplayStage.setScene(scene);
            graphicDisplayStage.show();
        }catch (Exception e){

        }
    }

    private void createSimulationSpace(DTOMap graphicDisplay) {

        simulationSpace.getData().clear();
        double column =0;
        double row =0;
        List<DTOEntityData> entities = treeViewController.getEntities();
        for (DTOEntityData entity : entities) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(entity.getName());
            row = 0;
            column = 0;
            for (DTOMapSpace[] rows : graphicDisplay.getMap()) {
                for(DTOMapSpace columns : rows){
                    if (columns.getEntityName().equals(entity.getName())){
                        series.getData().add(new XYChart.Data<>(column, row));
                    }
                    column= column+1;
                }
                column=0;
                row= row+1;
            }
            simulationSpace.getData().addAll(series);
        }
    }

    public void changeToDefaultStyle(ActionEvent actionEvent) {
        DetailsTab.setStyle(null);
        resultsTab.setStyle(null);
        newExecutionTab.setStyle(null);
        mainHbox.setStyle(null);
        loadFileButton.setStyle(null);
        detailsBorderPane.setStyle(null);
        statusColumn.setStyle(null);
        amountColumn.setStyle(null);
        environmentVarColumn.setStyle(null);
        valueColumn.setStyle(null);
        entityColumn.setStyle(null);
        populationColumn.setStyle(null);
        startSimulationButton.setStyle(null);
        clearSimulationButton.setStyle(null);
        executionListView.setStyle(null);
        entityRunColumn.setStyle(null);
        populationRunColumn.setStyle(null);
        resultsTreeView.setStyle(null);
        ticksLabel.setStyle(null);
        secondsLabel.setStyle(null);
        ticksValueLabel.setStyle(null);
        secondsValueLabel.setStyle(null);
        consistencyLabel.setStyle(null);
        averageLabel.setStyle(null);
        consistencyValueLabel.setStyle(null);
        averageValueLabel.setStyle(null);
        rerunButton.setStyle(null);
        resumeSimulationButton.setStyle(null);
        nextButton.setStyle(null);
        histogramButton.setStyle(null);
        resultsGraphButton.setStyle(null);
        pauseButton.setStyle(null);
        stopSimulationButton.setStyle(null);
        graphicDisplayButton.setStyle(null);
        queueManagementTable.refresh();
        environmentVarTable.refresh();
        entitiesTable.refresh();
        entitiesRunTable.refresh();
    }

    private void updateThreadPoolDetailsLoop(ObservableList<QueueManagement> threadPoolList){
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    threadPoolDetails(threadPoolList);
                    Thread.sleep(1000);
                }catch (Exception e){

                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void threadPoolDetails(ObservableList<QueueManagement> threadPoolList){
        try {
            DTOThreadPoolDetails threadPool = communication.getThreadPoolDetailsOnClient();
//            synchronized (communication.getThreadPoolDetails()) {
//                threadPool = threadPoolDetails;
//            }
            if (threadPool.getException().getException().equals("allGood")) {
                Integer poolSize = threadPool.getThreadPoolDetails().get("Running");
                Integer finedSimulation = threadPool.getThreadPoolDetails().get("Finished");
                Integer witting = threadPool.getThreadPoolDetails().get("Waiting");
//            synchronized (this.poolSize){
//                poolSize = this.poolSize;
//            }
//            synchronized (this) {
//                finedSimulation = worldsList.size();
//            }

                Platform.runLater(() -> threadPoolList.clear());
                setThreadPoolProperties(threadPoolList, witting, "Waiting");
                setThreadPoolProperties(threadPoolList, poolSize, "Running");
                setThreadPoolProperties(threadPoolList, finedSimulation, "Finished");
            }
        }catch (Exception e){

        }
    }

    private void setThreadPoolProperties(ObservableList<QueueManagement> threadPoolList, Integer value, String status){
        Platform.runLater(() -> threadPoolList.add(new QueueManagement(status, value)));
    }

    @FXML
    void amountToRun(ActionEvent event) {


    }

    @FXML
    void tick(ActionEvent event) {


    }

    @FXML
    void sec(ActionEvent event) {


    }

    @FXML
    void submitRequest(ActionEvent event) {
        try {
            simulationChosenName = simulationNameChoiceBox.getValue();
            try {
                amountToRun = Integer.parseInt(amountToRunfiled.getText());
            }catch (Exception e){
                alert.setContentText("must enter number to amount to run");
                alert.show();
            }
            try {
                if(tickfiled.getText().equals("")){
                    ticks = null;
                }else {
                    ticks = Integer.parseInt(tickfiled.getText());
                }
            }catch (Exception e){
                alert.setContentText("can only enter numbers to ticks or live empty");
                alert.show();
            }
            try{
                if(secfield.getText().equals("")){
                    sec = null;
                }else {
                    sec = Integer.parseInt(secfield.getText());
                }
            }catch (Exception e){
                alert.setContentText("can only enter numbers to ticks or live empty");
                alert.show();
            }
            communication._askToRunASimulation(simulationChosenName, amountToRun, ticks, sec);
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }

    }

    @FXML
    void executeSimulation(ActionEvent event) {
        requestTable data = requestTable.getSelectionModel().getSelectedItem();
        if(data == null){
            return;
        }
        communication.setChosenSimulationName(data.getSimulationName());
        communication.setRequestIdChosen(data.getRequestId());
        try {
            //treeViewController.displayFileDetails(engine, absolutePath, queueManagementData);
            fillEnvironmentVariableTable(communication);
            fillEntitiesTable(communication);
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }
}
