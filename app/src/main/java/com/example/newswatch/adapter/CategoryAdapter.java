package com.example.newswatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.newswatch.CategoryNewsActivity;
import com.example.newswatch.R;
import com.example.newswatch.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> implements View.OnClickListener
{
    private ArrayList<Category> dataSet;
    Context mContext;

    private static class ViewHolder
    {
        TextView txtName;
        ImageView logo;
        ConstraintLayout cardview;
    }

    public CategoryAdapter(ArrayList<Category> data, Context context)
    {
        super(context, R.layout.category_list_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v)
    {

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Category category = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.category_list_layout, parent, false);

            viewHolder.txtName = convertView.findViewById(R.id.txt_status);
            viewHolder.logo = convertView.findViewById(R.id.category_logo);
            viewHolder.cardview = convertView.findViewById(R.id.cardview);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.txtName.setText(category.getName());
        viewHolder.logo.setImageResource(category.getImageUrl());
        viewHolder.logo.setOnClickListener(this);
        viewHolder.logo.setTag(position);

        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c_intent = new Intent(mContext, CategoryNewsActivity.class);

                c_intent.putExtra("Category",category.getName());

                mContext.startActivity(c_intent);
            }
        });


        return convertView;
    }
}
