package dk.sdu.lahan14.cleanthestreet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Simon-PC on 21-11-2017.
 */

public final class Database {

    private Database() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USER_ID = "title";

        public static final String[] USER_PROJECTION = {
                Database.UserEntry._ID,
                Database.UserEntry.COLUMN_USER_ID,
        };
    }

    public static class MyTaskEntry implements  BaseColumns {
        public static final String TABLE_NAME = "myTask";
        public static final String COLUMN_TASK_ID = "taskId";
    }

    public static class UpvotedTasksEntry implements  BaseColumns{
        public static final String TABLE_NAME = "upvotedTasks";
        public static final String COLUMN_TASK_ID = "taskId";
    }
}
