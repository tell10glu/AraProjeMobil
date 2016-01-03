package com.tll.araproje.service;

/**
 * Created by abdullahtellioglu on 01/01/16.
 */
public class Urls {
    public String baseUrl;
    private static Urls singleton;
    public static Urls getInstance(String baseUrl){
        if(singleton==null){
            singleton = new Urls(baseUrl);
        }
        return singleton;
    }
    public static Urls getInstance(){
        if(singleton==null){
            throw new RuntimeException("Server Url Missing!");
        }

        return singleton;
    }
    private Urls(String baseUrl){
        this.baseUrl = baseUrl;
    }
    public String getRandomWordUrl(){
        return baseUrl+"/api/kelimeler";
    }
    public String getWordUrl(String word){
        return baseUrl+"/api/kelimeler/?kelime="+word;
        // url/?kelime=asd
    }

}
