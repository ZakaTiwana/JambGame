package com.example.jambgame;

import java.util.HashMap;
import java.util.Iterator;

public class Dices {
    int[] dices;
    HashMap<Integer,Integer> selected;

    public Dices() {
        this.dices=new int[6];
        this.selected=new HashMap<Integer, Integer>();
    }

    private int getRandom(){
        return (int)(Math.random() * (6) + 1);
    }

    public void generateNumbersOnDice(HashMap<Integer,Integer> selected){
        // Generate Random Numbers on Each Element
        for(int i=0;i<this.dices.length;i++){
            this.dices[i]=getRandom();
        }
        // Replacing Same Numbers on this dices from selected
        Iterator it = selected.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            int index=(int)(pair.getKey());
            int value=(int)(pair.getValue());
            this.dices[index]=value;
        }
    }

    public void addSelected(int index,int value){
        this.selected.put(index, value);
    }

    public HashMap<Integer,Integer> getSelected(){
        return this.getSelected();
    }

    public int[] getDices(){
        return this.dices;
    }
}
