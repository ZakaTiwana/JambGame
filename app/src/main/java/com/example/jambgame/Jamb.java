package com.example.jambgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Jamb {

    int rollCount;
    ArrayList<Dices> dices = new ArrayList<>();
    ArrayList<Column> columns = new ArrayList<>();
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
        return this.columns;
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

    public boolean setValue(String rowName, String columnName,int col_index, int row_index){
        boolean is_move_possible = false;
        Column column = this.columns.get(col_index);
        switch(columnName) {
            case "Bottom-Up":
                if(column.index==row_index){
                    int cellScore=calculateCellValue(column,rowName);
                    column.columns.put(rowName,cellScore);
                    column.index--;
                    is_move_possible = true;
                }
                break;
            case "Top-Down":
                if(column.index==row_index){
                    int cellScore=calculateCellValue(column,rowName);
                    column.columns.put(rowName,cellScore);
                    column.index++;
                   is_move_possible = true;
                }
                break;
            case "Nuetral":
                if(!nuetralFilled[row_index]){
                    int cellScore=calculateCellValue(column,rowName);
                    column.columns.put(rowName,cellScore);
                    is_move_possible = true;
                }
                break;
            case "Free":
                if(!freeFilled[row_index]) {
                    int cellScore = calculateCellValue(column, rowName);
                    column.columns.put(rowName, cellScore);
                    is_move_possible = true;
                }
                break;
        }
        return is_move_possible;

//        for (Column column : this.columns) {
//            if(column.getType().equals("Bottom-Up")){
//                if(column.index-1==index){
//                    int cellScore=calculateCellValue(column,rowName);
//                    column.columns.put(rowName,cellScore);
//                    return true;
//                }else{
//                    return false;
//                }
//            }else if(column.getType().equals("Top-Down")){
//                if(column.index+1==index){
//                    int cellScore=calculateCellValue(column,rowName);
//                    column.columns.put(rowName,cellScore);
//                    return true;
//                }else{
//                    return false;
//                }
//            }else if(column.getType().equals("Nuetral")){
//                if(nuetralFilled[index]==true){
//                    return false;
//                }else{
//                    int cellScore=calculateCellValue(column,rowName);
//                    column.columns.put(rowName,cellScore);
//                    return true;
//                }
//            }else if(column.getType().equals("Free")){
//                if(freeFilled[index]==true){
//                    return false;
//                }else{
//                    int cellScore=calculateCellValue(column,rowName);
//                    column.columns.put(rowName,cellScore);
//                    return true;
//                }
//            }
//        }
//        return true;
    }

    public int getCalculatedValue(int col_index, String row_name){
        return calculateCellValue(this.columns.get(col_index),row_name);
    }
    private int calculateCellValue(Column column, String rowName){
        int value=0;
        int cellScore=0;
        switch (rowName){
            case "1":
                int onesCount=numCountInLastDiceRolling(1);
                cellScore=onesCount*1;
                return cellScore;
            case "2":
                int twosCount=numCountInLastDiceRolling(2);
                cellScore=twosCount*2;
                return cellScore;
            case "3":
                int threesCount=numCountInLastDiceRolling(3);
                cellScore=threesCount*3;
                return cellScore;
            case "4":
                int foursCount=numCountInLastDiceRolling(4);
                cellScore=foursCount*4;
                return cellScore;
            case "5":
                int fivesCount=numCountInLastDiceRolling(5);
                cellScore=fivesCount*5;
                return cellScore;
            case "6":
                int sixsCount=numCountInLastDiceRolling(6);
                cellScore=sixsCount*6;
                return cellScore;
            case "Min":
                return maxValue();
            case "Max":
                return minValue();
            case "S":
                return straightValue()+20;
            case "F":
                return fullValue()+30;
            case "P":
                return pokerValue()+40;
            case "Y":
                return yambValue()+50;
            default:
                break;
        }
        return value;
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
        this.rollCount=0;
        this.dices=new ArrayList<Dices>();
        this.dices.add(new Dices());
    }

    public boolean saveToDB(){
        return true;
    }


    private int numCountInLastDiceRolling(int numItself){
        int lastIndex=this.dices.size()-1;
        int[] dicesNumbers=this.dices.get(lastIndex).dices;
        int count=0;
        for(int value:dicesNumbers){
            if(numItself==value){
                count++;
            }
        }
        return count;
    }

    private int straightValue(){
        int value=0;
        int lastIndex=this.dices.size()-1;
        int[] dices=this.dices.get(lastIndex).dices;
        //(1,2,3,4,5),6
        boolean con1=dices[0]==1 && dices[1]==2 && dices[2]==3 && dices[3]==4 && dices[4]==5;
        //(2,3,4,5,6),1
        boolean con2=dices[0]==2 && dices[1]==3 && dices[2]==4 && dices[3]==5 && dices[4]==6;
        //6,(1,2,3,4,5)
        boolean con3=dices[1]==1 && dices[2]==2 && dices[3]==3 && dices[4]==4 && dices[5]==5;
        //1,(2,3,4,5,6)
        boolean con4=dices[1]==2 && dices[2]==3 && dices[3]==4 && dices[4]==5 && dices[5]==6;
        if(con1 || con2 || con3 || con4){
            if(this.rollCount==1){
                value=66;
            }else if(this.rollCount==2){
                value=56;
            }else if(this.rollCount==3){
                value=46;
            }
        }
        return value;
    }

    private int fullValue(){
        int value=0;
        int lastIndex=this.dices.size()-1;
        int[] dices=this.dices.get(lastIndex).dices;
        HashMap<Integer, Integer> repitionMap=new HashMap<Integer, Integer>();
        //Create a Map where key represents number and value represents repition
        //Note put function replace new value when place on same key
        for(int diceValue:dices){
            repitionMap.put(diceValue,0);
        }
        //Increasing Counts
        for(int diceValue:dices){
            int preValue=repitionMap.get(diceValue)+1;
            repitionMap.put(diceValue,preValue);
        }
        //Checking 3 and 2
        boolean twoCountExist=false;
        boolean threeCountExist=false;
        int twoCountScore=0;
        int threeCountScore=0;
        Iterator it = repitionMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            int diceNumber=(int)(pair.getKey());
            int repCount=(int)(pair.getValue());
            if(repCount==2){
                twoCountExist=true;
                twoCountScore=diceNumber*repCount;
            }else if(repCount==3){
                threeCountExist=true;
                threeCountScore=diceNumber*repCount;
            }
        }
        if(twoCountExist && threeCountExist){
            value=twoCountScore+threeCountScore;
            return value;
        }else{
            return value;
        }
    }

    private int pokerValue(){
        int value=0;
        int lastIndex=this.dices.size()-1;
        int[] dices=this.dices.get(lastIndex).dices;
        HashMap<Integer, Integer> repitionMap=new HashMap<Integer, Integer>();
        //Create a Map where key represents number and value represents repition
        //Note put function replace new value when place on same key
        for(int diceValue:dices){
            repitionMap.put(diceValue,0);
        }
        //Increasing Counts
        for(int diceValue:dices){
            int preValue=repitionMap.get(diceValue)+1;
            repitionMap.put(diceValue,preValue);
        }
        //Checking 3 and 2
        boolean fourOrMoreCountExist=false;
        int countScore=0;
        Iterator it = repitionMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            int diceNumber=(int)(pair.getKey());
            int repCount=(int)(pair.getValue());
            if(repCount>=4){
                fourOrMoreCountExist=true;
                countScore=repCount*diceNumber;
            }
        }
        if(fourOrMoreCountExist){
            value=countScore;
            return value;
        }else{
            return value;
        }
    }

    private int yambValue(){
        int value=0;
        int lastIndex=this.dices.size()-1;
        int[] dices=this.dices.get(lastIndex).dices;
        HashMap<Integer, Integer> repitionMap=new HashMap<Integer, Integer>();
        //Create a Map where key represents number and value represents repition
        //Note put function replace new value when place on same key
        for(int diceValue:dices){
            repitionMap.put(diceValue,0);
        }
        //Increasing Counts
        for(int diceValue:dices){
            int preValue=repitionMap.get(diceValue)+1;
            repitionMap.put(diceValue,preValue);
        }
        //Checking 3 and 2
        boolean fiveCountExist=false;
        int countScore=0;
        Iterator it = repitionMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            int diceNumber=(int)(pair.getKey());
            int repCount=(int)(pair.getValue());
            if(repCount==5){
                fiveCountExist=true;
                countScore=repCount*diceNumber;
            }
        }
        if(fiveCountExist){
            value=countScore;
            return value;
        }else{
            return value;
        }
    }

    private int minValue(){
        int value=0;
        return value;
    }
    private int maxValue(){
        int value=0;
        return value;
    }
}
