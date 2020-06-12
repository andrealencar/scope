package org.billthefarmer.scope.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.billthefarmer.scope.R;

import java.util.Locale;

public class EndQuizActivity extends AppCompatActivity {

    ProgressBar correct_questions;
    Button btnCancel;
    Button btnRestart;
    TextView store_text;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_quiz);
        btnCancel           = findViewById(R.id.button_end_quiz);
        btnRestart          = findViewById(R.id.button_restart);
        correct_questions   = findViewById(R.id.stats_questions);
        store_text          = findViewById(R.id.store_text);
        result              = findViewById(R.id.textView_result);

        Intent intent = getIntent();

        String error_count       = intent.getStringExtra("error_count");
        String correct_count     = intent.getStringExtra("correct_count");
        String questions_count   = intent.getStringExtra("questions_count");

        int correct_count_int    = Integer.parseInt(correct_count);
        int error_count_int      = Integer.parseInt(error_count);
        int questions_count_int  = Integer.parseInt(questions_count);

        double correct_count_double = Double.parseDouble(correct_count);
        double questions_count_double = Double.parseDouble(questions_count);

        double percent = (correct_count_double/questions_count_double)*100;
        double res = (correct_count_double/questions_count_double) * 100;

        String stringFormat_score  = String.valueOf(percent) + " %";
        String stringFormat_result = String.format(Locale.US,
                                         "você acertou %2d de um total de %2d questões",
                                          correct_count_int,questions_count_int);

        result.setText(stringFormat_result);
        store_text.setText(stringFormat_score);
        correct_questions.setProgress((int) percent);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),QuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),ShowQuizActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


}