package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.ImageDetailsActivity;
import in.app.dharm.info.online.shopping.activity.ProductDetailActivity;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ContactHolder> {

    // List to store all the contact details
    public ArrayList<ProductListPojo> productList;
    private Context mContext;

    // Counstructor for the Class
    public ProductAdapter(ArrayList<ProductListPojo> contactsList, Context context) {
        this.productList = contactsList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_product_list, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        final ProductListPojo product = productList.get(position);

        // Set the data to the views here
        holder.setProductTitle(product.getName());
//        holder.setProductDesc(product.getTvDesc());
        holder.setProductCartoon(product.getTvPiecesPerCartoon() + " /Cartoons");
        holder.setProductStock(product.getTvStock()+" in stocks");
        holder.setProductPrice("₹ " + product.getTvPrice());
//        holder.setProductOfferDisc(product.getTvOfferDisc());
        if (productList.get(position).getListProductImages().size() > 0) {
            Glide
                    .with(mContext)
                    .load(productList.get(position).getListProductImages().get(0))
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
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

        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(mContext, ImageDetailsActivity.class);
                intentImage.putExtra("imageList", productList.get(position).getListProductImages());
                mContext.startActivity(intentImage);
            }
        });

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDesc, tvCartoon, tvStock, tvPrice;
//        CardView cardProducts;
        ImageView imgProduct;

        public ContactHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvCartoon = itemView.findViewById(R.id.tvCartoon);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvOfferDisc = itemView.findViewById(R.id.tvOfferDisc);
//            cardProducts = itemView.findViewById(R.id.cardProducts);
            imgProduct = itemView.findViewById(R.id.imgProduct);

        }

        public void setProductTitle(String title) {
            tvTitle.setText(title);
        }

//        public void setProductDesc(String desc) {
//            tvDesc.setText(desc);
//        }

        public void setProductCartoon(String cartoon) {
            tvCartoon.setText(cartoon);
        }

        public void setProductStock(String stock) {
            tvStock.setText(stock);
        }

        public void setProductPrice(String price) {
            tvPrice.setText(price);
        }
//        public void setProductOfferDisc(String offerDisc) {
//            tvOfferDisc.setText(offerDisc);
//        }
    }

    public void updateList(ArrayList<ProductListPojo> list) {
        productList = list;
        notifyDataSetChanged();
    }

}