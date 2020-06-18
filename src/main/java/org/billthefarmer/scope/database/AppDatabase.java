package org.billthefarmer.scope.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.billthefarmer.scope.Dao.PostDao;
import org.billthefarmer.scope.Dao.QuestionDao;
import org.billthefarmer.scope.models.Post;
import org.billthefarmer.scope.models.Question;
import org.billthefarmer.scope.models.QuestionTypeConverters;


@Database(entities = {Post.class, Question.class}, version = 9)
@TypeConverters({QuestionTypeConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "scope_db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PostDao postDao();
    public abstract QuestionDao questionDao();
}
