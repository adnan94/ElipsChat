package com.example.adnan.panachatfragment.Adaptors;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adnan.panachatfragment.R;

import java.util.ArrayList;

/**
 * Created by Adnan Ahmed on 2/22/2016.
 */
public class SliderAdaptor extends PagerAdapter {

    ArrayList<Integer> images;
    Context c;
    LayoutInflater inflater;


    public SliderAdaptor(Context c, ArrayList<Integer> images) {
        this.c = c;
        this.images = images;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return images.size();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v=inflater.inflate(R.layout.sliding,null);
        ImageView iv=(ImageView)v.findViewById(R.id.image);
        iv.setImageResource(images.get(position));
        container.addView(v,0);
        return v;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
