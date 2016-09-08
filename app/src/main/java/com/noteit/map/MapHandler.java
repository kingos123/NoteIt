package com.noteit.map;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.noteit.model.ModelToGoogle;
import com.noteit.model.POI;

import java.util.List;

/**
 * Created by rdanino on 9/7/2016.
 */
public class MapHandler {
    public static void setMarker(GoogleMap map, POI poi) {
        LatLng googlePoint = ModelToGoogle.convertLatLng(poi.getPoint());
        map.addMarker(new MarkerOptions().position(ModelToGoogle.convertLatLng(poi.getPoint())).title(poi.getName()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(googlePoint, 12));

    }

    public static void setMarkers(GoogleMap map, com.noteit.model.LatLng center, List<POI> pois) {

        for (POI poi : pois) {
            LatLng googlePoint = ModelToGoogle.convertLatLng(poi.getPoint());
            MarkerOptions marker = new MarkerOptions().position(ModelToGoogle.convertLatLng(poi.getPoint())).title(poi.getName()).snippet(poi.getName());

            map.addMarker(marker).setData(poi);

        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ModelToGoogle.convertLatLng(center), 15));


    }
}
