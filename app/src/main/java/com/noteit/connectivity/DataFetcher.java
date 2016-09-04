package com.noteit.connectivity;

import com.noteit.model.LatLng;
import com.noteit.model.PlaceDetails;

import java.util.List;

/**
 * Created by rdanino on 9/2/2016.
 */
public interface DataFetcher {
    public List<PlaceDetails.Review> getReviews(LatLng point, int radius);
}
