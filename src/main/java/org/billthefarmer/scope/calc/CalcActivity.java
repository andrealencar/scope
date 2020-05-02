package org.billthefarmer.scope.calc;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.billthefarmer.scope.R;

public class CalcActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView input_frequencia = findViewById(R.id.input_frequencia);
        TextView result =  findViewById(R.id.result);

        Button clickButton = (Button) findViewById(R.id.make_calc);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Integer velocidade_luz = 344;
                String frequencia = input_frequencia.getText().toString();
                Double l = velocidade_luz/Double.parseDouble(frequencia);
                String result_format = String.format("%.3f", l);
                //Log.e("res -->>: ", result_format);

                result.setText(result_format);

            }
        });

        input_frequencia.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(count == 0){
                    result.setText("000.00");
                }

            }
        });



    }




}
