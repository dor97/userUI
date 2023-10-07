package App;

public class entityOriginalSimulationValuesTable {
    private String entity;
    private Integer population;

    public entityOriginalSimulationValuesTable(String entity, Integer population) {
        this.entity = entity;
        this.population = population;

    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

}
