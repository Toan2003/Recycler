package com.example.myapplication.Model;

public class Post {
    String title;

    public String getTitle() {
        return title;
    }
    public Post(String title, String content, int image)
    {
        this.title = title;
        this.content=content;
        this.image=image;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    String content;
    int image;
}
