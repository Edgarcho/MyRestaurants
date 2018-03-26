package com.epicodus.myrestaurants.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.adapters.RestaurantListAdapter;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsListActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    public ArrayList<Restaurant> restaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        getRestaurants(location);
    }

    private void getRestaurants(String location){
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                restaurants = yelpService.processResults(response);

                RestaurantsListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                            mAdapter = new RestaurantListAdapter(getApplicationContext(), restaurants);
                            mRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(RestaurantsListActivity.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }
}
