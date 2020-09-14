package com.example.jambgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TableRow[] tableRows = new TableRow[12];
    private TextView[][] cells = new TextView[12][4];
    private Button btn_go, btn_rest;
    private TextView total_score;

    private int dice_ids[] = new int[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_go = findViewById(R.id.btn_go);
        btn_rest = findViewById(R.id.btn_reset);
        total_score = findViewById(R.id.total_score);

        dice_ids[0] = R.drawable.r_q;
        dice_ids[1] = R.drawable.r_d1;
        dice_ids[2] = R.drawable.r_d2;
        dice_ids[3] = R.drawable.r_d3;
        dice_ids[4] = R.drawable.r_d4;
        dice_ids[5] = R.drawable.r_d5;
        dice_ids[6] = R.drawable.r_d6;

        tableRows[0] = findViewById(R.id.ones_row);
        tableRows[1] = findViewById(R.id.twos_row);
        tableRows[2] = findViewById(R.id.threes_row);
        tableRows[3] = findViewById(R.id.fours_row);
        tableRows[4] = findViewById(R.id.fives_row);
        tableRows[5] = findViewById(R.id.sixes_row);
        tableRows[6] = findViewById(R.id.max_row);
        tableRows[7] = findViewById(R.id.min_row);
        tableRows[8] = findViewById(R.id.s_row);
        tableRows[9] = findViewById(R.id.f_row);
        tableRows[10] = findViewById(R.id.p_row);
        tableRows[11] = findViewById(R.id.y_row);

        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,30,
                getResources().getDisplayMetrics()); //dp to pixels

        Log.d(TAG, "onCreate: height = "+height);
        int id=0;
        for (int i=0;i < tableRows.length;i++){
            for (int j=0;j<4;j++){
                cells[i][j] = new TextView(this);
                cells[i][j].setId(100+id);
                id++;
                cells[i][j].setHeight(height);
                cells[i][j].setGravity(Gravity.CENTER_HORIZONTAL);
                cells[i][j].setTextColor(Color.BLACK);
                cells[i][j].setBackgroundResource(R.drawable.border_unselected);
                cells[i][j].setPadding(5, 5, 5, 5);
                cells[i][j].setClickable(true);
                cells[i][j].setOnClickListener(this);
                tableRows[i].addView(cells[i][j]);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: view = " +view.getId() + " ,  cells[2+3]"+cells[2][3].getId());
        for(int i=0; i < tableRows.length ; i++){
            for (int j=0; j < 4; j++){
                if(view.getId() == cells[i][j].getId()){
                    cells[i][j].setText("0");
                }
            }
        }
    }
}