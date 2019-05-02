package com.example.helislaptop.foodsharing.common;

import android.app.FragmentManager;
import android.os.Bundle;

public interface FoodFragmentManager {

    void doFragmentTransaction(FoodBasicFragment basicFragment);

    void startActivityWithBundle(Class<?> clazz, boolean isFinished, Bundle bundle);

    void showSnackBar(String message);

}
