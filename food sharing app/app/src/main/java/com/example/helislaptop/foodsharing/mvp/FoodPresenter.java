package com.example.helislaptop.foodsharing.mvp;

import com.example.helislaptop.foodsharing.ParseDatabase.FetchDataFromParse;
import com.example.helislaptop.foodsharing.foodList.FoodItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FoodPresenter implements FoodContract.Presenter {

    private FoodContract.Model model;
    private FoodContract.View view;
    public FoodPresenter() {
        this.model = new FoodModel();
        //link the model with presenter
        this.model.setPresenter(this);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewAttached(FoodContract.View view) {
        this.view = view;
        //model.deleteAllItem();
        //this.model.fetchData();
        loadFoodItems(new FetchDataFromParse().fetchDataFromParse());
    }

    @Override
    public void onViewDetached() {
        this.view = null;

    }

    @Override
    public void loadFoodItems(List<FoodItem> foodItemList) {
        //model.deleteAllItem();
        /*
        for (int i = 0; i < 10; i = i + 2) {
            FoodItem foodItem = new FoodItem();
            foodItem.setItemId(i);
            foodItem.setOwner("Henry");
            foodItem.setCapacity(4);
            foodItem.setDescription("Food from Henry");
            foodItem.setImage("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2018/6/0/FN_snapchat_coachella_wingman%20.jpeg.rend.hgtvcom.616.462.suffix/1523633513292.jpeg");
            foodItem.setPostOrRequest("Post");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            foodItem.setTime(timeStamp);
            model.addFoodItem(foodItem);
        }

        for (int i = 1; i < 10; i = i + 2) {
            FoodItem foodItem = new FoodItem();
            foodItem.setItemId(i);
            foodItem.setOwner("Henry");
            foodItem.setCapacity(4);
            foodItem.setDescription("Food from Henry");
            foodItem.setImage("https://food.fnr.sndimg.com/content/dam/images/food/fullset/2018/6/0/FN_snapchat_coachella_wingman%20.jpeg.rend.hgtvcom.616.462.suffix/1523633513292.jpeg");
            foodItem.setPostOrRequest("Request");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            foodItem.setTime(timeStamp);
            model.addFoodItem(foodItem);
        }
        */
        if (view != null) {
            view.loadFoodItems(foodItemList);
        }
    }


}
