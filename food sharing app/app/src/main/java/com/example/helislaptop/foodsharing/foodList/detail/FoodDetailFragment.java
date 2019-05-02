package com.example.helislaptop.foodsharing.foodList.detail;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helislaptop.foodsharing.FoodApplication;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodBasicFragment;
import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.example.helislaptop.foodsharing.foodList.FoodItem;
import com.example.helislaptop.foodsharing.foodList.FoodItemAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDetailFragment extends FoodBasicFragment{
    private static final String FOODITEM = "foodItem";
    private static int[] ICON_ARRAY = new int[]{R.drawable.post, R.drawable.request};
    private final AppDatabase db = FoodApplication.getDataBase();
    private TextView descriptionView;
    private TextView categoryView;
    private TextView capacityView;
    private TextView addressView;
    private TextView phoneView;
    private TextView ownerView;
    //private TextView timeView;
    private ImageView detailImage;
    private ImageView postImage;
    public static FoodDetailFragment newInstance(FoodItem foodItem) {
        Bundle args = new Bundle();
        args.putParcelable(FOODITEM, foodItem);
        FoodDetailFragment fragment = new FoodDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        ownerView = view.findViewById(R.id.owner_detail_info);
        descriptionView = view.findViewById(R.id.food_detail_description);
        //timeView = view.findViewById(R.id.food_time);
        detailImage = view.findViewById(R.id.food_detail_image);
        postImage = view.findViewById(R.id.post_detail);
        categoryView = view.findViewById(R.id.food_detail_category);
        capacityView = view.findViewById(R.id.food_detail_capacity);
        addressView = view.findViewById(R.id.address_detail_info);
        phoneView = view.findViewById(R.id.phone_number_detail_info);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFoodItemDetail(getArguments().getParcelable(FOODITEM));
    }

    private void loadFoodItemDetail(FoodItem foodItem) {
        descriptionView.setText(foodItem.getDescription());
        //timeView.setText(foodItem.getTime());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("ImageId",foodItem.foodImage);
        //Log.i("imageName",foodItem.foodImage.substring(4));
        query.orderByDescending("createdAt");
        Bitmap bitmap = null;
        try {
            List<ParseObject> objectList = query.find();
            ParseObject object = objectList.get(0);
            ParseFile file = (ParseFile) object.get("Image");
            byte[] data = file.getData();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        detailImage.setImageBitmap(bitmap);
        postImage.setImageResource(getDrawable(foodItem.getPostOrRequest()));
        ownerView.setText(foodItem.getUser());
        categoryView.setText(foodItem.getCategory());
        capacityView.setText(foodItem.getCapacity());
        addressView.setText(foodItem.getAddress());
        phoneView.setText(foodItem.getPhoneNumber());
    }
    private @DrawableRes
    int getDrawable(String postOrRequest) {
        return postOrRequest.equals("Post")? ICON_ARRAY[0] : ICON_ARRAY[1];
    }


    //Implement delete in detail fragment latter
    @SuppressLint("CheckResult")
    public void deleteFoodItem(FoodItem foodItem) {
        Completable.fromAction(() -> db.foodDao().deleteFoodItem(foodItem)).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{
        }, error -> {
        });
    }


}
