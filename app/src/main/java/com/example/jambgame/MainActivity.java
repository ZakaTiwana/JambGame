package com.example.jambgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TableRow[] tableRows = new TableRow[12];
    private TextView[][] cells;
    private TableRow[] sumRows = new TableRow[3];
    private TextView[][] all_sum_cells;
    private Button btn_go, btn_rest, btn_view_scores;
    private TextView total_score;

    private int[] dice_to_drawable_ids = new int[7];
    private String[] columnsNames={"Bottom-Up","Top-Down","Free","Nuetral"};
    private String[] rowNames={"1","2","3","4","5","6","Min","Max","S","F","P","Y"};

    private Jamb jamb;
    private boolean isNeutralSelected = false;
    private int neutralSelectedIndex = -1;
    private ImageView[] all_dices = new ImageView[6];
    private HashMap<Integer,Integer> previousSelectedDice = new HashMap<>();
    private DBConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jamb = new Jamb();
        db = new DBConnection(getApplicationContext());

        btn_go = findViewById(R.id.btn_go);
        btn_rest = findViewById(R.id.btn_reset);
        btn_view_scores = findViewById(R.id.view_scores);
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
        btn_view_scores.setOnClickListener(this);


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

        sumRows[0] = findViewById(R.id.first_sum_row);
        sumRows[1] = findViewById(R.id.second_sum_row);
        sumRows[2] = findViewById(R.id.third_sum_row);

        createCells();

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: view id = "+view.getId());
        if(btn_view_scores.getId() == view.getId()){
            Intent i = new Intent(this,ViewScoreActivity.class);
            startActivity(i);
        }
        else if (btn_go.getId() == view.getId()){
            if (jamb.tableFilled()){
                Toast.makeText(this,"Game is Completed. For new Game click 'Reset' ",Toast.LENGTH_SHORT).show();
                return;
            }
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
                            case "Nuetral":
                                if (isNeutralSelected && neutralSelectedIndex == j){
                                    move_possible = jamb.setValue(rowNames[j],columnsNames[i],i,j);
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
                            default:
                                move_possible =  neutralSelectedIndex == -1 &&
                                        jamb.setValue(rowNames[j],columnsNames[i],i,j);
                                Log.d(TAG, "onClick: row = "+rowNames[j]
                                        +", column = "+columnsNames[i]+", move_possible = "+move_possible+
                                        ", calculated score = "+ jamb.getCalculatedValue(i,rowNames[j]));
                                break;
                        }
                        if (move_possible){
                            cells[i][j].setText(String.valueOf(jamb.getCalculatedValue(i,rowNames[j])));
                            cells[i][j].setBackgroundResource(R.drawable.border_selected);
                            cells[i][j].setClickable(false);

                            jamb.resetDices();
                            previousSelectedDice.clear();
                            btn_go.setClickable(true);
                            neutralSelectedIndex = -1;
                            isNeutralSelected = false;
                            for (int k = 0; k < all_dices.length; k++) {
                                all_dices[k].setClickable(true);
                                all_dices[k].setColorFilter(null);
                                all_dices[k].setImageResource(dice_to_drawable_ids[0]);
                            }
                            total_score.setText(String.valueOf(jamb.totalScore()));
                            if (jamb.tableFilled()) {
                                Toast.makeText(this,
                                        "Game is Completed. Score Added to database",
                                        Toast.LENGTH_SHORT).show();
                                db.addScore(jamb.totalScore());
                                return;
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
        all_sum_cells = new TextView[3][4];
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
                TableRow.LayoutParams tlay =  new TableRow.LayoutParams();
                tlay.weight=1;
                cells[i][j].setLayoutParams(tlay);
                cells[i][j].setClickable(true);
                cells[i][j].setOnClickListener(this);
                tableRows[j].addView(cells[i][j]);
            }
        }
        // Create Sum Rows
        for (int i = 0; i < sumRows.length; i++) {
            for (int j = 0; j < 4; j++) {
                all_sum_cells[i][j] = new TextView(this);
                all_sum_cells[i][j].setId(200+id);
                id++;
                all_sum_cells[i][j].setHeight(height);
                all_sum_cells[i][j].setGravity(Gravity.CENTER_HORIZONTAL);
                all_sum_cells[i][j].setTextColor(Color.BLACK);
                all_sum_cells[i][j].setBackgroundResource(R.drawable.border_sum);
                all_sum_cells[i][j].setPadding(5, 5, 5, 5);
                TableRow.LayoutParams tlay =  new TableRow.LayoutParams();
                tlay.weight=1;
                all_sum_cells[i][j].setLayoutParams(tlay);
                all_sum_cells[i][j].setClickable(false);
                sumRows[i].addView(all_sum_cells[i][j]);
            }
        }
    }
    private void resetCells(){
        jamb = new Jamb();
        previousSelectedDice = new HashMap<>();
        neutralSelectedIndex = -1;
        isNeutralSelected = false;

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
        // reset sum cells
        for (int i = 0; i < sumRows.length; i++) {
            for (int j = 0; j < 4; j++) {
                all_sum_cells[i][j].setText("");
            }
        }
    }

}