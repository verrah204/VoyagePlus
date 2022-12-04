package io.network.voyageplus.Data;

import java.util.ArrayList;

import io.network.voyageplus.R;

public class TripList {

    public ArrayList<String> guest_trip_array() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Friends");
        arrayList.add("Solo");
        return arrayList;
    }

    public ArrayList<String> all_trips_db() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("family_trip");
        arrayList.add("friends_trip");
        arrayList.add("honeymoon_trip");
        arrayList.add("religious_trip");
        arrayList.add("solo_trip");
        return arrayList;
    }

    public ArrayList<String> all_trips_names() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Family");
        arrayList.add("Friends");
        arrayList.add("Honeymoon");
        arrayList.add("Religious");
        arrayList.add("Solo");
        return arrayList;
    }

    public ArrayList<String> all_trips_home_titles() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Some Family trips available");
        arrayList.add("Some best spots to enjoy with your friends");
        arrayList.add("Some best honeymoon places to visit");
        arrayList.add("Some religious places where most visits");
        arrayList.add("Some pleasant places to visit for your solo trip");
        return arrayList;
    }



}
