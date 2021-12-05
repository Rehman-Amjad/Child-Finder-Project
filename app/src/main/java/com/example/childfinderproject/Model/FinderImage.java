package com.example.childfinderproject.Model;

public class FinderImage {

    String finderId;
    String finderImage;
    String currentLatitude;
    String currentLongitude;

    public FinderImage(String finderId, String finderImage, String currentLatitude, String currentLongitude) {
        this.finderId = finderId;
        this.finderImage = finderImage;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
    }

    public FinderImage() {
    }

    public String getFinderId() {
        return finderId;
    }

    public void setFinderId(String finderId) {
        this.finderId = finderId;
    }

    public String getFinderImage() {
        return finderImage;
    }

    public void setFinderImage(String finderImage) {
        this.finderImage = finderImage;
    }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
}
