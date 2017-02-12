package com.essam.thed.ui.views;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.essam.thed.R;
import com.essam.thed.models.ProductItem;
import com.essam.thed.services.ProductRequest;
import com.essam.thed.services.RequestListener;
import com.essam.thed.ui.viewadapters.ProductAdapter;
import com.essam.thed.ui.viewcontrollers.ProductDetail;
import com.essam.thed.util.NetworkUtility;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.grid_view)
    RecyclerView ProductRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;
    List<ProductItem> listOfProducts;
    ProductAdapter adapter;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupRecyclerView();

        listOfProducts = new ArrayList<>();

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        if (NetworkUtility.isConnected(MainActivity.this)) {

                                            try {
                                                ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(), "") + "cacheFile.srl")));
                                                listOfProducts = (List<ProductItem>) in.readObject();
                                                in.close();
                                                adapter = new ProductAdapter(MainActivity.this, listOfProducts);
                                                ProductRecyclerView.setAdapter(adapter);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                listOfProducts = null;
                                            }

                                            fetchData();
                                        } else {
                                            swipeRefreshLayout.setRefreshing(false);
                                            try {
                                                ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(), "") + "cacheFile.srl")));
                                                listOfProducts = (List<ProductItem>) in.readObject();
                                                in.close();
                                                adapter = new ProductAdapter(MainActivity.this, listOfProducts);
                                                ProductRecyclerView.setAdapter(adapter);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                listOfProducts = null;
                                                Toast.makeText(MainActivity.this,"No internet Connection or cache",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
        );

    }

    void fetchData() {
        ProductRequest req = new ProductRequest(new RequestListener() {
            @Override
            public void onSuccess(List<ProductItem> products) {
                swipeRefreshLayout.setRefreshing(false);

                try {
                    ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(), "") + "cacheFile.srl"));
                    out.writeObject(products);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listOfProducts != null&&adapter!=null) {
                    listOfProducts = products;
                    adapter.notifyDataSetChanged();
                }else {
                    listOfProducts = products;
                    adapter = new ProductAdapter(MainActivity.this, listOfProducts);
                    ProductRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailed(String msg) {
                Log.e("failed",msg);
            }
        });
        req.execute();
    }

    void setupRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent);
        StaggeredGridLayoutManager VGridLayoutManager = new StaggeredGridLayoutManager(2, 1);

        ProductRecyclerView.setLayoutManager(VGridLayoutManager);
        ProductRecyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if(NetworkUtility.isConnected(MainActivity.this)){
            fetchData();
        }else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this,"No internet Connection",Toast.LENGTH_LONG).show();
        }
    }

}
