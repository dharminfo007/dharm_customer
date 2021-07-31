package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.CartProductsActivity;
import in.app.dharm.info.online.shopping.activity.ImageDetailsActivity;
import in.app.dharm.info.online.shopping.activity.ProductDetailActivity;
import in.app.dharm.info.online.shopping.activity.ProductListingActivity;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.fragment.CartFragment;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.CartHolder> {

    // List to store all the contact details
    public ArrayList<ProductListPojo> productList;
    private Context mContext;
    DataProcessor dataProcessor;

    // Counstructor for the Class
    public FavListAdapter(ArrayList<ProductListPojo> productList, Context context) {
        this.productList = productList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public FavListAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_fav_list, parent, false);
        return new FavListAdapter.CartHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull FavListAdapter.CartHolder holder, final int position) {
        final ProductListPojo product = productList.get(position);

        // Set the data to the views here
        holder.setProductTitle(product.getName());
        holder.setProductCartoon(product.getTvPiecesPerCartoon() + " /Cartoons");
        holder.setProductStock(product.getTvStock()+" in stocks");
        holder.setProductPrice("₹ " + product.getTvPrice());
        if (productList.get(position).getListProductImages().size() > 0) {
            Glide
                    .with(mContext)
                    .load(productList.get(position).getListProductImages().get(0))
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(holder.imgProduct);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductDetailActivity.class);
                i.putExtra("id", product.getId());
                mContext.startActivity(i);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataProcessor = new DataProcessor(mContext);
                productList.remove(position);
                dataProcessor.getFavoriteArrayList("favorite").remove(position);
                dataProcessor.saveFavoriteArrayList(productList, "favorite");
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productList.size());
            }
        });

        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(mContext, ImageDetailsActivity.class);
                intentImage.putExtra("imageList", productList.get(position).getListProductImages());
                mContext.startActivity(intentImage);
            }
        });


    }

    // This is your ViewHolder class that helps to populate data to the view
    public class CartHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, btnDelete, tvCartoon, tvStock, tvPrice;
        //        CardView cardProducts;
        ImageView imgProduct;

        public CartHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvCartoon = itemView.findViewById(R.id.tvCartoon);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);

        }

        public void setProductTitle(String title) {
            tvTitle.setText(title);
        }

        public void setProductCartoon(String cartoon) {
            tvCartoon.setText(cartoon);
        }

        public void setProductStock(String stock) {
            tvStock.setText(stock);
        }

        public void setProductPrice(String price) {
            tvPrice.setText(price);
        }
    }
}