package org.billthefarmer.scope.quiz;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import org.billthefarmer.scope.AppExecutors;
import org.billthefarmer.scope.R;
import org.billthefarmer.scope.database.AppDatabase;
import org.billthefarmer.scope.models.Question;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<Question> quetions = mDb.questionDao().getAll();

                for (int i = 0; i < quetions.size(); i++) {
                    String id = String.valueOf(quetions.get(i).uid);
                    Log.d("quetions id-->>",id);
                }

                //mDb.userDao().insertUser(user);
                //Log.d("ADD user", user.toString());
            }


        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button btn_start_quiz = (Button) findViewById(R.id.button_start_quiz);
        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("btn_start_quiz -->>","");

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}