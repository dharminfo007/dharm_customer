package in.app.dharm.info.online.shopping.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

import in.app.dharm.info.online.shopping.R;


@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements ViewFactory {
    Gallery gallery;
    ImageSwitcher imageSwitcher;
    Context context;
    int imageItem;
    Integer[] images = {R.drawable.ic_twitter, R.drawable.ic_fb, R.drawable.ic_linkedin,
            R.drawable.ic_linkedin, R.drawable.ic_twitter, R.drawable.ic_linkedin};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        gallery = (Gallery) findViewById(R.id.gallery);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(MainActivity.this);
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
        imageSwitcher.setAlpha(Float.parseFloat("50.0"));
        gallery.setAdapter(new ImageAdapter(this));
        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageItem = position;
                imageSwitcher.setImageResource(images[position]);
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {
        Context context;

        public ImageAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return images.length;
        }

        public Object getItem(int position) {
            return images[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(this.context);
            imageView.setImageResource(images[position]);
            imageView.setLayoutParams(new Gallery.LayoutParams(200, 150));
            return imageView;
        }
    }

    public View makeView() {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[imageItem]);
        return imageView;
    }
}
