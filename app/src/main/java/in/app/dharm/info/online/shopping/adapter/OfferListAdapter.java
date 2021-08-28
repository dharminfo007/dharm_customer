package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.ProductDetailActivity;
import in.app.dharm.info.online.shopping.model.CouponListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ContactHolder> {

    // List to store all the contact details
    public ArrayList<CouponListPojo> contactsList;
    private Context mContext;

    // Counstructor for the Class
    public OfferListAdapter(ArrayList<CouponListPojo> contactsList, Context context) {
        this.contactsList = contactsList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public OfferListAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_offer_list, parent, false);
        return new OfferListAdapter.ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull OfferListAdapter.ContactHolder holder, final int position) {
        final CouponListPojo couponListPojo = contactsList.get(position);
        holder.tvTitle.setText(couponListPojo.getCoupon_title());
        holder.tvDesc.setText(couponListPojo.getCoupon_desc());
        holder.tvApplyOffer.setText("Apply");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductDetailActivity.class);
                mContext.startActivity(i);
            }
        });

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDesc, tvApplyOffer;

        public ContactHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvApplyOffer = itemView.findViewById(R.id.tvApplyOffer);

        }


    }

}