package dk.sdu.lahan14.cleanthestreet;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Basic object of a Task.
 *
 * Created by Arpad on 11/12/2017.
 */

public class Task {

    private int id;
    private Bitmap image;
    private String description;
    private float rating;
    private Location location;
    private final String creator;
    private String accepter;

    public Task(Bitmap image, String description, int rating, Location location, String creator) {
        this.image = image;
        this.description = description;
        this.rating = rating;
        this.location = location;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void incrementRating() {
        rating += 1;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCreator() {
        return creator;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

}
