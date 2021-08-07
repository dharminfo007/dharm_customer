package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    ProductListAdapter listAdapter;

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
        holder.tvOrderId.setText(ordersListPojo.getOrderId());
        holder.tvOrderTotal.setText("Total price : "+"₹ " +ordersListPojo.getTotal_price());

        holder.rvProductItems.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        holder.rvProductItems.setLayoutManager(layoutManager);
        listAdapter = new ProductListAdapter(ordersListPojo.getHashMaps(), mContext);
        holder.rvProductItems.setAdapter(listAdapter);
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class ContactHolder extends RecyclerView.ViewHolder {

        private TextView tvOrderId, tvOrderTotal;
        RecyclerView rvProductItems;

        public ContactHolder(View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            rvProductItems = itemView.findViewById(R.id.rvProductItems);

        }


    }

}