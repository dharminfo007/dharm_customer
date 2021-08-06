package in.app.dharm.info.online.shopping.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import in.app.dharm.info.online.shopping.R;
import in.app.dharm.info.online.shopping.adapter.FavListAdapter;
import in.app.dharm.info.online.shopping.common.DataProcessor;
import in.app.dharm.info.online.shopping.model.ProductListPojo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rvFavProducts;
    private FavListAdapter listAdapter;
    ArrayList<ProductListPojo> productFavList;
    ImageView imgBack;
    DataProcessor dataProcessor;
    TextView txtNoFavDataMatch;
    public String TAG = "FavoriteFragment";
    ProgressDialog pd;
    View mView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_favorite, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading...");

        productFavList = new ArrayList<>();
        rvFavProducts = mView.findViewById(R.id.rvFavProducts);
        txtNoFavDataMatch = mView.findViewById(R.id.txtNoFavDataMatch);

        dataProcessor = new DataProcessor(getActivity());

        rvFavProducts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFavProducts.setLayoutManager(layoutManager);
        listAdapter = new FavListAdapter(productFavList, getActivity());
        rvFavProducts.setAdapter(listAdapter);

        //Load the date from the network or other resources
        //into the array list asynchronously

        if (dataProcessor.getFavoriteArrayList("favorite") != null
                && dataProcessor.getFavoriteArrayList("favorite").size() > 0) {
            pd.show();
            productFavList = dataProcessor.getFavoriteArrayList("favorite");
            listAdapter = new FavListAdapter(productFavList, getActivity());
            rvFavProducts.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            pd.dismiss();
        } else {
            checkFavList();
        }

//        imgBack.setOnClickListener(this);
//        txtBuyNow.setOnClickListener(this);
//        grandTotal(cartList);

        return mView;
    }

    private void checkFavList() {
        if(productFavList.size() > 0){
            txtNoFavDataMatch.setVisibility(View.GONE);
            rvFavProducts.setVisibility(View.VISIBLE);
        }else {
            txtNoFavDataMatch.setText("No Favorite data found");
            txtNoFavDataMatch.setVisibility(View.VISIBLE);
            rvFavProducts.setVisibility(View.GONE);
        }
    }

}