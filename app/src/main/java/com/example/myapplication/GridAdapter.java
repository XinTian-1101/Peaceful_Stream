package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private int [] img;
    private String [] name;

    LayoutInflater layoutInflater;
    public GridAdapter(Context context, int[] img, String[] name) {
        this.context = context;
        this.img = img;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item , null);
        }

        ImageView imageView = convertView.findViewById(R.id.gridImg);
        TextView textView = convertView.findViewById(R.id.gridText);
        imageView.setImageResource(img[position]);
        textView.setText(name[position]);
        return convertView;
    }
}
