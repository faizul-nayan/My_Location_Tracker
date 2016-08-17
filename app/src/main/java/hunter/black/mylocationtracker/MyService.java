package hunter.black.mylocationtracker;

/**
 * Created by FaizulHauqe on 8/17/2016.
 */
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Location location;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    public static ArrayList<LatLng> latLngs;
    public static  ArrayList<String> localityList;
    Geocoder geocoder;

    public MyService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        latLngs = new ArrayList<LatLng>();
        localityList = new ArrayList<String>();
        geocoder = new Geocoder(this, Locale.getDefault());
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(600000);
        googleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(googleApiClient.isConnected()){
            googleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if(location!=null){
            double lat=location.getLatitude();
            double lon=location.getLongitude();

            System.out.println(lat +"  "+lon);

        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {
        double lat=location.getLatitude();
        double lon=location.getLongitude();

        try {
            List<Address> addressList = geocoder.getFromLocation(lat,lon, 1);
            String featureName = addressList.get(0).getFeatureName();
            String subLocality = addressList.get(0).getSubLocality();
            localityList.add(subLocality+", "+featureName);
        } catch (IOException e) {
            e.printStackTrace();
        }


        latLngs.add(new LatLng(lat,lon));
        //System.out.println(lat +"  "+lon);
        FileOperations fileOperations =new FileOperations(new File(getApplicationContext().getFilesDir()+"/data.txt"),latLngs);
        if(fileOperations.WriteData()){
            //Toast.makeText(MyService.this, "Successfully Write", Toast.LENGTH_SHORT).show();
            Log.d("e","Successfully Write");
        }else{
            //Toast.makeText(MyService.this, "Faild to save", Toast.LENGTH_SHORT).show();
            Log.d("e","Faild to save");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
