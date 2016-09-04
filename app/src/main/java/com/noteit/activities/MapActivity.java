package com.noteit.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.noteit.R;
import com.noteit.connectivity.DataFetcher;
import com.noteit.connectivity.DataFetcherImpl;
import com.noteit.model.LatLng;
import com.noteit.model.PlaceDetails;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap mMap;

    public static void main(String args[]) {
        String key = "AIzaSyCBsxOi58HMJdx4csBB2jd2ztVGQ0X_6uw";
        try {

            String type = "establishment";
            String latLong = "40.758906,-73.985366";
            String radius = "1000";
            final String url =
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=" + key + "&location=" + latLong + "&radius=" + radius + "&type=" + type;
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper om = new ObjectMapper();
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));


            MappingJackson2HttpMessageConverter c = new MappingJackson2HttpMessageConverter();
            c.setObjectMapper(om);
            restTemplate.getMessageConverters().add(c);

            PlaceDetails response = restTemplate.getForObject(url, PlaceDetails.class);
            System.out.println(response);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        // Add a marker in Sydney and move the camera
        com.google.android.gms.maps.model.LatLng sydney = new com.google.android.gms.maps.model.LatLng(-34, 151);
        this.mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        new HttpRequestTask().execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<PlaceDetails.Review>> {
        @Override
        protected List<PlaceDetails.Review> doInBackground(Void... params) {
            LatLng point = new LatLng(40.758906, -73.985366);
            int radius = 1000;
            DataFetcher dataFetcher = new DataFetcherImpl();
            List<PlaceDetails.Review> reviews = dataFetcher.getReviews(point, radius);
            return reviews;
        }

        @Override
        protected void onPostExecute(List<PlaceDetails.Review> reviews) {


            for (int i = 0; i < reviews.size(); i++) {
                System.out.println("author name: " + reviews.get(i).author_name);
                System.out.println("review text: " + reviews.get(i).text);
                System.out.println("rating: " + reviews.get(i).rating);
            }


        }

    }
}
