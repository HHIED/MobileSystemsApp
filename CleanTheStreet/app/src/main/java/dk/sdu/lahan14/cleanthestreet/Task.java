package dk.sdu.lahan14.cleanthestreet;

import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Basic object of a Task.
 *
 * Created by Arpad on 11/12/2017.
 */

public class Task implements Parcelable {

    private int id;
    private Bitmap image;
    private String description;
    private int score;
    private Location location;
    private final User creator;
    private User accepter;

    public Task(int id) {
        this.id = id;
        this.creator = null;
    }

    public Task(int id, User creator, User accepter, int score, String description, Bitmap image, float latitude, float longitude) {
        this.id = id;
        this.location = new Location("");
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
        this.score = score;
        this.description = description;
        this.accepter = accepter;
        this.creator = creator;
    }

    public Task(Bitmap image, String description, int score, Location location, User creator) {
        this.image = image;
        this.description = description;
        this.score = score;
        this.location = location;
        this.creator = creator;
    }

    public Task(Parcel parcel) {
        this.id = parcel.readInt();
        this.description = parcel.readString();
        this.score = parcel.readInt();
        this.location = Location.CREATOR.createFromParcel(parcel);
        this.image = Bitmap.CREATOR.createFromParcel(parcel);
        this.creator = new User(parcel.readInt());
        this.accepter = new User(parcel.readInt());
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

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementRating() {
        score += 1;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getCreator() {
        return creator;
    }

    public User getAccepter() {
        return accepter;
    }

    public void setAccepter(User accepter) {
        this.accepter = accepter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeFloat(score);
        location.writeToParcel(parcel, 0);
        image.writeToParcel(parcel, 0);
        parcel.writeInt(creator.getId());
        parcel.writeInt(accepter.getId());
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel parcel) {
            return new Task(parcel);
        }

        @Override
        public Task[] newArray(int i) {
            return new Task[i];
        }
    };

    public TaskDto toDto() {
        byte[] byteImage = BitMapConverter.getBytes(this.image);
        TaskDto dto = new TaskDto(this.id, this.creator, this.accepter, byteImage, this.description, this.score, (float) this.location.getLatitude(), (float)this.location.getLongitude());
        return dto;
    }
}
