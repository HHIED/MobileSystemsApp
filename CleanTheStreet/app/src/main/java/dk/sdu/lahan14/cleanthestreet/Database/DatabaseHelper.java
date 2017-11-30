package dk.sdu.lahan14.cleanthestreet.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon-PC on 21-11-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "userData.db";

    private String[] projection = {
            Database.UpvotedTasksEntry._ID,
            Database.UpvotedTasksEntry.COLUMN_TASK_ID,
    };

    private static final String SQL_CREATE_UPVOTED_TASK_ENTRIES =
            "CREATE TABLE " + Database.UpvotedTasksEntry.TABLE_NAME + " (" +
                    Database.UpvotedTasksEntry._ID + " INTEGER PRIMARY KEY," +
                    Database.UpvotedTasksEntry.COLUMN_TASK_ID + " TEXT)";
    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + Database.UserEntry.TABLE_NAME + " (" +
                    Database.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    Database.UserEntry.COLUMN_USER_ID + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Database.UserEntry.TABLE_NAME + "; \n" +
            "DROP TABLE IF EXISTS " + Database.UpvotedTasksEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_UPVOTED_TASK_ENTRIES);
        db.execSQL(SQL_CREATE_USER_ENTRIES );
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Cursor queryTask(final int taskId) {
        Cursor query = getReadableDatabase().rawQuery("SELECT * FROM " + Database.UpvotedTasksEntry.TABLE_NAME + " WHERE " + Database.UpvotedTasksEntry.COLUMN_TASK_ID + " = " + taskId, null);
        return query;
    }

    public void persistUpvotedTask(final int taskId){
        SQLiteDatabase db_in = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Database.UpvotedTasksEntry.COLUMN_TASK_ID, taskId);
        db_in.insert(Database.UpvotedTasksEntry.TABLE_NAME, null, values);
    }
}
