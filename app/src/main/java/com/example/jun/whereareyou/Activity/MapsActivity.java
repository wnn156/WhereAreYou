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
import com.example.jun.whereareyou.Data.ListViewChatItem;
import com.example.jun.whereareyou.Data.User;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    PlaceAutocompleteFragment autocompleteFragment;
    private boolean mPermissionDenied = false;
    String str;
    private GpsInfo gps;
    //ArrayList<User> participants;
    private ListViewChatItem listViewChatItem;
    Marker marker;
    ArrayList<LatLng> participants_point = new ArrayList<LatLng>();
    ArrayList<Polyline> polylines = new ArrayList<Polyline>();
    User me;
    Button closebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        gps = new GpsInfo(this);
        geocoder = new Geocoder(this);
        button = (Button) findViewById(R.id.buttonn);
        closebtn = (Button) findViewById(R.id.backBtn);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

                // 좌표(위도, 경도) 생성

                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                /*MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point).title("여기요?");
                markerOptions.snippet(latitude.toString() + ", " + longitude.toString());*/

              /*  mMap.addMarker(markerOptions);*/
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
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng DEFAULT_LOCATION = new LatLng(gps.getLatitude(), gps.getLongitude());


        //participants = getIntent().getParcelableArrayListExtra("Participants");
        listViewChatItem = getIntent().getParcelableExtra("ListViewChatItem");
        me = getIntent().getParcelableExtra("USER");
        LatLng prom_place = new LatLng(listViewChatItem.getLatitude(), listViewChatItem.getLongitude());
        marker = mMap.addMarker(new MarkerOptions().position(prom_place).title(listViewChatItem.getChat_name()+" 약속장소").snippet(listViewChatItem.getLatitude() + ", " + listViewChatItem.getLongitude()));
        marker.showInfoWindow();

        LatLng mycurrent_place = new LatLng(gps.getLatitude(), gps.getLongitude());
        marker = mMap.addMarker(new MarkerOptions().position(mycurrent_place).title("내위치").snippet(gps.getLatitude() + ", " + gps.getLongitude()));



        for(int i=0;i<listViewChatItem.getparticipant().size();i++){
            participants_point.add(new LatLng(listViewChatItem.getparticipant().get(i).getLatitude(),listViewChatItem.getparticipant().get(i).getLongitude()));

            System.out.println("Participant : "+ participants_point);
        }

        System.out.println("GET CHATITEM : " + listViewChatItem);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 16);
        mMap.moveCamera(cameraUpdate);

        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .add(
                        prom_place,
                        mycurrent_place));
        for(int i=0;i<listViewChatItem.getparticipant().size();i++){
            if(listViewChatItem.getparticipant().get(i).getName().equals(me.getName())){
                continue;
            }
            marker = mMap.addMarker(new MarkerOptions().position(participants_point.get(i)).title(listViewChatItem.getparticipant().get(i).getName()).snippet(participants_point.get(i).latitude + ", " +participants_point.get(i).longitude));
            System.out.println("PolyLine "+i +": "+ listViewChatItem.getparticipant().get(i).getName());
            polylines.add(mMap.addPolyline(new PolylineOptions().add(prom_place,participants_point.get(i))));
        }
    }
}