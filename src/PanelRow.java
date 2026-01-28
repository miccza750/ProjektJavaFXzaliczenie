import javafx.beans.property.*;

public class PanelRow {
    private final IntegerProperty id;
    private final IntegerProperty farm_id;
    private final StringProperty openDate;
    private final StringProperty turnOn;

    public PanelRow(int id,int farm_id, String openDate,String turnOn) {
        this.id = new SimpleIntegerProperty(id);
        this.farm_id = new SimpleIntegerProperty(farm_id);
        this.openDate = new SimpleStringProperty(openDate);
        this.turnOn = new SimpleStringProperty(turnOn);
    }
    public IntegerProperty getFarm_id() { return farm_id; }
    public IntegerProperty getId() { return id; }
    public int getINTId() { return id.get(); }
    public int getINTIdFarm() { return farm_id.get(); }
    public StringProperty getopenDate() { return openDate; }
    public StringProperty getturnOn() { return turnOn; }
    public String getturnOnString() {
        return turnOn.get();
    }
}
