package com.example.jambgame;

import java.util.HashMap;

public class Column {

    String type;
    HashMap<String,Integer> columns = new HashMap<>();
    int index;

    public Column(String type) {
        this.type=type;
        intializeColumn();
        if(type.equals("Bottom-Up")) index = this.columns.size()-1;
        else  index = 0;
    }

    private void intializeColumn(){
        String[] rowNames={"1","2","3","4","5","6","Max","Min","S","F","P","Y"};
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
