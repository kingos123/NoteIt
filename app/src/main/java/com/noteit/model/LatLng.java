package com.noteit.model;

import com.google.maps.internal.StringJoin;

import java.util.Locale;

/**
 * Created by rdanino on 9/2/2016.
 */
public class LatLng implements StringJoin.UrlValue {

    /**
     * The latitude of this location.
     */
    public double lat;
    /**
     * The longitude of this location.
     */
    public double lng;

    public LatLng() {
    }

    /**
     * Construct a location with a latitude longitude pair.
     */
    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return toUrlValue();
    }

    @Override
    public String toUrlValue() {
        // Enforce Locale to English for double to string conversion
        return String.format(Locale.ENGLISH, "%f,%f", this.lat, this.lng);
    }
}
