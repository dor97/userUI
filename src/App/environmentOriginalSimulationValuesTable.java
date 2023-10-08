package App;

public class environmentOriginalSimulationValuesTable {
    private String environment;
    private String value;

    public environmentOriginalSimulationValuesTable(String environment, String value) {
        this.environment = environment;
        this.value = value;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
