package com.team.gajimarket.activity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    private String userName;
    private String userEmail;
    private String userPos;
    private String userCRN;

    public UserData() {}

    public UserData(String userName, String userEmail, String userPos, String userCRN) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPos = userPos;
        this.userCRN = userCRN;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userName", userName);
        result.put("userEmail", userEmail);
        result.put("userPos", userPos);
        result.put("userCRN", userCRN);

        return result;
    }


    public String getUserName() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserPos() {
        return userPos;
    }
    public String getUserCRN() { return userCRN; }
}
