package dk.sdu.lahan14.cleanthestreet;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

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
    private double latitude;
    private double longitude;
    private final String creator;
    private String accepter;

    public Task(int id) {
        this.id = id;
        this.creator = null;
    }

    public Task(int id, int score, String description, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
        this.description = description;
        this.creator = null;
    }

    public Task(Bitmap image, String description, int score, double latitude, double longitude, String creator) {
        this.image = image;
        this.description = description;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
    }

    public Task(int id, Bitmap image, String description, int score, double latitude, double longitude, String creator, String accepter) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.accepter = accepter;
    }

    public Task(Parcel parcel) {
        this.id = parcel.readInt();
        this.description = parcel.readString();
        this.score = parcel.readInt();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.image = Bitmap.CREATOR.createFromParcel(parcel);
        this.creator = parcel.readString();
        this.accepter = parcel.readString();
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        score += 1;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeInt(score);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        image.writeToParcel(parcel, 0);
        parcel.writeString(creator);
        parcel.writeString(accepter);
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

    public JSONObject getTaskAsJSONObject() {
        JSONObject jsonTask = new JSONObject();
        try {
            jsonTask.put(Constants.ID, id);
            jsonTask.put(Constants.IMAGE, image);
            jsonTask.put(Constants.DESCRIPTION, description);
            jsonTask.put(Constants.SCORE, score);
            jsonTask.put(Constants.LATITUDE, latitude);
            jsonTask.put(Constants.LONGITUDE, longitude);
            jsonTask.put(Constants.CREATOR, creator);
            jsonTask.put(Constants.ACCEPTER, accepter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonTask;
    }

    public static Task getTaskFromJSON(JSONObject json) {
        // TODO: consider getting null for properties. Null property || null task ??
        try {
            int id = json.getInt(Constants.ID);
            Bitmap image = (Bitmap) json.get(Constants.IMAGE);
            String description = json.getString(Constants.DESCRIPTION);
            int score = json.getInt(Constants.SCORE);
            double latitude = json.getDouble(Constants.LATITUDE);
            double longitude = json.getDouble(Constants.LONGITUDE);
            String creator = json.getString(Constants.CREATOR);
            String accepter = json.getString(Constants.ACCEPTER);
            return new Task(id, image, description, score, latitude, longitude, creator, accepter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
