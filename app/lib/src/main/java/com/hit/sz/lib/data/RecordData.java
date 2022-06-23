package com.hit.sz.lib.data;

import java.io.Serializable;

public class RecordData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;

    public String date;
    public String userName;
    public int    score;
    public String level; //"easy","medium","hard"

    public RecordData(int type, String date, String userName, int score, String level) {
        super(type);
        this.date =date;
        this.userName = userName;
        this.score = score;
        this.level = level;
    }

    public String getName(){
        return this.userName;
    }
    public String getDate() {return this.date;}
    public String getScore() {return String.valueOf(this.score);}
    public String getLevel(){
        return this.level;
    }
}