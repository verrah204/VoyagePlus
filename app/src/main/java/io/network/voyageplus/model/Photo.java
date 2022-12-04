package io.network.voyageplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Photo implements Parcelable {
    String photoId;
    String name;
    String description;
    String town;
    String categorie;
    String rate ;
    String imageUrl;

    protected Photo(Parcel in) {
        photoId = in.readString();
        name = in.readString();
        description = in.readString();
        town = in.readString();
        categorie = in.readString();
        rate = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public Photo(String photoId, String name, String description, String town, String categorie, String rate, String imageUrl) {
        this.photoId = photoId;
        this.name = name;
        this.description = description;
        this.town = town;
        this.categorie = categorie;
        this.rate = rate;
        this.imageUrl = imageUrl;
    }

    public Photo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(town);
        dest.writeString(categorie);
        dest.writeString(rate);
        dest.writeString(imageUrl);
    }
}
