package com.example.jun.whereareyou.Activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GpsInfo gps;
    private Geocoder geocoder;
    private Button button;
    String str;
    PlaceAutocompleteFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_searchmaps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GpsInfo(this);

        geocoder = new Geocoder(this);

        geocoder = new Geocoder(this);
        button = (Button) findViewById(R.id.buttonn);


        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "search Btn clicked", Toast.LENGTH_SHORT).show();
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

                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            }
        });

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
        mMap = googleMap;

<<<<<<< HEAD
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("My location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
=======
        LatLng DEFAULT_LOCATION = new LatLng(gps.getLatitude(), gps.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 16);
        mMap.moveCamera(cameraUpdate);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title("여기요?");

                mMap.addMarker(markerOptions);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Intent intent = new Intent();
                if(str != null) {
                    intent.putExtra("str", str);
                    Log.d("signupstr", str);
                }
                intent.putExtra("Latlng",latLng);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
        });
>>>>>>> 49caf94159098200501ddac426e180e7feb74f66
    }


}