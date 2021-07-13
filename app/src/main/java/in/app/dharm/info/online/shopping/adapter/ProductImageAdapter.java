package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ContactHolder> {

    // List to store all the contact details
//    public ArrayList<ImageListPojo> contactsList;
    public ArrayList<String> contactsList;
    private Context mContext;

    // Counstructor for the Class
    public ProductImageAdapter(ArrayList<String> contactsList, Context context) {
        this.contactsList = contactsList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ProductImageAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_product_image, parent, false);
        return new ProductImageAdapter.ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.ContactHolder holder, final int position) {
//        final ImageListPojo contact = contactsList.get(position);

        // Set the data to the views here
//        holder.setProductTitle(contact.getImage());
        Glide
                .with(mContext)
                .load(contactsList.get(position))
                .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                .into(holder.imgProduct);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductDetailActivity.class);
                mContext.startActivity(i);
            }
        });*/

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

//        private TextView tvTitle, tvDesc, tvCartoon, tvStock, tvPrice, tvOfferDisc;
//        CardView cardProducts;
        ImageView imgProduct;


        public ContactHolder(View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvDesc = itemView.findViewById(R.id.tvDesc);
//            tvCartoon = itemView.findViewById(R.id.tvCartoon);
//            tvStock = itemView.findViewById(R.id.tvStock);
//            tvPrice = itemView.findViewById(R.id.tvPrice);
//            tvOfferDisc = itemView.findViewById(R.id.tvOfferDisc);
//            cardProducts = itemView.findViewById(R.id.cardProducts);

        }

        public void setProductTitle(String title) {
            imgProduct.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_cart));

        }

//        public void setProductDesc(String desc) {
//            tvDesc.setText(desc);
//        }
//
//        public void setProductCartoon(String cartoon) {
//            tvCartoon.setText(cartoon);
//        }
//
//        public void setProductStock(String stock) {
//            tvStock.setText(stock);
//        }
//
//        public void setProductPrice(String price) {
//            tvPrice.setText(price);
//        }
//        public void setProductOfferDisc(String offerDisc) {
//            tvOfferDisc.setText(offerDisc);
//        }
    }

}