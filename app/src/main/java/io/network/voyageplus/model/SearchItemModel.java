package io.network.voyageplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SearchItemModel implements Parcelable {
    public static final Creator<SearchItemModel> CREATOR = new Creator<SearchItemModel>() {
        @Override
        public SearchItemModel createFromParcel(Parcel in) {
            return new SearchItemModel(in);
        }

        @Override
        public SearchItemModel[] newArray(int size) {
            return new SearchItemModel[size];
        }
    };
    String sdb_name, s_place_town, s_place_categorie, s_place_name, s_place_img;
    List<Like> likes;
    List<Comments> comments;

    public SearchItemModel(String sdb_name, String s_place_town, String s_place_categorie, String s_place_name, String s_place_img,List<Like> likes,List<Comments> comments) {
        this.sdb_name = sdb_name;
        this.s_place_town = s_place_town;
        this.s_place_categorie = s_place_categorie;
        this.s_place_name = s_place_name;
        this.s_place_img = s_place_img;
        this.likes = likes;
        this.comments = comments;
    }

    protected SearchItemModel(Parcel in) {
        sdb_name = in.readString();
        s_place_categorie = in.readString();
        s_place_town = in.readString();
        s_place_name = in.readString();
        s_place_img = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sdb_name);
        dest.writeString(s_place_town);
        dest.writeString(s_place_categorie);
        dest.writeString(s_place_name);
        dest.writeString(s_place_img);
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getSdb_name() {
        return sdb_name;
    }

    public void setSdb_name(String sdb_name) {
        this.sdb_name = sdb_name;
    }

    public String getS_place_town() {
        return s_place_town;
    }

    public void setS_place_town(String s_place_town) {
        this.s_place_town = s_place_town;
    }

    public String getS_place_categorie() {
        return s_place_categorie;
    }

    public void setS_place_categorie(String s_place_categorie) {
        this.s_place_categorie = s_place_categorie;
    }

    public String getS_place_name() {
        return s_place_name;
    }

    public void setS_place_name(String s_place_name) {
        this.s_place_name = s_place_name;
    }

    public String getS_place_img() {
        return s_place_img;
    }

    public void setS_place_img(String s_place_img) {
        this.s_place_img = s_place_img;
    }
}
