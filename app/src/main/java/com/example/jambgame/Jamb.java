package com.example.jambgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Jamb {

    int rollCount;
    ArrayList<Dices> dices;
    ArrayList<Column> columns;
    Boolean[] nuetralFilled;
    Boolean[] freeFilled;

    public Jamb() {
        this.rollCount=0;
        this.dices.add(new Dices());
        String[] columnsNames={"Bottom-Up","Top-Down","Free","Nuetral"};
        for(String columnName:columnsNames){
            this.columns.add(new Column(columnName));
        }
        nuetralFilled= new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
        freeFilled= new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false};
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

    public HashMap<Integer, Integer> getSelections(){
        int lastIndex=this.dices.size()-1;
        return this.dices.get(lastIndex).getSelected();
    }

    public void rollDices(HashMap<Integer,Integer> previousSelections){
        int lastIndex=this.dices.size()-1;
        this.dices.get(lastIndex).generateNumbersOnDice(previousSelections);
        this.incrementRollCount();
    }

    public void addDice(){
        this.dices.add(new Dices());
    }

    public void addSelection(int key, int value){
        int lastIndex=this.dices.size()-1;
        this.dices.get(lastIndex).addSelected(key,value);
    }














    public void setNuetral(int index){
        this.nuetralFilled[index]=true;
    }
    public void setFree(int index){
        this.freeFilled[index]=true;
    }











    public boolean setValue(){
        //Set Values Main Logic
        return true;
    }

    public boolean tableFilled(){
        boolean NUETRAL=Arrays.asList(this.nuetralFilled).contains(true);
        boolean FREE=Arrays.asList(this.nuetralFilled).contains(true);
        boolean TD=false;
        boolean BU=false;
        for (Column column : this.columns) {
            if(column.getType().equals("Bottom-Up")){
                if(column.index==12){
                    BU=true;
                }
            }else if(column.getType().equals("Top-Down")){
                if(column.index==12){
                    TD=true;
                }
            }
        }
        return (NUETRAL && FREE && TD && BU);
    }

    public int totalScore(){
        int[] numberSum={0,0,0,0};
        int[] sumMinMax={0,0,0,0};
        int[] sumSpecials={0,0,0,0};
        int index=0;
        for (Column column : this.columns) {
            int sum1=0;
            int sum2=0;
            int sum3=0;
            int oneValue=0;
            int minValue=0;
            int maxValue=0;
            Iterator it = column.columns.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                String columnName=pair.getKey().toString();
                int value=(int)(pair.getValue());
                switch (columnName){
                    case "1":
                        sum1+=value;
                        oneValue=value;
                        break;
                    case "2":
                        sum1+=value;
                        break;
                    case "3":
                        sum1+=value;
                        break;
                    case "4":
                        sum1+=value;
                        break;
                    case "5":
                        sum1+=value;
                        break;
                    case "6":
                        sum1+=value;
                        break;
                    case "Min":
                        minValue=value;
                        break;
                    case "Max":
                        maxValue=value;
                        break;
                    case "S":
                        sum3+=value;
                        break;
                    case "F":
                        sum3+=value;
                        break;
                    case "P":
                        sum3+=value;
                        break;
                    case "Y":
                        sum3+=value;
                        break;
                    default:
                        break;
                }
                sum2=((maxValue-minValue)*oneValue);
                numberSum[index]=sum1;
                sumMinMax[index]=sum2;
                sumSpecials[index]=sum3;
            }
            index+=1;
        }
        int total=0;
        for(int i=0;i<4;i++){
            total+=(numberSum[i]+sumMinMax[i]+sumSpecials[i]);
        }
        return total;
    }

    public void resetDices(){
        this.dices=new ArrayList<Dices>();
    }



    public boolean saveToDB(){
        return true;
    }
}
