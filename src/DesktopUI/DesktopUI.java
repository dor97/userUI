package DesktopUI;

import App.AppController;
import App.logInScreen;
import App.simulationValueController;
import TreeDetails.TreeDetailsController;
import TreeView.TreeViewController;
import httpClient.clientCommunication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class DesktopUI extends Application {
    public static AppController appController;
    public static clientCommunication communication = new clientCommunication();
    private static logInScreen logInScreen;
    private simulationValueController simulationValueController;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL url = getClass().getResource("/TreeView/TreeView.fxml");
        fxmlLoader.setLocation(url);
        BorderPane treeViewComponent = fxmlLoader.load(url.openStream());
        TreeViewController treeViewController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("/TreeDetails/TreeDetails.fxml");
        fxmlLoader.setLocation(url);
        BorderPane treeDetailsComponent = fxmlLoader.load(url.openStream());
        TreeDetailsController treeDetailsController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        URL showDetailsFXML = getClass().getResource("/resources/simulationValue.fxml");
        fxmlLoader.setLocation(showDetailsFXML);
        Parent showDetailsRoot = fxmlLoader.load();
        simulationValueController = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        URL logInFXML = getClass().getResource("/resources/logInPage.fxml");
        fxmlLoader.setLocation(logInFXML);
        Parent logInRoot = fxmlLoader.load();
        logInScreen = fxmlLoader.getController();

        fxmlLoader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/resources/javaFXproject.fxml");
        fxmlLoader.setLocation(mainFXML);
        Parent root = fxmlLoader.load();
        appController = fxmlLoader.getController();

        appController.setTreeViewComponentController(treeViewController);
        appController.setTreeDetailsComponentController(treeDetailsController);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Scene simulationDetailsScene = new Scene(showDetailsRoot);
        Scene scene = new Scene(scrollPane);
        Scene logInScene = new Scene(logInRoot);
        logInScreen.setAppScene(scene);
        logInScreen.setPrimaryStage(primaryStage);
        primaryStage.setScene(logInScene);
        appController.setStage(primaryStage);
        primaryStage.setTitle("Predictions");
        appController.setSimulationDetailsScene(simulationDetailsScene);
        logInScreen.setUserNameLabel(appController.getUserNameLabel());
        appController.setStage(primaryStage);
        appController.setAppControllerScene(scene);
        simulationValueController.setAppControllerScene(scene);
        appController.setSimulationValueController(simulationValueController);

        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
        appController.shutDownSystem();
    }
}