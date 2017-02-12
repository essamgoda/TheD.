package com.essam.thed.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by essam on 11/02/17.
 */
public class NetworkUtility {
    public static boolean isConnected(Context context) {
        if (isConnectedToWifi(context)) {
            return true;
        } else if (isConnectedToMobileNetwork(context)) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isConnectedToMobileNetwork(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * extract json array string from response
     *
     * @param key
     * @param response
     * @throws JSONException
     */
    public static String extractJSONArrayFromResponseString(String key,
                                                            String response) throws JSONException {
        JSONObject mainJsonObject = new JSONObject(response);
        String obj = "";
        if (!mainJsonObject.isNull(key)) {
            obj = mainJsonObject.getJSONArray(key).toString();
        }
        return obj;
    }

    /**
     * extract json array string from response
     *
     * @param key
     * @param response
     * @throws JSONException
     */
    public static String extractJSONObjectFromResponseString(String key,
                                                             String response) throws JSONException {
        JSONObject mainJsonObject = new JSONObject(response);
        String obj = "";
        if (!mainJsonObject.isNull(key)) {
            JSONObject subJsonObject = mainJsonObject.getJSONObject(key);
            if (subJsonObject != null) {
                obj = subJsonObject.toString();
            }
        }
        return obj;
    }

    public static boolean isGPSConnected(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        return isGPSEnabled;
    }

    /**
     * Encrypt password
     *
     * @param password
     * @return
     */
    public static String encryptPasswordMD5(String password) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * extract string value from JSON object
     *
     * @param key
     * @param response
     * @return
     * @throws JSONException
     */
    public static String extractJsonStringValueFromResponseString(String key,
                                                                  String response) throws JSONException {
        JSONObject json = new JSONObject(response);
        String sucessResponse = json.getString(key);
        return sucessResponse;
    }
}
