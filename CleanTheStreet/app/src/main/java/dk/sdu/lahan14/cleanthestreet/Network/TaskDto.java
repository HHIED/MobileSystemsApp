package dk.sdu.lahan14.cleanthestreet.Network;

import android.graphics.Bitmap;
import android.util.Base64;

import dk.sdu.lahan14.cleanthestreet.Util.Task;

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
    public String completedimage = "";
    public float distance = 0;
    public boolean isCompleted = false;
    public boolean isApproved = false;

    public TaskDto(int id, String image, String description, int score, float lattitude, float longtitude, String accepter, String creator, float distance) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
        this.accepter = accepter;
        this.creator = creator;
        this.distance = distance;

    }

    public TaskDto(int id, String image, String description, int score, float lattitude, float longtitude, String accepter, String creator) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
        this.accepter = accepter;
        this.creator = creator;

    }

    public TaskDto(int id, String image, String description, int score, float lattitude, float longtitude, String accepter, String creator, String completedImage) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.lattitude = lattitude;
        this.longtitude = longtitude;
        this.accepter = accepter;
        this.creator = creator;
        this.completedimage = completedImage;

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
        byte[] completedImage = Base64.decode(getImage(), Base64.NO_WRAP);
        Bitmap completedBit = BitMapConverter.getImage(completedImage);
        Task t = new Task(id, imagebit, description, score, lattitude, longtitude, creator,accepter);
        t.setCompletedImage(completedBit);
        if(isCompleted) {
            t.setIsCompleted(true);
        }
        t.setIsApproved(isApproved);
        t.setDistance(distance);
        return t;
    }

}
