package in.app.dharm.info.online.shopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.activity.CartProductsActivity;
import in.app.dharm.info.online.shopping.activity.ContactUsActivity;
import in.app.dharm.info.online.shopping.activity.DharmDealListingActivity;
import in.app.dharm.info.online.shopping.activity.ImageDetailsActivity;
import in.app.dharm.info.online.shopping.activity.LoginActivity;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

public class DharmDelListingAdapter extends RecyclerView.Adapter<DharmDelListingAdapter.DharmDealHolder> {

    // List to store all the contact details
    public ArrayList<ProductListPojo> productList;
    private Context mContext;
    DataProcessor dataProcessor;

    // Counstructor for the Class
    public DharmDelListingAdapter(ArrayList<ProductListPojo> contactsList, Context context) {
        this.productList = contactsList;
        this.mContext = context;
    }

    // This method creates views for the RecyclerView by inflating the layout
    // Into the viewHolders which helps to display the items in the RecyclerView
    @Override
    public DharmDelListingAdapter.DharmDealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_product_list, parent, false);
        return new DharmDelListingAdapter.DharmDealHolder(view);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // This method is called when binding the data to the views being created in RecyclerView
    @Override
    public void onBindViewHolder(@NonNull DharmDelListingAdapter.DharmDealHolder holder, final int position) {
        final ProductListPojo product = productList.get(position);

        // Set the data to the views here
        holder.setProductTitle(product.getName());
        holder.setProductCartoon(product.getTvPiecesPerCartoon() + " /Cartoons");
        holder.setProductStock(product.getTvStock() + " in stocks");
        holder.setProductPrice("₹ " + product.getTvPrice());
        holder.btnAddDeal.setVisibility(View.VISIBLE);
        holder.btnIncreaseQty.setVisibility(View.GONE);
        if (product.getListProductImages().size() > 0) {
            Glide
                    .with(mContext)
                    .load(product.getListProductImages().get(0))
                    .centerCrop()
                    .into(holder.imgProduct);
        }
        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImage = new Intent(mContext, ImageDetailsActivity.class);
                intentImage.putExtra("imageList", productList.get(position).getListProductImages());
                mContext.startActivity(intentImage);
            }
        });

        holder.btnAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(product);
            }
        });
        // You can set click listners to indvidual items in the viewholder here
        // make sure you pass down the listner or make the Data members of the viewHolder public

    }

    private void openDialog(ProductListPojo product) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);

        ImageView imgProduct = bottomSheetDialog.findViewById(R.id.imgProduct);
        TextView tvTitle = bottomSheetDialog.findViewById(R.id.tvTitle);
        TextView tvPrice = bottomSheetDialog.findViewById(R.id.tvPrice);
        TextView tvCartoon = bottomSheetDialog.findViewById(R.id.tvCartoon);
        TextView tvStock = bottomSheetDialog.findViewById(R.id.tvStock);
        TextView txt_TotalCartoons = bottomSheetDialog.findViewById(R.id.txt_TotalCartoons);
        TextView txt_TotalAmt = bottomSheetDialog.findViewById(R.id.txt_TotalAmt);
        EditText etReqCartoon = bottomSheetDialog.findViewById(R.id.etReqCartoon);
        EditText etReqAmt = bottomSheetDialog.findViewById(R.id.etReqAmt);
        TextView tvAddDeal = bottomSheetDialog.findViewById(R.id.tvAddDeal);
        ImageView imgClose = bottomSheetDialog.findViewById(R.id.imgClose);

        tvTitle.setText(product.getName());
        tvCartoon.setText(product.getTvPiecesPerCartoon() + " /Cartoons");
        tvStock.setText(product.getTvStock() + " in stocks");
        tvPrice.setText("₹ " + product.getTvPrice());

        if (product.getListProductImages().size() > 0) {
            Glide
                    .with(mContext)
                    .load(product.getListProductImages().get(0))
                    .centerCrop()
//                .placeholder(R.drawable.loading_spinner)
                    .into(imgProduct);
        }
        etReqCartoon.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    txt_TotalCartoons.setText(String.valueOf(Integer.parseInt(product.getTvPiecesPerCartoon()) *
                            Integer.parseInt(etReqCartoon.getText().toString())));
                }

            }
        });
        etReqAmt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    txt_TotalAmt.setText("₹ " + String.valueOf(Integer.parseInt(product.getTvPiecesPerCartoon()) *
                            Integer.parseInt(etReqAmt.getText().toString())
                            * Integer.parseInt(etReqCartoon.getText().toString())));
                }
            }
        });
        tvAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataProcessor = new DataProcessor(mContext);
                if(etReqCartoon.getText().toString().length() == 0){
                    Toast.makeText(mContext, "Add cartoon first", Toast.LENGTH_LONG).show();
                }else if(etReqAmt.getText().toString().length() == 0){
                    Toast.makeText(mContext, "Add amount to deal first", Toast.LENGTH_LONG).show();
                }else {
                    if (dataProcessor.getBool("isLogin") == true) {
                        if (mContext instanceof DharmDealListingActivity) {
                            ((DharmDealListingActivity) mContext).addDealToFireStore(bottomSheetDialog, etReqCartoon.getText().toString(),
                                    etReqAmt.getText().toString(), product.getId(), product.getName());
                        }
                    } else {
                        Toast.makeText(mContext, "You need to login first..", Toast.LENGTH_LONG).show();
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        ((Activity) mContext).finishAffinity();
                    }
                }


            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }

    // This is your ViewHolder class that helps to populate data to the view
    public class DharmDealHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, btnAddDeal, tvCartoon, tvStock, tvPrice, btnIncreaseQty;
        //        CardView cardProducts;
        ImageView imgProduct;

        public DharmDealHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnAddDeal = itemView.findViewById(R.id.btnAddDeal);
            tvCartoon = itemView.findViewById(R.id.tvCartoon);
            tvStock = itemView.findViewById(R.id.tvStock);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnIncreaseQty = itemView.findViewById(R.id.btnAddToFav);
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