package App;

import DTO.DTOConnected;
import DesktopUI.DesktopUI;
import httpClient.clientCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class logInScreen implements Initializable {
    private clientCommunication communication;
    private Scene appScene;
    private Stage primaryStage;
    @FXML
    private TextField userNameTextField;
    private Alert alert = new Alert(Alert.AlertType.ERROR);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alert.setTitle("Error");
        communication = DesktopUI.communication;
    }

    public void setAppScene(Scene appScene){
        this.appScene = appScene;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }


        @FXML
    void logInButton(ActionEvent event) {
        if(userNameTextField.getText().equals("admin")){
            alert.setContentText("can't use the name admin as user name");
            alert.show();
            return;
        }
        try{
            DTOConnected connected = communication.tryToConnectUser(userNameTextField.getText());
            if(!connected.getConnected()){
                alert.setContentText("user name all ready exist");
                alert.show();
                return;
            }
            primaryStage.setScene(appScene);
        }catch (Exception e){
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }


}