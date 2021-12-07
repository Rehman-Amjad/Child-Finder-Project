package com.example.childfinderproject.Model;

public class ParentUploadDataClass {

    String parentName;
    String parentMobileNumber;
    String babyFullName;
    String babyAge;
    String parentAddress;
    String parentImg;
    String parentId;

    public ParentUploadDataClass(String parentName, String parentMobileNumber, String babyFullName, String babyAge, String parentAddress, String parentImg, String parentId) {
        this.parentName = parentName;
        this.parentMobileNumber = parentMobileNumber;
        this.babyFullName = babyFullName;
        this.babyAge = babyAge;
        this.parentAddress = parentAddress;
        this.parentImg = parentImg;
        this.parentId = parentId;
    }

    public ParentUploadDataClass() {
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentMobileNumber() {
        return parentMobileNumber;
    }

    public void setParentMobileNumber(String parentMobileNumber) {
        this.parentMobileNumber = parentMobileNumber;
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

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
    }

    public String getParentImg() {
        return parentImg;
    }

    public void setParentImg(String parentImg) {
        this.parentImg = parentImg;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
