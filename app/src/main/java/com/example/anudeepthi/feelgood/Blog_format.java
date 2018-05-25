package com.example.anudeepthi.feelgood;

public class Blog_format {
    String userId;
    String title;
    String postID;
    String description;
    String visibility;

    public Blog_format(){

    }

    public Blog_format(String userID, String title, String postID, String description,String visibility){
        this.userId = userID;
        this.title = title;
        this.description = description;
        this.postID = postID;
        this.visibility=visibility;
    }

}
