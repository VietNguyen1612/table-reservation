package com.example.tablereservation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.tablereservation.R;
import com.example.tablereservation.databinding.ActivityMapBinding;
import com.example.tablereservation.databinding.ActivityRestaurantDetailBinding;
import com.example.tablereservation.model.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, LocationListener, GoogleMap.OnMapClickListener {
    GoogleMap mMap;
    private ActivityMapBinding mActivityMapBinding;
    private static Restaurant mRestaurant;
    private SupportMapFragment mSupportMapFragment;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng curentLocation;
    LatLng destination,origin;
    double endLatitude, endLongitude;
    double latitude, longitude;
    private Marker currentLocationMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(mActivityMapBinding.getRoot());
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getCurrentLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        mSupportMapFragment.getMapAsync(this);
        MapsInitializer.initialize(getApplicationContext());
        getDataIntent();
        initToolbar();
        mActivityMapBinding.zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        mActivityMapBinding.zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });
        mActivityMapBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                String location = mActivityMapBinding.etLocation.getText().toString();
                List<Address> addressList;
                MarkerOptions mo= new MarkerOptions();
                if(!location.trim().equals("")){
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                        if(addressList.size() == 0){
                            throw new IOException();
                        }
                        for (int i=0;i<addressList.size();i++){
                            Address myAddress = addressList.get(i);
                            LatLng latLng = new LatLng(myAddress.getLatitude(),myAddress.getLongitude());
                            mo.position(latLng);
                            mo.title("Your search result");
                            mMap.addMarker(mo);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                        destination = new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());
                        origin = curentLocation;
                    } catch (IOException e) {
                        notification("No location matched");
                    }
                }
            }
        });
        mActivityMapBinding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((mActivityMapBinding.etFrom.getText().toString().trim().equalsIgnoreCase("") ||
                        mActivityMapBinding.etTo.getText().toString().trim().equalsIgnoreCase("")) &&
                        !mActivityMapBinding.etLocation.getText().toString().trim().equals("") ){
                    mActivityMapBinding.etLocation.setText("");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + origin.latitude+ "," + origin.longitude + "&daddr=" + destination.latitude + "," + destination.longitude));
                    startActivity(intent);
                }
                if(!mActivityMapBinding.etFrom.getText().toString().trim().equalsIgnoreCase("") && !
                        mActivityMapBinding.etTo.getText().toString().trim().equalsIgnoreCase("")){
                    String from = mActivityMapBinding.etFrom.getText().toString();
                    String to = mActivityMapBinding.etTo.getText().toString();
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        mActivityMapBinding.etFrom.setText("");
                        mActivityMapBinding.etTo.setText("");
                        List<Address> fromAddress = geocoder.getFromLocationName(from,1);
                        List<Address> toAddress = geocoder.getFromLocationName(to,1);
                        if(fromAddress.size() == 0 || toAddress.size() == 0){
                            throw new IOException();
                        }
                        LatLng fromLatLng = new LatLng(fromAddress.get(0).getLatitude(),fromAddress.get(0).getLongitude());
                        LatLng toLatLng = new LatLng(toAddress.get(0).getLatitude(),toAddress.get(0).getLongitude());
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + fromLatLng.latitude+ "," + fromLatLng.longitude + "&daddr=" + toLatLng.latitude + "," + toLatLng.longitude));
                        startActivity(intent);
                    } catch (IOException e) {
                        notification("No location matched");
                    }
                }
            }
        });
        mActivityMapBinding.btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(endLatitude, endLongitude));
                markerOptions.title("Destination");
                Location location1 = new Location("");
                location1.setLatitude(latitude);
                location1.setLongitude(longitude);
                Location location2 = new Location("");
                location2.setLatitude(endLatitude);
                location2.setLongitude(endLongitude);
                float distance = location1.distanceTo(location2) / 1000;
                markerOptions.snippet("Distance: " + distance + "km");
                Marker marker =  mMap.addMarker(markerOptions);
                marker.showInfoWindow();
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions2 = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
        });
        mActivityMapBinding.btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap.setOnMarkerClickListener(MapActivity.this);
                        mMap.setOnMarkerDragListener(MapActivity.this);
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            curentLocation = latLng;
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Your current Location");
                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        } else {
                            Toast.makeText(MapActivity.this, "Please turn on your location permission.", Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
    }

    private void initToolbar() {
        mActivityMapBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityMapBinding.toolbar.tvTitle.setText("Map");

        mActivityMapBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRestaurant = (Restaurant) bundle.get("restaurant_object");
            mActivityMapBinding.etLocation.setText(mRestaurant.getAddress());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.setDraggable(true);
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        endLatitude = marker.getPosition().latitude;
        endLongitude = marker.getPosition().longitude;
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMarker = mMap.addMarker(markerOptions);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
        LatLng location;
        if (curentLocation != null) {
            location = curentLocation;
        } else {
            location = new LatLng(10.781843813878067, 106.70510182557214);
        }
        MarkerOptions markerOptions = new MarkerOptions().position(location).title("Pizza 4P Le Thanh ton");
        Marker marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }

    public void notification(String message){
        Toast.makeText(MapActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
