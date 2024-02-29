package com.example.farmerkonnect;

public class Farmer {
    private String farmername;

    public Farmer() {
    }

    private String farmlocation;
    private String profilePictureURL;

    public String getFarmername() {
        return farmername;
    }

    public void setFarmername(String farmername) {
        this.farmername = farmername;
    }

    public String getFarmlocation() {
        return farmlocation;
    }

    public void setFarmlocation(String farmlocation) {
        this.farmlocation = farmlocation;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public Farmer(String farmername, String farmlocation, String profilePictureURL) {
        this.farmername = farmername;
        this.farmlocation = farmlocation;
        this.profilePictureURL = profilePictureURL;
    }
// ... Constructor, getters, setters
}
