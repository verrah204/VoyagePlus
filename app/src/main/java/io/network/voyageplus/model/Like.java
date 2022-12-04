package io.network.voyageplus.model;

public class Like {
    String user_id;
    String photo_id ;

    public Like(String user_id, String photo_id) {
        this.user_id = user_id;
        this.photo_id = photo_id;

    }
    public Like() {

    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
