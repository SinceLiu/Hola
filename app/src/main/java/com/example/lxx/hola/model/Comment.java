package com.example.lxx.hola.model;


public class Comment {
    private String id;
    private String name;
    private String content;
    private String time;

    public Comment(){

    }

    public Comment(String name,String content){
        this.name = name;
        this.content = content;
    }

    public String getId(){
        return  id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time ;
    }
}
