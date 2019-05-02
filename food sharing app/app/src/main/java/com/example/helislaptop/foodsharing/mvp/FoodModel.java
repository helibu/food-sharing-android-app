package com.example.helislaptop.foodsharing.mvp;

import android.annotation.SuppressLint;

import com.example.helislaptop.foodsharing.FoodApplication;
import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.example.helislaptop.foodsharing.foodList.FoodItem;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodModel implements FoodContract.Model {
    private FoodContract.Presenter presenter;
    private final AppDatabase db;
    public FoodModel() {

        db = FoodApplication.getDataBase();
    }

    @Override
    public void setPresenter(FoodContract.Presenter presenter) {
        this.presenter = presenter;
    }



    @SuppressLint("CheckResult")
    @Override
    public void fetchData() {
        db.foodDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(presenter::loadFoodItems, error -> System.out.println("error"), () -> {
                    System.out.println("complete");
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void addFoodItem(FoodItem foodItem) {
        Completable.fromAction(() -> db.foodDao().insertFood(foodItem)).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{
        }, error -> {
        });
    }
    @SuppressLint("CheckResult")
    @Override
    public void deleteAllItem() {
        Completable.fromAction(() -> db.foodDao().deleteAllItems()).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(() ->{
        }, error -> {
        });
    }


}
