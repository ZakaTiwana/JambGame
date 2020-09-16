package com.example.jambgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Jamb {

    int rollCount;
    ArrayList<Dices> dices = new ArrayList<>();
    ArrayList<Column> columns = new ArrayList<>();
    Boolean[] nuetralFilled;
    Boolean[] freeFilled;
    ArrayList<int[]> sumRows=new ArrayList<int[]>();

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
                    nuetralFilled[row_index] = Boolean.TRUE;
                    is_move_possible = true;
                }
                break;
            case "Free":
                if(!freeFilled[row_index]) {
                    int cellScore = calculateCellValue(column, rowName);
                    column.columns.put(rowName, cellScore);
                    freeFilled[row_index] = Boolean.TRUE;
                    is_move_possible = true;
                }
                break;
        }
        return is_move_possible;
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
                return minValue();
            case "Max":
                return maxValue();
            case "S":
                if(straightValue()==0){
                    return 0;
                }else{
                    return straightValue();
                }
            case "F":
                if(fullValue()==0){
                    return 0;
                }else{
                    return fullValue()+30;
                }
            case "P":
                if(pokerValue()==0){
                    return 0;
                }else{
                    return pokerValue()+40;
                }
            case "Y":
                if(yambValue()==0){
                    return 0;
                }else{
                    return yambValue()+50;
                }
            default:
                break;
        }
        return value;
    }

    public boolean tableFilled(){
        boolean NUETRAL= !Arrays.asList(this.nuetralFilled).contains(false);
        boolean FREE= !Arrays.asList(this.nuetralFilled).contains(false);
        boolean TD=false;
        boolean BU=false;
        for (Column column : this.columns) {
            if(column.getType().equals("Bottom-Up")){
                if(column.index <= 0){
                    BU=true;
                }
            }else if(column.getType().equals("Top-Down")){
                if(column.index >= 11){
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
        this.sumRows=new ArrayList<int[]>();
        this.sumRows.add(numberSum);
        this.sumRows.add(sumMinMax);
        this.sumRows.add(sumSpecials);
        return total;
    }

    public void resetDices(){
        this.rollCount=0;
        this.dices=new ArrayList<Dices>();
        this.dices.add(new Dices());
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
        boolean[] checks={false,false,false,false,false,false};

        for(int i=0;i<dices.length;i++){
            switch (dices[i]){
                case 1:
                    checks[0]=true;
                    break;
                case 2:
                    checks[1]=true;
                    break;
                case 3:
                    checks[2]=true;
                    break;
                case 4:
                    checks[3]=true;
                    break;
                case 5:
                    checks[4]=true;
                    break;
                case 6:
                    checks[5]=true;
                    break;
                default:
                    break;
            }
        }

        boolean con1=checks[0] &&checks[1] &&checks[2] &&checks[3] &&checks[4];
        boolean con2=checks[1] &&checks[2] &&checks[3] &&checks[4] &&checks[5];
        if(con1 || con2){
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
        //Checking 4 or more exists
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
        //Checking 5 exists
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
        ArrayList<Integer> dicesValues=new ArrayList<Integer>();
        for(Dices dice:this.dices){
            for(int diceValue:dice.getDices()){
                dicesValues.add(diceValue);
            }
        }
        Collections.sort(dicesValues);
        value=dicesValues.get(0)+dicesValues.get(1)+dicesValues.get(2)+dicesValues.get(3)+dicesValues.get(4);
        return value;
    }
    private int maxValue(){
        int value=0;
        ArrayList<Integer> dicesValues=new ArrayList<Integer>();
        for(Dices dice:this.dices){
            for(int diceValue:dice.getDices()){
                dicesValues.add(diceValue);
            }
        }
        Collections.sort(dicesValues);
        int lastIndex=dicesValues.size()-1;
        for(int index=0;index<dicesValues.size();index++){
            if(index>(lastIndex-5)){
                value=value+dicesValues.get(index);
            }
        }
        return value;
    }

    public ArrayList<int[]> getRowsSum(){
        return this.sumRows;
    }
}
