package hunter.black.mylocationtracker;

/**
 * Created by FaizulHauqe on 8/17/2016.
 */
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Polyline polyline;
    int[] color = {Color.BLACK,Color.WHITE,Color.BLUE, Color.RED, Color.GRAY, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW};
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        random = new Random(color.length);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PolylineOptions options = new PolylineOptions().width(5).geodesic(true);
        for (int i = 0; i < MainActivity.listData.size(); i++) {
            LatLng point = MainActivity.listData.get(i);
            options.color(color[random.nextInt(color.length)]);
            options.add(point);
            googleMap.addMarker(new MarkerOptions().position(MainActivity.listData.get(i)).title(MyService.localityList.get(i)));
        }
        polyline = mMap.addPolyline(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MainActivity.listData.get(0)));
        mMap.getMaxZoomLevel();
    }

}
