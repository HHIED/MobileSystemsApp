package dk.sdu.lahan14.cleanthestreet;

/**
 * Created by Simon-PC on 21-11-2017.
 */

public class User {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(int id, String name) {
        this.id = id;

        this.name = name;
    }

    public User(){}
}
