package com.example.childfinderproject.Model;

public class FinderUploadDataClass {

    String FinderName;
    String FinderMobileNumber;
    String babyFullName;
    String babyAge;
    String FinderAddress;
    String FinderImg;


    public FinderUploadDataClass(String finderName, String finderMobileNumber, String babyFullName, String babyAge, String finderAddress, String finderImg) {
        FinderName = finderName;
        FinderMobileNumber = finderMobileNumber;
        this.babyFullName = babyFullName;
        this.babyAge = babyAge;
        FinderAddress = finderAddress;
        FinderImg = finderImg;
    }

    public FinderUploadDataClass() {
    }

    public String getFinderName() {
        return FinderName;
    }

    public void setFinderName(String finderName) {
        FinderName = finderName;
    }

    public String getFinderMobileNumber() {
        return FinderMobileNumber;
    }

    public void setFinderMobileNumber(String finderMobileNumber) {
        FinderMobileNumber = finderMobileNumber;
    }

    public String getBabyFullName() {
        return babyFullName;
    }

    public void setBabyFullName(String babyFullName) {
        this.babyFullName = babyFullName;
    }

    public String getBabyAge() {
        return babyAge;
    }

    public void setBabyAge(String babyAge) {
        this.babyAge = babyAge;
    }

    public String getFinderAddress() {
        return FinderAddress;
    }

    public void setFinderAddress(String finderAddress) {
        FinderAddress = finderAddress;
    }

    public String getFinderImg() {
        return FinderImg;
    }

    public void setFinderImg(String finderImg) {
        FinderImg = finderImg;
    }
}
