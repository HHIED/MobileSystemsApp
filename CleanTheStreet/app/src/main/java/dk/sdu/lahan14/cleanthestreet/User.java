package dk.sdu.lahan14.cleanthestreet;

/**
 * Created by lasse on 18-11-2017.
 */

public class User {

    private int id;
    private String name;

    public User(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
