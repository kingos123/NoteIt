package com.noteit.connectivity;

import com.noteit.R;
import com.noteit.activities.NoteItApplication;
import com.noteit.model.LatLng;
import com.noteit.model.POI;
import com.noteit.model.PlaceDetails;
import com.noteit.model.PlaceDetailsResults;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rdanino on 9/2/2016.
 */
public class DataFetcherImpl implements DataFetcher {

    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    public List<POI> getPOIS(LatLng point, int radius) {
        final String key = NoteItApplication.getContext().getResources().getString(R.string.google_maps_key);
//            String key = "AIzaSyCBsxOi58HMJdx4csBB2jd2ztVGQ0X_6uw";


        String type = "establishment";


        final String findAroundLocationUrl =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=" + key + "&location=" + point.toUrlValue() + "&radius=" + radius + "&type=" + type;

        final String reviewsUrl =
                "https://maps.googleapis.com/maps/api/place/details/json?placeid=%s&key=%s";


        final RestTemplate restTemplate = new RestClient().getRestTemplate();

        List<POI> pois = null;
        final PlaceDetailsResults simpleResponse = restTemplate.getForObject(findAroundLocationUrl, PlaceDetailsResults.class);
        if (simpleResponse != null && simpleResponse.getResults() != null && simpleResponse.getResults().length > 0) {
            PlaceDetails[] results = simpleResponse.getResults();
            List<FutureTask> futures = new ArrayList<>();

            ArrayBlockingQueue<Runnable> queue =
                    new ArrayBlockingQueue<Runnable>(
                            50);
            ExecutorService executor = new ThreadPoolExecutor(10, 20, 2, TimeUnit.SECONDS, queue);


            for (int i = 0; i < results.length; i++) {

                FutureTask future = getPOISForPoint(restTemplate, reviewsUrl, results[i].place_id, key);

                executor.execute(future);
                futures.add(future);
            }
            pois = new ArrayList<>();
            for (FutureTask future : futures) {
                try {
                    Collections.addAll(pois, (POI) future.get());
                } catch (Exception e) {
                    this.logger.log(Level.SEVERE, "failed to get reviews", e);
                }

            }

        }
        return pois;
    }

    private FutureTask getPOISForPoint(final RestOperations restTemplate, final String reviewsUrl, final String placeid, final String key) {
        FutureTask<POI> future =
                new FutureTask<>(new Callable<POI>() {
                    public POI call() {
                        PlaceDetailsResults response = restTemplate.getForObject(String.format(reviewsUrl, placeid, key), PlaceDetailsResults.class);
                        PlaceDetails result = response.getResult();
                        if (result != null) {
                            POI poi = new POI();
                            poi.setName(result.name);
//                            poi.setImage(result.photos == null ? null: result.photos[0]);
                            poi.setPoint(new LatLng(result.geometry.location.lat, result.geometry.location.lng));
                            if (result.reviews != null && result.reviews.length > 0) {
                                List<PlaceDetails.Review> reviews = new ArrayList<>();
                                Collections.addAll(reviews, result.reviews);
                                poi.setReviews(reviews);
                            }

                            return poi;
                        }

                        return null;
                    }
                });

        return future;
    }
}