package com.example.helislaptop.foodsharing;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.facebook.stetho.Stetho;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class FoodApplication extends Application {
    public static AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);


        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "food_db").build();
        //Use Parse as online server database
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("faf82024a567aa5bf7d2d9c286570db201e6e63e")
                .clientKey("ffa9a6c42110a3324074591c3b91a3a042672f01")
                .server("http://3.19.75.181:80/parse/")
                .build()
        );
        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);



    }
    public static AppDatabase getDataBase() {
        return database;
    }

}

