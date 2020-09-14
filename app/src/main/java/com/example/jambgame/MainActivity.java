package com.example.jambgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TableRow[] tableRows = new TableRow[12];
    private TextView[][] cells;
    private Button btn_go, btn_rest;
    private TextView total_score;

    private int dice_to_drawable_ids[] = new int[7];
    private String[] columnsNames={"Bottom-Up","Top-Down","Free","Nuetral"};
    private String[] rowNames={"1","2","3","4","5","6","Min","Max","S","F","P","Y"};

    private Jamb jamb;
    private boolean isNeutralSelected = false;
    private int neutralSelectedIndex = -1;
    private ImageView[] all_dices = new ImageView[6];
    private HashMap<Integer,Integer> previousSelectedDice = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jamb = new Jamb();

        btn_go = findViewById(R.id.btn_go);
        btn_rest = findViewById(R.id.btn_reset);
        total_score = findViewById(R.id.total_score);
        all_dices[0] = findViewById(R.id.dice1);
        all_dices[1] = findViewById(R.id.dice2);
        all_dices[2] = findViewById(R.id.dice3);
        all_dices[3] = findViewById(R.id.dice4);
        all_dices[4] = findViewById(R.id.dice5);
        all_dices[5] = findViewById(R.id.dice6);

        all_dices[0].setOnClickListener(this);
        all_dices[1].setOnClickListener(this);
        all_dices[2].setOnClickListener(this);
        all_dices[3].setOnClickListener(this);
        all_dices[4].setOnClickListener(this);
        all_dices[5].setOnClickListener(this);

        btn_rest.setOnClickListener(this);
        btn_go.setOnClickListener(this);


        dice_to_drawable_ids[0] = R.drawable.r_q;
        dice_to_drawable_ids[1] = R.drawable.r_d1;
        dice_to_drawable_ids[2] = R.drawable.r_d2;
        dice_to_drawable_ids[3] = R.drawable.r_d3;
        dice_to_drawable_ids[4] = R.drawable.r_d4;
        dice_to_drawable_ids[5] = R.drawable.r_d5;
        dice_to_drawable_ids[6] = R.drawable.r_d6;

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

        createCells();

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: view id = "+view.getId());

        if (btn_go.getId() == view.getId()){
            jamb.rollDices(previousSelectedDice);
            int[] dices = jamb.getLatestRolledDice().dices;
            for (int i = 0; i < dices.length; i++) {
                all_dices[i].setImageResource(dice_to_drawable_ids[dices[i]]);
            }
            if (jamb.getRollCount() >= 3){
                btn_go.setClickable(false);
                for (int i = 0; i < all_dices.length; i++) {
                    all_dices[i].setClickable(false);
                }
            }

        }
        else if(btn_rest.getId() == view.getId()){
            Log.d(TAG, "onClick: reset button clicked");
            resetCells();
        }
        else if(jamb.getRollCount() > 0) {
            for (int i = 0; i < all_dices.length; i++) {
                if(view.getId() == all_dices[i].getId()){
                    all_dices[i].setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN);
                    previousSelectedDice.put(i,jamb.getLatestRolledDice().dices[i]);
                    break;
                }
            }
            for(int i=0; i < 4 ; i++){
                for (int j=0; j < tableRows.length; j++){
                    if(view.getId() == cells[i][j].getId()){
                        boolean move_possible = false; // should set later accordingly
                        switch (columnsNames[i]){
                            case "Bottom-Up":
                                Column bu = jamb.getColumns().get(0);

                                break;
                            case "Top-Down":
                                Column td = jamb.getColumns().get(1);
                                break;
                            case "Free":
                                Column free = jamb.getColumns().get(2);
                                if (neutralSelectedIndex > -1) break;
                                cells[i][j].setText("0");
                                cells[i][j].setBackgroundResource(R.drawable.border);
                                cells[i][j].setClickable(false);
                                move_possible = true;
                                break;
                            case "Nuetral":
                                Column n = jamb.getColumns().get(3);
                                if (isNeutralSelected && neutralSelectedIndex == j){
                                    cells[i][j].setText("0");
                                    cells[i][j].setBackgroundResource(R.drawable.border);
                                    cells[i][j].setClickable(false);
                                    move_possible = true;
                                    break;
                                }
                                if (previousSelectedDice.isEmpty()){
                                    if (neutralSelectedIndex > -1){
                                        cells[i][neutralSelectedIndex].
                                                setBackgroundResource(R.drawable.border_unselected);
                                    }
                                    cells[i][j].setBackgroundResource(R.drawable.border_n_selected);
                                    isNeutralSelected = true;
                                    neutralSelectedIndex = j;
                                }
                                break;
                        }
                        if (move_possible){
                            jamb.resetDices();
                            btn_go.setClickable(true);
                            neutralSelectedIndex = -1;
                            isNeutralSelected = false;
                            for (int k = 0; k < all_dices.length; k++) {
                                all_dices[k].setClickable(true);
                                all_dices[k].setColorFilter(null);
                                all_dices[k].setImageResource(dice_to_drawable_ids[0]);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void createCells(){
        cells = new TextView[4][12];
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,30,
                getResources().getDisplayMetrics()); //dp to pixels

        Log.d(TAG, "onCreate: height = "+height);
        int id=0;
        for (int i=0;i < 4;i++){
            for (int j=0;j<tableRows.length;j++){
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
                tableRows[j].addView(cells[i][j]);
            }
        }
    }
    private void resetCells(){
        jamb = new Jamb();
        previousSelectedDice = new HashMap<>();

        btn_go.setClickable(true);
        for (int k = 0; k < all_dices.length; k++) {
            all_dices[k].setClickable(true);
            all_dices[k].setColorFilter(null);
            all_dices[k].setImageResource(dice_to_drawable_ids[0]);
        }
        for (int i=0;i < 4;i++){
            for (int j=0;j<tableRows.length;j++){
                cells[i][j].setBackgroundResource(R.drawable.border_unselected);
                cells[i][j].setClickable(true);
                cells[i][j].setOnClickListener(this);
                cells[i][j].setText("");
            }
        }
    }

}