package com.example.helislaptop.foodsharing.mvp;

import com.example.helislaptop.foodsharing.foodList.FoodItem;

import java.util.List;

public interface FoodContract {


    interface View extends MvpContract.View<Presenter> {
        void loadFoodItems(List<FoodItem> foodItemList);
    }

    interface Presenter extends MvpContract.Presenter<View, Model> {
        void loadFoodItems(List<FoodItem> foodItemList);
    }

    interface Model extends MvpContract.Model<Presenter> {
        void fetchData();
        void addFoodItem(FoodItem foodItem);
        void deleteAllItem();
    }
}
