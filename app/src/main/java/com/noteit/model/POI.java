package com.noteit.model;

import android.media.Image;

import java.util.List;

/**
 * Created by rdanino on 9/7/2016.
 */
public class POI {
    private LatLng point;
    private String name;
    private Image image;
    private List<PlaceDetails.Review> reviews;

    public POI() {
    }

    public POI(LatLng point, String name, Image image, List<PlaceDetails.Review> reviews) {
        this.point = point;
        this.name = name;
        this.image = image;
        this.reviews = reviews;
    }

    public LatLng getPoint() {
        return this.point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<PlaceDetails.Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<PlaceDetails.Review> reviews) {
        this.reviews = reviews;
    }
}
