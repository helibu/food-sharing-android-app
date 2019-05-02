package com.example.helislaptop.foodsharing.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.foodList.FoodFragment;
import com.example.helislaptop.foodsharing.map.MapFragment;
import com.example.helislaptop.foodsharing.map.MapViewFragment;
import com.example.helislaptop.foodsharing.setting.SettingFragment;

/**
 * A simple {@link Fragment} subclass.
 */


public class ContainerFragment extends FoodBasicFragment {
    public static final int HOME_PAGE = 0;
    public static final String HOME_PAGE_TAG = "home_page";
    public static final int MAP_PAGE = 1;
    public static final String MAP_PAGE_TAG = "map_page";
    public static final int SETTING_PAGE = 2;
    public static final String SETTING_PAGE_TAG = "setting_page";
    private int pageIndex;

    private Fragment initFragment;


    public static ContainerFragment newInstance(int pageIndex) {
        ContainerFragment containerFragment = new ContainerFragment();
        containerFragment.pageIndex = pageIndex;

        containerFragment.initFragment = createInitFragmentByIndex(pageIndex);
        return containerFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (initFragment != null && !initFragment.isAdded()) {
            getChildFragmentManager().beginTransaction().replace(R.id.child_fragment_container, initFragment, getCurrentTag(pageIndex))
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.child_fragment_container, container, false);
    }
    public static int getPositionById(int id) {
        switch (id) {
            case R.id.action_food:
                return HOME_PAGE;
            case R.id.action_map:
                return MAP_PAGE;
            case R.id.action_setting:
                return SETTING_PAGE;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private static Fragment createInitFragmentByIndex(int pageIndex) {
        switch (pageIndex) {
            case HOME_PAGE:
                return FoodFragment.newInstance();
            case MAP_PAGE:
                return new MapViewFragment();
            case SETTING_PAGE:
                return new SettingFragment();
            default:
                throw new IndexOutOfBoundsException();
        }
    }


    public static String getCurrentTag(int position) {
        switch (position) {
            case HOME_PAGE:
                return HOME_PAGE_TAG;
            case MAP_PAGE:
                return MAP_PAGE_TAG;
            case SETTING_PAGE:
                return SETTING_PAGE_TAG;
            default:
                return null;
        }
    }

}
