package org.billthefarmer.scope.database;
import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import org.billthefarmer.scope.Dao.PostDao;
import org.billthefarmer.scope.models.Post;


@Database(entities = {Post.class}, version = 1)
public abstract class PostDatabase extends RoomDatabase {

    private static final String LOG_TAG = PostDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "local_database";
    private static PostDatabase sInstance;

    public static PostDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PostDatabase.class, PostDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PostDao postDao();
}
