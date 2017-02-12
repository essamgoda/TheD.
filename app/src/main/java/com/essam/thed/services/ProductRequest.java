package com.essam.thed.services;

import android.os.AsyncTask;

import com.essam.thed.models.ProductItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by essam on 11/02/17.
 */

public class ProductRequest extends AsyncTask<Object,Object,Object> {
    RequestListener listener;
    List<ProductItem> productItemList;

    public ProductRequest(RequestListener listener) {
        this.listener = listener;
        productItemList=new ArrayList<>();
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            HttpURLConnection urlConnection = null;

            URL url = new URL("https://limitless-forest-98976.herokuapp.com");

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];


            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();


            JSONArray respond=new JSONArray(sb.toString());

            for (int i=0;i<respond.length();i++){
                JSONObject row=respond.getJSONObject(i);

                ProductItem item=new ProductItem();

                item.setId(row.getInt("id"));
                item.setProductDescription(row.getString("productDescription"));
                item.setProductPrice(row.getInt("price"));
                item.setImgUrl(row.getJSONObject("image").getString("url"));

                productItemList.add(item);

            }


        }catch (Exception e){
            listener.onFailed(e.toString());
            e.printStackTrace();
            return null;
        }

        return productItemList;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(o!=null){

            listener.onSuccess(productItemList);
        }else {
            listener.onFailed("Failed");
        }
    }
}
