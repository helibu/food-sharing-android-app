package com.example.helislaptop.foodsharing.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.helislaptop.foodsharing.foodList.FoodItem;

import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFood(FoodItem foodItem);
    @Query("SELECT * FROM foodItem")
    Flowable<List<FoodItem>> getAll();
    @Query("DELETE FROM foodItem")
    void deleteAllItems();
    @Delete
    void deleteFoodItem(FoodItem foodItem);
}
