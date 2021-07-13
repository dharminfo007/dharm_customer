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
import in.app.dharm.info.online.shopping.activity.ProductListingActivity;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ContactHolder> {

    // List to store all the contact details
    public ArrayList<String> productTypeList;
    private Context mContext;

    // Counstructor for the Class
    public FilterAdapter(ArrayList<String> productTypeList, Context context) {
        this.productTypeList = productTypeList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public FilterAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_filter_list, parent, false);
        return new FilterAdapter.ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return productTypeList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ContactHolder holder, final int position) {
        // Set the data to the views here
        holder.setFilterTitle(productTypeList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof ProductListingActivity) {
                    ((ProductListingActivity)mContext).filterProductsListing(productTypeList.get(position).toString());
                }
            }
        });

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        public ContactHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        public void setFilterTitle(String title) {
            tvTitle.setText(title);
        }

    }
    public interface IMethodCaller {
        void yourDesiredMethod();
    }

}