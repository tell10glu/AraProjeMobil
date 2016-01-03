package com.tll.araproje.models;

/**
 * Created by abdullahtellioglu on 01/01/16.
 */
public class Word {
    private int id;
    private String word;
    public Word(int id,String word){
        this.id = id;
        this.word = word;
    }
    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}
