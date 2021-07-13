package in.app.dharm.info.online.shopping.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String ARG_SECTION_NUMBER = "section_number";
    ImageView imgBanner;
    View mView;
    ArrayList<String> mImageList;

    public SlideFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SlideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlideFragment newInstance(ArrayList<String> imageList) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, imageList);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SlideFragment newInstance(int index, ArrayList<String> imageList) {
        SlideFragment fragment = new SlideFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageList = getArguments().getStringArrayList(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_slide, container, false);
        imgBanner = mView.findViewById(R.id.imgBanner);
        return mView;
    }
}