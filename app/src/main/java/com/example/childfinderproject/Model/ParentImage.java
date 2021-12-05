package com.example.childfinderproject.Model;

public class ParentImage {

    String parentId;
    String parentImage;

    public ParentImage(String parentId, String parentImage) {
        this.parentId = parentId;
        this.parentImage = parentImage;
    }


    public ParentImage() {
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentImage() {
        return parentImage;
    }

    public void setParentImage(String parentImage) {
        this.parentImage = parentImage;
    }
}
