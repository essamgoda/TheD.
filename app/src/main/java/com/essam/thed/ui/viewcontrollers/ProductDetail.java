package com.essam.thed.ui.viewcontrollers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.essam.thed.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by essam on 07/02/2017.
 */

public class ProductDetail  extends GestureDetector.SimpleOnGestureListener {

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public ImageView mImageView;
    public TextView productPrice;
    public TextView productDec;

    public PopupWindow popupWindow;

    private static ProductDetail self;

    public static ProductDetail getInstance() {
        if (self == null)
            self = new ProductDetail();
        return self;
    }

    public void setLoading(Context context, View v) {

        LayoutInflater layoutInflater
                = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.product_detail, null);
        popupWindow = new PopupWindow(
                popupView,
                CardView.LayoutParams.WRAP_CONTENT,
                CardView.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setTouchable(true);

        ScrollView sv=(ScrollView)popupView.findViewById(R.id.scroll);

        new SwipeDetector(sv).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum SwipeType) {

                if(SwipeType==SwipeDetector.SwipeTypeEnum.LEFT_TO_RIGHT)
                    popupWindow.dismiss();

            }

        });

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) popupView.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.9f;
        wm.updateViewLayout(popupView, p);

        mImageView = (ImageView) popupView.findViewById(R.id.image);

        productPrice = (TextView) popupView.findViewById(R.id.product_price);
        productDec = (TextView) popupView.findViewById(R.id.product_description);
    }


}
