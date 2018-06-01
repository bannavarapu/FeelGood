package com.example.anudeepthi.feelgood;

import java.util.HashMap;

public class User {
    String userName;
    String userTag;
    HashMap<String,String> fav_blog = new HashMap<String, String>();
    HashMap<String,String> my_blog = new HashMap<String, String>();

    public User(String userName, String userTag)
    {
        System.out.println("Here too");
        this.userName = userName;
        this.userTag = userTag;
        fav_blog.put("0","dummy");
        my_blog.put("0","dummy");
        MainActivity.formFlag = true;
    }

    public User ()
    {

    }

}
