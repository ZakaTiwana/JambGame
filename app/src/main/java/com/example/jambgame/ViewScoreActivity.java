package com.example.jambgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewScoreActivity extends AppCompatActivity {

    private ListView listView;
    private Button btn_back;
    private ArrayList<Integer> scores = new ArrayList<>();
    private DBConnection db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_score);

        db = new DBConnection(getApplicationContext());

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        scores = db.getScores();

        listView = findViewById(R.id.score_list_view);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(
                this,R.layout.support_simple_spinner_dropdown_item,
                    scores);
        listView.setAdapter(arrayAdapter);
    }

}