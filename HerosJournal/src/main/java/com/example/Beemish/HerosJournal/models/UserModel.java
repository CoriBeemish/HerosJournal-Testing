package com.example.Beemish.HerosJournal.models;

import com.example.Beemish.HerosJournal.R;

public class UserModel {
    private int userID;
    private String userEmail;
    private String userPassword;
    private int userAvatarValue;

    public UserModel(int userID, String userEmail, String userPassword) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAvatarValue = R.drawable.avatar;
    }

    public UserModel(String userEmail) {
        this.userEmail = userEmail;
        this.userAvatarValue = R.drawable.avatar;
    }

    public int getUserID() {return userID;}

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserAvatar() {return userAvatarValue;}

    public void setUserAvatar(int userAvatar) {this.userAvatarValue = userAvatar;}

    public String getUserEmail() {return userEmail;}

    public String getUserPassword() {return userPassword;}

    public boolean passwordIsEqual(String password) {
        if (password.equals(this.userPassword)) {
            return true;
        } return false;
    }
}
