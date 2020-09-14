package com.example.jambgame;

import java.util.ArrayList;
import java.util.HashMap;

public class Jamb {
    int rollCount;
    ArrayList<Dices> dices;
    ArrayList<Column> columns;

    public Jamb() {
        this.rollCount=0;
        this.dices.add(new Dices());
        String[] columnsNames={"Bottom-Up","Top-Down","Free","Nuetral"};
        for(String columnName:columnsNames){
            this.columns.add(new Column(columnName));
        }
    }
    public ArrayList<Column> getColumns(){
        return this.getColumns();
    }

    public ArrayList<Dices> getDices(){
        return this.dices;
    }

    public Dices getLatestRolledDice(){
        int lastIndex=this.dices.size()-1;
        return this.dices.get(lastIndex);
    }

    private void incrementRollCount(){
        this.rollCount+=1;
    }

    public int getRollCount() {
        return this.rollCount;
    }

    public void rollDices(HashMap<Integer,Integer> previousSelections){
        int lastIndex=this.dices.size()-1;
        this.dices.get(lastIndex).generateNumbersOnDice(previousSelections);
        this.incrementRollCount();
    }

    public void addSelection(int key, int value){
        int lastIndex=this.dices.size()-1;
        this.dices.get(lastIndex).addSelected(key,value);
    }

    public boolean setValue(){
        return true;
    }

    public boolean tableFilled(){
        return true;
    }

    public int totalScore(){
        return 0;
    }

    public boolean saveToDB(){
        return true;
    }
}
