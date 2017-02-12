package com.essam.thed.services;

import com.essam.thed.models.ProductItem;

import java.util.List;

/**
 * Created by essam on 11/02/17.
 */

public interface RequestListener {
    void onSuccess(List<ProductItem> products);
    void onFailed(String msg);
}
