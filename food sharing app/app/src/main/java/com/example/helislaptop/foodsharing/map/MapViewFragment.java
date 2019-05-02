package com.example.helislaptop.foodsharing.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helislaptop.foodsharing.FoodApplication;
import com.example.helislaptop.foodsharing.MainActivity;
import com.example.helislaptop.foodsharing.ParseDatabase.FetchDataFromParse;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodFragmentManager;
import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.example.helislaptop.foodsharing.database.FoodDao;
import com.example.helislaptop.foodsharing.foodList.FoodFragment;
import com.example.helislaptop.foodsharing.foodList.FoodItem;
import com.example.helislaptop.foodsharing.foodList.FoodItemAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {

    MapView mMapView;
    private ImageView refreshButton;
    private final AppDatabase db = FoodApplication.getDataBase();
    private static GoogleMap googleMap;

    public static List<FoodItem> foodItems;



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodItems = new FetchDataFromParse().fetchDataFromParse();
        showDataMap(foodItems);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        /*
        refreshButton = rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapViewFragment.clearMarkers();

                foodItems = new FetchDataFromParse().fetchDataFromParse();
                showDataMap(foodItems);
                for (FoodItem item : foodItems) {
                    //String itemName = Integer.toString(item.itemId);
                    addDataMap(item);
                }

                addMyLocation(FoodFragment.myLocation);

            }
        });
        */
        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button

                // For dropping a marker at a point on the Map
                //LatLng boston1 = new LatLng(42.349319,-71.106722);
                //googleMap.addMarker(new MarkerOptions().position(boston1).title("Photonic Center").snippet("Your Location"));
                // For zooming automatically to the location of the marker
                /*

                LatLng bostonFoodPost1 = new LatLng(42.34129,-71.128235);
                LatLng bostonFoodPost2 = new LatLng(42.331139, -71.099396);
                LatLng bostonFoodRequest1 = new LatLng(42.364125, -71.104202);
                googleMap.addMarker(new MarkerOptions().position(bostonFoodPost1).title("FoodPost1").snippet("Food for 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                googleMap.addMarker(new MarkerOptions().position(bostonFoodPost2).title("FoodPost2").snippet("Food for 3").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                googleMap.addMarker(new MarkerOptions().position(bostonFoodRequest1).title("FoodRequest1").snippet("Request for 2").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                   */

                Location myLocation = FoodFragment.myLocation;
                LatLng myCurrentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(myCurrentLocation).title("My Current Location"));

                for (FoodItem item : foodItems) {
                    //String itemName = Integer.toString(item.itemId);
                    addDataMap(item);
                }

                //googleMap.clear();
                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker arg0)
                    {
                        return null;
                    }
                    @Override
                    public View getInfoContents(Marker arg0) {
                        View view = getLayoutInflater().inflate(R.layout.custom_infowindow, null);
                        ImageView infoImage = view.findViewById(R.id.info_image);
                        TextView infoTitle = view.findViewById(R.id.info_title);
                        TextView infoCapacity = view.findViewById(R.id.info_capacity);
                        TextView infoPhone = view.findViewById(R.id.info_PhoneNumber);
                        infoTitle.setText(arg0.getTitle());
                        String imageId;
                        if (arg0.getSnippet() != null) {
                            String[] infos = arg0.getSnippet().split(",");
                            imageId = infos[0];
                            String capacity = infos[1];
                            String phoneNumber = infos[2];
                            infoCapacity.setText("Capacity: " + capacity);
                            infoPhone.setText("Phone Number: " + phoneNumber);


                        ParseQuery<ParseObject> query = new ParseQuery<>("Image");
                        query.whereEqualTo("ImageId",imageId);
                        query.orderByDescending("createdAt");
                        Bitmap bitmap = null;
                        try {
                            List<ParseObject> objectList = query.find();
                            if (objectList != null && objectList.size() > 0) {
                                ParseObject object = objectList.get(0);
                                ParseFile file = (ParseFile) object.get("Image");
                                byte[] data = file.getData();
                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                infoImage.setImageBitmap(bitmap);
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        }
                        return view;
                    }
                });

                CameraPosition cameraPosition = new CameraPosition.Builder().target(myCurrentLocation).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    public static void addMyLocation(Location myLocation) {
        LatLng myCurrentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myCurrentLocation).title("My Current Location"));

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @SuppressLint("CheckResult")
    public void fetchData() {
        db.foodDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(MapViewFragment::showDataMap, error -> System.out.println("error"), () -> {
                    System.out.println("complete");
                });
    }

    public static void showDataMap(List<FoodItem> foodItemList) {
        foodItems = new ArrayList<>();
        foodItems.addAll(foodItemList);
    }
    public static void addDataMap(FoodItem foodItem) {
        LatLng  geoPoint = new LatLng(foodItem.latitude,foodItem.longitude);

        if (foodItem.postOrRequest.equals("Post")) {
            googleMap.addMarker(new MarkerOptions().position(geoPoint).title("Food from " + foodItem.user).snippet(foodItem.foodImage + "," + foodItem.capacity +","+foodItem.phoneNumber).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        } else {
            googleMap.addMarker(new MarkerOptions().position(geoPoint).title("Request from " + foodItem.user).snippet(foodItem.foodImage+ "," + foodItem.capacity +","+foodItem.phoneNumber).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }
    public static void clearMarkers() {
        googleMap.clear();
    }

}