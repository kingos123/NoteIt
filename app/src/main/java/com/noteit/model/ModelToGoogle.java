package com.noteit.model;

/**
 * Created by rdanino on 9/7/2016.
 */
public class ModelToGoogle {
    public static com.google.android.gms.maps.model.LatLng convertLatLng(LatLng point) {
        return new com.google.android.gms.maps.model.LatLng(point.lat, point.lng);
    }

    public static LatLng convertLatLng(com.google.android.gms.maps.model.LatLng point) {
        return new LatLng(point.latitude, point.longitude);
    }


    public static PlaceDetails.Review[] convertReviews(com.google.maps.model.PlaceDetails.Review[] reviews) {
        PlaceDetails.Review[] modelReviews = null;
        if (reviews != null && reviews.length > 0) {
            modelReviews = new PlaceDetails.Review[reviews.length];
            for (int i = 0; i < reviews.length; i++) {
                PlaceDetails.Review modelReview = convertReview(reviews[i]);
                if (modelReview != null) {
                    modelReviews[i] = modelReview;
                }
            }
        }
        return modelReviews;
    }

    public static PlaceDetails.Review convertReview(com.google.maps.model.PlaceDetails.Review review) {
        PlaceDetails.Review modelReview = null;
        if (review != null) {

            modelReview = new PlaceDetails.Review();
            modelReview.author_name = review.authorName;
            modelReview.authorUrl = review.authorUrl;
            modelReview.text = review.text;
            modelReview.rating = review.rating;
        }

        return modelReview;
    }

}
