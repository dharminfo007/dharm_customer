package in.app.dharm.info.online.shopping.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import in.app.dharm.info.online.shopping.fragment.SlideFragment;

public class AutoScrollPagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> mImageList = new ArrayList<>();

    public AutoScrollPagerAdapter(FragmentManager fm, ArrayList<String> imageList) {
        super(fm);
        this.mImageList = imageList;
    }
    @Override
    public Fragment getItem(int position) {
        // Return a SlideFragment (defined as a static inner class below).
        return SlideFragment.newInstance(mImageList);
    }
    @Override
    public int getCount() {
        // Show total images.
        return mImageList.size();
    }
}