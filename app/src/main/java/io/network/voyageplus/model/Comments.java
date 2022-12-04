package io.network.voyageplus.model;

public class Comments {
    String comment_text ;
    String date;
    String photo_id;
    String user_id;

    public Comments(String comment_text, String date, String photo_id,  String user_id) {
        this.comment_text = comment_text;
        this.date = date;
        this.user_id = user_id;
        this.photo_id = photo_id;
    }

    public Comments(){

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }
}
