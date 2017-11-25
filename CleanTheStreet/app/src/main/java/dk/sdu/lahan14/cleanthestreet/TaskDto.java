package dk.sdu.lahan14.cleanthestreet;

import android.graphics.Bitmap;
import android.util.Base64;

/**
 * Created by lasse on 18-11-2017.
 */

public class TaskDto {

    public int id = 0;
    private String image = "";
    public String description="";
    public int score=1;
    public float lattitude=55;
    public float longtitude=56;
    public String creator="";
    public String accepter="";

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

    public String getImage() {
        return image;
    }

    public Task toTask() {
        byte[] image = Base64.decode(getImage(), Base64.NO_WRAP);
        Bitmap imagebit = BitMapConverter.getImage(image);
        Task t = new Task(id, imagebit, description, score, lattitude, longtitude, creator,accepter);
        return t;
    }

}
