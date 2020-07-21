package com.CyberNerdForHireGames.SlimeInvaders.ProfileSignUpSlashLogin;

// container class for user
public class User {

    private String name, email, uid, username;

    User(String name, String email, String uid, String username){
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.username = username;
    }

    public User() {}

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String userName){ this.username = userName; }

    public String getUsername (){ return username; }
}
