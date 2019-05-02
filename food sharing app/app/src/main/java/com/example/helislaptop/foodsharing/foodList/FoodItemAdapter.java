package com.example.helislaptop.foodsharing.foodList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helislaptop.foodsharing.FoodApplication;
import com.example.helislaptop.foodsharing.R;
import com.example.helislaptop.foodsharing.common.FoodFragmentManager;
import com.example.helislaptop.foodsharing.database.AppDatabase;
import com.example.helislaptop.foodsharing.foodList.detail.FoodDetailFragment;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder> {

    public List<FoodItem> foodList;
    private FoodFragmentManager fragmentManager;
    private static int[] ICON_ARRAY = new int[]{R.drawable.post, R.drawable.request};


    public FoodItemAdapter(FoodFragmentManager foodFragmentManager) {
        this.fragmentManager = foodFragmentManager;
        this.foodList = new LinkedList<>();

    }

    public void setFoodList(List<FoodItem> foodList) {
        this.foodList.clear();
        this.foodList.addAll(foodList);
        notifyDataSetChanged();
    }

    @Override
    public FoodItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodList.get(position);
        holder.owner.setText(foodItem.getUser());
        holder.description.setText(foodItem.getDescription());
        holder.category.setText(foodItem.getCategory());
        holder.capacity.setText(foodItem.getCapacity());

        holder.icon.setImageResource(getDrawable(foodItem.getPostOrRequest()));

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
        holder.foodImage.setImageBitmap(bitmap);
        holder.itemView.setOnClickListener(v -> {
            fragmentManager.doFragmentTransaction(FoodDetailFragment.newInstance(foodItem));
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodItemViewHolder extends RecyclerView.ViewHolder {

        TextView owner;
        TextView description;
        TextView capacity;
        TextView category;
        ImageView icon;
        ImageView foodImage;


        public FoodItemViewHolder(View itemView) {
            super(itemView);
            owner = itemView.findViewById(R.id.owner);
            description = itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.image);
            foodImage = itemView.findViewById(R.id.food_image);
            capacity = itemView.findViewById(R.id.capacity);
            category = itemView.findViewById(R.id.category);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
                int positon = viewHolder.getAdapterPosition();
                FoodItem itemToRemove = foodList.get(positon);
                foodList.remove(positon);

            }
        };


    }
    private @DrawableRes
    int getDrawable(String postOrRequest) {
        return postOrRequest.equals("Post")? ICON_ARRAY[0] : ICON_ARRAY[1];
    }


}

