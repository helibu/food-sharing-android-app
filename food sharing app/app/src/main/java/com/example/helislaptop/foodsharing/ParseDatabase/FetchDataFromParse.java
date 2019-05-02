package com.example.helislaptop.foodsharing.ParseDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.helislaptop.foodsharing.foodList.FoodItem;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FetchDataFromParse {
    public List<FoodItem> fetchDataFromParse() {
        List<FoodItem> foodParseList = new ArrayList<>();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("FoodItem");
        query.orderByAscending("createdAt");
        try {
            //Change from findInBackground to find, so this can fetch data synchronized
            List<ParseObject> objectList = query.find();
            for (ParseObject object: objectList) {
                foodParseList.add(toFoodItem(object));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            Log.i("ObjectId", object.getObjectId());
                            foodParseList.add(toFoodItem(object));

                            /*
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        /*
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        //imageView.setImageBitmap(bitmap);
                                        //linearLayout.addView(imageView);

                                    }
                                }
                            });


                        }

                    }

                }

            }

        });
*/



        Log.i("ListSize", String.valueOf(foodParseList.size()));
        return foodParseList;
    }
    public FoodItem toFoodItem(ParseObject object) {
        String user = object.get("user").toString();
        String time = object.get("time").toString();
        String capacity = object.get("capacity").toString();
        String description = object.get("description").toString();
        String foodImage = object.get("foodImage").toString();
        String postOrRequest = object.get("postOrRequest").toString();
        double longitude = Double.parseDouble(object.get("longitude").toString());
        double latitude= Double.parseDouble(object.get("latitude").toString());
        String phoneNumber = object.get("phoneNumber").toString();
        String address = object.get("address").toString();
        String category= object.get("category").toString();
        String expiredTime = object.get("expiredTime").toString();
        FoodItem foodItem = new FoodItem(user, description, postOrRequest, phoneNumber, address, longitude, latitude, category, capacity, expiredTime, foodImage);

        return foodItem;
    }
}
