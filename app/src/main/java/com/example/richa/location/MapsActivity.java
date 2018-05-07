package com.example.richa.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener ll;
    Location l;
    LatLng pos;

    public void showAlert() {

        AlertDialog alertDialogCheckConnection = new AlertDialog.Builder(MapsActivity.this)
                .setMessage("please turn on your location setting to continue using the app")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog alertDialogCompulsionForConnection = new AlertDialog.Builder(MapsActivity.this)
                                .setMessage("Please turn your location setting else you cannot " +
                                        "use the map in the app")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .create();
                        alertDialogCompulsionForConnection.show();
                    }
                })
                .create();
        alertDialogCheckConnection.show();
    }

    public boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point)
                    {
                        MarkerOptions option = new MarkerOptions();
                        //setting the option of the marker
                        option.position(point);
                        //get the latitude and latlang from touched point
                        double touchlat = point.latitude;
                        double touchlang = point.longitude;
                        final LatLng latLngg = new LatLng(touchlat, touchlang);
                        //gecoding which helps in gettting adddress from latlan
                        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            // Geocoder geo=new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = geo.getFromLocation(touchlat, touchlang, 1);
                            if (addresses.isEmpty()) {

                                Toast.makeText(getApplicationContext(), "waiting for location", Toast.LENGTH_SHORT).show();

                            } else {
                                if (addresses.size() > 0) {
                                    String strr = addresses.get(0).getSubLocality() + ",";
                                    strr += addresses.get(0).getLocality() + ",";
                                    strr += addresses.get(0).getCountryName();
                                    //clear the marker;
                                    mMap.clear();
                                    //add the new marker
                                    mMap.addMarker(new MarkerOptions().position(latLngg));
                                    //for zooming the map
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngg, 15));
                                    Toast.makeText(getApplicationContext(), "locality: " + strr, Toast.LENGTH_SHORT).show();
                                    //create a intent object,and specify class
                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                    //set our data using putExtra method which take
                                    //key  and value which we want to send
                                    intent.putExtra("location", strr);
                                    //for starting new activity
                                    startActivity(intent);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng point)
                    {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(point));


                    }
                });


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isLocationEnabled()) {
            showAlert();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location)
            {
                double currentlatitude = location.getLatitude();
                double currentlongitude = location.getLongitude();
                LatLng latlang = new LatLng(currentlatitude, currentlongitude);
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(currentlatitude, currentlongitude, 1);
                    String str = addressList.get(0).getSubLocality() + ",";
                    str += addressList.get(0).getLocality() + ",";
                    str += addressList.get(0).getCountryName();
                    mMap.addMarker(new MarkerOptions().position(latlang).title(str));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, 15));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        }




        }



