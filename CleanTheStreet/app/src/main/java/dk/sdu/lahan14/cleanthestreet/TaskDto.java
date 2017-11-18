package dk.sdu.lahan14.cleanthestreet;

/**
 * Created by lasse on 18-11-2017.
 */

public class TaskDto {

    private int id;
    private String image;
    private String description;
    private int score;
    private float lattitude;
    private float longtitude;

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
