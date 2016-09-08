package com.noteit.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.noteit.R;
import com.noteit.connectivity.DataFetcher;
import com.noteit.connectivity.DataFetcherImpl;
import com.noteit.map.MapHandler;
import com.noteit.model.LatLng;
import com.noteit.model.ModelToGoogle;
import com.noteit.model.POI;

import org.springframework.util.CollectionUtils;

import java.util.List;

//import com.google.android.gms.maps.GoogleMap;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapActivity.class.getSimpleName();
    int currentIndex = -1;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initTextSwitcher();
        initCloseBtn();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getExtendedMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {


                LatLng center = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                new HttpRequestTask(center).execute();


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
        this.mMap.getUiSettings().setZoomControlsEnabled(true);

        com.google.android.gms.maps.model.LatLng timesquare = new com.google.android.gms.maps.model.LatLng(40.758976, -73.985152);
        this.mMap.addMarker(new MarkerOptions().position(timesquare).title("Marker in Sydney"));
        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(timesquare));
        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.androidmapsextensions.Marker marker) {
                final POI poi = marker.getData();
                if (poi.getReviews() != null && poi.getReviews().size() > 0) {

                    findViewById(R.id.reviews_popup).setVisibility(View.VISIBLE);

                    final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);

                    MapActivity.this.currentIndex++;
                    String txt = poi.getReviews().get(MapActivity.this.currentIndex).text;
                    textSwitcher.setText(txt);
                    initBtnNext(textSwitcher, poi);
                    initBtnBack(textSwitcher, poi);


                }
                return true;
            }
        });
        new HttpRequestTask(ModelToGoogle.convertLatLng(timesquare)).execute();
    }

    private void initBtnBack(final TextSwitcher textSwitcher, final POI poi) {
        Button btnBack = (Button) findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (MapActivity.this.currentIndex > 0) {
                    MapActivity.this.currentIndex--;
                    if (MapActivity.this.currentIndex == poi.getReviews().size()) {
                        MapActivity.this.currentIndex = 0;
                    }
                    String txt = poi.getReviews().get(MapActivity.this.currentIndex).text;

                    textSwitcher.setText(txt);
                }

            }
        });
    }

    private void initCloseBtn() {
        Button btnClose = (Button) findViewById(R.id.buttonClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                findViewById(R.id.reviews_popup).setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initBtnNext(final TextSwitcher textSwitcher, final POI poi) {
        Button btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                MapActivity.this.currentIndex++;
                if (MapActivity.this.currentIndex == poi.getReviews().size()) {
                    MapActivity.this.currentIndex = 0;
                }
                String txt = poi.getReviews().get(MapActivity.this.currentIndex).text;

                textSwitcher.setText(txt);
            }
        });
    }

    private void initTextSwitcher() {

        TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView myText = new TextView(MapActivity.this);
                myText.setBackgroundColor(Color.WHITE);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setMaxLines(3);
                myText.setVerticalScrollBarEnabled(true);
                myText.setTextSize(20);
                myText.setTextColor(Color.BLUE);
                myText.setMovementMethod(new ScrollingMovementMethod());
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(MapActivity.this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(MapActivity.this, android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);


    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<POI>> {
        private LatLng point;

        public HttpRequestTask(LatLng point) {
            this.point = point;
        }

        @Override
        protected List<POI> doInBackground(Void... params) {
            int radius = 1000;
            DataFetcher dataFetcher = new DataFetcherImpl();
            List<POI> pois = dataFetcher.getPOIS(this.point, radius);
            return pois;
        }

        @Override
        protected void onPostExecute(List<POI> pois) {

            if (!CollectionUtils.isEmpty(pois)) {
                MapHandler.setMarkers(MapActivity.this.mMap, this.point, pois);
            }

        }

    }
}
