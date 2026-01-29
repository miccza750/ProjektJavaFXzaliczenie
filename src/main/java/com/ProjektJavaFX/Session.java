package main.java.com.ProjektJavaFX;
public class Session {
    private static Session instance = new Session();
    private String name;

    public Session() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Session getInstance() {
        return instance;
    }
}
