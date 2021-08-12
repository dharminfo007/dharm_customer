package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.common.DataProcessor;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ContactHolder> {

    // List to store all the contact details
    ArrayList<HashMap<String, String>> productList;
    private Context mContext;
    DataProcessor dataProcessor;

    // Counstructor for the Class
    public ProductListAdapter(ArrayList<HashMap<String, String>> contactsList, Context context) {
        this.productList = contactsList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_order, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
        final HashMap<String, String> product = productList.get(position);

        holder.tvName.setText(product.get("name"));
//        holder.tvOrderId.setText(ordersListPojo.getOrderId());
        holder.tvQty.setText(product.get("qty"));
        holder.tvUnit.setText(product.get("unit"));
        holder.tvPrice.setText("₹ " +String.valueOf(Integer.parseInt(product.get("qty")) * Integer.parseInt(product.get("price"))));

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderId, tvName, tvQty, tvUnit, tvPrice;

        public ContactHolder(View itemView) {
            super(itemView);

//            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvPrice = itemView.findViewById(R.id.tvPrice);

        }

    }


}