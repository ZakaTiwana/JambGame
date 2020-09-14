package com.example.jambgame;

import java.util.HashMap;

public class Column {

    String type;
    HashMap<String,Integer> columns;
    int index;

    public Column(String type) {
        this.type=type;
        intializeColumn();
        index=0;
    }

    private void intializeColumn(){
        String[] rowNames={"1","2","3","4","5","6","Min","Max","S","F","P","Y"};
        for (String i : rowNames) {
            this.columns.put(i,0);
        }
    }

    public void setValue(String key){
        //Main Logic
    }

    public String getType(){
        return this.type;
    }

}
