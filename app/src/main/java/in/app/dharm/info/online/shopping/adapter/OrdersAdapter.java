package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.ProductDetailActivity;
import in.app.dharm.info.online.shopping.model.OrdersListPojo;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ContactHolder> {

    // List to store all the contact details
    public ArrayList<OrdersListPojo> orderList;
    private Context mContext;

    // Counstructor for the Class
    public OrdersAdapter(ArrayList<OrdersListPojo> orderList, Context context) {
        this.orderList = orderList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public OrdersAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_order_list, parent, false);
        return new OrdersAdapter.ContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ContactHolder holder, final int position) {
        final OrdersListPojo ordersListPojo = orderList.get(position);
        holder.tvProdName.setText(ordersListPojo.getName());
        holder.tvProdId.setText(ordersListPojo.getId());
        holder.tvQty.setText("₹ " +String.valueOf(Integer.parseInt(ordersListPojo.getQty()) * Integer.parseInt(ordersListPojo.getPrice())));


        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvTotalPrice, tvQty, tvProdId, tvProdName;

        public ContactHolder(View itemView) {
            super(itemView);

            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvProdId = itemView.findViewById(R.id.tvProdId);
            tvProdName = itemView.findViewById(R.id.tvProdName);

        }


    }

}