import javafx.beans.property.*;

public class PanelRow {
    private final IntegerProperty id;
    private final StringProperty nazwa;
    private final StringProperty dzienOtwarcia;
    private final StringProperty wlaczony;

    public PanelRow(int id, String nazwa, String dzienOtwarcia, String wlaczony) {
        this.id = new SimpleIntegerProperty(id);
        this.nazwa = new SimpleStringProperty(nazwa);
        this.dzienOtwarcia = new SimpleStringProperty(dzienOtwarcia);
        this.wlaczony = new SimpleStringProperty(wlaczony);
    }
    public IntegerProperty getId() { return id; }
    public int getINTId() { return id.get(); }
    public StringProperty getNazwa() { return nazwa; }
    public StringProperty getDzienOtwarcia() { return dzienOtwarcia; }
    public StringProperty getWlaczony() { return wlaczony; }
    public String getWlaczonyString() {
        return wlaczony.get();
    }
}
