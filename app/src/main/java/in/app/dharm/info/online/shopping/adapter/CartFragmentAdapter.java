package in.app.dharm.info.online.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.CartProductsActivity;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.fragment.CartFragment;
import in.app.dharm.info.online.shopping.model.CartProductListPojo;

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.CartHolder> {

    // List to store all the contact details
    public ArrayList<CartProductListPojo> cartList;
    private Context mContext;
    int qty = 0;
    DataProcessor dataProcessor;
    CartFragment cartFragment;

    // Counstructor for the Class
    public CartFragmentAdapter(ArrayList<CartProductListPojo> cartList, Context context,
                               CartFragment cartFragment) {
        this.cartList = cartList;
        this.mContext = context;
        this.cartFragment = cartFragment;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public CartFragmentAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_cart_list, parent, false);
        return new CartFragmentAdapter.CartHolder(view);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull CartFragmentAdapter.CartHolder holder, final int position) {
        final CartProductListPojo cartProductPojo = cartList.get(position);
//        qty = Integer.parseInt(String.valueOf(cartProductPojo.getTvStock()));
        // Set the data to the views here
        holder.setProductTitle(cartProductPojo.getName());
        holder.setProductDesc(cartProductPojo.getTvDesc());
        holder.setProductCartoon(cartProductPojo.getUnit());
        holder.setProductStock(cartProductPojo.getPiecesPerCartoon() + "/Cartoon");
        if (cartProductPojo.getUnit().toLowerCase().equals("cartoon")) {
            holder.setProductPrice("Total price : " + "₹ " + String.valueOf(Integer.parseInt(cartProductPojo.getTvQty()) *
                    Integer.parseInt(cartProductPojo.getPiecesPerCartoon()) *
                    Integer.parseInt(cartProductPojo.getTvPrice())));
        } else {
            holder.setProductPrice("Total price : " + "₹ " + String.valueOf(Integer.parseInt(cartProductPojo.getTvQty()) *
                    Integer.parseInt(cartProductPojo.getTvPrice())));
        }
        holder.setProductQty(cartProductPojo.getTvQty());
//        holder.setProductOfferDisc(contact.getTvOfferDisc());

        holder.btnIncreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(String.valueOf(holder.tvQty.getText()));
                if (qty < 20) {
                    qty++;
                    holder.tvQty.setText(String.valueOf(qty));
                    cartProductPojo.setTvQty(qty + "");
                    dataProcessor = new DataProcessor(mContext);
                    dataProcessor.getArrayList("cart").set(position, cartProductPojo);
                    dataProcessor.saveArrayList(cartList, "cart");
                    updateGrandTotal();
                    notifyItemChanged(position);
//                    notifyDataSetChanged();
                }
            }
        });
        holder.btnDecreaseQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(String.valueOf(holder.tvQty.getText()));
                if (qty > 1) {
                    qty--;
                    holder.tvQty.setText(String.valueOf(qty));
                    cartProductPojo.setTvQty(qty + "");
                    dataProcessor = new DataProcessor(mContext);
                    dataProcessor.getArrayList("cart").set(position, cartProductPojo);
                    dataProcessor.saveArrayList(cartList, "cart");
                    updateGrandTotal();
                    notifyItemChanged(position);
                }
            }
        });
        holder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartFragment.removeAt(position);

            }
        });

        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class CartHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDesc, tvCartoon, tvStock, tvPrice, btnDecreaseQty,
                btnIncreaseQty, tvQty;
        CardView cardProducts;
        FrameLayout imgClose;

        public CartHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvCartoon = itemView.findViewById(R.id.tvCartoon);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            cardProducts = itemView.findViewById(R.id.cardProducts);
            btnDecreaseQty = itemView.findViewById(R.id.btnDecreaseQty);
            btnIncreaseQty = itemView.findViewById(R.id.btnIncreaseQty);
            tvQty = itemView.findViewById(R.id.tvQty);
            imgClose = itemView.findViewById(R.id.imgClose);

        }

        public void setProductTitle(String title) {
            tvTitle.setText(title);
        }

        public void setProductDesc(String desc) {
            tvDesc.setText(desc);
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

        public void setProductQty(String qty) {
            tvQty.setText(qty);
        }
    }

    public void removeAt(int position) {
        dataProcessor = new DataProcessor(mContext);
        cartList.remove(position);
        dataProcessor.getArrayList("cart").remove(position);
        dataProcessor.saveArrayList(cartList, "cart");
        updateGrandTotal();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartList.size());
    }

    public void updateGrandTotal() {
        cartFragment.grandTotal(cartList);

    }

}