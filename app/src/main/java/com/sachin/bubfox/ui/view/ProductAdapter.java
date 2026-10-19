package com.sachin.bubfox.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sachin.bubfox.R;
import com.sachin.bubfox.item.product.Product;
import com.sachin.bubfox.ui.UIEffect;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context mContext;
    private final List<Product> mProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_recycler_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int position) {
        int bindingPos = viewHolder.getBindingAdapterPosition();
        int itemIndex = (bindingPos != RecyclerView.NO_POSITION) ? bindingPos : position;
        Product product = mProductList.get(itemIndex);
        viewHolder.mTxtProductDescription.setText(product.getDescription());
        viewHolder.mImageProduct.setImageResource(product.getDrawableId());
        viewHolder.mBtnProduct.setBackgroundResource(product.getButtonId());
        viewHolder.mBtnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPos = viewHolder.getBindingAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION && currentPos < mProductList.size()) {
                    // Show the confirm dialog
                    showDialog(mProductList.get(currentPos));
                }
            }
        });
        // Init UI effect
        int popOrder = (bindingPos != RecyclerView.NO_POSITION) ? bindingPos : 0;
        UIEffect.createPopUpEffect(viewHolder.itemView, popOrder);
        UIEffect.createPopUpEffect(viewHolder.mBtnProduct, popOrder + 2);
        UIEffect.createButtonEffect(viewHolder.mBtnProduct);
    }

    public void showDialog(Product product) {
        // Override this method to show the dialog
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mImageProduct;
        private final TextView mTxtProductDescription;
        private final ImageButton mBtnProduct;

        public ProductViewHolder(View view) {
            super(view);
            mTxtProductDescription = view.findViewById(R.id.txt_product);
            mImageProduct = view.findViewById(R.id.image_product);
            mBtnProduct = view.findViewById(R.id.btn_product);
        }

    }

}
