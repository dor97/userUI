package App;

//import App.EntitiesRunTable;

import DTO.DTORunningSimulationDetails;
import DTO.Status;
import httpClient.clientCommunication;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.Map;

//public class BooleanTaskWithMessage extends Task<Boolean> {
//
//    @Override
//    protected Boolean call() throws Exception {
//        // Simulate some time-consuming work
//        for (int i = 1; i <= 10; i++) {
//            Thread.sleep(1000); // Sleep for 1 second (simulating work)
//
//            // Update the message property with progress information
//            updateMessage("Processing step " + i + " of 10");
//        }
//
//        // Return the result of the task (true or false)
//        return true;
//    }
//}

public class myTask extends Task<ObservableMap<String, Integer>> {
    private final Object pauseLock = new Object();
    private boolean paused = false;

    private BooleanProperty pause = new SimpleBooleanProperty();
    private StringProperty m_tick = new SimpleStringProperty("0");
    private StringProperty m_sec = new SimpleStringProperty("0");
    private Map<String, Integer> map = new HashMap<>(); // MapProperty == SimpleMapProperty
    private ObservableList<EntitiesRunTable> table;
    private StringProperty text;
    private StringProperty exception = new SimpleStringProperty();
    private BooleanProperty endSimulation;
    private clientCommunication communication;


    public void bindProperties(StringProperty tick, StringProperty sec, StringProperty exception, ObservableList<EntitiesRunTable> table, BooleanProperty endSimulation){//MapProperty<String, Integer> map,
        //map.bind(this.map);
        //tick.bind(m_tick);
        //sec.bind(m_sec);
        Bindings.bindBidirectional(sec, m_sec);
        Bindings.bindBidirectional(tick, m_tick);
        Bindings.bindBidirectional(exception, this.exception);
        this.endSimulation = endSimulation;
        //tick.bindBidirectional(m_tick);
        //text = tick;
        this.table = table;
    }


    public void loadClientCommunication(clientCommunication communication){
        this.communication = communication;
    }

    private void updateTable(){
        table.clear();
        map.entrySet().stream().forEach(en -> table.add(new EntitiesRunTable(en.getKey(), en.getValue())));

    }

    public void setError(String error){
        Platform.runLater(() -> exception.set(error));
    }

    public Boolean setData(DTORunningSimulationDetails runningSimulationDetails) throws Exception{
        if (!communication.getSimulationStatus(communication.getChosenSimulationId()).getSimulationStatus().equals(Status.RUNNING)) {
            //DTORunningSimulationDetails finishedRunning = world.getSimulationRunningDetailsDTO();
            Platform.runLater(() -> m_tick.set(runningSimulationDetails.getTick().toString()));
            Platform.runLater(() -> m_sec.set(runningSimulationDetails.getTime().toString()));
            //runningSimulationDetails.getEntities().entrySet().stream().forEach();
            Platform.runLater(() -> {
                map.clear();
                map.putAll(runningSimulationDetails.getEntities());
                updateTable();
            });
            String error;
            try{
                error = communication.getSimulationError(communication.getChosenSimulationId()).getError();
            }catch (Exception e){
                error = e.getMessage();
            }
            setError(error);
            //Platform.runLater(() -> exception.set(error));
            Platform.runLater(() -> endSimulation.set(!endSimulation.get()));
            return true;
        } else {

            //map = runningSimulationDetails.getEntities();
            Platform.runLater(() -> m_tick.set(runningSimulationDetails.getTick().toString()));
            Platform.runLater(() -> m_sec.set(runningSimulationDetails.getTime().toString()));
            //runningSimulationDetails.getEntities().entrySet().stream().forEach();
            Platform.runLater(() -> {
                map.clear();
                map.putAll(runningSimulationDetails.getEntities());
                updateTable();
            });
            //Platform.runLater(() -> map = runningSimulationDetails.getEntities());
            //mmm.bind(ma);
            //updateValue(runningSimulationDetails.getEntities());
            return false;
        }

    }


    @Override
    protected ObservableMap<String, Integer> call() throws Exception {
        DTORunningSimulationDetails runningSimulationDetails = new DTORunningSimulationDetails();
        while(!isCancelled()){
            synchronized (communication.getRunningSimulationDataLock()) {
                runningSimulationDetails = communication.getRunningSimulationData();
                if(runningSimulationDetails == null){
                    continue;
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                return new SimpleMapProperty<>();
            }

            if(setData(runningSimulationDetails)){
                return new SimpleMapProperty<>();
            }

            try {
                Thread.sleep(1000);
            }catch (InterruptedException e){
                return new SimpleMapProperty<>();
            }
        }

        return new SimpleMapProperty<>();

    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public BooleanProperty pauseProperty(){
        return pause;
    }

    public StringProperty tickProperty(){
        return m_tick;
    }

    public StringProperty secProperty(){
        return m_sec;
    }

    public void setTick(Integer tick){
        Platform.runLater(() -> m_tick.set(tick.toString()));
        //m_tick.set(tick);
    }

    public void setSce(Long sec){
        Platform.runLater(() -> m_sec.set(sec.toString()));
    }

    public Boolean getPause() {
        return pause.get();
    }

//    public StringProperty pauseProperty() {
//        return new SimpleStringProperty() {
//            @Override
//            public void set(String newValue) {
//                if (newValue.equals("pause")) {
//                    pause();
//                } else {
//                    resume();
//                }
//                super.set(newValue);
//            }
//        };
//    }
}