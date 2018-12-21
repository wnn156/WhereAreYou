package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.jun.whereareyou.Data.GpsInfo;
import com.example.jun.whereareyou.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SearchMapActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    PlaceAutocompleteFragment autocompleteFragment;
    private boolean mPermissionDenied = false;
    String str;
    private GpsInfo gps;
    //MarkerOptions markerOptions;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_searchmaps);

        gps = new GpsInfo(this);
        geocoder = new Geocoder(this);
        button = (Button) findViewById(R.id.buttonn);


        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "search Btn clicked", Toast.LENGTH_SHORT).show();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    Toast.makeText(getApplicationContext(), "search Btn clicked", Toast.LENGTH_SHORT).show();
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수

                } catch (IOException e) {
                    e.printStackTrace();
                }


                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String[] splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

<<<<<<< HEAD
=======

>>>>>>> 959cbac2040285aa1aeb784e98871de0ceaa0794
                // 좌표(위도, 경도) 생성

                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                marker = mMap.addMarker(new MarkerOptions().position(point).title(address).snippet(latitude.toString() + ", " + longitude.toString()));
                marker.showInfoWindow();


                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));


            }
        });



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                System.out.println("Place: " + place.getName());
                str = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred: " + status);
            }
        });
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("!!!!");
    }
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Double latitude = gps.getLatitude();
        Double longitude = gps.getLongitude();

        LatLng point = new LatLng(latitude, longitude);
        /*markerOptions = new MarkerOptions();
        markerOptions.position(point).title("현재위치");

        markerOptions.snippet(latitude.toString() + ", " + longitude.toString());*/
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 16);
        mMap.moveCamera(cameraUpdate);
        //mMap.addMarker(markerOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title("");

                mMap.addMarker(markerOptions);
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent place_data = new Intent(getApplicationContext(),MainActivity.class);
                place_data.putExtra("PLACE",marker.getTitle());
                place_data.putExtra("X_POS",marker.getPosition().latitude);
                place_data.putExtra("Y_POS",marker.getPosition().longitude);

                setResult(RESULT_OK,place_data);
                finish();
            } });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();


                return false;
            }
        });


    }
}