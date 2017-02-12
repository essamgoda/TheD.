package com.essam.thed.ui.viewadapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essam.thed.R;
import com.essam.thed.models.ProductItem;
import com.essam.thed.ui.viewcontrollers.ProductDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by essam on 11/02/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
     List<ProductItem> products;
     Context context;

    public ProductAdapter(Context context, List<ProductItem> products) {
        this.products = products;
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return products.size();
    }
    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final ProductItem product = products.get(position);

        holder.productDescription.setText(product.getProductDescription());
        holder.productPrice.setText(String.valueOf(product.getProductPrice()));

        Glide.with(context)
                .load(product.getImgUrl())
                .placeholder(R.drawable.img_placeholder)
                .into(holder.productImage);



        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetail.getInstance().setLoading(context,v);
                ProductDetail.getInstance().productDec.setText(product.getProductDescription());
                ProductDetail.getInstance().productPrice.setText(String.valueOf(product.getProductPrice()));
                ProductDetail.getInstance().mImageView.setImageDrawable(holder.productImage.getDrawable());

            }
        });

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView productImage;
        @BindView(R.id.product_description)
        TextView productDescription;
        @BindView(R.id.product_price)
        TextView productPrice;
        @BindView(R.id.card)
        CardView card;

        public ProductViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
