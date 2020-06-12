package org.billthefarmer.scope.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.billthefarmer.scope.R;
import org.billthefarmer.scope.ViewModel.BaseViewModel;
import org.billthefarmer.scope.models.Alternative;
import org.billthefarmer.scope.models.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ShowQuizActivity extends AppCompatActivity implements OnClickListener {

    int clock               = 0;
    int currentQuestionNum  = 0;
    int limitQuestionNum    = 5;
    int AlternativeCorrect  = 0;
    int QuestionsCount      = 0;
    int correct_count       = 0;
    int error_count         = 0;

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
    Button[] list_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz);
        mViewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        mViewModel.getQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions_) {
                for (int i = 0; i < questions_.size(); i++) {
                    String id = String.valueOf(questions_.get(i).uid);
                    questions.add(questions_.get(i));
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StartCountTime();

        list_buttons = new Button[4];
        for(int i=0; i<list_buttons.length; i++) {
                String buttonID = "button_response_" + (i+1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                list_buttons[i] = findViewById(resID);
                list_buttons[i].setOnClickListener(this);
        }

        btnCancel       = findViewById(R.id.button_cancel_quiz);
        question_view   = findViewById(R.id.textView_question);
        clock_view      = findViewById(R.id.textView_clock);
        current_question_view = findViewById(R.id.textView_current_question);
        loading         = findViewById(R.id.view_loading);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RandomQuestion();
            }
        }, 500);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHideForm(false);
                StopCountTime();
                finish();
            }
        });
    }

    private void EndQuiz(){

        ShowHideForm(false);
        Intent intent                        = new Intent(getApplicationContext(),EndQuizActivity.class);
        String error_count_string            = String.valueOf(error_count);
        String correct_count_string          = String.valueOf(correct_count);
        String correct_count_questions_count = String.valueOf(limitQuestionNum);

        intent.putExtra("error_count",error_count_string);
        intent.putExtra("correct_count", correct_count_string);
        intent.putExtra("questions_count", correct_count_questions_count);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        int correct_color = Color.parseColor("#009624");
        int wrong_color   = Color.parseColor("#ff3d00");
        int text_color    = Color.parseColor("#ffffff");

        int index = 0;
        for (int i = 0; i < list_buttons.length; i++) {
            if (list_buttons[i].getId() == v.getId()) {
                index = i;
                if(AlternativeCorrect == i) {
                    correct_count++;
                    //list_buttons[i].setBackgroundColor(correct_color);
                    //list_buttons[i].setTextColor(text_color);
                }else{
                    error_count++;
                    //list_buttons[i].setBackgroundColor(wrong_color);
                    //list_buttons[i].setTextColor(text_color);
                }
            }

        }

//        Log.d("error_count-->>",  "> "+error_count);
//        Log.d("correct_count-->>",  "> "+correct_count);

        NextQuiz();
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

    private void ResetColors(){
        int bg_color = Color.parseColor("#ffffff");
        int text_color    = Color.parseColor("#212121");
        for(int i=0; i< list_buttons.length; i++) {
            Button btn_ = list_buttons[i];
            btn_.setTextColor(text_color);
            list_buttons[i].setBackgroundColor(bg_color);
        }

    }

    private void NextQuiz(){

        EnableButtons(false);
        StopCountTime();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RandomQuestion();
                QuestionsCount = QuestionsCount+1;
                EnableButtons(true);
                StartCountTime();
            }
        }, 500);

    }


    private void EnableButtons(Boolean status){
        for(int i=0; i< list_buttons.length; i++) {
            Button btn_ =  list_buttons[i];
            btn_.setEnabled(status);
        }
    }

    private void ShowHideForm(Boolean status){
        try{

            for(int i=0; i< list_buttons.length; i++) {
                Button btn_ =  list_buttons[i];
                if(status.equals(true)){
                    loading.setVisibility(View.INVISIBLE);
                    btn_.setVisibility(View.VISIBLE);
                    question_view.setVisibility(View.VISIBLE);

                }else{
                    btn_.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    question_view.setVisibility(View.VISIBLE);
                }
            }

        }catch (Exception e){
            Log.d("Error/ShowHideForm-->>", String.valueOf(e));
        }

    }

    private void RandomQuestion(){
        ShowHideForm(false);
        int limit = questions.size();
        final int random = new Random().nextInt(limit) + 1;
        int random_ = random-1;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowHideForm(true);
                SetCurrentQuestion(random_,limit);
                ResetColors();
            }
        }, 500);

    };

    private void SetCurrentQuestion(int question,int total_question){
            String stringFormat = String.format(Locale.US,"Questão %02d/%02d",QuestionsCount,total_question);

            if(QuestionsCount >= limitQuestionNum){
                    StopCountTime();
                    EndQuiz();
            }

            try{
                currentQuestion = questions.get(question);
                current_question_view.setText(stringFormat);
                question_view.setText(currentQuestion.title);
                ArrayList<Alternative> alternatives =  currentQuestion.alternatives;

                for (int i = 0; i < alternatives.size(); i++) {
                    String id    = alternatives.get(i).id;
                    String title = alternatives.get(i).title;
                    Boolean correct = alternatives.get(i).correct;
                    list_buttons[i].setText(title);
                    if(correct){
                        AlternativeCorrect = i;
                    }
                }

                ShowHideForm(true);

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
                clock_view.setText(stringFormat);
            }

            @Override
            public void onFinish() {
                NextQuiz();
            }
        }.start();
    }

    private void StopCountTime(){
        timer.cancel();
    }
}