package com.myrole.utils;

/**
 * Created by pankaj.jain on 7/11/2016.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> following = new ArrayList<String>();

        following.add ("Mindi started following Dan");
        following.add("Joy started following Mark, Piter and 3 others");
        following.add("Lincon likes 5 photos");


        List<String> you = new ArrayList<String>();
        you.add("You started following Jon De");
        you.add("You liked Piter's pic");


        expandableListDetail.put("FOLLOWING", following);
        expandableListDetail.put("YOU", you);

        return expandableListDetail;
    }
}

