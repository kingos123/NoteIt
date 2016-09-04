package com.noteit.model;


import com.google.maps.model.LocationType;

/**
 * Created by rdanino on 9/2/2016.
 */
public class Geometry {
    /**
     * {@code bounds} (optionally returned) stores the bounding box which can fully contain the
     * returned result. Note that these bounds may not match the recommended viewport. (For example,
     * San Francisco includes the Farallon islands, which are technically part of the city, but
     * probably should not be returned in the viewport.)
     */
    public Bounds bounds;

    /**
     * {@code location} contains the geocoded {@code latitude,longitude} value. For normal address
     * lookups, this field is typically the most important.
     */
    public LatLng location;

    /**
     * The level of certainty of this geocoding result.
     */
    public LocationType locationType;

    /**
     * {@code viewport} contains the recommended viewport for displaying the returned result.
     * Generally the viewport is used to frame a result when displaying it to a user.
     */
    public Bounds viewport;
}