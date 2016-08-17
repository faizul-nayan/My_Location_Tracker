package hunter.black.mylocationtracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    public static ArrayList<LatLng> listData;
    private Button viewMapBtn;
    private Button viewListBtn;
    private Switch onOffSwitch;
    boolean flag = false;
    private ListView addressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewMapBtn = (Button) findViewById(R.id.viewMapBtn);
        viewListBtn = (Button) findViewById(R.id.viewListBtn);

        onOffSwitch = (Switch) findViewById(R.id.switch1);
        addressList = (ListView) findViewById(R.id.locationList);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent=new Intent(getApplicationContext(),MyService.class);
                    startService(intent);
                    flag = true;
                }else{
                    Intent intent=new Intent(getApplicationContext(),MyService.class);
                    stopService(intent);
                    flag = false;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1000);

            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public void viewMap(View view) {
        if(flag){
            FileOperations fileOperations =new FileOperations(new File(getApplicationContext().getFilesDir()+"/data.txt"), MyService.latLngs);
            listData= fileOperations.getAllData();
            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"You must start service before view Map",Toast.LENGTH_LONG).show();
    }

    public void viewList(View view) {
        if(flag){
            ArrayAdapter myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, MyService.localityList);
            addressList.setAdapter(myAdapter);
        }
        else
            Toast.makeText(getApplicationContext(),"You must start service before view List",Toast.LENGTH_LONG).show();
    }
}
