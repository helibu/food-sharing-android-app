package com.example.helislaptop.foodsharing.foodList;


import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helislaptop.foodsharing.ParseDatabase.FetchDataFromParse;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodBasicFragment;
import com.example.helislaptop.foodsharing.common.FoodFragmentManager;
import com.example.helislaptop.foodsharing.foodList.detail.FoodDetailFragment;
import com.example.helislaptop.foodsharing.foodList.foodPost.FoodPostFragment;
import com.example.helislaptop.foodsharing.map.MapViewFragment;
import com.example.helislaptop.foodsharing.mvp.FoodContract;
import com.example.helislaptop.foodsharing.mvp.FoodPresenter;
import com.example.helislaptop.foodsharing.mvp.MvpFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends MvpFragment<FoodContract.Presenter> implements FoodContract.View, FoodContract.Model, FoodFragmentManager{

    RecyclerView recyclerView;
    private FoodItemAdapter foodItemAdapter;
    private TextView emptyState;
    private LocationManager locationManager;
    private String locationProvider;
    public static Location myLocation;
    View view;
    public static FoodFragment newInstance() {

        FoodFragment fragment = new FoodFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //7.8
        view = inflater.inflate(R.layout.fragment_food, container, false);
        getCurrentLocation();

        emptyState = view.findViewById(R.id.empty_state);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodItemAdapter = new FoodItemAdapter(foodFragmentManager);
        recyclerView.setAdapter(foodItemAdapter);
        ImageView addButton;
        addButton = view.findViewById(R.id.add_button);
        //buttonView.setImageResource(R.drawable.add);
        //ParseUser.logOutInBackground();
        //Log.i("user",ParseUser.getCurrentUser().toString());
        addButton.setOnClickListener(v -> {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
                if (ParseUser.getCurrentUser() != null || account != null) {
                    foodFragmentManager.doFragmentTransaction(FoodPostFragment.newInstance(myLocation));
                } else {
                    Toast.makeText((Context) getContext(), "Please log in first!", Toast.LENGTH_LONG).show();
                }
            }
        );
        ImageView refreshButton;
        refreshButton = view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MapViewFragment.clearMarkers();
                List<FoodItem> foodItems = new FetchDataFromParse().fetchDataFromParse();
                MapViewFragment.showDataMap(foodItems);
                for (FoodItem item : foodItems) {
                    //String itemName = Integer.toString(item.itemId);
                    MapViewFragment.addDataMap(item);
                }

                MapViewFragment.addMyLocation(FoodFragment.myLocation);
                foodFragmentManager.doFragmentTransaction(FoodFragment.newInstance());
                System.gc();
                //getFragmentManager().popBackStack();
            }
        });



        return view;
    }


    @Override
    public FoodContract.Presenter getPresenter() {
        return new FoodPresenter();
    }

    @Override
    public void loadFoodItems(List<FoodItem> foodItemList) {
        if (foodItemList.size() == 0) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
        if (foodItemList != null) {
            //Sorting here?
            //Double lat = myLocation.getLatitude();
            //Double lon = myLocation.getLongitude();
            //sortFoodList(lat, lon, foodItemList);
            //MapViewFragment.clearMarkers();
            foodItemAdapter.setFoodList(foodItemList);

        }

    }



    @Override
    public void fetchData() {

    }

    @Override
    public void addFoodItem(FoodItem foodItem) {

    }

    @Override
    public void deleteAllItem() {

    }

    @Override
    public void setPresenter(FoodContract.Presenter presenter) {

    }

    @Override
    public void doFragmentTransaction(FoodBasicFragment basicFragment) {

    }

    @Override
    public void startActivityWithBundle(Class<?> clazz, boolean isFinished, Bundle bundle) {

    }

    @Override
    public void showSnackBar(String message) {

    }

    public void getCurrentLocation() {

        //get position from all possible sources
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(false);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //if it's Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //if it's GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }  else {
            myLocation = new Location("");
            double lon = -71.1027;
            double lat = 42.3471;
            myLocation.setLongitude(lon);
            myLocation.setLatitude(lat);
            //Toast.makeText(getContext(), "Can't get Geo location", Toast.LENGTH_SHORT).show();
        }
        //get Location

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //myLocation = locationManager.
            myLocation = locationManager.getLastKnownLocation(locationProvider);
            if(myLocation!=null){
                //showLocation(myLocation);
            } else {
                myLocation = new Location("");
                double lon = -71.1027;
                double lat = 42.3471;
                myLocation.setLongitude(lon);
                myLocation.setLatitude(lat);
                //Toast.makeText(getContext(), "Can't get Geo location", Toast.LENGTH_SHORT).show();
            }

            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            //uploadButton.setVisibility(View.GONE);
        }
    }

    private void showLocation(Location location){
        String locationStr = "Lat：" + location.getLatitude() +"\n"
                + "Lon：" + location.getLongitude();
        Toast.makeText(getContext(),locationStr,Toast.LENGTH_SHORT).show();
    }

    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //showLocation(location);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }

    }
}
