package com.example.anudeepthi.feelgood;

public class Relax_option_format {
    String title;
    String desc;
    String image;
    Long likes;
    Long dislikes;
    String id;

    public Relax_option_format(){

    }

    public Relax_option_format(String title, String desc, String image, Long likes, Long dislikes, String id)
    {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.likes = likes;
        this.dislikes = dislikes;
        this.id = id;
    }
}
