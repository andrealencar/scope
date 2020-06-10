package org.billthefarmer.scope.quiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.billthefarmer.scope.AppExecutors;
import org.billthefarmer.scope.R;
import org.billthefarmer.scope.ViewModel.BaseViewModel;
import org.billthefarmer.scope.database.AppDatabase;
import org.billthefarmer.scope.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowQuizActivity extends AppCompatActivity {

    int clock = 0;
    int currentQuestionNum = 0;
    int limitQuestionNum = 5;
    Question currentQuestion;
    List<Question> questions = new ArrayList<>();
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btnCancel;
    TextView question_view;
    TextView clock_view;
    TextView current_question_view;
    CountDownTimer timer = null;
    ProgressBar loading;
    BaseViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz);
        mViewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        mViewModel.getQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                Log.d("mViewModel 1-->>", String.valueOf(questions));
            }
        });

        mViewModel.getCurrentQuestion().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                Log.d("mViewModel 2-->>", String.valueOf(questions));
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        LoadQuestions();
        StartCountTime();

        btn1            = findViewById(R.id.button_response_1);
        btn2            = findViewById(R.id.button_response_2);
        btn3            = findViewById(R.id.button_response_3);
        btn4            = findViewById(R.id.button_response_4);
        btnCancel       = findViewById(R.id.button_cancel_quiz);
        question_view   = findViewById(R.id.textView_question);
        clock_view      = findViewById(R.id.textView_clock);
        current_question_view = findViewById(R.id.textView_current_question);
        loading         = findViewById(R.id.view_loading);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetCurrentQuestion(currentQuestionNum,limitQuestionNum);
            }
        }, 500);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextQuiz();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextQuiz();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextQuiz();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextQuiz();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHideForm(false);
                StopCountTime();
                finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        ShowHideForm(true);
    };

    private void NextQuiz(){
        LoadQuestions();
    }

    private void EndQuiz(){
        ShowHideForm(false);
    }

    private void ShowHideForm(Boolean status){
        try{
            if(status.equals(true)){
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
                question_view.setVisibility(View.VISIBLE);
                loading.setVisibility(View.INVISIBLE);
            }else{
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.INVISIBLE);
                btn4.setVisibility(View.INVISIBLE);
                question_view.setVisibility(View.INVISIBLE);
                loading.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){

        }

    }
    
    private void LoadQuestions(){
            AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    questions = mDb.questionDao().getAll();
                    for (int i = 0; i < questions.size(); i++) {
                        String id = String.valueOf(questions.get(i).uid);
                        Log.d("quetions id-->>",id);
                    }
                }
            });
            ShowHideForm(false);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewModel.SetQuestions(questions);
                    SetCurrentQuestion(currentQuestionNum,limitQuestionNum);
                    ShowHideForm(true);
                }
            }, 500);

    }

    private void SetCurrentQuestion(int question,int total_question){
            String stringFormat = String.format(Locale.US,"Questão %2d/%02d",question+1,total_question);
            try{
                currentQuestion = questions.get(question);
                current_question_view.setText(stringFormat);
                question_view.setText(currentQuestion.title);
            }catch (Exception e){
                Log.d("Exception-->>", String.valueOf(e));
            }
    }

    private void StartCountTime(){
            timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished/1000);
                String clock = String.valueOf(seconds);
                String stringFormat = String.format(Locale.US,"00:%02d",seconds);

                Log.d("StartCountTime -->>", stringFormat);
                clock_view.setText(stringFormat);
            }

            @Override
            public void onFinish() {
                EndQuiz();
                Log.d("onFinish -->>","");

            }
        }.start();
    }

    private void StopCountTime(){
        timer.cancel();
    }
}