package dk.sdu.lahan14.cleanthestreet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import java.io.ByteArrayOutputStream;

/**
 * Created by lasse on 18-11-2017.
 */

public class TaskDto {

    public int id;
    public byte[] image;
    public String description;
    public int score;
    public float lattitude;
    public float longtitude;
    public final User creator;
    public User accepter;

    public TaskDto(int id, User creator, User accepter, byte[] image, String description, int score, float lattitude, float longtitude) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
        this.accepter = accepter;
        this.creator = creator;
    }

    public TaskDto(int id) {
        this.id = id;
        this.creator = null;
    }

    public Task toTask() {
        Bitmap bitMapImage = BitMapConverter.getImage(this.image);
        Task t = new Task(this.id, this.creator, this.accepter, this.score, this.description, bitMapImage, this.lattitude, this.longtitude);
        return t;
    }




}
