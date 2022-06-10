package com.example.mysenior.Adapter;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mysenior.Activity.Fragment_Frame;

import java.util.ArrayList;

public class Adapter_notification_image_viewpager extends FragmentStateAdapter {

    public ArrayList<Bitmap> images;
    public int count;

    public Adapter_notification_image_viewpager(FragmentActivity fa, ArrayList<Bitmap> images) {
        super(fa);
        this.images = images;
        this.count = images.size();
    }
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        return new Fragment_Frame(images.get(index));
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public int getRealPosition(int position) { return position % count; }

    @Override
    public long getItemId(int position) {
        Log.w("Position",Integer.toString(position));
        return images.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return images.contains(itemId);
    }
}