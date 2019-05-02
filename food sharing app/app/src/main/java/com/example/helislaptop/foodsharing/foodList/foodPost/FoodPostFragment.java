package com.example.helislaptop.foodsharing.foodList.foodPost;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helislaptop.foodsharing.FoodApplication;
import com.example.helislaptop.foodsharing.MainActivity;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodBasicFragment;
import com.example.helislaptop.foodsharing.common.FoodFragmentManager;
import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.example.helislaptop.foodsharing.foodList.FoodFragment;
import com.example.helislaptop.foodsharing.foodList.FoodItem;
import com.example.helislaptop.foodsharing.map.MapViewFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodPostFragment extends FoodBasicFragment {
    private TextView postionView;
    private LocationManager locationManager;
    private String locationProvider;
    private static Location myLocation;

    //Connect to database
    //find all required views, and bind with text view, new one FoodItem, and insert into database

    private final AppDatabase db = FoodApplication.getDataBase();
    private EditText ownerInput;
    private EditText descriptionInput;
    private EditText phoneNumber;
    private EditText address;
    private EditText capacity;
    private EditText category;

    private Button requestButton;
    private Button postButton;
    private String imageId;
    private ImageView uploadButton;
    private ImageView uploadedImage;

    public static FoodPostFragment newInstance(Location location) {
        myLocation = location;
        Bundle args = new Bundle();
        FoodPostFragment fragment = new FoodPostFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_post, container, false);
        ownerInput = view.findViewById(R.id.owner_info);

        descriptionInput = view.findViewById(R.id.food_info);
        phoneNumber = view.findViewById(R.id.phone_number);
        address = view.findViewById(R.id.address_info);
        capacity = view.findViewById(R.id.capacity_info);
        category = view.findViewById(R.id.category_info);
        uploadedImage = view.findViewById(R.id.uploaded_image);
        uploadButton = view.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                upload();
                //uploadButton.setVisibility(View.GONE);
            }
        });


        requestButton = view.findViewById(R.id.request_button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null && capacity != null && imageId != null && ownerInput.getText() != null && ownerInput.getText().toString().length() != 0 && descriptionInput.getText() != null && descriptionInput.getText().toString().length() != 0) {
                    FoodItem foodItem = new FoodItem(ownerInput.getText().toString(), descriptionInput.getText().toString(),
                            "Request", phoneNumber.getText().toString(), address.getText().toString(), myLocation.getLongitude(), myLocation.getLatitude(), category.getText().toString(), capacity.getText().toString(),
                            "", imageId);
                    addFoodItem(foodItem);
                    AddToParse(foodItem);
                    //onBackPressed();
                    View tempView = LayoutInflater.from(getContext()).inflate(R.layout.toast_message, null);
                    TextView textView = tempView.findViewById(R.id.error);
                    textView.setText("    A request has been made.");
                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.CENTER, 0, 550);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(tempView);
                    toast.show();
                    MapViewFragment.addDataMap(foodItem);
                    foodFragmentManager.doFragmentTransaction(FoodFragment.newInstance());
                } else {
                    //Long s = new Date().getTime();
                    Toast errorToast = Toast.makeText(getContext(), "Please fill all required information!", Toast.LENGTH_LONG);
                    //Toast errorToast = Toast.makeText(getContext(),Double.toString(myLocation.getLatitude()),Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }

        });
        postButton = view.findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null && capacity != null && imageId != null && ownerInput.getText() != null && ownerInput.getText().toString().length() != 0 && descriptionInput.getText() != null && descriptionInput.getText().toString().length() != 0) {
                    FoodItem foodItem = new FoodItem(ownerInput.getText().toString(), descriptionInput.getText().toString(),
                            "Post", phoneNumber.getText().toString(), address.getText().toString(), myLocation.getLongitude(), myLocation.getLatitude(), category.getText().toString(), capacity.getText().toString(),
                            "", imageId);

                    addFoodItem(foodItem);
                    //onBackPressed();
                    AddToParse(foodItem);
                    View tempView = LayoutInflater.from(getContext()).inflate(R.layout.toast_message, null);
                    TextView textView = tempView.findViewById(R.id.error);
                    Toast toast = new Toast(getContext());
                    toast.setGravity(Gravity.CENTER, 0, 550);
                    toast.setDuration(Toast.LENGTH_LONG);
                    textView.setText("    A post has been made.");
                    toast.setView(tempView);
                    toast.show();
                    MapViewFragment.addDataMap(foodItem);
                    foodFragmentManager.doFragmentTransaction(FoodFragment.newInstance());
                } else {
                    Toast errorToast = Toast.makeText(getContext(), "Please fill all required information!", Toast.LENGTH_LONG);
                    errorToast.show();
                }
            }

        });
        return view;
    }

    private void AddToParse(FoodItem foodItem) {
        ParseObject object = new ParseObject("FoodItem");
        object.put("user", foodItem.user);
        object.put("time", foodItem.time);
        object.put("capacity", foodItem.capacity);
        object.put("description", foodItem.description);

        object.put("foodImage", imageId);

        object.put("postOrRequest", foodItem.postOrRequest);
        object.put("longitude", foodItem.longitude);
        object.put("latitude", foodItem.latitude);
        object.put("phoneNumber", foodItem.phoneNumber);
        object.put("address", foodItem.address);
        object.put("category", foodItem.category);
        object.put("expiredTime", foodItem.expiredTime);
        object.saveInBackground();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                upload();
            }
        }
    }

    private void upload() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                uploadButton.setVisibility(uploadButton.GONE);

                //Log.i("Photo","Received");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                imageId = UUID.randomUUID().toString() + ".png";
                ParseFile file = new ParseFile(imageId, byteArray);
                ParseObject object = new ParseObject("Image");
                object.put("Image", file);
                object.put("ImageId", imageId);
                object.saveInBackground();
                uploadedImage.setImageBitmap(bitmap);
                //object.put();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    @SuppressLint("CheckResult")
    public void addFoodItem(FoodItem foodItem) {
        Completable.fromAction(() -> db.foodDao().insertFood(foodItem)).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
        }, error -> {
        });
    }

}