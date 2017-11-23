package dk.sdu.lahan14.cleanthestreet.Network;

/**
 * Created by lasse on 18-11-2017.
 */

public class TaskDto {

    public int id;
    public String image;
    public String description;
    public int score;
    public float lattitude;
    public float longtitude;

    public TaskDto(int id, String image, String description, int score, float lattitude, float longtitude) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
    }

    public TaskDto(int id) {
        this.id = id;
    }

}
